package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.thrift.MetadataHandler;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespEnum;
import com.github.dapeng.json.OptimizedMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-07 3:44 PM
 */
@Controller
@Slf4j
public class UploadController {

    private final MetadataHandler metadataHandler;

    public UploadController(MetadataHandler metadataHandler) {
        this.metadataHandler = metadataHandler;
    }

    @RequestMapping("/upload")
    public String handleUpload() {
        return "upload";
    }

    @RequestMapping("/upload2")
    public String handleUpload2() {
        return "upload2";
    }


    @RequestMapping("/uploadFile")
    @ResponseBody
    public String handleUpload(MultipartFile file) {
        if (file.isEmpty()) {
            log.info("文件为空");
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);

        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);

        // 文件上传后的路径
        String filePath = "/Users/maple/ideaspace/dapeng/dapeng-mock-server/file/";
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "上传失败";

    }

    //文件下载相关代码
    @RequestMapping("/download")
    @ResponseBody
    public String downloadFile(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        if (fileName != null) {
            String realPath = "/Users/maple/ideaspace/dapeng/dapeng-mock-server/file/";

            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名

/*
                response.setHeader("Content-Type", "text/plain");
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "utf-8"));
                response.addHeader("Content-Length", "" + file.length());*/


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
                    log.info("success");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return null;
    }

    //多文件上传
    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleBatchUpload(HttpServletRequest request) {
        String realPath = "/Users/maple/ideaspace/dapeng/dapeng-mock-server/file/";
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file;
        BufferedOutputStream stream;
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(realPath + file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();

                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => "
                            + e.getMessage();
                }
            } else {
                return "You failed to upload " + i
                        + " because the file was empty.";
            }
        }
        return "upload successful";
    }

    //多文件上传
    @RequestMapping(value = "/batch/upload2", method = RequestMethod.POST)
    @ResponseBody
    public String handleBatchUpload2(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String realPath = "/Users/maple/ideaspace/dapeng/dapeng-mock-server/file/";
        if (files == null || files.size() == 0) {
            return "error";
        }
        for (MultipartFile file : files) {
            String filePath = realPath + file.getOriginalFilename();

            if (filePath.lastIndexOf('/') > 0) {
                String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }

            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return "SUCCESS";
    }

    @ResponseBody
    @RequestMapping("/startParse")
    public Object parseMetadata(String targetDir) {
        try {
            List<Map<String, OptimizedMetadata.OptimizedService>> servicesMapList = metadataHandler.loadMetadata(targetDir);

            if (servicesMapList == null) {
                throw new IllegalArgumentException("根据路径 [" + targetDir + "] 未解析处元数据信息");
            }
            return servicesMapList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(), "parseMetadata failed: " + e.getMessage()));
        }

    }


}
