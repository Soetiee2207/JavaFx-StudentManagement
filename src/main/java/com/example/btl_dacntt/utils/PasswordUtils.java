package com.example.btl_dacntt.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    private static final int BCRYPT_WORKLOAD = 12;

    /**
     * Hash mật khẩu sử dụng BCrypt
     * @param plainPassword mật khẩu dạng plain text
     * @return mật khẩu đã được hash
     */
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(BCRYPT_WORKLOAD);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Kiểm tra mật khẩu plain text với mật khẩu đã hash
     * @param plainPassword mật khẩu người dùng nhập vào
     * @param hashedPassword mật khẩu đã hash lưu trong database
     * @return true nếu mật khẩu khớp, false nếu không khớp
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Tạo hash cho mật khẩu với salt tùy chỉnh (dùng cho testing)
     * @param plainPassword mật khẩu dạng plain text
     * @param salt salt tùy chỉnh
     * @return mật khẩu đã được hash
     */
    public static String hashPasswordWithSalt(String plainPassword, String salt) {
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Tạo salt mới cho BCrypt
     * @return salt string
     */
    public static String generateSalt() {
        return BCrypt.gensalt(BCRYPT_WORKLOAD);
    }
}
