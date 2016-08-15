package com.zakhuang.submitcanceltransaction.service;

import com.zakhuang.submitcanceltransaction.annotation.CancelableMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 2016/8/10.
 */
@Service
@Transactional
public class ExampleAService {
    @Autowired
    private ExampleBService bService;
    @Autowired
    private ExampleDService dService;
    @Autowired
    private ExampleCService cService;

    @CancelableMethod
    public void begin(String aaa) {
        bService.server();
        cService.server();
        dService.server();
    }
}
