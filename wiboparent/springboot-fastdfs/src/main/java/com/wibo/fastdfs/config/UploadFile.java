package com.wibo.fastdfs.config;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UploadFile {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;


}
