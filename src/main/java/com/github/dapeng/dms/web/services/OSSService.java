//package com.github.dapeng.dms.web.services;
//
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.model.ObjectMetadata;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
// * @since 2018-11-07 3:57 PM
// */
//@Service
//@Slf4j
//public class OSSService {
//    /**
//     * 文件路径Key
//     */
//    private static final String FILE_KEY = "file/";
//    /**
//     * 图片路径Key
//     */
//    private static final String IMAGE_KEY = "image/";
//
//    @Value("${OSS_endpoint}")
//    private String endpoint;
//
//    @Value("${OSS_accessKeyId}")
//    private String accessKeyId;
//
//    @Value("${OSS_accessKeySecret}")
//    private String accessKeySecret;
//
//    @Value("${OSS_bucketName}")
//    private String bucketName;
//
//    @Value("${OSS_mainImageBucketName}")
//    private String mainImageBucketName;
//
//    @Value("${OSS_detailImageBucketName}")
//    private String detailImageBucketName;
//
//    @Value("${CDN_mainImage}")
//    private String cdnMainImage;
//
//    @Value("${CDN_detailImage}")
//    private String cdnDetailImage;
//
//
//    /**
//     * 上传文件
//     *
//     * @param multipartFile 存储了待上传的文件信息
//     * @param request       本次请求的request
//     * @return 上传成功后oss的fileKey
//     * @author caolu-msi
//     * @date 2018/2/11
//     */
//    public String putFile(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
//        if (multipartFile.isEmpty()) {
//            throw new Exception("Assert_Biz", "请上传文件！");
//        }
//        // 生成OSSClient
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//        // 判断Bucket是否存在，如果不存在则创建
//        if (ossClient.doesBucketExist(bucketName)) {
//            log.info("您已经创建Bucket：" + bucketName + "。");
//        } else {
//            log.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
//            ossClient.createBucket(bucketName);
//        }
//
//        // 创建上传Object的元信息
//        ObjectMetadata meta = new ObjectMetadata();
//        if (StringUtils.isNotEmpty(request.getParameter("uploaderId"))) {
//            meta.addUserMetadata("uploaderId", request.getParameter("uploaderId"));     //上传人id
//        }
//        if (StringUtils.isNotEmpty(request.getParameter("businessType"))) {
//            meta.addUserMetadata("businessType", request.getParameter("businessType"));  //业务类型
//        }
//
//        String fileType = multipartFile.getContentType();
//        String prekey = fileType.contains("image") ? IMAGE_KEY : FILE_KEY;
//        String[] section = multipartFile.getOriginalFilename().split("\\.");
//        String suffkey = section.length > 1 ? "." + section[section.length - 1] : "";
//
//        //生成文件路径：文件类型(image/file)+年/月/日/+系统当前时间毫秒数+原文件后缀
//        String fileKey = prekey + CommonUtil.filePathGen() + System.currentTimeMillis() + suffkey;
//        try {
//            //上传到OSS
//            ossClient.putObject(bucketName, fileKey, multipartFile.getInputStream(), meta);
//            log.info("Object：" + fileKey + "存入OSS成功。");
//            // https://today-service-test.oss-cn-hangzhou.aliyuncs.com/image/2018/4/25/1524647602120.png
//            return fileKey;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            ossClient.shutdown();
//        }
//        return null;
//    }
//
//    /**
//     * 流上传
//     *
//     * @param localInputStram 本地输入流
//     * @return
//     */
//    public String uploadFileStream(InputStream localInputStram) throws SoaException {
//        if (localInputStram == null) {
//            throw new SoaException("Assert_Biz", "上传文件为空！");
//        }
//        // 创建OSSClient实例
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//        // 创建上传Object的元信息
//        ObjectMetadata meta = new ObjectMetadata();
//        // 判断Bucket是否存在，如果不存在则创建
//        if (ossClient.doesBucketExist(bucketName)) {
//            log.info("您已经创建Bucket：" + bucketName + "。");
//        } else {
//            log.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
//            ossClient.createBucket(bucketName);
//        }
//
//        //生成文件路径：文件类型(image/file)+年/月/日/+系统当前时间毫秒数+原文件后缀
//        String fileKey = FILE_KEY + CommonUtil.filePathGen() + System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
//        try {
//            // 上传文件流
//            ossClient.putObject(bucketName, fileKey, localInputStram);
//        } catch (Exception e) {
//            log.error("OSS流上传失败", e);
//        } finally {
//            // 关闭client
//            ossClient.shutdown();
//            try {
//                localInputStram.close();
//            } catch (IOException e) {
//            }
//        }
//        return fileKey;
//    }
//
//    /**
//     * 下载到本地文件
//     *
//     * @param fileKey   上传到OSS文件Key
//     * @param localFile 本地文件
//     * @return
//     * @throws SoaException
//     */
//    public File downloadFile(String fileKey, File localFile) throws SoaException {
//        if (StringUtils.isEmpty(fileKey)) {
//            throw new SoaException("Assert_Biz", "文件路径为空！");
//        }
//        if (localFile == null) {
//            throw new SoaException("Assert_Biz", "本地文件路径为空！");
//        }
//        // 生成OSSClient
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//        // 判断Bucket是否存在，如果不存在则创建
//        if (ossClient.doesBucketExist(bucketName)) {
//            log.info("您已经创建Bucket：" + bucketName + "。");
//        } else {
//            log.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
//            ossClient.createBucket(bucketName);
//        }
//        // 下载object到文件
//        try {
//            ossClient.getObject(new GetObjectRequest(bucketName, fileKey), localFile);
//        } catch (Exception e) {
//            log.error("OSS下载文件失败", e);
//        } finally {
//            ossClient.shutdown();
//        }
//        return localFile;
//    }
//
//    /**
//     * 获取文件的元信息<br>
//     * ps：元信息的名称大小写不敏感，统一为小写
//     *
//     * @param fileKey oss系统中的filekey
//     * @author caolu-msi
//     * @date 2018/2/12
//     */
//    public Map<String, String> getFileMetadata(String fileKey) throws Exception {
//        if (StringUtils.isEmpty(fileKey.trim())) {
//            throw new SoaException("Assert_Biz", "请输入文件对象的filekey！");
//        }
//        // 生成OSSClient
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//        // Object是否存在
//        boolean found = ossClient.doesObjectExist(bucketName, fileKey);
//
//        if (!found) {
//            throw new SoaException("Assert_Biz", "文件对象不存在，无法获取元信息！");
//        }
//        try {
//            // 获取文件的全部用户自定义元信息
//            ObjectMetadata metadata = ossClient.getObjectMetadata(bucketName, fileKey);
//            return metadata.getUserMetadata();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭client
//            ossClient.shutdown();
//        }
//        return null;
//    }
//
//    /**
//     * 商品主图上传
//     *
//     * @param multipartFile
//     * @param request
//     * @return
//     * @throws SoaException
//     */
//    public String uploadMainImage(MultipartFile multipartFile, HttpServletRequest request, String skuNo) throws SoaException {
//        // https://today-service-test.oss-cn-hangzhou.aliyuncs.com/image/2018/4/25/1524647602120.png
//        return cdnMainImage + uploadImage(multipartFile, request, mainImageBucketName, skuNo);
//    }
//
//
//    /**
//     * 商品详情图上传
//     *
//     * @param multipartFile
//     * @param request
//     * @return
//     * @throws SoaException
//     */
//    public String uploadDetailsImage(MultipartFile multipartFile, HttpServletRequest request, String skuNo) throws SoaException {
//        // https://today-service-test.oss-cn-hangzhou.aliyuncs.com/image/2018/4/25/1524647602120.png
//        return cdnDetailImage + uploadImage(multipartFile, request, detailImageBucketName, skuNo);
//    }
//
//
//    /**
//     * 文件删除
//     *
//     * @param bucketName
//     * @param objectName
//     * @return
//     */
//    public void deleteObject(String bucketName, List<String> objectName) {
//
//        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>delBucketName：" + bucketName);
//        // 创建OSSClient实例。
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//        try {
//            objectName.forEach(
//                    x -> {
//                        // 删除文件。
//                        ossClient.deleteObject(bucketName, x);
//                        log.info("Object：" + x + "从OSS删除成功。");
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
////    /**
////     * DeleteOSSObject对象根据bucketName分组
////     * @param list
////     * @return
////     */
////    private static List<DeleteOSSObject> getListByGroup(List<DeleteOSSObject> list) {
////        List<DeleteOSSObject> result = new ArrayList<DeleteOSSObject>();
////        Map<String, String> map = new HashMap<String, String>();
////
////        for (DeleteOSSObject bean : list) {
////            if (map.containsKey(bean.getBucketName())) {
////                map.put(bean.getBucketName(), map.get(bean.getBucketName()) + bean.getObjectName());
////            } else {
////                map.put(bean.getBucketName(), bean.getObjectName());
////            }
////        }
////        for (Map.Entry<String, String> entry : map.entrySet()) {
////            result.add(new DeleteOSSObject(entry.getKey(), entry.getValue()));
////        }
////        return result;
////    }
//
//
//    /**
//     * 上传商品图片公共方法
//     *
//     * @param multipartFile
//     * @param request
//     * @param imageBucketName
//     * @return
//     * @throws SoaException
//     */
//    public String uploadImage(MultipartFile multipartFile, HttpServletRequest request, String imageBucketName, String skuNo) throws SoaException {
//        if (multipartFile.isEmpty()) {
//            throw new SoaException("Assert_Biz", "请上传文件！");
//        }
//        // 生成OSSClient
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//        // 判断Bucket是否存在，如果不存在则创建
//        if (ossClient.doesBucketExist(imageBucketName)) {
//            log.info("您已经创建Bucket：" + imageBucketName + "。");
//        } else {
//            log.info("您的Bucket不存在，创建Bucket：" + imageBucketName + "。");
//            ossClient.createBucket(imageBucketName);
//        }
//
//        // 创建上传Object的元信息
//        ObjectMetadata meta = new ObjectMetadata();
//        if (StringUtils.isNotEmpty(request.getParameter("uploaderId"))) {
//            meta.addUserMetadata("uploaderId", request.getParameter("uploaderId"));     //上传人id
//        }
//        if (StringUtils.isNotEmpty(request.getParameter("businessType"))) {
//            meta.addUserMetadata("businessType", request.getParameter("businessType"));  //业务类型
//        }
//        meta.addUserMetadata("skuNo", skuNo);
//
//        String fileType = multipartFile.getContentType();
//        String prekey = fileType.contains("image") ? IMAGE_KEY : FILE_KEY;
//        String[] section = multipartFile.getOriginalFilename().split("\\.");
//        String suffkey = section.length > 1 ? "." + section[section.length - 1] : "";
//
//        //生成文件路径：文件类型(image/file)+年/月/日/+系统当前时间毫秒数+原文件后缀
//        String fileKey = skuNo.substring(6, 8) + "/" + System.currentTimeMillis() + UUID.randomUUID().toString() + suffkey;
//        try {
//            //上传到OSS
//            ossClient.putObject(imageBucketName, fileKey, multipartFile.getInputStream(), meta);
//            log.info("Object：" + fileKey + "存入OSS成功。");
//            // https://today-service-test.oss-cn-hangzhou.aliyuncs.com/image/2018/4/25/1524647602120.png
//            return fileKey;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            ossClient.shutdown();
//        }
//        return null;
//    }
//}
