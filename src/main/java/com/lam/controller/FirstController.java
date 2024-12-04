package com.lam.controller;

import com.lam.POJO.Result;
import com.lam.mapping.FirstMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class FirstController {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // 盐长度
    @Autowired
    private FirstMapper firstMapper;
    @GetMapping("option")
    public Result sayHi(){
        List<Map<String, Object>> config = firstMapper.getConfig();
//        System.out.println(config.toString());
        return Result.success(config);
    }
    @PostMapping("addoption")
    public Result addOption(@RequestBody Map<String,Object> params){
        String key = params.get("key")+"";
        String value = params.get("value")+"";
        try{
            firstMapper.addOption(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("插入失败");
        }
        return Result.success("插入成功");
    }

    @PostMapping("updateoption")
    public Result updateOption(@RequestBody Map<String,Object> params){
        Integer key = (Integer) params.get("id");
        String value = params.get("value")+"";
        try{
            firstMapper.updateOption(value,key);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    @PostMapping("/register")
    public Result registerAccount(@RequestBody Map<String,String> params)  {
        String account = params.get("account");
        String pwd = params.get("pwd");
        try {
            //生成盐
            byte[] salt = generateSalt();
            String encodedSalt = Base64.getEncoder().encodeToString(salt); // 使用Base64编码
            String hashCode = hashPassword(pwd, salt);
            firstMapper.registerAccount(account,hashCode, encodedSalt);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("register failed");
        }
        return Result.success("register success!");
    }
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> params){
        String account = params.get("account");
        String pwd = params.get("pwd");
        Map<String, String> login = firstMapper.login(account);
        if(login == null || login.get("pwd").isEmpty()){
            return Result.error("账号或密码不正确！");
        }
        String pwd1 = login.get("pwd");
//        String salt = login.get("salt");
        byte[] decodedSalt = Base64.getDecoder().decode(login.get("salt"));
        try {
            boolean b = verifyPassword(pwd, decodedSalt, pwd1);
            if(b){
                return Result.success("登录成功");
            }else {
                return Result.error("密码错误");
            }
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("账号或密码不正确！");
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
