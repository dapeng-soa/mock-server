package com.github.dapeng.dms.web.vo;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 3:55 PM
 */
@Data
public class UploadResp {
    private String fileName;
    private String filePath;

    public UploadResp(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
