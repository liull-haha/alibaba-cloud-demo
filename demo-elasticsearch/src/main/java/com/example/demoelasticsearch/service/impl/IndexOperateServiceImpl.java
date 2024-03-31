package com.example.demoelasticsearch.service.impl;

import com.example.demoelasticsearch.service.IndexOperateService;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demoelasticsearch.constant.ElasticsearchConstant.CREATE_INDEX_TEMPLATE;

/**
 * @Author liull
 * @Date 2024/3/31 16:07
 **/
@Service
public class IndexOperateServiceImpl implements IndexOperateService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex() throws Exception{
        CreateIndexRequest request = new CreateIndexRequest("personinfo2");
        request.source(CREATE_INDEX_TEMPLATE, XContentType.JSON);
        //发起创建请求
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }
}
