package com.exam;

import com.exam.service.PaperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperServiceTest {
    @Autowired
    private PaperService paperService;

    @Test
    public void getPaperInformation() {
        // 注释掉是因为下面需要用到别的微服务
//        assertNotNull(paperService.getPaperInformation("aaaabbbbcccc","s1"));
//        assertNull(paperService.getPaperInformation("luanma","s1"));
    }

}
