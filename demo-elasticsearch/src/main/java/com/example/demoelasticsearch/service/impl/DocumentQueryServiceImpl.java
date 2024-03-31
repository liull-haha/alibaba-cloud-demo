package com.example.demoelasticsearch.service.impl;

import com.example.demoelasticsearch.service.DocumentQueryService;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author liull
 * @Date 2024/3/31 16:27
 **/
@Service
public class DocumentQueryServiceImpl implements DocumentQueryService {


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResponse queryMatchAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().query(
                QueryBuilders.matchAllQuery()
        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse queryMatch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        // 单字段查询
        searchRequest.source().query(
                QueryBuilders.matchQuery("info", "知夏")
        );
        // 多字段查询
//        searchRequest.source().query(
//                QueryBuilders.multiMatchQuery("知夏", "info","name.firstName")
//        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse queryBool() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("name.secondName", "盛"))
                        .should(QueryBuilders.matchQuery("info", "美女"))
                        .mustNot(QueryBuilders.rangeQuery("age").gte(20))
                        .filter(QueryBuilders.rangeQuery("age").lte(20).gte(10))
        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void handleResponse(SearchResponse searchResponse) {
        //解析结果
        SearchHits hits = searchResponse.getHits();
        //获取总条数
        TotalHits totalHits = hits.getTotalHits();
        System.out.println(totalHits.value);
        //获取结果集
        SearchHit[] searchHits = hits.getHits();
        Arrays.stream(searchHits).forEach(searchHit -> System.out.println(searchHit.getSourceAsString()));
    }

}
