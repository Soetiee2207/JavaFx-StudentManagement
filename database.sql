-- 1. Hủy diệt thế giới cũ
DROP TABLE IF EXISTS grades;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS users;

-- =================================================================
-- BẢNG 1: USERS
-- =================================================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL, -- 1: Admin, 2: Teacher, 3: Student
    teacher_id VARCHAR(20) 
);

-- =================================================================
-- BẢNG 2: TEACHERS
-- =================================================================
CREATE TABLE teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,  
    -- QUAN TRỌNG: Thêm UNIQUE để đảm bảo mã GV không trùng -> Mới làm khóa ngoại được
    teacher_id VARCHAR(20) NOT NULL UNIQUE, 
    
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

-- =================================================================
-- BƯỚC QUAN TRỌNG: LIÊN KẾT USERS -> TEACHERS
-- =================================================================
-- Bây giờ teacher_id đã là UNIQUE, lệnh này sẽ chạy ngon lành
ALTER TABLE users 
ADD CONSTRAINT fk_user_teacher 
FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL;


-- =================================================================
-- BẢNG 3: STUDENTS
-- =================================================================
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    -- QUAN TRỌNG: Thêm UNIQUE
    student_id VARCHAR(20) NOT NULL UNIQUE, 
    full_name VARCHAR(100) NOT NULL,
    dob DATE,
    class_id VARCHAR(50),     
    type VARCHAR(20) NOT NULL 
);

-- =================================================================
-- BẢNG 4: SUBJECTS
-- =================================================================
CREATE TABLE subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    -- QUAN TRỌNG: Thêm UNIQUE
    subject_id VARCHAR(20) NOT NULL UNIQUE, 
    subject_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL
);

-- =================================================================
-- BẢNG 5: GRADES (Bảng Điểm)
-- =================================================================
CREATE TABLE grades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    subject_id VARCHAR(20) NOT NULL,
    score_cc DOUBLE DEFAULT 0,
    score_gk DOUBLE DEFAULT 0,
    score_ck DOUBLE DEFAULT 0,
    
    -- Bây giờ student_id và subject_id ở bảng gốc đã là UNIQUE nên tham chiếu được
    CONSTRAINT fk_grade_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_grade_subject FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    
    UNIQUE KEY unique_grade_student_subject (student_id, subject_id)
);

-- =================================================================
-- DỮ LIỆU TEST (Chạy thử để kiểm tra)
-- =================================================================

-- 1. Tạo Giáo viên trước (Có mã TC1)
INSERT INTO teachers (teacher_id, full_name, email) 
VALUES ('TC1', 'Thầy Huấn', 'huan@bds.com');

-- 2. Tạo User và gán teacher_id = TC1
INSERT INTO users (username, password, role_id, teacher_id) 
VALUES ('thayhuan', '123', 2, 'TC1');

-- 3. Tạo Sinh viên (SV1)
INSERT INTO students (student_id, full_name, type) 
VALUES ('SV1', 'Khá Bảnh', 'Regular');

-- 4. Tạo Môn học (MH1)
INSERT INTO subjects (subject_id, subject_name, credits) 
VALUES ('MH1', 'Múa Quạt', 3);

-- 5. Nhập điểm
INSERT INTO grades (student_id, subject_id, score_cc, score_gk, score_ck) 
VALUES ('SV1', 'MH1', 9, 8, 10);

-- ==========================================================
-- 1. THÊM DỮ LIỆU BẢNG TEACHERS (Tạo giáo viên trước)
-- ==========================================================
INSERT INTO teachers (teacher_id, full_name, email) VALUES 
('TC001', 'Nguyễn Thanh Tùng', 'tung.nt@dh.edu.vn'),
('TC002', 'Lê Thị Thu Hà', 'ha.ltt@dh.edu.vn'),
('TC003', 'Trần Minh Tuấn', 'tuan.tm@dh.edu.vn'),
('TC004', 'Phạm Hoàng Yến', 'yen.ph@dh.edu.vn');

-- ==========================================================
-- 2. THÊM DỮ LIỆU BẢNG USERS 
-- (Lưu ý: Map đúng teacher_id vừa tạo ở trên)
-- ==========================================================
INSERT INTO users (username, password, role_id, teacher_id) VALUES 
-- Admin (Không cần teacher_id)
('admin', '123456', 1, NULL),

-- Tài khoản cho 3 giáo viên trên (Role = 2)
('tungnt', '123456', 2, 'TC001'),
('haltt', '123456', 2, 'TC002'),
('tuantm', '123456', 2, 'TC003');

-- ==========================================================
-- 3. THÊM DỮ LIỆU BẢNG SUBJECTS (Môn học)
-- ==========================================================
INSERT INTO subjects (subject_id, subject_name, credits) VALUES 
('MH001', 'Lập trình Java nâng cao', 3),
('MH002', 'Cơ sở dữ liệu', 4),
('MH003', 'Phát triển ứng dụng Web', 3),
('MH004', 'Trí tuệ nhân tạo', 3);

-- ==========================================================
-- 4. THÊM DỮ LIỆU BẢNG STUDENTS (Sinh viên)
-- ==========================================================
INSERT INTO students (student_id, full_name, dob, class_id, type) VALUES 
('SV001', 'Trương Vô Kỵ', '2003-01-15', 'CNTT_K60', 'Regular'),
('SV002', 'Chu Chỉ Nhược', '2003-05-20', 'CNTT_K60', 'Regular'),
('SV003', 'Triệu Mẫn', '2003-08-10', 'CNTT_K60', 'InService'),
('SV004', 'Đoàn Dự', '2004-02-02', 'KTPM_K61', 'Regular');

-- ==========================================================
-- 5. THÊM DỮ LIỆU BẢNG GRADES (Bảng điểm)
-- (Phối hợp SV và MH để tạo bảng điểm)
-- ==========================================================
INSERT INTO grades (student_id, subject_id, score_cc, score_gk, score_ck) VALUES 
-- Điểm của Trương Vô Kỵ (SV001)
('SV001', 'MH001', 10.0, 9.5, 9.0), -- Giỏi Java
('SV001', 'MH002', 8.0, 8.5, 8.0),  -- Khá CSDL

-- Điểm của Chu Chỉ Nhược (SV002)
('SV002', 'MH001', 9.0, 9.0, 8.5),
('SV002', 'MH003', 7.5, 8.0, 9.0),

-- Điểm của Triệu Mẫn (SV003)
('SV003', 'MH002', 6.0, 7.0, 7.5),
('SV003', 'MH004', 8.5, 9.0, 9.5);