# Course API - Postman 테스트 가이드

## 목차
1. [환경 설정](#1-환경-설정)
2. [API 엔드포인트 목록](#2-api-엔드포인트-목록)
3. [상세 API 명세](#3-상세-api-명세)
4. [테스트 시나리오](#4-테스트-시나리오)
5. [에러 응답](#5-에러-응답)
6. [Postman Collection](#6-postman-collection)

---

## 1. 환경 설정

### 1.1 Base URL
```
http://localhost:8080
```

### 1.2 Postman 환경 변수 설정

Postman에서 Environment 생성:

```json
{
  "name": "SeSAC Run - Local",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "enabled": true
    },
    {
      "key": "api_version",
      "value": "v1",
      "enabled": true
    }
  ]
}
```

### 1.3 공통 Headers

모든 요청에 다음 헤더 추가:

```
Content-Type: application/json
Accept: application/json
```

---

## 2. API 엔드포인트 목록

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/api/v1/courses` | 전체 강의 목록 조회 | - |
| GET | `/api/v1/courses/{courseId}` | 강의 상세 조회 | - |
| GET | `/api/v1/courses/category/{category}` | 카테고리별 강의 조회 | - |
| GET | `/api/v1/courses/search` | 강의 검색 | - |
| GET | `/api/v1/courses/popular` | 인기 강의 조회 | - |
| POST | `/api/v1/courses` | 강의 생성 | 필요 (추후) |
| GET | `/api/v1/courses/my` | 내 강의 목록 조회 | 필요 (추후) |
| DELETE | `/api/v1/courses/{courseId}` | 강의 삭제 | 필요 (추후) |

---

## 3. 상세 API 명세

### 3.1 전체 강의 목록 조회

#### Request

```
GET {{base_url}}/api/v1/courses?page=0&size=12&sort=createdAt,DESC
```

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | 페이지 번호 (0부터 시작) |
| size | Integer | No | 12 | 페이지당 항목 수 |
| sort | String | No | createdAt,DESC | 정렬 기준 (필드명,방향) |

**정렬 옵션:**
- `createdAt,DESC` - 최신순
- `createdAt,ASC` - 오래된순
- `rating,DESC` - 평점 높은순
- `price,ASC` - 가격 낮은순

#### Response (200 OK)

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
        "rating": 4.5,
        "reviewCount": 120,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-22T10:00:00",
        "updatedAt": "2024-01-22T10:00:00"
      },
      {
        "id": 2,
        "instructorId": 1,
        "title": "스프링 부트 입문",
        "description": "웹 개발의 시작 스프링 부트",
        "thumbnail": "https://example.com/spring.png",
        "category": "Web",
        "price": 60000,
        "rating": 4.7,
        "reviewCount": 85,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-22T11:00:00",
        "updatedAt": "2024-01-22T11:00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 12,
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 2,
    "totalPages": 1,
    "size": 12,
    "number": 0,
    "first": true,
    "last": true,
    "numberOfElements": 2,
    "empty": false
  },
  "message": null
}
```

#### Postman 테스트 스크립트

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has success field", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
});

pm.test("Content is array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.data.content).to.be.an('array');
});

pm.test("First course has required fields", function () {
    var jsonData = pm.response.json();
    if (jsonData.data.content.length > 0) {
        var course = jsonData.data.content[0];
        pm.expect(course).to.have.property('id');
        pm.expect(course).to.have.property('title');
        pm.expect(course).to.have.property('price');
    }
});
```

---

### 3.2 강의 상세 조회

#### Request

```
GET {{base_url}}/api/v1/courses/1
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| courseId | Long | Yes | 조회할 강의 ID |

#### Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "instructorId": 1,
    "title": "자바 완전 정복",
    "description": "자바 기초부터 심화까지",
    "detailedDescription": "자바의 모든 것을 다룹니다. 변수, 연산자, 제어문부터 객체지향 프로그래밍까지 체계적으로 학습합니다.",
    "thumbnail": "https://example.com/java.png",
    "category": "Development",
    "price": 50000,
    "rating": 4.5,
    "reviewCount": 120,
    "studentCount": 0,
    "features": [
      "평생 수강",
      "수료증 제공",
      "Q&A 답변"
    ],
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
    "createdAt": "2024-01-22T10:00:00",
    "updatedAt": "2024-01-22T10:00:00"
  },
  "message": null
}
```

#### Response (404 Not Found)

```json
{
  "success": false,
  "data": null,
  "message": "요청한 리소스를 찾을 수 없습니다"
}
```

#### Postman 테스트 스크립트

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Course has sections", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.data.sections).to.be.an('array');
});

pm.test("Sections have lectures", function () {
    var jsonData = pm.response.json();
    if (jsonData.data.sections.length > 0) {
        pm.expect(jsonData.data.sections[0].lectures).to.be.an('array');
    }
});

pm.test("Features is array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.data.features).to.be.an('array');
});
```

---

### 3.3 카테고리별 강의 조회

#### Request

```
GET {{base_url}}/api/v1/courses/category/Development?page=0&size=12
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| category | String | Yes | 카테고리명 |

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | 페이지 번호 |
| size | Integer | No | 12 | 페이지당 항목 수 |

**카테고리 목록:**
- `Development` - 개발
- `Web` - 웹 개발
- `Mobile` - 모바일
- `Data` - 데이터 사이언스
- `Design` - 디자인
- `Business` - 비즈니스

#### Response (200 OK)

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
        "rating": 4.5,
        "reviewCount": 120,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-22T10:00:00",
        "updatedAt": "2024-01-22T10:00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 12
    },
    "totalElements": 1,
    "totalPages": 1,
    "size": 12,
    "number": 0,
    "first": true,
    "last": true
  },
  "message": null
}
```

---

### 3.4 강의 검색

#### Request

```
GET {{base_url}}/api/v1/courses/search?keyword=자바&page=0&size=12
```

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| keyword | String | Yes | - | 검색 키워드 |
| page | Integer | No | 0 | 페이지 번호 |
| size | Integer | No | 12 | 페이지당 항목 수 |

**검색 범위:**
- 강의 제목 (title)
- 강의 설명 (description)

**검색 특징:**
- 대소문자 구분 없음
- 부분 일치 검색

#### Response (200 OK)

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
        "rating": 4.5,
        "reviewCount": 120,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-22T10:00:00",
        "updatedAt": "2024-01-22T10:00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 12
    },
    "totalElements": 1,
    "totalPages": 1
  },
  "message": null
}
```

#### Postman 테스트 예시

**검색 키워드 예시:**
- `자바` - "자바"가 포함된 강의
- `스프링` - "스프링"이 포함된 강의
- `웹` - "웹"이 포함된 강의
- `JAVA` - 대소문자 구분 없이 "java" 검색

---

### 3.5 인기 강의 조회

#### Request

```
GET {{base_url}}/api/v1/courses/popular?page=0&size=12
```

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | 페이지 번호 |
| size | Integer | No | 12 | 페이지당 항목 수 |

**정렬 기준:**
1. 평점 (rating) 높은 순
2. 수강생 수 (studentCount) 많은 순

#### Response (200 OK)

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 2,
        "instructorId": 1,
        "title": "스프링 부트 입문",
        "description": "웹 개발의 시작 스프링 부트",
        "thumbnail": "https://example.com/spring.png",
        "category": "Web",
        "price": 60000,
        "rating": 4.7,
        "reviewCount": 85,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-22T11:00:00",
        "updatedAt": "2024-01-22T11:00:00"
      },
      {
        "id": 1,
        "instructorId": 1,
        "title": "자바 완전 정복",
        "description": "자바 기초부터 심화까지",
        "thumbnail": "https://example.com/java.png",
        "category": "Development",
        "price": 50000,
        "rating": 4.5,
        "reviewCount": 120,
        "studentCount": 0,
        "status": "PUBLISHED",
        "createdAt": "2024-01-22T10:00:00",
        "updatedAt": "2024-01-22T10:00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 12
    },
    "totalElements": 2,
    "totalPages": 1
  },
  "message": null
}
```

