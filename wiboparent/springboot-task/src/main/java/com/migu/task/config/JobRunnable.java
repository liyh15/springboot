package com.migu.task.config;

import com.migu.task.job.JobInterface;
import com.wibo.common.pojo.TastJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobRunnable implements Runnable {

    private JobInterface jobInterface;

    public JobRunnable(TastJob tastJob) {
        try {
            Class clz = Class.forName(tastJob.getTaskUrl());
            jobInterface = (JobInterface) clz.newInstance();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void run() {
        jobInterface.runJob();
    }
}
