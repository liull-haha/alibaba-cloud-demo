package com.example.demoelasticsearch.service;

import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * @Author liull
 * @Date 2024/3/31 16:27
 **/
public interface DocumentQueryService {
    SearchResponse queryMatchAll() throws IOException;

    SearchResponse queryMatch() throws IOException;

    SearchResponse queryBool() throws IOException;

    SearchResponse pagingAndSortQuery() throws IOException;

    SearchResponse highlightQuery() throws IOException;

    SearchResponse functionScoreQuery() throws IOException;

    SearchResponse aggegationQuery() throws IOException;

    void handleSourceResponse(SearchResponse searchResponse);

    void handleHighlightResponse(SearchResponse searchResponse);

    void handleAggregationResponse(SearchResponse searchResponse);
}
