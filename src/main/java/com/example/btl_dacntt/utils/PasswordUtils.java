package com.example.btl_dacntt.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    private static final int BCRYPT_WORKLOAD = 12;
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(BCRYPT_WORKLOAD);
        return BCrypt.hashpw(plainPassword, salt);
    }
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    public static String hashPasswordWithSalt(String plainPassword, String salt) {
        return BCrypt.hashpw(plainPassword, salt);
    }
    public static String generateSalt() {
        return BCrypt.gensalt(BCRYPT_WORKLOAD);
    }
}
