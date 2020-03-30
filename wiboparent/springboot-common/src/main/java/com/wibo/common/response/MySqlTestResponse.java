package com.wibo.common.response;

import com.wibo.common.pojo.Test;
import lombok.Data;
import java.util.List;

@Data
public class MySqlTestResponse extends BaseResponse<MySqlTestResponse>{

    private List<Test> testList;
}
