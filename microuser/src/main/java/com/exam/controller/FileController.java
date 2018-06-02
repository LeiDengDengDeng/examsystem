package com.exam.controller;

import com.exam.bean.ReturnBean;
import com.exam.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * Created by deng on 2017/10/21.
 */
@RestController
@RequestMapping(value = "/files")
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
public class FileController {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.downloadMoban}")
    private String downloadPath;
    @Value("${file.uploadPath}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ReturnBean<String> upload(@RequestParam("groupId") int groupId, @RequestParam("file") MultipartFile file) {
        ReturnBean<String> result = new ReturnBean<>();
        result.setSuccess(false);
        if (file == null || file.isEmpty()) {
            result.setMsg("文件为空");
            return result;
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);

        String completePath = uploadPath + UUID.randomUUID() + "." + suffixName;
        File dest = new File(completePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
            userService.importUsers(groupId, completePath);
            result.setSuccess(true);
            return result;
        } catch (IllegalStateException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        result.setMsg("上传失败");
        return result;
    }

    @RequestMapping(value = "/download")
    public String downloadFile(HttpServletResponse response) {
        File file = new File(downloadPath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return "failed:" + e.getMessage();
                }
            }
            return "success";
        } else {
            return "not existing";
        }
    }
}