---

### 3.6 강의 생성

#### Request

```
POST {{base_url}}/api/v1/courses
Content-Type: application/json
```

**Request Body:**

```json
{
  "title": "React 완벽 가이드",
  "description": "React 기초부터 고급까지",
  "detailedDescription": "React의 모든 것을 다룹니다. Hooks, Context API, Redux까지 완벽하게 마스터합니다.",
  "thumbnail": "https://example.com/react.png",
  "category": "Web",
  "price": 55000,
  "features": [
    "평생 수강",
    "수료증 제공",
    "Q&A 답변",
    "실습 프로젝트 제공"
  ],
  "sections": [
    {
      "title": "React 기초",
      "order": 1,
      "lectures": [
        {
          "title": "React 소개",
          "videoUrl": "https://youtu.be/react1",
          "duration": 720,
          "order": 1,
          "isFree": true
        },
        {
          "title": "컴포넌트 기초",
          "videoUrl": "https://youtu.be/react2",
          "duration": 900,
          "order": 2,
          "isFree": false
        }
      ]
    },
    {
      "title": "React Hooks",
      "order": 2,
      "lectures": [
        {
          "title": "useState Hook",
          "videoUrl": "https://youtu.be/react3",
          "duration": 1080,
          "order": 1,
          "isFree": false
        },
        {
          "title": "useEffect Hook",
          "videoUrl": "https://youtu.be/react4",
          "duration": 1200,
          "order": 2,
          "isFree": false
        }
      ]
    }
  ]
}
```

