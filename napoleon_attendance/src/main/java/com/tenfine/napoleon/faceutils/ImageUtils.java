package com.tenfine.napoleon.faceutils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

public class ImageUtils {


    /**
     * 图片转二进制数组
     * @param file
     * @param imageType：jpg，png，bmp等
     * @return
     */
    public static byte[] imageToBinary(File file, String imageType) {
        BufferedImage buffer;
        try {
            buffer = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buffer, imageType, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将图片转成Base64字符串
     * @param imgPath
     * @return
     */
    public static String imageBase64(String imgPath) {
        InputStream inputStream = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            inputStream = new FileInputStream(imgPath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过后的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 将图片转成Base64字符串
     * @param file
     * @return
     */
    public static String imageBase64(File file) {
        InputStream inputStream = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            inputStream = new FileInputStream(file);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过后的字节数组字符串
        return encoder.encode(data);
    }
    
    /**
     * 将图片转成Base64字符串
     * @param file
     * @return
     */
    public static String imageBase64(MultipartFile file) throws Exception{
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String base64EncoderImg = base64Encoder.encode(file.getBytes());
        return base64EncoderImg;
    }

}
