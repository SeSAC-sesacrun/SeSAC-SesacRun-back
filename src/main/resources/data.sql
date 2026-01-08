-- ============================================
-- 더미 데이터 생성 스크립트 (최적화 버전)
-- ============================================

-- ============================================
-- 1. 사용자 데이터 (총 120명)
-- ============================================

-- 강사 20명 생성
INSERT INTO users (email, password, name, role, created_at, updated_at)
SELECT
    CONCAT('instructor', n, '@sesac.com'),
    '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG',
    CONCAT(
        ELT(1 + FLOOR(RAND()*10), '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임'),
        ELT(1 + FLOOR(RAND()*10), '민준', '서준', '도윤', '예준', '시우', '하준', '지훈', '현우', '지민', '지우'),
        ' 강사'
    ),
    'INSTRUCTOR',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
    NOW()
FROM (
    SELECT @i := @i + 1 AS n
    FROM information_schema.tables, (SELECT @i := 0) r
    LIMIT 20
) nums;

-- 일반 사용자 100명 생성
INSERT INTO users (email, password, name, role, created_at, updated_at)
SELECT
    CONCAT('user', n, '@sesac.com'),
    '$2a$10$BWu6umBCNEsCR7705J1LH.oiJfFB9XQK3wUMvW5eGiX9b0gx/CcyG',
    CONCAT(
        ELT(1 + FLOOR(RAND()*15), '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임', '한', '오', '서', '신', '권'),
        ELT(1 + FLOOR(RAND()*20), '민준', '서준', '도윤', '예준', '시우', '하준', '지훈', '현우', '지민', '지우', 
            '서연', '서윤', '지아', '하윤', '민서', '수아', '예은', '채원', '다은', '유진')
    ),
    'USER',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 730) DAY),
    NOW()
FROM (
    SELECT @u := @u + 1 AS n
    FROM information_schema.tables, (SELECT @u := 0) r
    LIMIT 100
) nums;

-- ============================================
-- 2. 강의 데이터 (총 50개)
-- ============================================

