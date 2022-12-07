package com.example.waimai.controller;

import com.example.waimai.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

/**
 *
 * 文件上传和下载的通用类
 */

@RequestMapping("/common")
@RestController
@Slf4j
public class CommonController {

    @Value("${imgPath}")
    private String basePath;
    /**
     *
     * @param file  //必须和前端发送来的参数名一样
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        File dir = new File(basePath);
        dir.mkdirs();
        String originName = file.getOriginalFilename();
        String suffix = originName.substring(originName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        try {
            file.transferTo(new File(basePath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success(fileName,"上传成功");
    }

    /**
     * 下载文件
     * @param name
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath, name));

            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
            while( (len=fileInputStream.read(bytes))!= -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        return Result.success(null,"下载成功");
    }

}
