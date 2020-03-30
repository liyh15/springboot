package com.wibo.server.mapper;

import com.wibo.common.pojo.Test;

import java.util.List;

public interface MysqlMapper {

    List<Test> selectTest();
}