INSERT INTO courses (
    title, description, detailed_description, thumbnail,
    category, level, language, price,
    student_count, status, instructor_id,
    created_at, updated_at
)
SELECT
    CONCAT(
        ELT(1 + FLOOR(RAND()*10),
            '실전', '완벽 마스터', '초보자를 위한', '프로처럼 배우는', '현업 개발자의',
            '단기 완성', '심화', '기초부터 실전까지', '프로젝트로 배우는', '알기 쉬운'
        ),
        ' ',
        ELT(1 + FLOOR(RAND()*15),
            'React', 'Vue.js', 'Spring Boot', 'Node.js', 'Python',
            'Java', 'JavaScript', 'TypeScript', 'Kotlin', 'Swift',
            'Flutter', 'AWS', 'Docker', 'MySQL', 'MongoDB'
        ),
        ' 강의'
    ),
    ELT(1 + FLOOR(RAND()*5),
        '실무에서 바로 사용할 수 있는 실전 강의입니다.',
        '기초부터 차근차근 배울 수 있습니다.',
        '프로젝트를 통해 실력을 향상시킬 수 있습니다.',
        '현업 개발자가 직접 알려드립니다.',
        '초보자도 쉽게 따라할 수 있습니다.'
    ),
    CONCAT(
        '이 강의는 ',
        ELT(1 + FLOOR(RAND()*3), '입문자', '중급자', '실무자'),
        '를 위한 강의로, 실무에서 바로 활용할 수 있는 능력을 기를 수 있습니다.'
    ),
    CONCAT('https://picsum.photos/seed/course', @course_num := @course_num + 1, '/800/600'),
    ELT(1 + FLOOR(RAND()*6), 
        'Development', 'Design', 'Business', 'Marketing', 'Data Science', 'DevOps'
    ),
    ELT(1 + FLOOR(RAND()*3), 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'),
    'KOREAN',
    ELT(1 + FLOOR(RAND()*8), 0, 9900, 19900, 29900, 39900, 49900, 69900, 99900),
    0,
    ELT(1 + FLOOR(RAND()*5), 'PUBLISHED', 'PUBLISHED', 'PUBLISHED', 'PUBLISHED', 'ARCHIVED'),
    u.id,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
    NOW()
FROM (SELECT @course_num := 0) init,
     users u
WHERE u.role = 'INSTRUCTOR'
ORDER BY RAND()
LIMIT 50;


-- ============================================
-- 3. 섹션 데이터 (강의당 4개)
-- ============================================

INSERT INTO sections (title, section_order, course_id, created_at, updated_at)
SELECT
    CONCAT(
        ELT(s.n, '시작하기', '기본 개념', '실전 프로젝트', '마무리')
    ),
    s.n,
    c.id,
    c.created_at,
    NOW()
FROM courses c
JOIN (SELECT 1 n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) s
WHERE c.status = 'PUBLISHED';

-- ============================================
-- 4. 강의 영상 데이터 (섹션당 5개)
-- ============================================

INSERT INTO lectures (
    title, video_url, duration, is_free,
    lecture_order, section_id, created_at, updated_at
)
SELECT
    CONCAT(
        ELT(1 + FLOOR(RAND()*5), '소개', '개념 이해', '실습', '예제', '정리'),
        ' - ', l.n, '강'
    ),
    CONCAT('https://www.youtube.com/watch?v=', SUBSTRING(MD5(RAND()), 1, 11)),
    600 + FLOOR(RAND() * 600),
    IF(l.n = 1, TRUE, FALSE),
    l.n,
    s.id,
    s.created_at,
    NOW()
FROM sections s
JOIN (SELECT 1 n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) l;

-- ============================================
-- 5. 주문 데이터 (총 500건)
-- ============================================

INSERT INTO orders (order_number, total_amount, status, user_id, created_at, updated_at)
SELECT
    CONCAT('ORD-', DATE_FORMAT(NOW(), '%Y%m%d'), '-', LPAD(@order_num := @order_num + 1, 6, '0')),
    c.price,
    ELT(1 + FLOOR(RAND()*10), 
        'COMPLETED', 'COMPLETED', 'COMPLETED', 'COMPLETED', 'COMPLETED',
        'COMPLETED', 'COMPLETED', 'COMPLETED', 'REFUND', 'CREATED'
    ),
    u.id,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
    NOW()
FROM (SELECT @order_num := 0) init,
     users u
JOIN courses c ON c.price > 0 AND c.status = 'PUBLISHED'
WHERE u.role = 'USER'
ORDER BY RAND()
LIMIT 500;

-- ============================================
-- 6. 주문 항목 데이터
-- ============================================

INSERT INTO order_items (course_name, price, order_id, course_id, created_at)
SELECT
    c.title,
    o.total_amount,
    o.id,
    c.id,
    o.created_at
FROM orders o
JOIN courses c ON c.price = o.total_amount AND c.status = 'PUBLISHED'
LIMIT 500;

-- ============================================
-- 7. 결제 데이터
-- ============================================

INSERT INTO payments (portone_payment_id, amount, status, order_id, created_at, updated_at)
SELECT
    CONCAT('imp_', SUBSTRING(MD5(RAND()), 1, 24)),
    o.total_amount,
    CASE o.status
        WHEN 'COMPLETED' THEN 'COMPLETED'
        WHEN 'REFUND' THEN 'REFUND'
        ELSE 'PENDING'
    END,
    o.id,
    o.created_at,
    o.updated_at
FROM orders o;

-- ============================================
-- 8. 수강 등록 데이터
-- ============================================

INSERT INTO enrollments (user_id, course_id, order_id, status, created_at, updated_at)
SELECT
    o.user_id,
    oi.course_id,
    o.id,
    CASE 
        WHEN o.status = 'COMPLETED' THEN 'ACTIVE'
        WHEN o.status = 'REFUND' THEN 'CANCELED'
        ELSE 'ACTIVE'
    END,
    o.created_at,
    o.updated_at
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
WHERE o.status IN ('COMPLETED', 'REFUND');

-- ============================================
-- 9. 강의 수강생 수 업데이트
-- ============================================

UPDATE courses c
SET student_count = (
    SELECT COUNT(*)
    FROM enrollments e
    WHERE e.course_id = c.id AND e.status = 'ACTIVE'
);

-- ============================================
-- 10. 장바구니 데이터 (총 100건)
-- ============================================

INSERT INTO cart_items (user_id, course_id, created_at)
SELECT DISTINCT
    u.id,
    c.id,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)
FROM users u
JOIN courses c ON c.price > 0 AND c.status = 'PUBLISHED'
LEFT JOIN enrollments e ON e.user_id = u.id AND e.course_id = c.id AND e.status = 'ACTIVE'
WHERE u.role = 'USER' AND e.id IS NULL
ORDER BY RAND()
LIMIT 100;

-- ============================================
-- 11. 환불 데이터
-- ============================================

INSERT INTO refunds (payment_id, amount, reason, status, created_at, updated_at)
SELECT
    p.id,
    p.amount,
    ELT(1 + FLOOR(RAND()*5),
        '단순 변심', '강의 내용 불만족', '중복 구매', '기술적 문제', '기타 사유'
    ),
    'COMPLETED',
    DATE_ADD(p.created_at, INTERVAL FLOOR(RAND() * 7) DAY),
    NOW()
FROM payments p
WHERE p.status = 'REFUND';

-- ============================================
-- 12. 모집 게시글 데이터 (총 50건)
-- ============================================