**필수 필드:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | String | Yes | 강의 제목 |
| description | String | Yes | 간단한 설명 |
| detailedDescription | String | Yes | 상세 설명 |
| thumbnail | String | Yes | 썸네일 URL |
| category | String | Yes | 카테고리 |
| price | Integer | Yes | 가격 (0 이상) |

**선택 필드:**

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| features | Array | No | [] | 기능 목록 |
| sections | Array | No | [] | 섹션 목록 |

**Section 객체:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | String | Yes | 섹션 제목 |
| order | Integer | Yes | 섹션 순서 |
| lectures | Array | No | 강의 목록 |

**Lecture 객체:**

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| title | String | Yes | - | 강의 제목 |
| videoUrl | String | Yes | - | 영상 URL |
| order | Integer | Yes | - | 강의 순서 |
| duration | Integer | No | 0 | 재생 시간 (초) |
| isFree | Boolean | No | false | 무료 공개 여부 |

#### Response (201 Created)

```json
{
  "success": true,
  "data": {
    "id": 3,
    "instructorId": 1,
    "title": "React 완벽 가이드",
    "description": "React 기초부터 고급까지",
    "thumbnail": "https://example.com/react.png",
    "category": "Web",
    "price": 55000,
    "rating": 0.0,
    "reviewCount": 0,
    "studentCount": 0,
    "status": "PUBLISHED",
    "createdAt": "2024-01-22T15:30:00",
    "updatedAt": "2024-01-22T15:30:00"
  },
  "message": null
}
```

#### Response (400 Bad Request) - 검증 실패

```json
{
  "success": false,
  "data": null,
  "message": "강의 제목은 필수입니다"
}
```

#### Postman Pre-request Script

새 강의 ID를 환경 변수에 저장:

```javascript
// 응답에서 생성된 강의 ID를 저장
pm.test("Save course ID", function () {
    var jsonData = pm.response.json();
    if (jsonData.success && jsonData.data.id) {
        pm.environment.set("created_course_id", jsonData.data.id);
    }
});
```

#### Postman 테스트 스크립트

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Course created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
    pm.expect(jsonData.data).to.have.property('id');
});

pm.test("Default values are set", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.data.rating).to.eql(0.0);
    pm.expect(jsonData.data.reviewCount).to.eql(0);
    pm.expect(jsonData.data.status).to.eql("PUBLISHED");
});
```

---

### 3.7 내 강의 목록 조회

#### Request

```
GET {{base_url}}/api/v1/courses/my
```

**현재 동작:**
- 더미 사용자 ID (1) 기준으로 강의 조회
- 추후 인증 시스템 연동 시 실제 사용자 ID로 변경 예정

#### Response (200 OK)

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "instructorId": 1,
      "title": "자바 완전 정복",
      "description": "자바 기초부터 심화까지",
      "thumbnail": "https://example.com/java.png",
      "category": "Development",
      "price": 50000,
      "rating": 4.5,
      "reviewCount": 120,
      "studentCount": 0,
      "status": "PUBLISHED",
      "createdAt": "2024-01-22T10:00:00",
      "updatedAt": "2024-01-22T10:00:00"
    },
    {
      "id": 2,
      "instructorId": 1,
      "title": "스프링 부트 입문",
      "description": "웹 개발의 시작 스프링 부트",
      "thumbnail": "https://example.com/spring.png",
      "category": "Web",
      "price": 60000,
      "rating": 4.7,
      "reviewCount": 85,
      "studentCount": 0,
      "status": "PUBLISHED",
      "createdAt": "2024-01-22T11:00:00",
      "updatedAt": "2024-01-22T11:00:00"
    }
  ],
  "message": null
}
```

