package com.ruoyi.common.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author liujiajia
 * 文件上传工具包
 */
public class FileUtils {

    /**
     * @param file     文件
     * @param path     文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {

        // 生成新的文件名
//        String realPath = path + "/" + getFileName(fileName);

        //使用原文件名
        String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    /**
     *   * 获取文件后缀
     *   * @param fileName
     *   * @return
     *   
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     *   * 生成新的文件名
     *   * @param fileOriginName 源文件名
     *   * @return
     *   
     */
    public static String getFileName(String fileOriginName) {
        return UUID.randomUUID().toString().replace("-", "") + getSuffix(fileOriginName);
    }

    /**
     * 删除服务上的文件
     *
     * @param filePath 路径
     * @return
     * @author Master.Pan
     * @date 2017年11月20日 上午11:06:48
     */
    public static boolean deleteServerFile(String filePath) {
        boolean delete_flag = false;
        File file = new File(filePath);
        if (file.exists() && file.isFile() && file.delete())
            delete_flag = true;
        else
            delete_flag = false;
        return delete_flag;
    }

    public static ResponseEntity<byte[]> download(HttpServletRequest request, String fileName) {
        try {
            File file = new File("D:\\uploadfiles\\777c96fa0daa456eb0992d45adc91430.zip");
            byte[] body = null;
            InputStream is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + file.getName());
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}
