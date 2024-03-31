package com.example.demoelasticsearch.service.impl;

import com.example.demoelasticsearch.service.DocumentOperateService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.demoelasticsearch.constant.ElasticsearchConstant.ADD_DOCUMENT_TEMPLATE;

/**
 * @Author liull
 * @Date 2024/3/31 16:11
 **/
@Service
public class DocumentOperateServiceImpl implements DocumentOperateService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加文档
     * @throws IOException
     */
    @Override
    public void addDocument() throws IOException {
        IndexRequest request = new IndexRequest("personinfo");
        request.source(ADD_DOCUMENT_TEMPLATE, XContentType.JSON);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 获取文档
     * @throws IOException
     */
    @Override
    public void getDocument() throws IOException {
        GetRequest request = new GetRequest("personinfo","LYl6J44BFAKuM2la5TRX");
        GetResponse documentFields = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSourceAsString());
    }

    /**
     * 更新文档
     * @throws IOException
     */
    @Override
    public void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("personinfo","LYl6J44BFAKuM2la5TRX");
		/*Map<String,Object> sourceMap = new HashMap<>();
		sourceMap.put("age","20");
		request.doc(sourceMap);*/
        request.doc(
                "age","10"
        );
        restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除文档
     * @throws IOException
     */
    @Override
    public void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("personinfo","LIljIo4BFAKuM2la2DRw");
        restHighLevelClient.delete(request,RequestOptions.DEFAULT);
    }


    /**
     * 批量操作
     * @throws IOException
     */
    @Override
    public void bulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        //可以添加增删改操作到批量操作里
        bulkRequest.add(new IndexRequest("personinfo").source("。。。",XContentType.JSON));
        bulkRequest.add(new IndexRequest("personinfo").source("。。。",XContentType.JSON));
        bulkRequest.add(new IndexRequest("personinfo").source("。。。",XContentType.JSON));
        bulkRequest.add(new IndexRequest("personinfo").source("。。。",XContentType.JSON));
        //发起批量操作
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
    }
}
