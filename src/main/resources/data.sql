-- Users
INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES
                                                                            ('instructor@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '김강사', 'INSTRUCTOR', NOW(), NOW()),
                                                                            ('user1@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '이학생', 'USER', NOW(), NOW()),
                                                                            ('user2@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '박학생', 'USER', NOW(), NOW()),
                                                                            ('user3@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '최학생', 'USER', NOW(), NOW()),
                                                                            ('user4@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '정학생', 'USER', NOW(), NOW());

-- ========================================
-- Courses (1 ~ 48)
-- ========================================
INSERT INTO courses (
    title,
    description,
    detailed_description,
    thumbnail,
    category,
    level,
    language,
    price,
    student_count,
    status,
    instructor_id,
    created_at,
    updated_at
) VALUES
-- 1 ~ 8 (기본 강의)
('자바 완전 정복','자바 기초부터 심화까지','자바의 모든 것을 다룹니다.',
 'https://picsum.photos/seed/course1/400/300','Development','INTERMEDIATE','KOREAN',0,0,'PUBLISHED',1,NOW(),NOW()),

('스프링 부트 입문','웹 개발의 시작','스프링 부트 REST API',
 'https://picsum.photos/seed/course2/400/300','Web','BEGINNER','KOREAN',1000,0,'PUBLISHED',1,NOW(),NOW()),

('리액트 기초','프론트엔드 입문','React 기본',
 'https://picsum.photos/seed/course3/400/300','Development','BEGINNER','KOREAN',0,1,'PUBLISHED',1,NOW(),NOW()),

('리액트 심화','실전 React','Hooks & 최적화',
 'https://picsum.photos/seed/course4/400/300','Development','INTERMEDIATE','KOREAN',3000,2,'PUBLISHED',1,NOW(),NOW()),

('Node.js 서버 개발','백엔드 기초','Express API',
 'https://picsum.photos/seed/course5/400/300','Development','BEGINNER','KOREAN',2000,1,'PUBLISHED',1,NOW(),NOW()),

('DB 설계','DB 모델링','ERD & 정규화',
 'https://picsum.photos/seed/course6/400/300','Development','INTERMEDIATE','KOREAN',1500,0,'PUBLISHED',1,NOW(),NOW()),

('알고리즘 문제 풀이','코테 대비','알고리즘 정리',
 'https://picsum.photos/seed/course7/400/300','Development','INTERMEDIATE','KOREAN',0,3,'PUBLISHED',1,NOW(),NOW()),

('시스템 디자인 입문','아키텍처','확장 설계',
 'https://picsum.photos/seed/course8/400/300','Development','ADVANCED','KOREAN',5000,1,'PUBLISHED',1,NOW(),NOW()),

-- 9 ~ 48 (목업 강의)
('Java 컬렉션 프레임워크','컬렉션 핵심 정리','List, Set, Map 구조와 실무 활용을 학습합니다.',
 'https://picsum.photos/seed/course9/400/300','Development','INTERMEDIATE','KOREAN',1500,0,'PUBLISHED',1,NOW(),NOW()),

('JPA 기초','ORM 이해','JPA와 Hibernate의 기본 개념과 매핑 전략을 학습합니다.',
 'https://picsum.photos/seed/course10/400/300','Development','INTERMEDIATE','KOREAN',2000,0,'PUBLISHED',1,NOW(),NOW()),

('Spring Security 입문','인증/인가','JWT 기반 인증과 권한 처리를 구현합니다.',
 'https://picsum.photos/seed/course11/400/300','Development','ADVANCED','KOREAN',3000,0,'PUBLISHED',1,NOW(),NOW()),

('REST API 설계','API 설계 원칙','RESTful API 설계 원칙과 실전 예제를 다룹니다.',
 'https://picsum.photos/seed/course12/400/300','Web','INTERMEDIATE','KOREAN',1800,0,'PUBLISHED',1,NOW(),NOW()),

('React 상태 관리','상태 관리 패턴','useState, useReducer, Context API를 학습합니다.',
 'https://picsum.photos/seed/course13/400/300','Development','INTERMEDIATE','KOREAN',2000,0,'PUBLISHED',1,NOW(),NOW()),

('Next.js 입문','React 프레임워크','SSR과 파일 기반 라우팅을 학습합니다.',
 'https://picsum.photos/seed/course14/400/300','Web','BEGINNER','KOREAN',1500,0,'PUBLISHED',1,NOW(),NOW()),

('Docker 기초','컨테이너 입문','Docker 이미지와 컨테이너 개념을 학습합니다.',
 'https://picsum.photos/seed/course15/400/300','Development','BEGINNER','KOREAN',1200,0,'PUBLISHED',1,NOW(),NOW()),

('Kubernetes 개요','컨테이너 오케스트레이션','쿠버네티스 기본 구조와 배포 개념을 이해합니다.',
 'https://picsum.photos/seed/course16/400/300','Development','ADVANCED','KOREAN',4000,0,'PUBLISHED',1,NOW(),NOW()),

('SQL 튜닝 입문','성능 최적화','인덱스와 실행 계획을 통한 쿼리 튜닝을 학습합니다.',
 'https://picsum.photos/seed/course17/400/300','Development','INTERMEDIATE','KOREAN',2200,0,'PUBLISHED',1,NOW(),NOW()),

('Git 실전 활용','협업 전략','브랜치 전략과 협업 워크플로우를 다룹니다.',
 'https://picsum.photos/seed/course18/400/300','Development','BEGINNER','KOREAN',1000,0,'PUBLISHED',1,NOW(),NOW()),

('Java 예외 처리','예외 처리 핵심','Checked / Unchecked Exception을 학습합니다.',
 'https://picsum.photos/seed/course19/400/300','Development','BEGINNER','KOREAN',1000,0,'PUBLISHED',1,NOW(),NOW()),

('Java Stream API','Stream 활용','Stream을 활용한 함수형 프로그래밍.',
 'https://picsum.photos/seed/course20/400/300','Development','INTERMEDIATE','KOREAN',1800,0,'PUBLISHED',1,NOW(),NOW()),

('객체지향 설계 원칙','SOLID 원칙','유지보수 가능한 설계 기법.',
 'https://picsum.photos/seed/course21/400/300','Development','INTERMEDIATE','KOREAN',2000,0,'PUBLISHED',1,NOW(),NOW()),

('Spring MVC 구조','MVC 패턴','Controller, Service, Repository 구조 이해.',
 'https://picsum.photos/seed/course22/400/300','Web','BEGINNER','KOREAN',1500,0,'PUBLISHED',1,NOW(),NOW()),

('Spring Validation','유효성 검사','Bean Validation 실전 활용.',
 'https://picsum.photos/seed/course23/400/300','Web','INTERMEDIATE','KOREAN',1300,0,'PUBLISHED',1,NOW(),NOW()),

('JPA 연관관계','연관관계 매핑','OneToMany, ManyToOne 정복.',
 'https://picsum.photos/seed/course24/400/300','Development','ADVANCED','KOREAN',2500,0,'PUBLISHED',1,NOW(),NOW()),

('JPA 성능 최적화','쿼리 최적화','N+1 문제 해결 전략.',
 'https://picsum.photos/seed/course25/400/300','Development','ADVANCED','KOREAN',3000,0,'PUBLISHED',1,NOW(),NOW()),

('HTTP 완전 정복','웹 통신','HTTP/HTTPS, 상태 코드 이해.',
 'https://picsum.photos/seed/course26/400/300','Web','BEGINNER','KOREAN',1200,0,'PUBLISHED',1,NOW(),NOW()),

('REST API 실습','실무 API','실제 서비스 수준 API 설계.',
 'https://picsum.photos/seed/course27/400/300','Web','INTERMEDIATE','KOREAN',2200,0,'PUBLISHED',1,NOW(),NOW()),

('OAuth 2.0','소셜 로그인','OAuth 인증 흐름 이해.',
 'https://picsum.photos/seed/course28/400/300','Security','ADVANCED','KOREAN',3500,0,'PUBLISHED',1,NOW(),NOW()),

('Redis 기초','캐시 시스템','Redis 자료구조와 활용.',
 'https://picsum.photos/seed/course29/400/300','Development','BEGINNER','KOREAN',1400,0,'PUBLISHED',1,NOW(),NOW()),

('Redis 실전','캐시 전략','세션, 토큰 캐싱.',
 'https://picsum.photos/seed/course30/400/300','Development','INTERMEDIATE','KOREAN',2200,0,'PUBLISHED',1,NOW(),NOW()),

('MySQL 인덱스','DB 성능','인덱스 구조 이해.',
 'https://picsum.photos/seed/course31/400/300','Database','BEGINNER','KOREAN',1500,0,'PUBLISHED',1,NOW(),NOW()),

('MySQL 트랜잭션','트랜잭션 관리','Isolation Level 이해.',
 'https://picsum.photos/seed/course32/400/300','Database','INTERMEDIATE','KOREAN',2000,0,'PUBLISHED',1,NOW(),NOW()),

('MySQL 락','동시성 제어','DB Lock 전략.',
 'https://picsum.photos/seed/course33/400/300','Database','ADVANCED','KOREAN',2600,0,'PUBLISHED',1,NOW(),NOW()),

('Git 기초','버전 관리','Git 기본 명령어.',
 'https://picsum.photos/seed/course34/400/300','Tools','BEGINNER','KOREAN',800,0,'PUBLISHED',1,NOW(),NOW()),

('Git 협업','PR 전략','실무 협업 방식.',
 'https://picsum.photos/seed/course35/400/300','Tools','INTERMEDIATE','KOREAN',1500,0,'PUBLISHED',1,NOW(),NOW()),

('CI/CD 개요','자동 배포','CI/CD 파이프라인 이해.',
 'https://picsum.photos/seed/course36/400/300','DevOps','BEGINNER','KOREAN',1800,0,'PUBLISHED',1,NOW(),NOW()),

('GitHub Actions','자동화','Actions 실습.',
 'https://picsum.photos/seed/course37/400/300','DevOps','INTERMEDIATE','KOREAN',2500,0,'PUBLISHED',1,NOW(),NOW()),

('AWS EC2','클라우드 서버','EC2 배포 실습.',
 'https://picsum.photos/seed/course38/400/300','Cloud','BEGINNER','KOREAN',2000,0,'PUBLISHED',1,NOW(),NOW()),

('AWS RDS','관리형 DB','RDS 구성과 운영.',
 'https://picsum.photos/seed/course39/400/300','Cloud','INTERMEDIATE','KOREAN',2500,0,'PUBLISHED',1,NOW(),NOW()),

('AWS 아키텍처','확장 설계','확장 가능한 구조 설계.',
 'https://picsum.photos/seed/course40/400/300','Cloud','ADVANCED','KOREAN',4000,0,'PUBLISHED',1,NOW(),NOW()),

('테스트 코드 입문','JUnit','단위 테스트 작성.',
 'https://picsum.photos/seed/course41/400/300','Testing','BEGINNER','KOREAN',1200,0,'PUBLISHED',1,NOW(),NOW()),

('Mock 테스트','Mockito','Mock 객체 활용.',
 'https://picsum.photos/seed/course42/400/300','Testing','INTERMEDIATE','KOREAN',1800,0,'PUBLISHED',1,NOW(),NOW()),

('아키텍처 패턴','Layered / Hexagonal','구조 설계 패턴.',
 'https://picsum.photos/seed/course43/400/300','Architecture','ADVANCED','KOREAN',3500,0,'PUBLISHED',1,NOW(),NOW()),

('DDD 입문','도메인 설계','도메인 중심 설계.',
 'https://picsum.photos/seed/course44/400/300','Architecture','INTERMEDIATE','KOREAN',2800,0,'PUBLISHED',1,NOW(),NOW()),

('대규모 트래픽','성능 설계','트래픽 대응 전략.',
 'https://picsum.photos/seed/course45/400/300','Architecture','ADVANCED','KOREAN',4200,0,'PUBLISHED',1,NOW(),NOW()),

('로그 설계','로그 전략','로그 수집과 분석.',
 'https://picsum.photos/seed/course46/400/300','Infrastructure','BEGINNER','KOREAN',1300,0,'PUBLISHED',1,NOW(),NOW()),

('모니터링','시스템 관찰','Prometheus 개요.',
 'https://picsum.photos/seed/course47/400/300','Infrastructure','INTERMEDIATE','KOREAN',2000,0,'PUBLISHED',1,NOW(),NOW()),

('장애 대응','운영 전략','실무 장애 대응.',
 'https://picsum.photos/seed/course48/400/300','Infrastructure','ADVANCED','KOREAN',3800,0,'PUBLISHED',1,NOW(),NOW());


-- Course Features
INSERT INTO course_features (course_id, feature) VALUES
                                                     (1, '평생 수강'),
                                                     (1, '수료증 제공'),
                                                     (1, 'Q&A 답변'),
                                                     (2, '평생 수강'),
                                                     (2, '실습 프로젝트'),
                                                     (2, '취업 지원');

-- ========================================
-- Sections (1 ~ 48)
-- ========================================
INSERT INTO sections (title, section_order, course_id, created_at, updated_at)
SELECT
    '섹션 1',
    1,
    id,
    NOW(),
    NOW()
FROM courses
WHERE id BETWEEN 1 AND 48;


-- ========================================
-- Lectures (1 ~ 48)
-- Section 당 3개 (무료 1 + 유료 2)
-- ========================================

INSERT INTO lectures (
    title,
    video_url,
    duration,
    is_free,
    lecture_order,
    section_id,
    created_at,
    updated_at
)
SELECT
    '강의 1',
    'https://www.youtube.com/watch?v=yZ89etxVBKs', -- 무료
    600,
    TRUE,
    1,
    s.id,
    NOW(),
    NOW()
FROM sections s
WHERE s.course_id BETWEEN 1 AND 48;

INSERT INTO lectures (
    title,
    video_url,
    duration,
    is_free,
    lecture_order,
    section_id,
    created_at,
    updated_at
)
SELECT
    '강의 2',
    'https://www.youtube.com/watch?v=9bZkp7q19f0', -- 유료
    900,
    FALSE,
    2,
    s.id,
    NOW(),
    NOW()
FROM sections s
WHERE s.course_id BETWEEN 1 AND 48;

INSERT INTO lectures (
    title,
    video_url,
    duration,
    is_free,
    lecture_order,
    section_id,
    created_at,
    updated_at
)
SELECT
    '강의 3',
    'https://www.youtube.com/watch?v=3tmd-ClpJxA', -- 유료
    1200,
    FALSE,
    3,
    s.id,
    NOW(),
    NOW()
FROM sections s
WHERE s.course_id BETWEEN 1 AND 48;


-- Recruitment Posts
INSERT INTO recruitment_posts (category, status, title, content, current_members, total_members, views, author_id, created_at, updated_at) VALUES
('STUDY', 'RECRUITING', '자바 스터디 하실 분', '매주 토요일 자바 공부하실 분 구합니다.', 1, 4, 0, 2, NOW(), NOW()),
('PROJECT', 'RECRUITING', '스프링 프로젝트 팀원 모집', '쇼핑몰 만드실 분', 1, 4, 10, 3, NOW(), NOW());

-- Recruitment Members
INSERT INTO recruitment_members (role, status, post_id, user_id, created_at, updated_at) VALUES
('ORGANIZER', 'APPROVED', 1, 2, NOW(), NOW()),
('PARTICIPANT', 'APPROVED', 1, 3, NOW(), NOW());

-- Chat
INSERT INTO chat (post_id, name, created_at)
VALUES (1, '자바 스터디 채팅방', NOW());

-- Chat Participants
INSERT INTO chat_participants (chat_id, user_id, role, created_at)
VALUES (1, 2, 'HOST', NOW()), -- 모집자
       (1, 3, 'MEMBER', NOW());
-- 참여자

-- Chat Messages
INSERT INTO chat_messages (message, chat_id, sender_id, created_at)
VALUES ('안녕하세요 스터디 참여하고 싶습니다.', 1, 3, NOW()),
       ('네 환영합니다!', 1, 2, NOW());
-- Orders
INSERT INTO orders (order_number, total_amount, status, user_id, created_at, updated_at) VALUES
('ORD-20231222-0001', 50000, 'ORDER', 2, NOW(), NOW());

-- Order Items
INSERT INTO order_items (course_name, price, order_id, course_id, created_at) VALUES
('자바 완전 정복', 50000, 1, 1, NOW());

-- Payments
INSERT INTO payments (portone_payment_id, amount, status, order_id, created_at, updated_at) VALUES
('imp_1234567890', 50000, 'COMPLETED', 1, NOW(), NOW());

-- Cart Items
INSERT INTO cart_items (user_id, course_id, created_at) VALUES
(3, 2, NOW());

-- COMPLETED Order (이학생이 자바 완전 정복 구매 완료)

INSERT INTO orders (
    order_number,
    total_amount,
    status,
    user_id,
    created_at,
    updated_at
) VALUES (
             'ORD-20231222-0002',
             50000,
             'COMPLETED',
             2,
             NOW(),
             NOW()
         );

-- 해당 주문에 포함된 강의 (자바 완전 정복)
INSERT INTO order_items (
    course_name,
    price,
    order_id,
    course_id,
    created_at
) VALUES (
             '자바 완전 정복',
             50000,
             LAST_INSERT_ID(),
             1,
             NOW()
         );

-- Enrollments (수강 권한)
-- 이학생(user_id=2)이 자바 완전 정복(course_id=1) 구매 완료 → 수강 권한 생성
INSERT INTO enrollments (
    user_id,
    course_id,
    order_id,
    status,
    created_at,
    updated_at
) VALUES (
             2,  -- 이학생
             1,  -- 자바 완전 정복
             2,  -- Order 2 (COMPLETED)
             'ACTIVE',
             NOW(),
             NOW()
         );

-- ========================================
-- 추가 테스트 데이터 (총 수강생 수 & 수익 검증용)
-- ========================================

-- [강의 1: 자바 완전 정복 - 무료 강의]
-- 박학생(user_id=3)이 무료 강의 수강
INSERT INTO orders (order_number, total_amount, status, user_id, created_at, updated_at)
VALUES ('ORD-20231223-0001', 0, 'COMPLETED', 3, NOW(), NOW());

INSERT INTO order_items (course_name, price, order_id, course_id, created_at)
VALUES ('자바 완전 정복', 0, LAST_INSERT_ID(), 1, NOW());

INSERT INTO payments (portone_payment_id, amount, status, order_id, created_at, updated_at)
VALUES ('imp_free_001', 0, 'COMPLETED', LAST_INSERT_ID(), NOW(), NOW());

INSERT INTO enrollments (user_id, course_id, order_id, status, created_at, updated_at)
VALUES (3, 1, LAST_INSERT_ID(), 'ACTIVE', NOW(), NOW());

-- [강의 2: 스프링 부트 입문 - 유료 강의]
-- 박학생(user_id=3)이 유료 강의 구매 (1000원)
INSERT INTO orders (order_number, total_amount, status, user_id, created_at, updated_at)
VALUES ('ORD-20231223-0002', 1000, 'COMPLETED', 3, NOW(), NOW());

INSERT INTO order_items (course_name, price, order_id, course_id, created_at)
VALUES ('스프링 부트 입문', 1000, LAST_INSERT_ID(), 2, NOW());

INSERT INTO payments (portone_payment_id, amount, status, order_id, created_at, updated_at)
VALUES ('imp_paid_001', 1000, 'COMPLETED', LAST_INSERT_ID(), NOW(), NOW());

INSERT INTO enrollments (user_id, course_id, order_id, status, created_at, updated_at)
VALUES (3, 2, LAST_INSERT_ID(), 'ACTIVE', NOW(), NOW());

-- 최학생(user_id=4)이 유료 강의 구매 (1000원)
INSERT INTO orders (order_number, total_amount, status, user_id, created_at, updated_at)
VALUES ('ORD-20231223-0003', 1000, 'COMPLETED', 4, NOW(), NOW());

INSERT INTO order_items (course_name, price, order_id, course_id, created_at)
VALUES ('스프링 부트 입문', 1000, LAST_INSERT_ID(), 2, NOW());

INSERT INTO payments (portone_payment_id, amount, status, order_id, created_at, updated_at)
VALUES ('imp_paid_002', 1000, 'COMPLETED', LAST_INSERT_ID(), NOW(), NOW());

INSERT INTO enrollments (user_id, course_id, order_id, status, created_at, updated_at)
VALUES (4, 2, LAST_INSERT_ID(), 'ACTIVE', NOW(), NOW());

-- 정학생(user_id=5)이 유료 강의 구매 후 환불 (CANCELED)
INSERT INTO orders (order_number, total_amount, status, user_id, created_at, updated_at)
VALUES ('ORD-20231223-0004', 1000, 'COMPLETED', 5, NOW(), NOW());

INSERT INTO order_items (course_name, price, order_id, course_id, created_at)
VALUES ('스프링 부트 입문', 1000, LAST_INSERT_ID(), 2, NOW());

-- 결제는 완료되었지만 환불됨 (REFUND)
INSERT INTO payments (portone_payment_id, amount, status, order_id, created_at, updated_at)
VALUES ('imp_refund_001', 1000, 'REFUND', LAST_INSERT_ID(), NOW(), NOW());

-- Enrollment도 CANCELED
INSERT INTO enrollments (user_id, course_id, order_id, status, created_at, updated_at)
VALUES (5, 2, LAST_INSERT_ID(), 'CANCELED', NOW(), NOW());

-- ========================================
-- Course studentCount 업데이트
-- ========================================
-- 강의 1: ACTIVE Enrollment 2개 (user_id=2, 3)
UPDATE courses SET student_count = 2 WHERE id = 1;

-- 강의 2: ACTIVE Enrollment 2개 (user_id=3, 4), CANCELED 1개 (user_id=5는 카운트 안됨)
UPDATE courses SET student_count = 2 WHERE id = 2;

-- ========================================
-- 검증 요약
-- ========================================
-- 강사(instructor_id=1) 통계:
-- - 총 강의 수: 2개
-- - 총 수강생 수: 4명 (강의1: 2명, 강의2: 2명)
-- - 총 수익: 2000원 (강의1: 0원, 강의2: 2000원)
--   * Payment.status='COMPLETED'만 집계
--   * 환불된 건(REFUND)은 수익에서 제외됨
