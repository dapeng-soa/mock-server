package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.services.MetadataService;
import com.github.dapeng.dms.web.util.MockException;
import com.github.dapeng.dms.web.vo.UploadResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-07 3:44 PM
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class UploadController {

    /**
     * 可能为 thrift 也可能为 xml，基于用户存储什么样的类型。
     */
    @Value("${dms.first.baseDir}")
    private String firstInDir;

    private final MetadataService metadataService;

    public UploadController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }


    @PostConstruct
    public void init() {
        log.info("======================    DMS upload file base dir: {}     ======================", firstInDir);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     *             解决中文问题，liunx下中文路径，图片显示问题
     *             // 获取文件的后缀名
     *             String suffixName = fileName.substring(fileName.lastIndexOf("."));
     *             fileName = UUID.randomUUID() + suffixName;
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Object handleUpload(String data, MultipartFile file) {
        try {
            log.info("===== upload 生成 file  tag 为 {}  ===== ", data);
            List<String> uploadResult = commonUpload(data, file);
            return Resp.success(new UploadResp(uploadResult));
        } catch (Exception e) {
            log.error("handleUpload Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/resourceGenerator")
    public Object thriftGenerator(@RequestBody Map<String, String> params) {
        try {
            String tag = CommonUtil.notNullRet(params.get("tag"), "服务Tag不能为空");
            commonGenerator(tag, null);
            return Resp.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    /**
     * 更新一个微服务
     */
    @RequestMapping("/updateUpload")
    @ResponseBody
    public Object handleUpdateUpload(String serviceName, MultipartFile file) {
        try {
            String tag = CommonUtil.convertName(serviceName);
            commonUpload(tag, file);
            return Resp.success(serviceName);
        } catch (Exception e) {
            log.error("handleUpdateUpload Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }

    }

    @RequestMapping("/updateResourceGenerator")
    @ResponseBody
    public Object handleUpdateResourceGenerate(@RequestBody Map<String, String> params) {
        try {
            String serviceName = CommonUtil.notNullRet(params.get("serviceName"), "更新时 serviceName 不能为空.");
            String tag = CommonUtil.convertName(serviceName);
            commonGenerator(tag, serviceName);
            return Resp.success();
        } catch (Exception e) {
            log.error("handleUpdateResourceGenerate Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }

    }


    @ResponseBody
    @PostMapping("/xmlGenerator")
    public Object xmlMetaGenerator(String tag) {
        try {
            metadataService.processThriftGenerator(firstInDir, tag, null);
            return Resp.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    /**
     * ================================================   commmon method  ================================================
     */

    private List<String> commonUpload(String data, MultipartFile file) throws IOException {
        String filePath = null;
        try {
            CommonUtil.notNull(data, "上传服务tag不能为空");
            if (file.isEmpty()) {
                throw new MockException("上传文件不能为空");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();

            if (fileName == null) {
                throw new MockException("上传文件的文件名称不能为空");
            }
            // 文件上传后的路径
            filePath = firstInDir + data + "/" + fileName;
            File dest = new File(filePath);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            log.info("上传文件成功，文件名: {},保存路径: {}", fileName, filePath);

            return Arrays.asList(fileName, filePath);
        } catch (Exception e) {
            metadataService.deleteTargetFiles(filePath);
            throw e;
        }
    }

    /**
     * 通用解析上传的 xml 或者 thrift 文件 为可用元数据信息
     *
     * @param tag           上传 tag
     * @param serviceFilter 是否根据 service 过滤只跟新指定的服务元数据内容。
     * @return
     * @throws IOException file exception
     */
    private void commonGenerator(String tag, String serviceFilter) throws IOException {
        String fullPath = combinaDir(tag);
        try {
            String type = metadataService.candidateGenerateType(tag, fullPath);
            switch (type) {
                case "thrift":
                    metadataService.processThriftGenerator(firstInDir, tag, serviceFilter);
                    break;
                case "xml":
                    metadataService.processXmlGenerator(firstInDir, tag, serviceFilter);
                    break;
                default:
                    throw new MockException("请指定上传文件类型为 thrift 或 xml");
            }
        } finally {
            metadataService.deleteTargetFiles(fullPath);
        }
    }




    /*@ResponseBody
    @RequestMapping("/startParse")
    public Object parseMetadata(String dir) {
        try {
            String targetDir = xmlResourceBaseDir + "/" + dir;
            List<Map<String, OptimizedMetadata.OptimizedService>> servicesMapList = metadataService.loadMetadata(targetDir);
            if (servicesMapList == null) {
                throw new IllegalArgumentException("根据路径 [" + targetDir + "] 未解析处元数据信息");
            }
            return servicesMapList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, "parseMetadata failed: " + e.getMessage());
        }

    }*/


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

    private String combinaDir(String childPath) {
        return firstInDir + childPath;
    }


    /*@RequestMapping("/upload")
    public String handleUpload() {
        return "upload";
    }

    @RequestMapping("/upload2")
    public String handleUpload2() {
        return "upload2";
    }*/


}
