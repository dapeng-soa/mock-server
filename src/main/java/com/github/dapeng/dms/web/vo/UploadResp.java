package com.github.dapeng.dms.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 3:55 PM
 */
@Data
public class UploadResp {
    private String fileName;
    private String filePath;

    public UploadResp(List<String> result) {
        if (result != null && result.size() == 2) {
            this.fileName = result.get(0);
            this.filePath = result.get(1);
            ;
        }
    }

    public UploadResp(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
