package com.migu.task.config;

import com.migu.task.mapper.MysqlMapper;
import com.wibo.common.pojo.TastJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;

@Configuration
@Slf4j
public class TaskConfig implements CommandLineRunner {

    @Autowired
    private MysqlMapper mysqlMapper;

    @Autowired
    private TaskScheduler taskScheduler;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(4);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("TaskSchedulerThreadPool-");
        return taskScheduler;
    }

    @Override
    public void run(String... args) throws Exception {
        TastJob tastJob = mysqlMapper.selectTaskJob(args[0]);
        if (null == tastJob) {
           log.error("task is not exist");
        }
        CronTask cronTask = new CronTask(new JobRunnable(tastJob), tastJob.getCron());
        taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }
}
