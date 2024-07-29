package com.ispan.ktv.controller;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Base64;
//
//import javax.imageio.ImageIO;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ispan.ktv.service.CaptchaService;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api")
//public class CaptchaController {
//
//    private final CaptchaService captchaService;
//
//    public CaptchaController(CaptchaService captchaService) {
//        this.captchaService = captchaService;
//    }
//
//    @GetMapping("/captcha")
//    public ResponseEntity<CaptchaResponse> getCaptcha() {
//        // 获取随机验证码
//        String captchaCode = captchaService.generateCaptcha();
//
//        // 生成图片并返回 base64 编码的图片
//        String captchaImage = generateCaptchaImage(captchaCode);
//
//        // 返回给前端
//        CaptchaResponse captchaResponse = new CaptchaResponse(captchaCode, captchaImage);
//        return ResponseEntity.ok(captchaResponse);
//    }
//
//    private String generateCaptchaImage(String captchaCode) {
//        try {
//            // 创建图片
//            BufferedImage image = new BufferedImage(120, 40, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics = image.createGraphics();
//
//            // 设置背景颜色
//            graphics.setColor(Color.WHITE);
//            graphics.fillRect(0, 0, 120, 40);
//
//            // 绘制随机字符
//            graphics.setColor(Color.BLACK);
//            graphics.setFont(new Font("Arial", Font.BOLD, 20));
//            graphics.drawString(captchaCode, 10, 28);
//
//            // 转换图片为 base64 字符串
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", baos);
//            byte[] bytes = baos.toByteArray();
//            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // 定义返回给前端的验证码和图片的数据结构
//    static class CaptchaResponse {
//        private String captchaCode;
//        private String captchaImage;
//
//        public CaptchaResponse(String captchaCode, String captchaImage) {
//            this.captchaCode = captchaCode;
//            this.captchaImage = captchaImage;
//        }
//
//        public String getCaptchaCode() {
//            return captchaCode;
//        }
//
//        public String getCaptchaImage() {
//            return captchaImage;
//        }
//    }
//}