**특징:**
- 페이징 없이 전체 목록 반환
- Array 형태로 반환 (Page 객체 아님)

---

### 3.8 강의 삭제

#### Request

```
DELETE {{base_url}}/api/v1/courses/1
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| courseId | Long | Yes | 삭제할 강의 ID |

**권한 검증:**
- 강의 작성자(instructorId)와 현재 사용자 ID 일치 확인
- 현재는 더미 사용자 ID(1)로 검증

**삭제 범위:**
- Course 삭제 시 관련 Section, Lecture 모두 삭제 (Cascade)

#### Response (200 OK)

```json
{
  "success": true,
  "data": null,
  "message": null
}
```

#### Response (403 Forbidden) - 권한 없음

```json
{
  "success": false,
  "data": null,
  "message": "권한이 없습니다"
}
```

#### Response (404 Not Found) - 강의 없음

```json
{
  "success": false,
  "data": null,
  "message": "요청한 리소스를 찾을 수 없습니다"
}
```

#### Postman 테스트 스크립트

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Deletion successful", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
});

// 삭제 확인: 같은 ID로 조회 시 404 응답
pm.test("Verify deletion", function () {
    pm.sendRequest({
        url: pm.environment.get("base_url") + "/api/v1/courses/1",
        method: 'GET'
    }, function (err, response) {
        pm.expect(response).to.have.status(404);
    });
});
```

---

## 4. 테스트 시나리오

### 4.1 기본 CRUD 플로우

#### 시나리오 1: 강의 생성 → 조회 → 삭제

**Step 1: 강의 생성**
```
POST /api/v1/courses
```
- Request Body에 강의 정보 포함
- 응답에서 생성된 강의 ID 확인 (예: id = 3)

**Step 2: 생성된 강의 상세 조회**
```
GET /api/v1/courses/3
```
- sections와 lectures가 포함되어 있는지 확인
- 입력한 데이터가 정확히 저장되었는지 확인

**Step 3: 내 강의 목록에서 확인**
```
GET /api/v1/courses/my
```
- 방금 생성한 강의가 목록에 포함되어 있는지 확인

**Step 4: 강의 삭제**
```
DELETE /api/v1/courses/3
```
- 삭제 성공 응답 확인

**Step 5: 삭제 확인**
```
GET /api/v1/courses/3
```
- 404 응답 확인

---

### 4.2 검색 및 필터링 테스트

#### 시나리오 2: 다양한 조회 방법 테스트

**Step 1: 전체 강의 목록**
```
GET /api/v1/courses?page=0&size=12&sort=createdAt,DESC
```
- 최신순 정렬 확인

**Step 2: 카테고리별 조회**
```
GET /api/v1/courses/category/Development?page=0&size=12
```
- Development 카테고리 강의만 조회되는지 확인

**Step 3: 키워드 검색**
```
GET /api/v1/courses/search?keyword=자바&page=0&size=12
```
- "자바"가 포함된 강의만 조회되는지 확인

**Step 4: 인기 강의**
```
GET /api/v1/courses/popular?page=0&size=12
```
- rating 높은 순으로 정렬되어 있는지 확인

---

### 4.3 페이징 테스트

#### 시나리오 3: 페이징 동작 확인

**Step 1: 첫 페이지 조회**
```
GET /api/v1/courses?page=0&size=2
```
- totalElements, totalPages 확인
- content 배열 크기가 2인지 확인

**Step 2: 두 번째 페이지 조회**
```
GET /api/v1/courses?page=1&size=2
```
- 다른 강의들이 조회되는지 확인

