# 강의 수정 PUT API 설계 가이드

## 📋 목차
1. [API 개요](#api-개요)
2. [핵심 설계 결정](#핵심-설계-결정)
3. [추천 구조](#추천-구조)
4. [구현 단계](#구현-단계)
5. [주의사항](#주의사항)

---

## API 개요

### 엔드포인트
```
PUT /api/courses/{courseId}
```

### 목적
- 강의 기본 정보 수정 (제목, 설명, 가격 등)
- Section/Lecture 구조는 **별도 API**로 분리 (Phase 2)

---

## 핵심 설계 결정

### ✅ 1️⃣ 수정 범위 결정

**Option A: 기본 정보만 수정 (추천 ⭐)**
```
PUT /api/courses/{courseId}
→ 제목, 설명, 가격, 카테고리, 썸네일, 특징만 수정
→ Section/Lecture는 건드리지 않음
```

**장점:**
- 단순하고 안전
- 실수로 Section 전체 삭제 위험 없음
- 빠른 응답 속도

**단점:**
- Section 수정은 별도 API 필요

---

**Option B: 전체 구조 수정 (복잡함 ⚠️)**
```
PUT /api/courses/{courseId}
→ Section/Lecture까지 전부 교체
→ 기존 Section 삭제 후 새로 생성
```

**장점:**
- 한 번에 모든 것 수정 가능

**단점:**
- 복잡하고 위험
- 트랜잭션 관리 어려움
- 실수로 데이터 날릴 위험

---

### ✅ 2️⃣ 수정 방식 결정

**PUT vs PATCH**

| 방식 | 의미 | 사용 시기 |
|------|------|-----------|
| PUT | 전체 교체 | 모든 필드를 보내서 전체 교체 |
| PATCH | 부분 수정 | 수정할 필드만 보냄 |

**추천: PUT으로 기본 정보 전체 교체 ⭐**
- 클라이언트가 모든 필드 값을 보냄
- null 처리 고민 불필요
- 명확한 의도

---

## 추천 구조

### 📂 파일 구조
```
course/
├── dto/
│   └── request/
│       ├── CreateCourseRequest.java    (이미 존재)
│       └── UpdateCourseRequest.java    (신규 생성)
├── controller/
│   └── CourseController.java
│       └── updateCourse(Long id, UpdateCourseRequest)
├── service/
│   └── CourseService.java
│       └── updateCourse(Long id, UpdateCourseRequest, Long userId)
└── entity/
    └── Course.java
        └── update() 메서드 활용
```

---

### 1️⃣ UpdateCourseRequest DTO

```java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    @NotBlank(message = "강의 제목은 필수입니다")
    private String title;

    @NotBlank(message = "간단 소개는 필수입니다")
    private String description;

    @NotBlank(message = "상세 설명은 필수입니다")
    private String detailedDescription;

    @NotBlank(message = "썸네일 URL은 필수입니다")
    private String thumbnail;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
    private Integer price;

    private List<String> features = new ArrayList<>();

    /**
     * Course 엔티티에 변경사항 적용
     * DTO가 엔티티 업데이트 로직을 캡슐화
     */
    public void applyTo(Course course) {
        course.changeTitle(title);
        course.changeDescription(description);
        course.changeDetailedDescription(detailedDescription);
        course.changeThumbnail(thumbnail);
        course.changeCategory(category);
        course.changePrice(price);
        course.changeFeatures(features);
    }
}
```

**핵심 포인트:**
- ✅ `applyTo(Course)` 메서드로 변환 책임을 DTO에 위임
- ✅ Course의 기존 `change*()` 메서드들을 활용
- ✅ CreateCourseRequest와 일관된 패턴

---

### 2️⃣ CourseController

```java
@PutMapping("/{courseId}")
public ResponseEntity<CourseResponse> updateCourse(
        @PathVariable Long courseId,
        @Valid @RequestBody UpdateCourseRequest request) {

    CourseResponse response = courseService.updateCourse(courseId, request);
    return ResponseEntity.ok(response);
}
```

**포인트:**
- 현재 사용자 ID는 Service에서 `getCurrentUserId()` 활용
- 나중에 Spring Security 도입 시 `@AuthenticationPrincipal` 사용

---

### 3️⃣ CourseService

```java
@Transactional
public CourseResponse updateCourse(Long courseId, UpdateCourseRequest request) {
    log.info("Updating course with ID: {}", courseId);

    // 1. Course 조회
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

    // 2. 권한 검증 (엔티티가 판단, 서비스가 예외 처리)
    Long currentUserId = getCurrentUserId();
    if (!course.isOwner(currentUserId)) {
        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    // 3. DTO가 엔티티에 변경사항 적용
    request.applyTo(course);

    // 4. 변경 감지로 자동 UPDATE (save 불필요)
    return CourseResponse.from(course);
}
```

**핵심 포인트:**
- ✅ `course.isOwner()` - 엔티티가 권한 판단
- ✅ `request.applyTo(course)` - DTO가 변환 책임
- ✅ 변경 감지(Dirty Checking) 활용 - save() 불필요

---

### 4️⃣ Course 엔티티 (이미 존재)

```java
// 이미 구현되어 있음!
public void changeTitle(String title) {
    this.title = title;
}

public void changeDescription(String description) {
    this.description = description;
}

// ... 나머지 change 메서드들
```

**이미 준비 완료! 추가 작업 없음** ✅

---

## 구현 단계

### Phase 1: 기본 정보 수정 (지금 구현)
```
PUT /api/courses/{courseId}
→ 제목, 설명, 가격 등 기본 정보만 수정
```

1. `UpdateCourseRequest` DTO 생성
2. `CourseController.updateCourse()` 추가
3. `CourseService.updateCourse()` 구현
4. 테스트

---

### Phase 2: Section 수정 (나중에)
```
PUT /api/courses/{courseId}/sections/{sectionId}
POST /api/courses/{courseId}/sections
DELETE /api/courses/{courseId}/sections/{sectionId}
```

**왜 분리?**
- Section 추가/삭제/순서 변경은 별도 유스케이스
- 실수로 전체 Section 날릴 위험 방지
- RESTful 설계 원칙 준수

---

### Phase 3: Lecture 수정 (나중에)
```
PUT /api/sections/{sectionId}/lectures/{lectureId}
POST /api/sections/{sectionId}/lectures
DELETE /api/sections/{sectionId}/lectures/{lectureId}
```

---

## 주의사항

### ⚠️ 1. 동시성 문제
**문제:**
- A 사용자가 강의 수정 중
- B 사용자가 동시에 같은 강의 수정
- 먼저 커밋한 변경사항이 덮어씌워질 수 있음

**해결책 (Phase 2):**
```java
@Version
private Long version;  // Optimistic Lock
```

**지금은:**
- 강사 본인만 수정 가능하므로 큰 문제 없음
- 나중에 관리자 기능 추가 시 고려

---

### ⚠️ 2. 상태 변경 제한

**고려사항:**
- 게시 중인 강의만 수정 가능? (PUBLISHED)
- 아카이브된 강의는 수정 불가? (ARCHIVED)

**추천:**
```java
// Course.java
public void validateCanUpdate() {
    if (this.status == CourseStatus.ARCHIVED) {
        throw new IllegalStateException("아카이브된 강의는 수정할 수 없습니다");
    }
}

// Service
public CourseResponse updateCourse(...) {
    course.validateCanUpdate();  // 상태 검증
    request.applyTo(course);
}
```

---

### ⚠️ 3. 가격 변경 시 고려사항

**문제:**
- 이미 수강 중인 학생이 있는 강의의 가격을 변경하면?
- 장바구니에 담긴 강의의 가격이 변경되면?

**지금은:**
- 그냥 변경 허용 (간단)

**나중에:**
- 가격 이력 테이블 추가
- 장바구니 가격 동기화 로직

---

## 예외 처리

### ErrorCode 추가 (필요 시)
```java
// ErrorCode.java
COURSE_ALREADY_ARCHIVED("이미 아카이브된 강의입니다"),
COURSE_CANNOT_UPDATE("수정할 수 없는 강의입니다");
```

---

## 응답 예시

### 성공 (200 OK)
```json
{
  "id": 1,
  "instructorId": 1,
  "title": "자바 완전 정복 (수정됨)",
  "description": "자바 기초부터 심화까지 (업데이트)",
  "thumbnail": "https://example.com/new-thumbnail.png",
  "category": "Development",
  "price": 55000,
  "studentCount": 120,
  "status": "PUBLISHED",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 실패 (403 FORBIDDEN)
```json
{
  "error": "FORBIDDEN",
  "message": "강의를 수정할 권한이 없습니다"
}
```

### 실패 (404 NOT_FOUND)
```json
{
  "error": "NOT_FOUND",
  "message": "강의를 찾을 수 없습니다"
}
```

---

## 테스트 체크리스트

### 단위 테스트
- [ ] UpdateCourseRequest.applyTo() 동작 확인
- [ ] Course.change*() 메서드들 정상 동작
- [ ] Course.isOwner() 권한 판단 정확성

### 통합 테스트
- [ ] 정상 수정 시 200 OK
- [ ] 본인 아닌 경우 403 FORBIDDEN
- [ ] 존재하지 않는 강의 404 NOT_FOUND
- [ ] Validation 실패 시 400 BAD_REQUEST
- [ ] updatedAt 자동 갱신 확인

---

## 최종 추천 구조 요약

```
✅ UpdateCourseRequest DTO
   └─ applyTo(Course course) 메서드

✅ CourseService.updateCourse()
   ├─ Course 조회
   ├─ course.isOwner() 권한 검증
   ├─ request.applyTo(course) 변경사항 적용
   └─ 변경 감지로 자동 UPDATE

✅ Course 엔티티
   └─ 기존 change*() 메서드 활용

✅ Phase 1: 기본 정보만 수정
✅ Phase 2: Section/Lecture는 별도 API
```

---

## 다음 단계

1. **지금 구현할 것:**
   - UpdateCourseRequest DTO
   - CourseController.updateCourse()
   - CourseService.updateCourse()

2. **나중에 구현할 것:**
   - Section 수정 API (POST/PUT/DELETE)
   - Lecture 수정 API (POST/PUT/DELETE)
   - 가격 이력 관리
   - Optimistic Lock

---

## 궁금한 점?

- Section/Lecture도 지금 함께 수정하고 싶다면?
  - **비추천:** 너무 복잡하고 위험
  - **추천:** 단계별로 구현 (기본 정보 → Section → Lecture)

- PATCH로 부분 수정하고 싶다면?
  - 가능하지만 null 처리가 복잡해짐
  - PUT으로 전체 교체가 더 단순

- 상태(status) 변경도 여기서?
  - **비추천:** 별도 API 분리 권장
  - `PUT /api/courses/{id}/archive` (아카이빙)
  - `PUT /api/courses/{id}/publish` (재게시)

이대로 구현하면 됩니다! 🚀
