-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 23, 2025 lúc 07:40 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlysinhvien`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `grades`
--

CREATE TABLE `grades` (
  `id` int(11) NOT NULL,
  `student_id` varchar(20) NOT NULL,
  `subject_id` varchar(20) NOT NULL,
  `score_cc` double DEFAULT 0,
  `score_gk` double DEFAULT 0,
  `score_ck` double DEFAULT 0,
  `score_tk` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `grades`
--

INSERT INTO `grades` (`id`, `student_id`, `subject_id`, `score_cc`, `score_gk`, `score_ck`, `score_tk`) VALUES
(1, 'SV1', 'MH1', 9, 8, 10, 9.3),
(4, 'SV002', 'MH001', 9, 9, 8.5, 8.7),
(5, 'SV002', 'MH003', 7.5, 8, 9, 8.55),
(6, 'SV003', 'MH002', 6, 7, 7.5, 7.15),
(7, 'SV003', 'MH004', 8.5, 9, 9.5, 9.225),
(8, 'SV004', 'MH005', 10, 9, 9.5, 9.4),
(9, 'SV004', 'MH1', 9, 9, 9, 9),
(11, 'SV006', 'AN006', 10, 10, 10, 10),
(12, 'SV006', 'CH007', 4, 4, 4, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `students`
--

CREATE TABLE `students` (
  `id` int(11) NOT NULL,
  `student_id` varchar(20) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `dob` date DEFAULT NULL,
  `class_id` varchar(50) DEFAULT NULL,
  `type` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `students`
--

INSERT INTO `students` (`id`, `student_id`, `full_name`, `dob`, `class_id`, `type`) VALUES
(1, 'SV1', 'Khá Bảnh', NULL, NULL, 'Regular'),
(3, 'SV002', 'Chu Chỉ Nhược', '2003-05-20', 'CNTT_K60', 'Regular'),
(4, 'SV003', 'Triệu Mẫn', '2003-08-10', 'CNTT_K60', 'InService'),
(5, 'SV004', 'Đoàn Dự', '2004-02-02', 'KTPM_K61', 'Regular'),
(8, 'SV005', 'Nguyễn Nhật Minh', '2004-07-22', 'TT35CL03', 'InService'),
(9, 'SV006', 'Nguyễn Nhật Ánh', '2005-03-06', 'HA16', 'Regular');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `subjects`
--

CREATE TABLE `subjects` (
  `id` int(11) NOT NULL,
  `subject_id` varchar(20) NOT NULL,
  `subject_name` varchar(100) NOT NULL,
  `credits` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `subjects`
--

INSERT INTO `subjects` (`id`, `subject_id`, `subject_name`, `credits`) VALUES
(1, 'MH1', 'Múa Quạt', 3),
(2, 'MH001', 'Lập trình Java nâng cao', 3),
(3, 'MH002', 'Cơ sở dữ liệu', 4),
(4, 'MH003', 'Phát triển ứng dụng Web', 3),
(5, 'MH004', 'Trí tuệ nhân tạo', 3),
(7, 'MH005', 'Dự án công nghệ thông tin', 2),
(8, 'AN006', 'Ăn Ngủ Nghỉ', 10),
(9, 'CH007', 'Chăm Học', 10);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `teachers`
--

CREATE TABLE `teachers` (
  `id` int(11) NOT NULL,
  `teacher_id` varchar(20) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `teachers`
--

INSERT INTO `teachers` (`id`, `teacher_id`, `full_name`, `email`) VALUES
(1, 'TC1', 'Thầy Huấn', 'huan@bds.com'),
(2, 'TC001', 'Nguyễn Thanh Tùng', 'tung.nt@dh.edu.vn'),
(3, 'TC002', 'Lê Thị Thu Hà', 'ha.ltt@dh.edu.vn'),
(4, 'TC003', 'Trần Minh Tuấn', 'tuan.tm@dh.edu.vn'),
(5, 'TC004', 'Phạm Hoàng Yến', 'yen.ph@dh.edu.vn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL,
  `teacher_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
-- password tất cả đều là 123456
--

INSERT INTO `users` (`id`, `username`, `password`, `role_id`, `teacher_id`) VALUES
(1, 'thayhuan', '$2a$12$6y98DMhmYT6596RW4vS9s.IIiFlSXSoCMf8z4dOLl76AOmaGizZ4O', 2, 'TC1'),
(2, 'admin', '$2a$12$CSQaLceN3Ywc5DyzlbQXieP.er.LECwG6sPY38X.LDD9B1Y06pxfC', 1, NULL),
(3, 'tungnt', '$2a$12$CSQaLceN3Ywc5DyzlbQXieP.er.LECwG6sPY38X.LDD9B1Y06pxfC', 2, 'TC001'),
(4, 'haltt', '$2a$12$CSQaLceN3Ywc5DyzlbQXieP.er.LECwG6sPY38X.LDD9B1Y06pxfC', 2, 'TC002'),
(5, 'tuantm', '$2a$12$CSQaLceN3Ywc5DyzlbQXieP.er.LECwG6sPY38X.LDD9B1Y06pxfC', 2, 'TC003'),
(6, 'teacher4', '$2a$12$CSQaLceN3Ywc5DyzlbQXieP.er.LECwG6sPY38X.LDD9B1Y06pxfC', 2, 'TC004');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `grades`
--
ALTER TABLE `grades`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_grade_student_subject` (`student_id`,`subject_id`),
  ADD KEY `fk_grade_subject` (`subject_id`);

--
-- Chỉ mục cho bảng `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `student_id` (`student_id`),
  ADD UNIQUE KEY `student_id_2` (`student_id`);

--
-- Chỉ mục cho bảng `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `subject_id` (`subject_id`),
  ADD UNIQUE KEY `subject_id_2` (`subject_id`);

--
-- Chỉ mục cho bảng `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `teacher_id` (`teacher_id`),
  ADD UNIQUE KEY `teacher_id_2` (`teacher_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `username_2` (`username`),
  ADD UNIQUE KEY `teacher_id` (`teacher_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `grades`
--
ALTER TABLE `grades`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `students`
--
ALTER TABLE `students`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `subjects`
--
ALTER TABLE `subjects`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `teachers`
--
ALTER TABLE `teachers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `fk_grade_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_grade_subject` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `fk_user_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