**Step 3: 마지막 페이지 조회**
```
GET /api/v1/courses?page=10&size=2
```
- content가 빈 배열인지 확인

---

### 4.4 에러 케이스 테스트

#### 시나리오 4: 잘못된 요청 처리

**Test 1: 존재하지 않는 강의 조회**
```
GET /api/v1/courses/99999
```
- 예상 응답: 404 Not Found

**Test 2: 필수 필드 누락**
```json
POST /api/v1/courses
{
  "description": "설명만 있음"
}
```
- 예상 응답: 400 Bad Request
- message: "강의 제목은 필수입니다"

**Test 3: 잘못된 가격 (음수)**
```json
POST /api/v1/courses
{
  "title": "테스트",
  "description": "테스트",
  "detailedDescription": "테스트",
  "thumbnail": "url",
  "category": "Development",
  "price": -1000
}
```
- 예상 응답: 400 Bad Request
- message: "가격은 0원 이상이어야 합니다"

**Test 4: 다른 사용자의 강의 삭제 시도**
```
DELETE /api/v1/courses/1
```
(다른 사용자가 생성한 강의)
- 예상 응답: 403 Forbidden
- 현재는 더미 사용자 ID가 1이므로 instructorId=1인 강의만 삭제 가능

---

### 4.5 복잡한 강의 생성 테스트

#### 시나리오 5: 여러 섹션과 강의를 포함한 강의 생성

```json
POST /api/v1/courses
{
  "title": "풀스택 웹 개발 마스터",
  "description": "프론트엔드부터 백엔드까지",
  "detailedDescription": "HTML, CSS, JavaScript, React, Node.js, MongoDB까지 모든 것을 배웁니다.",
  "thumbnail": "https://example.com/fullstack.png",
  "category": "Web",
  "price": 150000,
  "features": [
    "평생 수강",
    "수료증 제공",
    "1:1 멘토링",
    "실전 프로젝트 10개",
    "취업 지원"
  ],
  "sections": [
    {
      "title": "프론트엔드 기초",
      "order": 1,
      "lectures": [
        {
          "title": "HTML 기초",
          "videoUrl": "https://youtu.be/html1",
          "duration": 1800,
          "order": 1,
          "isFree": true
        },
        {
          "title": "CSS 스타일링",
          "videoUrl": "https://youtu.be/css1",
          "duration": 2400,
          "order": 2,
          "isFree": true
        },
        {
          "title": "JavaScript 기초",
          "videoUrl": "https://youtu.be/js1",
          "duration": 3600,
          "order": 3,
          "isFree": false
        }
      ]
    },
    {
      "title": "React 마스터",
      "order": 2,
      "lectures": [
        {
          "title": "React 시작하기",
          "videoUrl": "https://youtu.be/react1",
          "duration": 1200,
          "order": 1,
          "isFree": false
        },
        {
          "title": "컴포넌트 심화",
          "videoUrl": "https://youtu.be/react2",
          "duration": 2700,
          "order": 2,
          "isFree": false
        }
      ]
    },
    {
      "title": "백엔드 개발",
      "order": 3,
      "lectures": [
        {
          "title": "Node.js 기초",
          "videoUrl": "https://youtu.be/node1",
          "duration": 2100,
          "order": 1,
          "isFree": false
        },
        {
          "title": "Express.js",
          "videoUrl": "https://youtu.be/express1",
          "duration": 1800,
          "order": 2,
          "isFree": false
        },
        {
          "title": "MongoDB 연동",
          "videoUrl": "https://youtu.be/mongo1",
          "duration": 2400,
          "order": 3,
          "isFree": false
        }
      ]
    }
  ]
}
```

**검증 포인트:**
1. 3개의 섹션이 모두 생성되었는지
2. 총 8개의 강의가 모두 생성되었는지
3. 섹션 순서가 유지되는지 (order 필드)
4. 강의 순서가 유지되는지
5. 무료 강의가 올바르게 설정되었는지

---

## 5. 에러 응답

### 5.1 공통 에러 응답 포맷

```json
{
  "success": false,
  "data": null,
  "message": "에러 메시지"
}
```

### 5.2 HTTP 상태 코드별 에러

