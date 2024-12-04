package com.lam;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
class ExampleApplicationTests {

    private final String salt = "ty12#io9";
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // 盐长度
    @Test
    void contextLoads() {
        System.out.println("hello world");
        String userPWD = "123456";
        try {
            //生成盐
            byte[] salt = generateSalt();
            // 计算并打印哈希后的密码
            String hashedPassword = hashPassword(userPWD, salt);
            System.out.println("Hashed Password: " + hashedPassword);
            // 验证密码
            boolean isVerified = verifyPassword("123456", salt, hashedPassword);
            System.out.println("Password verified: " + isVerified);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static boolean verifyPassword(String providedPassword, byte[] storedSalt, String storedHashedPassword) throws Exception {
        String newHashedPassword = hashPassword(providedPassword, storedSalt);
        return newHashedPassword.equals(storedHashedPassword);
    }
    public static byte[] generateSalt() throws Exception {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    public static String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt); // 添加盐到消息摘要
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // 返回Base64编码后的哈希值
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
