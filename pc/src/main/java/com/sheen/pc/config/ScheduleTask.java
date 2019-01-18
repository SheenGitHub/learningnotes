package com.sheen.pc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zxj7044 on 2018-11-7.
 */
@Component
@Slf4j
@EnableScheduling
public class ScheduleTask {

    private static int count  = 0;
    public void justForTest() {
        log.info("开始");
        System.out.println("Schedule:" + count++ );
        log.info("结束");
    }
}
