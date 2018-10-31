package com.github.dapeng.mockserver.controller;

import com.github.dapeng.mockserver.services.MockService;
import com.github.dapeng.mockserver.util.Resp;
import com.github.dapeng.mockserver.util.RespEnum;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:13 PM
 */
@RestController
@RequestMapping("/mock")
@Slf4j
public class MockDataController {
    private final MockService mockService;

    public MockDataController(MockService mockService) {
        this.mockService = mockService;
    }

    @PostMapping("/add")
    public ResponseEntity addMockData(String service, String method, String version,
                                      String mockExpress, String mockData) {
        try {
            mockService.addMockInfo(service, method, version, mockExpress, mockData);
            return ResponseEntity.ok(Resp.of(RespEnum.OK));
        } catch (JSONException e) {
            log.error("Json Schema 解析失败，请检查格式: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(),
                            "Json Schema 解析失败，请检查格式: " + e.getMessage()));
        }
    }
}
