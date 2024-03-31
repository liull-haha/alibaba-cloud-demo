package com.example.demoelasticsearch;

import com.example.demoelasticsearch.service.DocumentOperateService;
import com.example.demoelasticsearch.service.DocumentQueryService;
import com.example.demoelasticsearch.service.IndexOperateService;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
class DemoElasticsearchApplicationTests {

	@Autowired
	private IndexOperateService indexOperateService;

	@Autowired
	private DocumentOperateService documentOperateService;

	@Autowired
	private DocumentQueryService documentQueryService;

	/**
	 * 创建索引库
	 */
	@Test
	void testCreateIndex() throws Exception {
		indexOperateService.createIndex();
	}

	/**
	 * 新增一个文档
	 */
	@Test
	void testAddDocument() throws IOException {
		documentOperateService.addDocument();
	}

	/**
	 * 根据文档id查询文档信息
	 */
	@Test
	void testGetDocument() throws IOException {
		documentOperateService.getDocument();
	}

	/**
	 * 根据文档id更新文档信息
	 */
	@Test
	void testUpdateDocument() throws IOException {
		documentOperateService.updateDocument();
	}

	/**
	 * 根据文档id删除文档信息
	 */
	@Test
	void testDeletDocument() throws IOException {
		documentOperateService.deleteDocument();
	}

	/**
	 * 发起批量操作
	 */
	@Test
	void testBulk() throws IOException {
		documentOperateService.bulk();
	}

	/**
	 * 查询所有
	 */
	@Test
	void testQueryMatchAll() throws IOException {
		SearchResponse searchResponse = documentQueryService.queryMatchAll();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleResponse(searchResponse);
	}

	/**
	 * 查询匹配
	 */
	@Test
	void testQueryMatch() throws IOException {
		SearchResponse searchResponse = documentQueryService.queryMatch();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleResponse(searchResponse);
	}

	/**
	 * 查询组合
	 */
	@Test
	void testQueryBool() throws IOException {
		SearchResponse searchResponse = documentQueryService.queryBool();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleResponse(searchResponse);
	}

}
