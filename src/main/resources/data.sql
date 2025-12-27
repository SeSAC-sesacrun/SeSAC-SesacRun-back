-- Users
INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES
('instructor@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '김강사', 'INSTRUCTOR', NOW(), NOW()),
('user1@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '이학생', 'USER', NOW(), NOW()),
('user2@test.com', '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG', '박학생', 'USER', NOW(), NOW());

-- Courses
INSERT INTO courses (title, description, detailed_description, thumbnail, category, price, student_count, status, instructor_id, created_at, updated_at) VALUES
('자바 완전 정복', '자바 기초부터 심화까지', '자바의 모든 것을 다룹니다. 변수, 연산자, 제어문부터 객체지향 프로그래밍까지 체계적으로 학습합니다.', 'https://example.com/java.png', 'Development', 50000, 0, 'PUBLISHED', 1, NOW(), NOW()),
('스프링 부트 입문', '웹 개발의 시작 스프링 부트', '스프링 부트로 웹 서버를 만들어봅니다. REST API 설계와 데이터베이스 연동을 배웁니다.', 'https://example.com/spring.png', 'Web', 60000, 0, 'PUBLISHED', 1, NOW(), NOW());

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
('변수와 자료형', 'https://youtu.be/example1', 600, true, 1, 1, NOW(), NOW()),
('연산자', 'https://youtu.be/example2', 900, false, 2, 1, NOW(), NOW()),
('클래스와 객체', 'https://youtu.be/example3', 1200, false, 1, 2, NOW(), NOW()),
('스프링 프로젝트 생성', 'https://youtu.be/example4', 720, true, 1, 3, NOW(), NOW());

-- Recruitment Posts
INSERT INTO recruitment_posts (category, status, title, content, current_members, total_members, views, author_id, created_at, updated_at) VALUES
('STUDY', 'RECRUITING', '자바 스터디 하실 분', '매주 토요일 자바 공부하실 분 구합니다.', 1, 4, 0, 2, NOW(), NOW()),
('PROJECT', 'RECRUITING', '스프링 프로젝트 팀원 모집', '쇼핑몰 만드실 분', 1, 4, 10, 3, NOW(), NOW());

-- Recruitment Members
INSERT INTO recruitment_members (role, status, post_id, user_id, created_at, updated_at) VALUES
('ORGANIZER', 'APPROVED', 1, 2, NOW(), NOW()),
('PARTICIPANT', 'APPROVED', 1, 3, NOW(), NOW());

-- Chat
INSERT INTO chat (name, created_at) VALUES
('자바 스터디 채팅방', NOW());

-- Chat Messages
INSERT INTO chat_messages (content, type, chat_id, sender_id, created_at) VALUES
('안녕하세요 스터디 참여하고 싶습니다.', 'TEXT', 1, 3, NOW()),
('네 환영합니다!', 'TEXT', 1, 2, NOW());

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
