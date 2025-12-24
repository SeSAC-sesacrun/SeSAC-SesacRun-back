# JWT 인증 시스템 마이그레이션 가이드

## 📋 개요

현재 Course 도메인에서는 더미 사용자 ID(1L)를 사용하여 강의 생성 및 관리 기능을 구현했습니다.
JWT 인증 시스템이 완성되면 아래 가이드를 따라 실제 로그인한 사용자 정보를 사용하도록 수정해야 합니다.

---

## 🔍 현재 구조

### 1. CourseService의 더미 사용자 로직

**파일**: `src/main/java/com/example/sesacrunback/domain/course/course/service/CourseService.java`

```java
/**
 * 현재 로그인한 사용자 ID 반환 (더미)
 */
private Long getCurrentUserId() {
    return 1L; // 임시 강의자 ID
}
```

**사용 위치**:
- `createCourse()` - 강의 생성 시 instructorId 설정 (line 48)
- `getMyCourses()` - 강사의 강의 목록 조회 (line 172)
- `deleteCourse()` - 강의 삭제 권한 확인 (line 192)

### 2. Controller의 주석 처리된 인가 어노테이션

**파일**: `src/main/java/com/example/sesacrunback/domain/course/course/controller/CourseController.java`

```java
@PostMapping
// @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
        @Valid @RequestBody CreateCourseRequest request) {
    // ...
}
```

### 3. Repository 메서드 수정 (2025-12-23 완료)

**파일**: `src/main/java/com/example/sesacrunback/domain/course/course/repository/CourseRepository.java`

**변경 내용**: `findByInstructor_Id()` → `findByInstructorId()`

**변경 이유**:
- `findByInstructor_Id(Long instructorId)`는 Spring Data JPA가 `instructor.id`(User 엔티티와 조인)로 해석
- Course 엔티티는 `instructorId` 필드를 직접 사용하므로 불필요한 조인 발생
- `findByInstructorId(Long instructorId)`로 변경하여 `instructorId` 컬럼만 직접 사용

**수정 전**:
```java
List<Course> findByInstructor_Id(Long instructorId);
```

**수정 후**:
```java
List<Course> findByInstructorId(Long instructorId);
```

**영향 받는 메서드**: `CourseService.getMyCourses()` (line 175)

---

## 🔧 JWT 인증 시스템 도입 후 수정 사항

### 1단계: SecurityContext에서 사용자 정보 가져오기

JWT 인증이 구현되면 Spring Security의 `SecurityContext`에서 현재 로그인한 사용자 정보를 가져올 수 있습니다.

#### 수정 전 (더미)
```java
private Long getCurrentUserId() {
    return 1L; // 임시 강의자 ID
}
```

#### 수정 후 (JWT 기반)
```java
private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        throw new CustomException(ErrorCode.UNAUTHORIZED);
    }

    // JWT에서 추출한 사용자 정보 (UserDetails 또는 Custom Principal)
    Object principal = authentication.getPrincipal();

    if (principal instanceof UserDetails) {
        // UserDetails를 사용하는 경우
        String username = ((UserDetails) principal).getUsername();
        // username으로 User 조회 후 ID 반환
        // return userRepository.findByUsername(username).orElseThrow().getId();
    }

    if (principal instanceof Long) {
        // JWT에서 직접 userId를 principal로 설정한 경우
        return (Long) principal;
    }

    throw new CustomException(ErrorCode.UNAUTHORIZED);
}
```

### 2단계: 유틸리티 클래스 생성 (권장)

인증 관련 로직을 재사용하기 위해 유틸리티 클래스를 만드는 것을 권장합니다.

**새 파일**: `src/main/java/com/example/sesacrunback/global/util/SecurityUtil.java`

```java
package com.example.sesacrunback.global.util;

import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * 현재 인증된 사용자의 ID를 반환
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        // JWT Filter에서 설정한 principal 타입에 따라 처리
        if (principal instanceof Long) {
            return (Long) principal;
        }

        // Custom UserPrincipal 객체를 사용하는 경우
        // if (principal instanceof UserPrincipal) {
        //     return ((UserPrincipal) principal).getId();
        // }

        throw new CustomException(ErrorCode.UNAUTHORIZED);
    }

    /**
     * 현재 인증된 사용자가 특정 역할을 가지고 있는지 확인
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}
```

### 3단계: CourseService 수정

**수정할 파일**: `src/main/java/com/example/sesacrunback/domain/course/course/service/CourseService.java`

```java
// import 추가
import com.example.sesacrunback.global.util.SecurityUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * 현재 로그인한 사용자 ID 반환
     */
    private Long getCurrentUserId() {
        // 더미 코드 제거하고 SecurityUtil 사용
        return SecurityUtil.getCurrentUserId();
    }

    // 나머지 코드는 동일
}
```

