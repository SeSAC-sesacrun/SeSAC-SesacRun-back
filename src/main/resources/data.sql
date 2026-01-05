-- Users
INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES
('instructor@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '김강사', 'INSTRUCTOR', NOW(), NOW()),
('user1@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '이학생', 'USER', NOW(), NOW()),
('user2@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '박학생', 'USER', NOW(), NOW());

-- Courses
INSERT INTO courses (title, description, detailed_description, thumbnail, category, price, student_count, status, instructor_id, created_at, updated_at) VALUES
('자바 완전 정복', '자바 기초부터 심화까지', '자바의 모든 것을 다룹니다. 변수, 연산자, 제어문부터 객체지향 프로그래밍까지 체계적으로 학습합니다.', 'data:image/png;base64,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', 'Development', 0, 0, 'PUBLISHED', 1, NOW(), NOW()),
('스프링 부트 입문', '웹 개발의 시작 스프링 부트', '스프링 부트로 웹 서버를 만들어봅니다. REST API 설계와 데이터베이스 연동을 배웁니다.', 'data:image/png;base64,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', 'Web', 1000, 0, 'PUBLISHED', 1, NOW(), NOW());

-- Course Features
INSERT INTO course_features (course_id, feature) VALUES
(1, '평생 수강'),
(1, '수료증 제공'),
(1, 'Q&A 답변'),
(2, '평생 수강'),
(2, '실습 프로젝트'),
(2, '취업 지원');

-- Sections
INSERT INTO sections (title, section_order, course_id, created_at, updated_at) VALUES
('자바 기초', 1, 1, NOW(), NOW()),
('객체 지향 프로그래밍', 2, 1, NOW(), NOW()),
('스프링 환경설정', 1, 2, NOW(), NOW());

-- Lectures
INSERT INTO lectures (title, video_url, duration, is_free, lecture_order, section_id, created_at, updated_at) VALUES
('변수와 자료형', 'https://www.youtube.com/watch?v=yZ89etxVBKs', 600, true, 1, 1, NOW(), NOW()),
('연산자', 'https://www.youtube.com/watch?v=yZ89etxVBKs', 900, false, 2, 1, NOW(), NOW()),
('클래스와 객체', 'https://www.youtube.com/watch?v=yZ89etxVBKs', 1200, false, 1, 2, NOW(), NOW()),
('스프링 프로젝트 생성', 'https://www.youtube.com/watch?v=yZ89etxVBKs', 720, true, 1, 3, NOW(), NOW());

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