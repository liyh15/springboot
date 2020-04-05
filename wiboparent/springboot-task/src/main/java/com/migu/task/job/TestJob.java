package com.migu.task.job;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestJob implements JobInterface {

    @Override
    public void runJob() {
      log.error("this is test job");
    }
}
