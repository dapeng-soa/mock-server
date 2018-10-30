package com.github.dapeng.mockserver.controller;

import com.github.dapeng.mockserver.services.MockService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 2:01 PM
 */
@RestController
@RequestMapping("mock")
public class IndexController {
    private final MockService mockService;

    public IndexController(MockService mockService) {
        this.mockService = mockService;
    }

    @RequestMapping("{name}")
    public Object testMock(@PathVariable String name) {

        return mockService.findMockByName(name);
    }

}