| 상태 코드 | 설명 | 예시 |
|-----------|------|------|
| 400 | Bad Request | 검증 실패, 잘못된 요청 형식 |
| 403 | Forbidden | 권한 없음 (다른 사용자의 강의 삭제 시도) |
| 404 | Not Found | 존재하지 않는 리소스 조회 |
| 500 | Internal Server Error | 서버 내부 오류 |

### 5.3 검증 오류 메시지

| 필드 | 검증 규칙 | 에러 메시지 |
|------|----------|-------------|
| title | @NotBlank | "강의 제목은 필수입니다" |
| description | @NotBlank | "강의 설명은 필수입니다" |
| detailedDescription | @NotBlank | "상세 설명은 필수입니다" |
| thumbnail | @NotBlank | "썸네일 URL은 필수입니다" |
| category | @NotBlank | "카테고리는 필수입니다" |
| price | @NotNull | "가격은 필수입니다" |
| price | @Min(0) | "가격은 0원 이상이어야 합니다" |
| sections[].title | @NotBlank | "섹션 제목은 필수입니다" |
| sections[].order | @NotNull | "섹션 순서는 필수입니다" |
| lectures[].title | @NotBlank | "강의 제목은 필수입니다" |
| lectures[].videoUrl | @NotBlank | "영상 URL은 필수입니다" |
| lectures[].order | @NotNull | "강의 순서는 필수입니다" |

---

## 6. Postman Collection

### 6.1 Collection 구조

```
📁 SeSAC Run - Course API
├── 📁 1. Courses - Read Operations
│   ├── GET All Courses
│   ├── GET Course Detail
│   ├── GET Courses by Category
│   ├── GET Search Courses
│   └── GET Popular Courses
├── 📁 2. Courses - Write Operations
│   ├── POST Create Course (Simple)
│   ├── POST Create Course (Complex)
│   ├── GET My Courses
│   └── DELETE Course
└── 📁 3. Test Scenarios
    ├── Scenario 1: Full CRUD
    ├── Scenario 2: Search & Filter
    └── Scenario 3: Pagination
```

### 6.2 Collection JSON 파일

다음 내용을 `SeSAC_Run_Course_API.postman_collection.json` 파일로 저장:

```json
{
  "info": {
    "name": "SeSAC Run - Course API",
    "description": "Course API 테스트 Collection",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "1. Courses - Read Operations",
      "item": [
        {
          "name": "GET All Courses",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses?page=0&size=12&sort=createdAt,DESC",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses"],
              "query": [
                {"key": "page", "value": "0"},
                {"key": "size", "value": "12"},
                {"key": "sort", "value": "createdAt,DESC"}
              ]
            }
          }
        },
        {
          "name": "GET Course Detail",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses/1",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses", "1"]
            }
          }
        },
        {
          "name": "GET Courses by Category",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses/category/Development?page=0&size=12",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses", "category", "Development"],
              "query": [
                {"key": "page", "value": "0"},
                {"key": "size", "value": "12"}
              ]
            }
          }
        },
        {
          "name": "GET Search Courses",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses/search?keyword=자바&page=0&size=12",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses", "search"],
              "query": [
                {"key": "keyword", "value": "자바"},
                {"key": "page", "value": "0"},
                {"key": "size", "value": "12"}
              ]
            }
          }
        },
        {
          "name": "GET Popular Courses",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses/popular?page=0&size=12",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses", "popular"],
              "query": [
                {"key": "page", "value": "0"},
                {"key": "size", "value": "12"}
              ]
            }
          }
        }
      ]
    },
    {
      "name": "2. Courses - Write Operations",
      "item": [
        {
          "name": "POST Create Course (Simple)",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"title\": \"Python 기초 완성\",\n  \"description\": \"파이썬 기초부터 실전까지\",\n  \"detailedDescription\": \"Python의 기본 문법부터 실전 프로젝트까지 모두 다룹니다.\",\n  \"thumbnail\": \"https://example.com/python.png\",\n  \"category\": \"Development\",\n  \"price\": 45000,\n  \"features\": [\n    \"평생 수강\",\n    \"수료증 제공\"\n  ]\n}"
            },
            "url": {
              "raw": "{{base_url}}/api/v1/courses",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses"]
            }
          }
        },
        {
          "name": "GET My Courses",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses/my",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses", "my"]
            }
          }
        },
        {
          "name": "DELETE Course",
          "request": {
            "method": "DELETE",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/v1/courses/1",
              "host": ["{{base_url}}"],
              "path": ["api", "v1", "courses", "1"]
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "type": "string"
    }
  ]
}
```

