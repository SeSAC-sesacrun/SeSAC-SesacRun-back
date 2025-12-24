# Course 도메인 완전 분석 문서

## 목차
1. [도메인 개요](#1-도메인-개요)
2. [아키텍처 구조](#2-아키텍처-구조)
3. [엔티티 계층](#3-엔티티-계층)
4. [Repository 계층](#4-repository-계층)
5. [Service 계층](#5-service-계층)
6. [Controller 계층](#6-controller-계층)
7. [DTO 구조](#7-dto-구조)
8. [데이터베이스 스키마](#8-데이터베이스-스키마)
9. [API 명세](#9-api-명세)
10. [주요 설계 결정사항](#10-주요-설계-결정사항)
11. [성능 최적화](#11-성능-최적화)

---

## 1. 도메인 개요

### 1.1 도메인 목적
Course 도메인은 온라인 강의 플랫폼의 핵심 도메인으로, 강의 생성, 조회, 검색, 삭제 등의 기능을 제공합니다.

### 1.2 주요 기능
- 강의 전체 목록 조회 (페이징)
- 강의 상세 정보 조회 (섹션, 강의 영상 포함)
- 카테고리별 강의 조회
- 키워드 검색
- 인기 강의 조회 (평점 기준)
- 강의 생성 (섹션, 강의 영상 포함)
- 내 강의 목록 조회
- 강의 삭제

### 1.3 도메인 모델
```
Course (강의)
  ├── Section (섹션) - 1:N
  │     └── Lecture (강의 영상) - 1:N
  └── Instructor (강사) - N:1
```

---

## 2. 아키텍처 구조

### 2.1 패키지 구조
```
com.example.sesacrunback.domain.course
├── course
│   ├── controller
│   │   └── CourseController.java
│   ├── dto
│   │   ├── request
│   │   │   └── CreateCourseRequest.java
│   │   └── response
│   │       ├── CourseResponse.java
│   │       └── CourseDetailResponse.java
│   ├── entity
│   │   ├── Course.java
│   │   └── enums
│   │       └── CourseStatus.java
│   ├── repository
│   │   └── CourseRepository.java
│   ├── service
│   │   └── CourseService.java
│   └── converter
│       └── StringListJsonConverter.java
├── section
│   ├── dto
│   │   └── response
│   │       └── SectionResponse.java
│   └── entity
│       └── Section.java
└── lecture
    ├── dto
    │   └── response
    │       └── LectureResponse.java
    └── entity
        └── Lecture.java
```

### 2.2 계층별 책임

| 계층 | 책임 | 주요 클래스 |
|------|------|-------------|
| Controller | HTTP 요청/응답 처리, 입력 검증 | CourseController |
| Service | 비즈니스 로직, 트랜잭션 관리 | CourseService |
| Repository | 데이터 접근, 쿼리 실행 | CourseRepository |
| Entity | 도메인 모델, 비즈니스 규칙 | Course, Section, Lecture |
| DTO | 데이터 전송, 응답 포맷 | Request/Response 객체들 |

---

## 3. 엔티티 계층

### 3.1 Course 엔티티

#### 3.1.1 엔티티 정의
```java
@Entity
@Table(name = "courses")
public class Course extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 강사 정보 (양방향 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", insertable = false, updatable = false)
    private User instructor;

    // 강사 ID (실제 FK 컬럼)
    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    // 기본 정보
    private String title;              // 강의 제목
    private String description;        // 간단한 설명
    private String detailedDescription; // 상세 설명
    private String thumbnail;          // 썸네일 URL
    private String category;           // 카테고리

    // 가격 정보
    private Integer price;            // 현재 가격
    private Integer originalPrice;    // 원가
    private Integer discount;         // 할인액

    // 통계 정보
    private Double rating;            // 평점 (0.0 ~ 5.0)
    private Integer reviewCount;      // 리뷰 개수
    private Integer studentCount;     // 수강생 수

    // 기능 목록 (JSON 배열로 저장)
    @Convert(converter = StringListJsonConverter.class)
    private List<String> features;

    // 상태
    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    // 자식 엔티티 (섹션 목록)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @BatchSize(size = 10)
    private List<Section> sections = new ArrayList<>();
}
```

#### 3.1.2 주요 필드 설명

**ID 전략**
- `GenerationType.IDENTITY`: MySQL의 AUTO_INCREMENT 사용
- UUID에서 Long으로 변경하여 성능 및 인덱스 효율성 향상

**강사 관계 설계**
```java
// 1. @ManyToOne 관계 (조인용)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "instructor_id", insertable = false, updatable = false)
private User instructor;

// 2. instructorId 필드 (실제 데이터 관리)
@Column(name = "instructor_id", nullable = false)
private Long instructorId;
```
- **이유**: User 엔티티에 대한 의존성을 최소화하면서도 필요시 조인 가능
- `insertable = false, updatable = false`: instructorId 필드가 실제 값 관리
- 강의 생성 시 `instructorId`만 설정하면 됨

**features 필드 (JSON 저장)**
```java
@Convert(converter = StringListJsonConverter.class)
private List<String> features;
```
- DB에는 JSON 문자열로 저장: `["평생 수강", "수료증 제공", "Q&A 답변"]`
- Java에서는 `List<String>`으로 사용
- StringListJsonConverter가 자동 변환 처리

**섹션 관계**
```java
@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
@OrderBy("order ASC")
@BatchSize(size = 10)
private List<Section> sections = new ArrayList<>();
```
- `cascade = ALL`: Course 저장 시 Section도 함께 저장
- `orphanRemoval = true`: Course에서 제거된 Section은 DB에서도 삭제
- `@OrderBy`: 섹션을 order 필드 기준으로 정렬
- `@BatchSize(size = 10)`: N+1 문제 방지 (뒤에서 상세 설명)

#### 3.1.3 비즈니스 메서드
```java
// 섹션 추가
public void addSection(Section section) {
    sections.add(section);
    section.setCourse(this);
}

// 섹션 제거
public void removeSection(Section section) {
    sections.remove(section);
    section.setCourse(null);
}
```

### 3.2 Section 엔티티

#### 3.2.1 엔티티 정의
```java
@Entity
@Table(name = "sections")
public class Section extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부모 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // 기본 정보
    private String title;

    @Column(nullable = false, name = "section_order")
    private Integer order;  // 섹션 순서

    // 자식 엔티티
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @BatchSize(size = 20)
    private List<Lecture> lectures = new ArrayList<>();
}
```

#### 3.2.2 특징
- **컬럼명 매핑**: `order` → `section_order` (SQL 예약어 충돌 방지)
- **계층 구조**: Course와 Lecture 사이의 중간 계층
- **정렬**: lectures를 order 기준으로 자동 정렬

### 3.3 Lecture 엔티티

#### 3.3.1 엔티티 정의
```java
@Entity
@Table(name = "lectures")
public class Lecture extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부모 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    // 기본 정보
    private String title;
    private String videoUrl;        // 영상 URL
    private Integer duration;       // 재생 시간 (초)

    @Column(nullable = false, name = "lecture_order")
    private Integer order;          // 강의 순서

    private Boolean isFree;         // 무료 공개 여부
}
```

#### 3.3.2 특징
- **duration**: 초 단위로 저장 (예: 600 = 10분)
- **isFree**: 일부 강의는 무료 공개 가능
- **videoUrl**: YouTube 등 외부 영상 URL

### 3.4 Base Entity 구조

```java
// BaseCreateEntity
@MappedSuperclass
@Getter
public abstract class BaseCreateEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

// BaseTimeEntity (BaseCreateEntity 상속)
@MappedSuperclass
@Getter
public abstract class BaseTimeEntity extends BaseCreateEntity {
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

**설계 이유**
- 생성일만 필요한 엔티티: `BaseCreateEntity` 상속
- 생성일 + 수정일 필요한 엔티티: `BaseTimeEntity` 상속
- Course, Section, Lecture는 모두 `BaseTimeEntity` 상속

### 3.5 CourseStatus Enum

```java
public enum CourseStatus {
    DRAFT,      // 초안 (작성 중)
    PUBLISHED,  // 발행됨 (공개)
    ARCHIVED    // 보관됨 (숨김)
}
```

---

## 4. Repository 계층

### 4.1 CourseRepository 인터페이스

```java
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 1. 상세 조회 (섹션 포함)
    @Query("""
        SELECT DISTINCT c
        FROM Course c
        LEFT JOIN FETCH c.sections s
        WHERE c.id = :id
    """)
    Optional<Course> findDetailWithSections(@Param("id") Long id);

    // 2. 카테고리별 조회
    Page<Course> findByCategory(String category, Pageable pageable);

    // 3. 강사별 조회
    List<Course> findByInstructor_Id(Long instructorId);

    // 4. 검색
    @Query("""
        SELECT c
        FROM Course c
        WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);
}
```

### 4.2 메서드 상세 설명

#### 4.2.1 findDetailWithSections
```java
@Query("""
    SELECT DISTINCT c
    FROM Course c
    LEFT JOIN FETCH c.sections s
    WHERE c.id = :id
""")
Optional<Course> findDetailWithSections(@Param("id") Long id);
```

**목적**: 강의 상세 조회 시 N+1 문제 방지

**동작 방식**:
1. Course와 Section을 한 번의 쿼리로 조회 (JOIN FETCH)
2. DISTINCT로 중복 제거
3. Lecture는 @BatchSize로 별도 최적화 (Section당 최대 20개씩 배치 로딩)

**실행 쿼리**:
```sql
SELECT DISTINCT c.*, s.*
FROM courses c
LEFT JOIN sections s ON c.id = s.course_id
WHERE c.id = ?
```

#### 4.2.2 findByCategory
```java
Page<Course> findByCategory(String category, Pageable pageable);
```

**Spring Data JPA 메서드 네이밍 규칙 사용**
- `findBy` + `Category`: category 필드로 검색
- 자동으로 페이징 쿼리 생성

**실행 쿼리**:
```sql
SELECT c.* FROM courses c
WHERE c.category = ?
ORDER BY c.created_at DESC
LIMIT ? OFFSET ?
```

#### 4.2.3 findByInstructor_Id
```java
List<Course> findByInstructor_Id(Long instructorId);
```

**언더스코어(_) 사용 이유**:
- `instructor`는 객체 관계
- `instructorId`는 필드명
- `Instructor_Id`는 `instructor.id`를 의미

**실행 쿼리**:
```sql
SELECT c.* FROM courses c
WHERE c.instructor_id = ?
```

#### 4.2.4 searchCourses
```java
@Query("""
    SELECT c
    FROM Course c
    WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);
```

**특징**:
- 대소문자 구분 없이 검색 (LOWER 함수)
- 제목과 설명 모두에서 검색 (OR 조건)
- 부분 일치 검색 (LIKE '%keyword%')

**실행 쿼리**:
```sql
SELECT c.* FROM courses c
WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', ?, '%'))
   OR LOWER(c.description) LIKE LOWER(CONCAT('%', ?, '%'))
ORDER BY c.created_at DESC
LIMIT ? OFFSET ?
```

### 4.3 JpaRepository 기본 제공 메서드

```java
// CourseRepository가 JpaRepository를 상속받아 자동으로 제공되는 메서드들

// 1. 단건 조회
Optional<Course> findById(Long id);
Course getById(Long id);  // 프록시 반환

// 2. 전체 조회
List<Course> findAll();
Page<Course> findAll(Pageable pageable);

// 3. 저장
Course save(Course course);  // INSERT or UPDATE
List<Course> saveAll(Iterable<Course> courses);

// 4. 삭제
void delete(Course course);
void deleteById(Long id);
void deleteAll();

// 5. 존재 확인
boolean existsById(Long id);

// 6. 개수
long count();
```

---

## 5. Service 계층

### 5.1 CourseService 전체 구조

```java
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    // 더미 사용자 ID 반환 (인증 시스템 연동 전)
    private Long getCurrentUserId() {
        return 1L;
    }

    // 1. 강의 생성
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request);

    // 2. 강의 상세 조회
    public CourseDetailResponse getCourseDetail(Long courseId);

    // 3. 전체 강의 목록
    public Page<CourseResponse> getAllCourses(Pageable pageable);

    // 4. 카테고리별 강의
    public Page<CourseResponse> getCoursesByCategory(String category, Pageable pageable);

    // 5. 강의 검색
    public Page<CourseResponse> searchCourses(String keyword, Pageable pageable);

    // 6. 인기 강의
    public Page<CourseResponse> getPopularCourses(Pageable pageable);

    // 7. 내 강의 목록
    public List<CourseResponse> getMyCourses();

    // 8. 강의 삭제
    @Transactional
    public void deleteCourse(Long courseId);
}
```

### 5.2 메서드별 상세 로직

#### 5.2.1 createCourse - 강의 생성

```java
@Transactional
public CourseResponse createCourse(CreateCourseRequest request) {
    log.info("Creating new course: {}", request.getTitle());

    // 1. 현재 사용자 ID 획득 (더미)
    Long instructorId = getCurrentUserId();

    // 2. Course 엔티티 생성
    Course course = Course.builder()
            .instructorId(instructorId)
            .title(request.getTitle())
            .description(request.getDescription())
            .detailedDescription(request.getDetailedDescription())
            .thumbnail(request.getThumbnail())
            .category(request.getCategory())
            .price(request.getPrice())
            .originalPrice(request.getOriginalPrice())
            .discount(request.getDiscount())
            .features(request.getFeatures())
            .status(CourseStatus.PUBLISHED)
            .build();

    // 3. Section 및 Lecture 생성 (중첩 구조)
    if (request.getSections() != null) {
        for (CreateCourseRequest.CreateSectionRequest sectionReq : request.getSections()) {

            Section section = Section.builder()
                    .course(course)
                    .title(sectionReq.getTitle())
                    .order(sectionReq.getOrder())
                    .build();

            if (sectionReq.getLectures() != null) {
                for (CreateCourseRequest.CreateLectureRequest lectureReq : sectionReq.getLectures()) {

                    Lecture lecture = Lecture.builder()
                            .section(section)
                            .title(lectureReq.getTitle())
                            .videoUrl(lectureReq.getVideoUrl())
                            .duration(lectureReq.getDuration() != null ? lectureReq.getDuration() : 0)
                            .order(lectureReq.getOrder())
                            .isFree(lectureReq.getIsFree() != null ? lectureReq.getIsFree() : false)
                            .build();

                    section.addLecture(lecture);  // 양방향 관계 설정
                }
            }

            course.addSection(section);  // 양방향 관계 설정
        }
    }

    // 4. 저장 (Cascade로 Section, Lecture도 함께 저장됨)
    Course savedCourse = courseRepository.save(course);

    log.info("Course created successfully with ID: {}", savedCourse.getId());

    return CourseResponse.from(savedCourse);
}
```

**핵심 로직**:
1. **계층적 엔티티 생성**: Course → Section → Lecture 순서로 생성
2. **양방향 관계 설정**: `addSection()`, `addLecture()` 메서드 사용
3. **Cascade 저장**: Course만 저장해도 Section, Lecture 모두 저장됨
4. **기본값 처리**: duration과 isFree는 null일 경우 기본값 설정
5. **자동 상태 설정**: 생성 시 PUBLISHED 상태로 설정

**트랜잭션**:
- `@Transactional`: INSERT 쿼리 실행 보장
- 예외 발생 시 모든 변경사항 롤백

#### 5.2.2 getCourseDetail - 강의 상세 조회

```java
public CourseDetailResponse getCourseDetail(Long courseId) {
    log.info("Getting course detail for ID: {}", courseId);

    // 1. Course와 Section을 함께 조회 (JOIN FETCH)
    Course course = courseRepository.findDetailWithSections(courseId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

    // 2. DTO 변환 (Lecture는 @BatchSize로 자동 로딩)
    return CourseDetailResponse.from(course);
}
```

**성능 최적화**:
```
쿼리 실행 순서:
1. Course + Section JOIN FETCH (1번 쿼리)
2. Lecture 배치 로딩 (@BatchSize=20, 최대 1번 쿼리)

총 쿼리 수: 2번 (N+1 문제 해결)
```

#### 5.2.3 getAllCourses - 전체 강의 목록

```java
public Page<CourseResponse> getAllCourses(Pageable pageable) {
    log.info("Getting all courses");

    return courseRepository.findAll(pageable)
            .map(CourseResponse::from);
}
```

**Pageable 파라미터**:
```java
// Controller에서 전달되는 기본값
@PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)

// 실행 쿼리
SELECT c.* FROM courses c
ORDER BY c.created_at DESC
LIMIT 12 OFFSET 0
```

**Page 객체 구조**:
```json
{
  "content": [/* CourseResponse 배열 */],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 12
  },
  "totalElements": 100,
  "totalPages": 9,
  "size": 12,
  "number": 0,
  "first": true,
  "last": false
}
```

#### 5.2.4 getPopularCourses - 인기 강의 조회

```java
public Page<CourseResponse> getPopularCourses(Pageable pageable) {
    log.info("Getting popular courses");

    // 평점 내림차순, 수강생 수 내림차순 정렬
    Pageable sortedPageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by(Sort.Direction.DESC, "rating", "studentCount")
    );

    return courseRepository.findAll(sortedPageable)
            .map(CourseResponse::from);
}
```

**정렬 로직**:
1. 1차 정렬: rating (평점 높은 순)
2. 2차 정렬: studentCount (수강생 많은 순)

**실행 쿼리**:
```sql
SELECT c.* FROM courses c
ORDER BY c.rating DESC, c.student_count DESC
LIMIT 12
```

#### 5.2.5 getMyCourses - 내 강의 목록

```java
public List<CourseResponse> getMyCourses() {
    Long instructorId = getCurrentUserId();
    log.info("Getting courses for instructor ID: {}", instructorId);

    return courseRepository.findByInstructor_Id(instructorId)
            .stream()
            .map(CourseResponse::from)
            .collect(Collectors.toList());
}
```

**특징**:
- 페이징 없이 전체 목록 반환
- 현재 사용자(강사)의 강의만 조회
- 더미 사용자 ID = 1L 사용

#### 5.2.6 deleteCourse - 강의 삭제

```java
@Transactional
public void deleteCourse(Long courseId) {
    log.info("Deleting course with ID: {}", courseId);

    // 1. 강의 조회
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

    // 2. 권한 확인 (본인만 삭제 가능)
    Long currentUserId = getCurrentUserId();
    if (!course.getInstructorId().equals(currentUserId)) {
        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    // 3. 삭제 (Cascade로 Section, Lecture도 함께 삭제)
    courseRepository.delete(course);
}
```

**삭제 순서** (Cascade):
```
1. Lecture 삭제 (lecture 테이블)
2. Section 삭제 (section 테이블)
3. Course 삭제 (courses 테이블)
```

**권한 검증**:
- 강의 작성자(instructorId)와 현재 사용자 ID 비교
- 불일치 시 `FORBIDDEN` 예외 발생

### 5.3 트랜잭션 전략

```java
@Transactional(readOnly = true)  // 클래스 레벨
public class CourseService {

    @Transactional  // 쓰기 작업에만 readOnly=false
    public CourseResponse createCourse(...) { }

    @Transactional
    public void deleteCourse(...) { }

    // 나머지는 readOnly=true 상속
}
```

**이점**:
- 읽기 전용 트랜잭션: flush 생략, 성능 향상
- 쓰기 작업만 명시적으로 `@Transactional` 추가

---

## 6. Controller 계층

### 6.1 CourseController 전체 구조

```java
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(...);

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseDetail(...);

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getCoursesByCategory(...);

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> searchCourses(...);

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getPopularCourses(...);

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(...);

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses();

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(...);
}
```

### 6.2 엔드포인트별 상세

#### 6.2.1 GET /api/v1/courses - 전체 강의 목록

```java
@GetMapping
public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(
        @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {

    Page<CourseResponse> courses = courseService.getAllCourses(pageable);
    return ResponseEntity.ok(ApiResponse.success(courses));
}
```

**요청 예시**:
```
GET /api/v1/courses?page=0&size=12&sort=createdAt,DESC
```

**응답 예시**:
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "instructorId": 1,
        "title": "자바 완전 정복",
        "description": "자바 기초부터 심화까지",
        "thumbnail": "https://example.com/java.png",
        "category": "Development",
        "price": 50000,
        "originalPrice": 70000,
        "discount": 20000,
        "rating": 4.5,
        "reviewCount": 120,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-01T10:00:00",
        "updatedAt": "2024-01-01T10:00:00"
      }
    ],
    "pageable": { "pageNumber": 0, "pageSize": 12 },
    "totalElements": 2,
    "totalPages": 1
  }
}
```

#### 6.2.2 GET /api/v1/courses/{courseId} - 강의 상세

```java
@GetMapping("/{courseId}")
public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseDetail(
        @PathVariable Long courseId) {

    CourseDetailResponse course = courseService.getCourseDetail(courseId);
    return ResponseEntity.ok(ApiResponse.success(course));
}
```

**요청 예시**:
```
GET /api/v1/courses/1
```

**응답 예시**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "instructorId": 1,
    "title": "자바 완전 정복",
    "description": "자바 기초부터 심화까지",
    "detailedDescription": "자바의 모든 것을 다룹니다...",
    "thumbnail": "https://example.com/java.png",
    "category": "Development",
    "price": 50000,
    "originalPrice": 70000,
    "discount": 20000,
    "rating": 4.5,
    "reviewCount": 120,
    "studentCount": 0,
    "features": ["평생 수강", "수료증 제공", "Q&A 답변"],
    "status": "PUBLISHED",
    "sections": [
      {
        "id": 1,
        "title": "자바 기초",
        "order": 1,
        "lectures": [
          {
            "id": 1,
            "title": "변수와 자료형",
            "duration": 600,
            "order": 1,
            "videoUrl": "https://youtu.be/example1",
            "isFree": true
          },
          {
            "id": 2,
            "title": "연산자",
            "duration": 900,
            "order": 2,
            "videoUrl": "https://youtu.be/example2",
            "isFree": false
          }
        ]
      },
      {
        "id": 2,
        "title": "객체 지향 프로그래밍",
        "order": 2,
        "lectures": [
          {
            "id": 3,
            "title": "클래스와 객체",
            "duration": 1200,
            "order": 1,
            "videoUrl": "https://youtu.be/example3",
            "isFree": false
          }
        ]
      }
    ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

#### 6.2.3 GET /api/v1/courses/category/{category} - 카테고리별 조회

```java
@GetMapping("/category/{category}")
public ResponseEntity<ApiResponse<Page<CourseResponse>>> getCoursesByCategory(
        @PathVariable String category,
        @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {

    Page<CourseResponse> courses = courseService.getCoursesByCategory(category, pageable);
    return ResponseEntity.ok(ApiResponse.success(courses));
}
```

**요청 예시**:
```
GET /api/v1/courses/category/Development?page=0&size=12
```

#### 6.2.4 GET /api/v1/courses/search - 검색

```java
@GetMapping("/search")
public ResponseEntity<ApiResponse<Page<CourseResponse>>> searchCourses(
        @RequestParam String keyword,
        @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {

    Page<CourseResponse> courses = courseService.searchCourses(keyword, pageable);
    return ResponseEntity.ok(ApiResponse.success(courses));
}
```

**요청 예시**:
```
GET /api/v1/courses/search?keyword=자바&page=0&size=12
```

#### 6.2.5 GET /api/v1/courses/popular - 인기 강의

```java
@GetMapping("/popular")
public ResponseEntity<ApiResponse<Page<CourseResponse>>> getPopularCourses(
        @PageableDefault(size = 12) Pageable pageable) {

    Page<CourseResponse> courses = courseService.getPopularCourses(pageable);
    return ResponseEntity.ok(ApiResponse.success(courses));
}
```

**요청 예시**:
```
GET /api/v1/courses/popular?page=0&size=12
```

#### 6.2.6 POST /api/v1/courses - 강의 생성

```java
@PostMapping
// @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
        @Valid @RequestBody CreateCourseRequest request) {

    CourseResponse course = courseService.createCourse(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(course));
}
```

**요청 예시**:
```json
POST /api/v1/courses
Content-Type: application/json

{
  "title": "자바 완전 정복",
  "description": "자바 기초부터 심화까지",
  "detailedDescription": "자바의 모든 것을 다룹니다",
  "thumbnail": "https://example.com/java.png",
  "category": "Development",
  "price": 50000,
  "originalPrice": 70000,
  "discount": 20000,
  "features": ["평생 수강", "수료증 제공"],
  "sections": [
    {
      "title": "자바 기초",
      "order": 1,
      "lectures": [
        {
          "title": "변수와 자료형",
          "videoUrl": "https://youtu.be/example1",
          "duration": 600,
          "order": 1,
          "isFree": true
        }
      ]
    }
  ]
}
```

**응답 예시**:
```json
{
  "success": true,
  "data": {
    "id": 3,
    "instructorId": 1,
    "title": "자바 완전 정복",
    ...
  }
}
```

#### 6.2.7 GET /api/v1/courses/my - 내 강의 목록

```java
@GetMapping("/my")
// @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses() {
    List<CourseResponse> courses = courseService.getMyCourses();
    return ResponseEntity.ok(ApiResponse.success(courses));
}
```

**요청 예시**:
```
GET /api/v1/courses/my
```

#### 6.2.8 DELETE /api/v1/courses/{courseId} - 강의 삭제

```java
@DeleteMapping("/{courseId}")
// @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
public ResponseEntity<ApiResponse<Void>> deleteCourse(
        @PathVariable Long courseId) {

    courseService.deleteCourse(courseId);
    return ResponseEntity.ok(ApiResponse.success(null));
}
```

**요청 예시**:
```
DELETE /api/v1/courses/1
```

**응답 예시**:
```json
{
  "success": true,
  "data": null
}
```

### 6.3 공통 응답 구조

```java
// ApiResponse 클래스
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
```

---

## 7. DTO 구조

### 7.1 Request DTO

#### 7.1.1 CreateCourseRequest

```java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

    @NotBlank(message = "강의 제목은 필수입니다")
    private String title;

    @NotBlank(message = "강의 설명은 필수입니다")
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

    private Integer originalPrice;
    private Integer discount;

    private List<String> features;

    private List<CreateSectionRequest> sections;

    // 중첩 클래스: Section 생성 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSectionRequest {

        @NotBlank(message = "섹션 제목은 필수입니다")
        private String title;

        @NotNull(message = "섹션 순서는 필수입니다")
        private Integer order;

        private List<CreateLectureRequest> lectures;
    }

    // 중첩 클래스: Lecture 생성 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateLectureRequest {

        @NotBlank(message = "강의 제목은 필수입니다")
        private String title;

        @NotBlank(message = "영상 URL은 필수입니다")
        private String videoUrl;

        private Integer duration;

        @NotNull(message = "강의 순서는 필수입니다")
        private Integer order;

        private Boolean isFree;
    }
}
```

**검증 어노테이션**:
- `@NotBlank`: 문자열 필수 (null, 빈 문자열, 공백 불가)
- `@NotNull`: null 불가
- `@Min`: 최소값 검증

### 7.2 Response DTO

#### 7.2.1 CourseResponse (목록용)

```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private Long instructorId;
    private String title;
    private String description;
    private String thumbnail;
    private String category;
    private Integer price;
    private Integer originalPrice;
    private Integer discount;
    private Double rating;
    private Integer reviewCount;
    private Integer studentCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity → DTO 변환
    public static CourseResponse from(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .instructorId(course.getInstructorId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnail(course.getThumbnail())
                .category(course.getCategory())
                .price(course.getPrice())
                .originalPrice(course.getOriginalPrice())
                .discount(course.getDiscount())
                .rating(course.getRating())
                .reviewCount(course.getReviewCount())
                .studentCount(course.getStudentCount())
                .status(course.getStatus().name())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
```

**특징**:
- Section, Lecture 정보 없음 (목록 조회용)
- `from()` 정적 메서드로 변환

#### 7.2.2 CourseDetailResponse (상세용)

```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailResponse {
    private Long id;
    private Long instructorId;
    private String title;
    private String description;
    private String detailedDescription;  // 추가
    private String thumbnail;
    private String category;
    private Integer price;
    private Integer originalPrice;
    private Integer discount;
    private Double rating;
    private Integer reviewCount;
    private Integer studentCount;
    private List<String> features;       // 추가
    private String status;
    private List<SectionResponse> sections;  // 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CourseDetailResponse from(Course course) {
        return CourseDetailResponse.builder()
                .id(course.getId())
                .instructorId(course.getInstructorId())
                .title(course.getTitle())
                .description(course.getDescription())
                .detailedDescription(course.getDetailedDescription())
                .thumbnail(course.getThumbnail())
                .category(course.getCategory())
                .price(course.getPrice())
                .originalPrice(course.getOriginalPrice())
                .discount(course.getDiscount())
                .rating(course.getRating())
                .reviewCount(course.getReviewCount())
                .studentCount(course.getStudentCount())
                .features(course.getFeatures())
                .status(course.getStatus().name())
                .sections(course.getSections().stream()
                        .map(SectionResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
```

**CourseResponse와 차이**:
- `detailedDescription`: 상세 설명 추가
- `features`: 기능 목록 추가
- `sections`: 섹션 목록 추가 (중첩 구조)

#### 7.2.3 SectionResponse

```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {
    private Long id;
    private String title;
    private Integer order;
    private List<LectureResponse> lectures;

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .order(section.getOrder())
                .lectures(section.getLectures().stream()
                        .map(LectureResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
```

#### 7.2.4 LectureResponse

```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureResponse {
    private Long id;
    private String title;
    private Integer duration;
    private Integer order;
    private String videoUrl;
    private Boolean isFree;

    public static LectureResponse from(Lecture lecture) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .duration(lecture.getDuration())
                .order(lecture.getOrder())
                .videoUrl(lecture.getVideoUrl())
                .isFree(lecture.getIsFree())
                .build();
    }
}
```

### 7.3 DTO 변환 흐름

```
Entity → DTO 변환 (Service 계층에서 수행)

Course 엔티티
  ↓ CourseResponse.from(course)
CourseResponse DTO

Course 엔티티 (sections 포함)
  ↓ CourseDetailResponse.from(course)
  ├─ SectionResponse.from(section)
  │   └─ LectureResponse.from(lecture)
CourseDetailResponse DTO
```

---

## 8. 데이터베이스 스키마

### 8.1 ERD

```
┌─────────────────────────────────┐
│          courses                │
├─────────────────────────────────┤
│ id (PK)                  BIGINT │
│ instructor_id (FK)       BIGINT │──┐
│ title                   VARCHAR │  │
│ description             VARCHAR │  │
│ detailed_description      TEXT  │  │
│ thumbnail               VARCHAR │  │
│ category                VARCHAR │  │
│ price                       INT │  │
│ original_price              INT │  │
│ discount                    INT │  │
│ rating                   DOUBLE │  │
│ review_count                INT │  │
│ student_count               INT │  │
│ features                   TEXT │  │
│ status                  VARCHAR │  │
│ created_at             DATETIME │  │
│ updated_at             DATETIME │  │
└─────────────────────────────────┘  │
          ↓ 1:N                      │
┌─────────────────────────────────┐  │
│         sections                │  │
├─────────────────────────────────┤  │
│ id (PK)                  BIGINT │  │
│ course_id (FK)           BIGINT │  │
│ title                   VARCHAR │  │
│ section_order               INT │  │
│ created_at             DATETIME │  │
│ updated_at             DATETIME │  │
└─────────────────────────────────┘  │
          ↓ 1:N                      │
┌─────────────────────────────────┐  │
│         lectures                │  │
├─────────────────────────────────┤  │
│ id (PK)                  BIGINT │  │
│ section_id (FK)          BIGINT │  │
│ title                   VARCHAR │  │
│ video_url               VARCHAR │  │
│ duration                    INT │  │
│ lecture_order               INT │  │
│ is_free                 BOOLEAN │  │
│ created_at             DATETIME │  │
│ updated_at             DATETIME │  │
└─────────────────────────────────┘  │
                                     │
┌─────────────────────────────────┐  │
│           users                 │  │
├─────────────────────────────────┤  │
│ id (PK)                  BIGINT │←─┘
│ email                   VARCHAR │
│ password                VARCHAR │
│ name                    VARCHAR │
│ role                    VARCHAR │
│ created_at             DATETIME │
│ updated_at             DATETIME │
└─────────────────────────────────┘
```

### 8.2 테이블 정의

#### 8.2.1 courses 테이블

```sql
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    instructor_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    detailed_description TEXT,
    thumbnail VARCHAR(500),
    category VARCHAR(100),
    price INT DEFAULT 0,
    original_price INT DEFAULT 0,
    discount INT DEFAULT 0,
    rating DOUBLE DEFAULT 0.0,
    review_count INT DEFAULT 0,
    student_count INT DEFAULT 0,
    features TEXT,  -- JSON 배열 문자열
    status VARCHAR(20) DEFAULT 'DRAFT',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    FOREIGN KEY (instructor_id) REFERENCES users(id),
    INDEX idx_category (category),
    INDEX idx_instructor_id (instructor_id),
    INDEX idx_status (status),
    INDEX idx_rating (rating DESC),
    INDEX idx_created_at (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**인덱스 설명**:
- `idx_category`: 카테고리별 조회 최적화
- `idx_instructor_id`: 강사별 강의 조회 최적화
- `idx_rating`: 인기 강의 정렬 최적화
- `idx_created_at`: 최신순 정렬 최적화

#### 8.2.2 sections 테이블

```sql
CREATE TABLE sections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    section_order INT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_order (section_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**ON DELETE CASCADE**: Course 삭제 시 Section도 자동 삭제

#### 8.2.3 lectures 테이블

```sql
CREATE TABLE lectures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    video_url VARCHAR(500) NOT NULL,
    duration INT DEFAULT 0,  -- 초 단위
    lecture_order INT NOT NULL,
    is_free BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE,
    INDEX idx_section_id (section_id),
    INDEX idx_order (lecture_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 8.3 초기 데이터 (data.sql)

```sql
-- 사용자 (강사)
INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES
('instructor@test.com', 'password123', '김강사', 'INSTRUCTOR', NOW(), NOW());

-- 강의
INSERT INTO courses (
    title, description, detailed_description, thumbnail, category,
    price, original_price, discount, rating, review_count, student_count,
    features, status, instructor_id, created_at, updated_at
) VALUES (
    '자바 완전 정복',
    '자바 기초부터 심화까지',
    '자바의 모든 것을 다룹니다...',
    'https://example.com/java.png',
    'Development',
    50000, 70000, 20000,
    4.5, 120, 0,
    '["평생 수강", "수료증 제공", "Q&A 답변"]',
    'PUBLISHED',
    1,
    NOW(), NOW()
);

-- 섹션
INSERT INTO sections (title, section_order, course_id, created_at, updated_at) VALUES
('자바 기초', 1, 1, NOW(), NOW()),
('객체 지향 프로그래밍', 2, 1, NOW(), NOW());

-- 강의 영상
INSERT INTO lectures (title, video_url, duration, is_free, lecture_order, section_id, created_at, updated_at) VALUES
('변수와 자료형', 'https://youtu.be/example1', 600, true, 1, 1, NOW(), NOW()),
('연산자', 'https://youtu.be/example2', 900, false, 2, 1, NOW(), NOW()),
('클래스와 객체', 'https://youtu.be/example3', 1200, false, 1, 2, NOW(), NOW());
```

---

## 9. API 명세

### 9.1 전체 API 목록

| Method | Endpoint | 설명 | 페이징 |
|--------|----------|------|--------|
| GET | /api/v1/courses | 전체 강의 목록 | O |
| GET | /api/v1/courses/{courseId} | 강의 상세 조회 | X |
| GET | /api/v1/courses/category/{category} | 카테고리별 강의 | O |
| GET | /api/v1/courses/search | 키워드 검색 | O |
| GET | /api/v1/courses/popular | 인기 강의 | O |
| POST | /api/v1/courses | 강의 생성 | X |
| GET | /api/v1/courses/my | 내 강의 목록 | X |
| DELETE | /api/v1/courses/{courseId} | 강의 삭제 | X |

### 9.2 공통 사항

**Base URL**: `http://localhost:8080`

**응답 포맷**: JSON

**공통 응답 구조**:
```json
{
  "success": true,  // 성공 여부
  "data": {},       // 실제 데이터
  "message": null   // 에러 메시지 (에러 시에만)
}
```

**페이징 파라미터**:
- `page`: 페이지 번호 (0부터 시작, 기본값: 0)
- `size`: 페이지 크기 (기본값: 12)
- `sort`: 정렬 기준 (기본값: createdAt,DESC)

### 9.3 상태 코드

| 코드 | 설명 |
|------|------|
| 200 | 조회/삭제 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 (검증 실패) |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 500 | 서버 오류 |

---

## 10. 주요 설계 결정사항

### 10.1 UUID → Long ID 전환

**이유**:
1. **성능**: Long(8바이트) < UUID(16바이트) → 인덱스 크기 절반
2. **정렬**: AUTO_INCREMENT로 삽입 순서 보장
3. **가독성**: 1, 2, 3... 형태로 디버깅 용이
4. **호환성**: 대부분의 레거시 시스템과 호환

**트레이드오프**:
- UUID: 분산 환경에서 ID 충돌 없음
- Long: 중앙 집중식 ID 생성 (단일 DB 환경에 적합)

### 10.2 instructorId 필드 분리

**기존 설계 (User 의존)**:
```java
@ManyToOne
@JoinColumn(name = "instructor_id")
private User instructor;
```

**현재 설계 (의존성 최소화)**:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "instructor_id", insertable = false, updatable = false)
private User instructor;

@Column(name = "instructor_id", nullable = false)
private Long instructorId;
```

**장점**:
1. Course 생성 시 User 엔티티 조회 불필요
2. User 도메인 변경이 Course에 영향 최소화
3. 필요시에만 instructor 관계 로딩 가능

### 10.3 @PreAuthorize 주석 처리

**이유**:
- 인증 시스템이 아직 완성되지 않음
- 더미 사용자(ID=1)로 테스트 진행 중
- 추후 인증 시스템 완성 후 주석 해제 예정

**주석 처리 위치**:
```java
// @PreAuthorize("hasRole('INSTRUCTOR')")
public ResponseEntity<...> createCourse(...) { }
```

### 10.4 briefIntroduction → description 변경

**이유**:
- API 네이밍 간소화
- 프론트엔드와 협의된 명명 규칙 준수

**변경 사항**:
- Entity 필드명: description
- DTO 필드명: description
- DB 컬럼명: description

### 10.5 Cascade 전략

```java
@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Section> sections;

@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Lecture> lectures;
```

**이유**:
- Course 저장 시 Section, Lecture도 함께 저장
- Course 삭제 시 Section, Lecture도 자동 삭제
- 강의는 독립적으로 존재할 수 없는 집약 관계

### 10.6 features JSON 저장

**선택지**:
1. 별도 테이블 생성 (course_features)
2. JSON 컬럼 사용

**결정**: JSON 컬럼 사용

**이유**:
- features는 조회만 하고 검색/필터링 안 함
- 테이블 조인 오버헤드 제거
- CRUD 작업 단순화

**구현**:
```java
@Convert(converter = StringListJsonConverter.class)
private List<String> features;
```

### 10.7 BaseTimeEntity 분리

**구조**:
```
BaseCreateEntity (createdAt만)
    ↓ 상속
BaseTimeEntity (createdAt + updatedAt)
```

**이유**:
- 일부 엔티티는 생성일만 필요할 수 있음
- 유연한 상속 구조
- 도메인별 요구사항 대응

---

## 11. 성능 최적화

### 11.1 N+1 문제 해결

#### 문제 상황
```java
// Course 1개 조회 시
Course course = courseRepository.findById(1L).get();

// sections 접근 시 쿼리 실행
List<Section> sections = course.getSections();  // SELECT * FROM sections WHERE course_id = 1

// 각 section의 lectures 접근 시 쿼리 실행
for (Section section : sections) {
    List<Lecture> lectures = section.getLectures();  // SELECT * FROM lectures WHERE section_id = ?
}

// 총 쿼리 수: 1 (Course) + N (Sections) + M (Lectures) = 1 + N + M
```

#### 해결 방법 1: JOIN FETCH

```java
@Query("""
    SELECT DISTINCT c
    FROM Course c
    LEFT JOIN FETCH c.sections s
    WHERE c.id = :id
""")
Optional<Course> findDetailWithSections(@Param("id") Long id);
```

**효과**: Course + Section을 1번의 쿼리로 조회

#### 해결 방법 2: @BatchSize

```java
@OneToMany(mappedBy = "section", ...)
@BatchSize(size = 20)
private List<Lecture> lectures;
```

**동작 방식**:
```sql
-- Lecture 조회 시 IN 절로 배치 로딩
SELECT * FROM lectures
WHERE section_id IN (1, 2, 3, ..., 20)
```

**최종 쿼리 수**:
- Course + Section: 1번 (JOIN FETCH)
- Lecture: 1번 (배치 로딩)
- **총 2번의 쿼리로 완료**

### 11.2 페이징 최적화

#### Count 쿼리 분리
```java
Page<Course> findAll(Pageable pageable);
```

**실행 쿼리**:
```sql
-- 1. 데이터 조회
SELECT c.* FROM courses c
ORDER BY c.created_at DESC
LIMIT 12 OFFSET 0;

-- 2. 전체 개수 (자동 실행)
SELECT COUNT(c.id) FROM courses c;
```

### 11.3 인덱스 전략

```sql
-- 자주 사용되는 조회 패턴에 인덱스 생성
INDEX idx_category (category)           -- 카테고리별 조회
INDEX idx_instructor_id (instructor_id) -- 강사별 조회
INDEX idx_rating (rating DESC)          -- 인기 강의 정렬
INDEX idx_created_at (created_at DESC)  -- 최신순 정렬
```

**인덱스 사용 예시**:
```sql
-- idx_category 사용
SELECT * FROM courses WHERE category = 'Development';

-- idx_rating 사용
SELECT * FROM courses ORDER BY rating DESC, student_count DESC;

-- idx_created_at 사용
SELECT * FROM courses ORDER BY created_at DESC;
```

### 11.4 Lazy Loading 전략

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "instructor_id", ...)
private User instructor;
```

**이유**:
- 대부분의 경우 instructor 정보 불필요
- 필요시에만 로딩하여 불필요한 조인 방지

### 11.5 @Transactional(readOnly = true)

```java
@Transactional(readOnly = true)
public class CourseService {
    // 읽기 전용 트랜잭션
}
```

**효과**:
1. flush 모드 MANUAL로 설정 → 변경 감지 스킵
2. 스냅샷 저장 생략
3. 약간의 성능 향상

### 11.6 DTO 변환 시점

```java
// Service에서 DTO 변환
public Page<CourseResponse> getAllCourses(Pageable pageable) {
    return courseRepository.findAll(pageable)
            .map(CourseResponse::from);  // Entity → DTO
}
```

**이유**:
- Controller에 Entity 노출 방지
- 필요한 필드만 응답에 포함
- JSON 직렬화 성능 향상

---

## 12. 예외 처리

### 12.1 CustomException

```java
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```

### 12.2 ErrorCode

```java
public enum ErrorCode {
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다"),
    FORBIDDEN(403, "권한이 없습니다"),
    INVALID_INPUT(400, "잘못된 입력입니다");

    private final int status;
    private final String message;
}
```

### 12.3 사용 예시

```java
// 강의 없음
Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

// 권한 없음
if (!course.getInstructorId().equals(currentUserId)) {
    throw new CustomException(ErrorCode.FORBIDDEN);
}
```

---

## 13. 테스트 시나리오

### 13.1 강의 생성 플로우

```
1. POST /api/v1/courses
   ├─ 요청: CreateCourseRequest (섹션, 강의 포함)
   ├─ 처리: CourseService.createCourse()
   │   ├─ Course 엔티티 생성
   │   ├─ Section 엔티티 생성 및 연결
   │   ├─ Lecture 엔티티 생성 및 연결
   │   └─ DB 저장 (Cascade)
   └─ 응답: CourseResponse (생성된 강의 정보)

2. 검증
   ├─ GET /api/v1/courses/my → 내 강의 목록에 포함 확인
   └─ GET /api/v1/courses/{id} → 섹션, 강의 포함 확인
```

### 13.2 강의 조회 플로우

```
1. GET /api/v1/courses?page=0&size=12
   ├─ CourseService.getAllCourses()
   ├─ DB 쿼리: SELECT * FROM courses LIMIT 12
   └─ 응답: Page<CourseResponse>

2. GET /api/v1/courses/1
   ├─ CourseService.getCourseDetail()
   ├─ DB 쿼리 1: Course + Section JOIN FETCH
   ├─ DB 쿼리 2: Lecture 배치 로딩
   └─ 응답: CourseDetailResponse (섹션, 강의 포함)
```

### 13.3 강의 삭제 플로우

```
1. DELETE /api/v1/courses/1
   ├─ CourseService.deleteCourse()
   ├─ 강의 조회 및 존재 확인
   ├─ 권한 검증 (instructorId 비교)
   ├─ DB 삭제 (Cascade)
   │   ├─ DELETE FROM lectures WHERE section_id IN (...)
   │   ├─ DELETE FROM sections WHERE course_id = 1
   │   └─ DELETE FROM courses WHERE id = 1
   └─ 응답: ApiResponse<Void>

2. 검증
   └─ GET /api/v1/courses/1 → 404 NOT_FOUND
```

---

## 14. 향후 개선 사항

### 14.1 인증 시스템 연동
- Spring Security 통합
- JWT 토큰 인증
- @PreAuthorize 주석 해제
- getCurrentUserId() 실제 구현

### 14.2 파일 업로드
- 썸네일 업로드 API
- S3 연동
- 이미지 리사이징

### 14.3 검색 개선
- Elasticsearch 도입
- 전문 검색 기능
- 자동완성

### 14.4 캐싱
- Redis 캐싱
- 인기 강의 캐시
- 강의 상세 캐시

### 14.5 통계 기능
- 수강생 수 실시간 업데이트
- 평점 계산 자동화
- 강의 조회수 추적

---

**문서 작성일**: 2024-01-22
**작성자**: SeSAC Run Team
**버전**: 1.0.0
