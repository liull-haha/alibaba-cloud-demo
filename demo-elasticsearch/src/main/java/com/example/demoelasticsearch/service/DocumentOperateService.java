package com.example.demoelasticsearch.service;

import java.io.IOException;

/**
 * @Author liull
 * @Date 2024/3/31 16:11
 **/
public interface DocumentOperateService {
    void addDocument() throws IOException;

    void getDocument() throws IOException;

    void updateDocument() throws IOException;

    void deleteDocument() throws IOException;

    void bulk() throws IOException;
}
