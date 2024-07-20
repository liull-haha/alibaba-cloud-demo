package com.example.demoelasticsearch;

import com.example.demoelasticsearch.service.DocumentOperateService;
import com.example.demoelasticsearch.service.DocumentQueryService;
import com.example.demoelasticsearch.service.IndexOperateService;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

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
		documentQueryService.handleSourceResponse(searchResponse);
	}

	/**
	 * 查询匹配
	 */
	@Test
	void testQueryMatch() throws IOException {
		SearchResponse searchResponse = documentQueryService.queryMatch();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleSourceResponse(searchResponse);
	}

	/**
	 * 查询组合
	 */
	@Test
	void testQueryBool() throws IOException {
		SearchResponse searchResponse = documentQueryService.queryBool();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleSourceResponse(searchResponse);
	}

	/**
	 * 高亮
	 */
	@Test
	void testHighlightQuery() throws IOException {
		SearchResponse searchResponse = documentQueryService.highlightQuery();
//		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleHighlightResponse(searchResponse);
	}

	@Test
	void testFunctionScoreQuery() throws IOException {
		SearchResponse searchResponse = documentQueryService.functionScoreQuery();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleSourceResponse(searchResponse);
	}
	@Test
	void testAggregationQuery() throws IOException {
		SearchResponse searchResponse = documentQueryService.aggegationQuery();
		System.out.println(searchResponse);
		//解析结
		documentQueryService.handleAggregationResponse(searchResponse);
	}

	@Test
	void testSuggestQuery() throws IOException {
		SearchResponse searchResponse = documentQueryService.suggestQuery();
		System.out.println(searchResponse);
		//解析结果
		documentQueryService.handleSuggestResponse(searchResponse);
	}

	public static void main(String[] args) {
		// 1. 读取视频文件内容到字节数组
		byte[] videoBytes = new byte[0];
		try {
			videoBytes = Files.readAllBytes(Paths.get("/Users/liulili/Downloads/2515_1712902958.mp4"));
		} catch (IOException e) {

		}

		// 2. 应用Base64编码
		String encodedVideo = Base64.getEncoder().encodeToString(videoBytes);
		System.out.println(encodedVideo);
	}
}
