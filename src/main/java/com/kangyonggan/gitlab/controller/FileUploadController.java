package com.kangyonggan.gitlab.controller;

import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.service.RedisService;
import com.kangyonggan.gitlab.util.FileUpload;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kyg
 */
@RestController
@RequestMapping("fileUpload")
@Log4j2
public class FileUploadController extends BaseController {

    @Autowired
    private RedisService redisService;

    /**
     * 文件跟路径
     */
    @Value("${app.file-upload}")
    @Getter
    private String fileUploadPath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping
    public Response defaultUpload(@RequestParam("file") MultipartFile file) {
        Response response = successResponse();
        try {
            String fileName = redisService.getIncrSerialNo();
            FileUpload.upload(fileUploadPath + "default/", fileName, file);

            response.put("url", "upload/default/" + fileName + "." + FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase());
        } catch (Exception e) {
            response.failure(e.getMessage());
            log.error("文件上传失败", e);
        }

        return response;
    }

}