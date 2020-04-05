package com.migu.task.mapper;

import com.wibo.common.pojo.TastJob;
import com.wibo.common.pojo.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MysqlMapper {

    List<Test> selectTest();

    TastJob selectTaskJob(@Param("taskCode") String taskCode);
}
