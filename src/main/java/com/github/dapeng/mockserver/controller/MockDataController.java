package com.github.dapeng.mockserver.controller;

import com.github.dapeng.mockserver.services.MockService;
import com.github.dapeng.mockserver.util.Resp;
import com.github.dapeng.mockserver.util.RespEnum;
import lombok.extern.slf4j.Slf4j;
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.status(500).body(Resp.of(RespEnum.ERROR));
    }
}
