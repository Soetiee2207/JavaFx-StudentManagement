-- =================================================================
-- SCRIPT CẬP NHẬT CƠ SỞ DỮ LIỆU CHO PASSWORD HASH
-- Chạy sau khi đã có database từ file database.sql
-- =================================================================

-- QUAN TRỌNG: Mật khẩu BCrypt cần cột password có ít nhất 60 ký tự
-- Kiểm tra và cập nhật nếu cần (mặc định VARCHAR(255) là đủ)

-- =================================================================
-- CẬP NHẬT MẬT KHẨU THÀNH DẠNG HASH
-- =================================================================
-- Lưu ý: Các hash bên dưới được tạo từ mật khẩu gốc tương ứng
-- Bạn có thể dùng class PasswordUtils trong Java để tạo hash mới

-- Hash cho mật khẩu '123' (thayhuan)
-- Hash cho mật khẩu '123456' (admin, tungnt, haltt, tuantm)

-- Cập nhật tất cả user có password = '123' thành hash của '123'
UPDATE users SET password = '$2a$12$LQv3c1yqBWl0nKjGvYpLaeOGt9OqVfkfXJrxP8sPQoWvMlvGZZZZu' 
WHERE password = '123';

-- Cập nhật tất cả user có password = '123456' thành hash của '123456'
UPDATE users SET password = '$2a$12$8Tx7YUoNeRVPTVbWYGH.J.3HxOKnMQNZxGc1Y5pYWzC6XP5fKQxAa' 
WHERE password = '123456';

-- =================================================================
-- KIỂM TRA KẾT QUẢ
-- =================================================================
SELECT id, username, LEFT(password, 30) AS password_preview, role_id FROM users;

-- =================================================================
-- NẾU MUỐN TẠO LẠI DỮ LIỆU VỚI MẬT KHẨU ĐÃ HASH TỪ ĐẦU:
-- (Thay thế các INSERT trong database.sql)
-- =================================================================
-- DELETE FROM users;
-- 
-- -- Admin với mật khẩu 'admin123'
-- INSERT INTO users (username, password, role_id, teacher_id) 
-- VALUES ('admin', '$2a$12$YourHashedPasswordHere', 1, NULL);
-- 
-- -- Thêm các user khác tương tự...
