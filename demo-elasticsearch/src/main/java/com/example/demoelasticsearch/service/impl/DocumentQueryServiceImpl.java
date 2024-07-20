package com.example.demoelasticsearch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demoelasticsearch.dto.PersonInfoDTO;
import com.example.demoelasticsearch.service.DocumentQueryService;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public SearchResponse pagingAndSortQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().query(QueryBuilders.matchAllQuery());
        //排序
        searchRequest.source().sort("age", SortOrder.DESC);
        //分页查询
        searchRequest.source().from(0).size(5);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse highlightQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().query(QueryBuilders.matchQuery("info", "知夏"));
        searchRequest.source().highlighter(
                new HighlightBuilder()
                .field("info")
                .requireFieldMatch(false)
                .preTags("<font color='red'>")
                .postTags("</font>")
        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 给部分文档增加权重，排序靠前
     */
    @Override
    public SearchResponse functionScoreQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().query(
                QueryBuilders.functionScoreQuery(
                        QueryBuilders.matchQuery("info", "知夏"),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                        QueryBuilders.termQuery("isAD", true),
                                        ScoreFunctionBuilders.weightFactorFunction(5)
                                )
                        }
                )
        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 聚合查询
     */
    @Override
    public SearchResponse aggegationQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().query(QueryBuilders.matchAllQuery());
        //不需要返回文档数据
        searchRequest.source().size(0);
        //聚合
        searchRequest.source().aggregation(
                AggregationBuilders.terms("firstNameAggs").field("name.firstName").size(10).order(BucketOrder.count(true))
        );
        searchRequest.source().aggregation(
                AggregationBuilders.terms("secondNameAggs").field("name.secondName").size(10).order(BucketOrder.count(true))
        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse suggestQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("personinfo");
        searchRequest.source().suggest(new SuggestBuilder()
                .addSuggestion(
                        "titlesSuggestion",
                        SuggestBuilders.completionSuggestion("title")
                                .prefix("知")
                                .skipDuplicates(true)
                )
        );
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void handleSourceResponse(SearchResponse searchResponse) {
        //解析结果
        SearchHits hits = searchResponse.getHits();
        //获取总条数
        TotalHits totalHits = hits.getTotalHits();
        System.out.println(totalHits.value);
        //获取结果集
        SearchHit[] searchHits = hits.getHits();
        Arrays.stream(searchHits).forEach(searchHit -> System.out.println(searchHit.getSourceAsString()));
    }

    @Override
    public void handleHighlightResponse(SearchResponse searchResponse) {
        //解析结果
        SearchHits hits = searchResponse.getHits();
        //获取总条数
        TotalHits totalHits = hits.getTotalHits();
        System.out.println(totalHits.value);
        //获取结果集
        SearchHit[] searchHits = hits.getHits();
        Arrays.stream(searchHits).forEach(searchHit -> {
            PersonInfoDTO personInfoDTO = JSONObject.parseObject(searchHit.getSourceAsString(), PersonInfoDTO.class);
            //获取高亮字段
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            highlightFields.forEach((k,v)->{
                String highLightText = v.getFragments()[0].string();
                //使用基础的反射
//                Class<? extends PersonInfoDTO> personInfoDTOClass = personInfoDTO.getClass();
//                try {
//                    Field declaredField = personInfoDTOClass.getDeclaredField(k);
//                    declaredField.setAccessible(true);
//                    declaredField.set(personInfoDTO,highLightText);
//                } catch (IllegalAccessException | NoSuchFieldException e) {
//                    throw new RuntimeException(e);
//                }
                //使用BeanWrapper
                BeanWrapper beanWrapper = new BeanWrapperImpl(personInfoDTO);
                beanWrapper.setPropertyValue(k,highLightText);
            });

            //打印结果
            System.out.println(personInfoDTO.toString());
        });
   }


   @Override
   public void handleAggregationResponse(SearchResponse searchResponse) {
       Aggregations aggregations = searchResponse.getAggregations();
       if (aggregations != null){
           //遍历聚合结果集 k为聚合名称
            aggregations.asMap().forEach((k,v)->{
                if (v instanceof Terms){
                    //获取聚合结果集
                    Terms terms = (Terms) v;
                    //获取桶
                    List<? extends Terms.Bucket> buckets = terms.getBuckets();
                    buckets.forEach(bucket -> {
                        System.out.println(bucket.getKey());
                        System.out.println(bucket.getDocCount());
                    });
                }
            });
       }
   }

   @Override
   public void handleSuggestResponse(SearchResponse searchResponse) {
       Suggest suggest = searchResponse.getSuggest();
       if (suggest != null) {
           Iterator<Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>>> iterator = suggest.iterator();
           while (iterator.hasNext()) {
               Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> next = iterator.next();
               List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries = next.getEntries();
               entries.forEach(entry -> {
                   List<? extends Suggest.Suggestion.Entry.Option> options = entry.getOptions();
                   options.forEach(option -> {
                       String text = option.getText().string();
                       System.out.println(text);
                   });
               });
           }
       }
   }

}