INSERT INTO recruitment_posts (
    category, title, content, current_members, total_members, 
    views, status, author_id, created_at, updated_at
)
SELECT
    ELT(1 + FLOOR(RAND()*2), 'STUDY', 'PROJECT'),
    CONCAT(
        ELT(1 + FLOOR(RAND()*5), '함께 공부할', '열정적인', '온라인', '주말', '평일 저녁'),
        ' ',
        ELT(1 + FLOOR(RAND()*10),
            'React 스터디원', 'Spring 프로젝트팀', 'Python 공부 모임',
            'JavaScript 스터디', 'Java 프로젝트', 'AWS 스터디',
            'TypeScript 스터디', 'Vue.js 프로젝트', 'Node.js 스터디', '알고리즘 스터디'
        ),
        ' 모집합니다!'
    ),
    CONCAT(
        '안녕하세요! ',
        ELT(1 + FLOOR(RAND()*3),
            '함께 성장할 스터디원을 찾습니다.',
            '열정적으로 공부할 분들을 모집합니다.',
            '프로젝트를 함께 진행할 팀원을 구합니다.'
        ),
        '\n\n많은 관심 부탁드립니다!'
    ),
    1,
    FLOOR(3 + RAND() * 5),
    FLOOR(RAND() * 100),
    ELT(1 + FLOOR(RAND()*3), 'RECRUITING', 'RECRUITING', 'CLOSED'),
    u.id,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 60) DAY),
    NOW()
FROM users u
JOIN (
    SELECT @p := @p + 1 AS id
    FROM information_schema.tables, (SELECT @p := 0) r
    LIMIT 50
) p
WHERE u.role = 'USER'
ORDER BY RAND()
LIMIT 50;

-- ============================================
-- 13. 모집 멤버 데이터 - ORGANIZER
-- ============================================

INSERT INTO recruitment_members (role, status, post_id, user_id, created_at, updated_at)
SELECT
    'ORGANIZER',
    'APPROVED',
    rp.id,
    rp.author_id,
    rp.created_at,
    rp.created_at
FROM recruitment_posts rp;

-- ============================================
-- 14. 모집 멤버 데이터 - PARTICIPANT
-- ============================================

INSERT INTO recruitment_members (role, status, post_id, user_id, created_at, updated_at)
SELECT
    'PARTICIPANT',
    ELT(1 + FLOOR(RAND()*4), 'APPROVED', 'APPROVED', 'PENDING', 'REJECTED'),
    rp.id,
    u.id,
    DATE_ADD(rp.created_at, INTERVAL FLOOR(RAND() * 20) DAY),
    NOW()
FROM recruitment_posts rp
JOIN users u ON u.role = 'USER' AND u.id <> rp.author_id
WHERE rp.status = 'RECRUITING'
ORDER BY RAND()
LIMIT 200;

-- ============================================
-- 15. 모집글 현재 인원 업데이트
-- ============================================

UPDATE recruitment_posts rp
SET current_members = (
    SELECT COUNT(*)
    FROM recruitment_members rm
    WHERE rm.post_id = rp.id AND rm.status = 'APPROVED'
);

-- ============================================
-- 16. 채팅방 데이터
-- ============================================

INSERT INTO chat (post_id, name, created_at)
SELECT
    rp.id,
    CONCAT(rp.title, ' 채팅방'),
    rp.created_at
FROM recruitment_posts rp;

-- ============================================
-- 17. 채팅 참여자 - HOST
-- ============================================

INSERT INTO chat_participants (chat_id, user_id, role, created_at)
SELECT
    c.id,
    rp.author_id,
    'HOST',
    c.created_at
FROM chat c
JOIN recruitment_posts rp ON rp.id = c.post_id;

-- ============================================
-- 18. 채팅 참여자 - MEMBER
-- ============================================

INSERT INTO chat_participants (chat_id, user_id, role, created_at)
SELECT
    c.id,
    rm.user_id,
    'MEMBER',
    rm.created_at
FROM chat c
JOIN recruitment_members rm ON rm.post_id = c.post_id
WHERE rm.role = 'PARTICIPANT' AND rm.status = 'APPROVED';

-- ============================================
-- 19. 채팅 메시지 데이터 (총 300건)
-- ============================================

INSERT INTO chat_messages (message, chat_id, sender_id, created_at)
SELECT
    ELT(1 + FLOOR(RAND()*10),
        '안녕하세요!', '반갑습니다!', '잘 부탁드립니다!', '참여하고 싶어요.',
        '일정은 어떻게 되나요?', '자료 공유 부탁드립니다.', '오늘 수고하셨습니다!',
        '질문이 있는데요.', '도움 주셔서 감사합니다.', '다들 화이팅하세요!'
    ),
    cp.chat_id,
    cp.user_id,
    DATE_ADD(c.created_at, INTERVAL FLOOR(RAND() * 30) DAY)
FROM chat_participants cp
JOIN chat c ON c.id = cp.chat_id
JOIN (SELECT 1 n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6) nums
ORDER BY RAND()
LIMIT 300;