### 6.3 Environment 파일

다음 내용을 `SeSAC_Run_Local.postman_environment.json` 파일로 저장:

```json
{
  "name": "SeSAC Run - Local",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "enabled": true
    },
    {
      "key": "api_version",
      "value": "v1",
      "enabled": true
    },
    {
      "key": "created_course_id",
      "value": "",
      "enabled": true
    }
  ]
}
```

---

## 7. 빠른 테스트 가이드

### 7.1 서버 실행

```bash
cd back/SeSAC-SesacRun-back
./gradlew bootRun
```

서버 시작 확인:
```
http://localhost:8080
```

### 7.2 기본 데이터 확인

서버 시작 시 `data.sql`에 의해 자동으로 삽입되는 데이터:

**Users:**
- ID=1, email=instructor@test.com, name=김강사, role=INSTRUCTOR

**Courses:**
- ID=1: 자바 완전 정복 (Development)
- ID=2: 스프링 부트 입문 (Web)

**Sections & Lectures:**
- Course 1에 2개 섹션, 3개 강의
- Course 2에 1개 섹션, 1개 강의

### 7.3 5분 빠른 테스트

**1단계: 전체 강의 조회 (30초)**
```
GET http://localhost:8080/api/v1/courses
```
→ 2개의 강의가 조회되는지 확인

**2단계: 강의 상세 조회 (30초)**
```
GET http://localhost:8080/api/v1/courses/1
```
→ sections와 lectures가 포함되는지 확인

**3단계: 검색 테스트 (30초)**
```
GET http://localhost:8080/api/v1/courses/search?keyword=자바
```
→ "자바 완전 정복" 강의가 조회되는지 확인

**4단계: 강의 생성 (1분)**
```
POST http://localhost:8080/api/v1/courses
Content-Type: application/json

{
  "title": "테스트 강의",
  "description": "테스트",
  "detailedDescription": "테스트",
  "thumbnail": "https://example.com/test.png",
  "category": "Development",
  "price": 10000
}
```
→ 201 응답과 함께 생성된 ID 확인

**5단계: 내 강의 확인 (30초)**
```
GET http://localhost:8080/api/v1/courses/my
```
→ 방금 생성한 강의가 포함되는지 확인

**6단계: 강의 삭제 (30초)**
```
DELETE http://localhost:8080/api/v1/courses/3
```
(4단계에서 생성된 ID 사용)
→ 200 응답 확인

**7단계: 삭제 확인 (30초)**
```
GET http://localhost:8080/api/v1/courses/3
```
→ 404 응답 확인

---

## 8. 자주 발생하는 문제 해결

### 8.1 서버 연결 실패

**문제:**
```
Could not get any response
```

**해결:**
1. 서버가 실행 중인지 확인
2. 포트 번호 확인 (8080)
3. base_url이 올바른지 확인

### 8.2 404 Not Found

**문제:**
```json
{
  "success": false,
  "message": "요청한 리소스를 찾을 수 없습니다"
}
```

**해결:**
1. URL 경로 확인 (`/api/v1/courses`)
2. courseId가 존재하는지 확인
3. 삭제된 강의인지 확인

### 8.3 400 Bad Request (검증 실패)

**문제:**
```json
{
  "success": false,
  "message": "강의 제목은 필수입니다"
}
```

**해결:**
1. 필수 필드가 모두 포함되었는지 확인
2. 필드 타입이 올바른지 확인 (price는 숫자)
3. JSON 형식이 올바른지 확인

### 8.4 500 Internal Server Error

**문제:**
서버 내부 오류

**해결:**
1. 서버 로그 확인
2. 데이터베이스 연결 확인
3. data.sql이 정상 실행되었는지 확인

---

**문서 작성일**: 2024-01-22
**작성자**: SeSAC Run Team
**버전**: 1.0.0
**Last Updated**: 2024-01-22