### 4단계: Controller 인가 어노테이션 활성화

**수정할 파일**: `src/main/java/com/example/sesacrunback/domain/course/course/controller/CourseController.java`

```java
@PostMapping
@PreAuthorize("hasRole('INSTRUCTOR')") // 주석 제거
public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
        @Valid @RequestBody CreateCourseRequest request) {
    CourseResponse course = courseService.createCourse(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(course));
}

@GetMapping("/my")
@PreAuthorize("hasRole('INSTRUCTOR')") // 주석 제거
public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses() {
    List<CourseResponse> courses = courseService.getMyCourses();
    return ResponseEntity.ok(ApiResponse.success(courses));
}

@DeleteMapping("/{courseId}")
@PreAuthorize("hasRole('INSTRUCTOR')") // 주석 제거
public ResponseEntity<ApiResponse<Void>> deleteCourse(
        @PathVariable Long courseId) {
    courseService.deleteCourse(courseId);
    return ResponseEntity.ok(ApiResponse.success(null));
}
```

---

## 🎯 JWT 필터 구현 시 고려사항

### JWT 필터에서 해야 할 작업

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 헤더에서 JWT 토큰 추출
        String token = extractToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 2. JWT에서 사용자 정보 추출
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            String role = jwtTokenProvider.getRoleFromToken(token);

            // 3. Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userId,  // principal - SecurityUtil에서 사용
                null,    // credentials
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            // 4. SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
```

---

## 📝 체크리스트

### 사전 작업 (완료)
- [x] 0. `CourseRepository.findByInstructor_Id()` → `findByInstructorId()` 수정 (2025-12-23)

### JWT 인증 시스템 도입 시 진행 사항
JWT 인증 시스템 도입 시 아래 항목들을 순서대로 진행하세요:

- [ ] 1. JWT 토큰 생성/검증 로직 구현
- [ ] 2. JwtAuthenticationFilter 구현 및 등록
- [ ] 3. SecurityConfig 설정 완료
- [ ] 4. `SecurityUtil.java` 유틸리티 클래스 생성
- [ ] 5. `CourseService.getCurrentUserId()` 메서드 수정
- [ ] 6. `CourseController`의 `@PreAuthorize` 어노테이션 주석 해제
- [ ] 7. ErrorCode에 `UNAUTHORIZED` 추가 확인
- [ ] 8. Postman에서 JWT 토큰 포함하여 API 테스트
- [ ] 9. 권한이 없는 사용자 접근 시 403 응답 확인
- [ ] 10. 토큰 없이 접근 시 401 응답 확인

---

## 🧪 테스트 시나리오

### 1. 강의 생성 API 테스트

**Before (더미)**
```http
POST /api/v1/courses
Content-Type: application/json

{
  "title": "React 완벽 가이드",
  "description": "React 기초부터 고급까지",
  ...
}
```

**After (JWT)**
```http
POST /api/v1/courses
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "title": "React 완벽 가이드",
  "description": "React 기초부터 고급까지",
  ...
}
```

### 2. 권한 확인 테스트

- **INSTRUCTOR 역할 사용자**: 강의 생성 성공 (201)
- **STUDENT 역할 사용자**: 강의 생성 실패 (403 Forbidden)
- **토큰 없음**: 인증 실패 (401 Unauthorized)
- **만료된 토큰**: 인증 실패 (401 Unauthorized)

---

## ⚠️ 주의사항

1. **ErrorCode 확인**: `UNAUTHORIZED`, `FORBIDDEN` ErrorCode가 정의되어 있는지 확인
   - 파일: `src/main/java/com/example/sesacrunback/global/exception/ErrorCode.java`

2. **User 엔티티 연동**: Course의 `instructor` 필드가 실제 User와 연동되는지 확인
   - 현재 `instructorId`만 저장되고 `instructor` 객체는 lazy loading

3. **테스트 데이터**: 개발/테스트 환경에서는 더미 사용자 데이터(ID=1L)가 실제로 존재하는지 확인

4. **SecurityConfig**: JWT 필터가 적절한 순서로 등록되었는지 확인
   ```java
   http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
   ```

---

## 📚 참고 자료

- Spring Security 공식 문서: https://docs.spring.io/spring-security/reference/
- JWT (JSON Web Token): https://jwt.io/
- `@PreAuthorize` 사용법: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html

---

## 문의사항

JWT 인증 시스템 구현 중 문제가 발생하면:
1. SecurityContext에 Authentication이 제대로 설정되었는지 디버깅
2. JWT 토큰의 payload에 필요한 정보(userId, role)가 포함되어 있는지 확인
3. Filter 체인이 올바른 순서로 동작하는지 확인
