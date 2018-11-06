package com.github.dapeng.dms.mvc.web.controller;

import com.github.dapeng.dms.mvc.entity.Mock;
import com.github.dapeng.dms.mvc.services.MockService;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespEnum;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:13 PM
 */
@RestController
@RequestMapping("/mock")
@Slf4j
//@Indexed
public class MockDataController {

    private final MockService mockService;

    public MockDataController(MockService mockService) {
        this.mockService = mockService;
    }

    /**
     * 显示指定 mock-key 下面的 mock 规则
     */
    @GetMapping("/list/{service}/{method}/{version}")
    public Object findMockDataByMockKey(@PathVariable String service, @PathVariable String method,
                                        @PathVariable String version) {

        try {
            return mockService.findMockDataByMockKey(service, method, version);
        } catch (Exception e) {
            log.error("FindMockDataByMockKey Failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Resp.of(RespEnum.ERROR.getCode(),
                    "FindMockDataByMockKey Failed: " + e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity addMockData(@RequestParam String service, @RequestParam String method, @RequestParam String version,
                                      @RequestParam String mockExpress, @RequestParam String mockData) {
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


    /**
     * 优先级修改。默认规则是 从下往上进行移动。
     * a     =>     b
     * b     =>     a
     *
     * @param frontId 要修改的mock id
     * @param belowId 需要排到某条规则之前的，那条规则的 mock id
     * @return ResponseEntity
     */
    @PostMapping("/modify/priority")
    public ResponseEntity modifyMockPriority(@RequestParam long frontId, @RequestParam long belowId) {
       /* if (belowId < frontId) {
            return ResponseEntity.status(HttpStatus.OK).body(Resp.of(RespEnum.OK));
        }*/
        try {
            mockService.modifyMockPriority(frontId, belowId);
            return ResponseEntity.ok(Resp.of(RespEnum.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Resp.of(RespEnum.ERROR));
        }
    }
}
