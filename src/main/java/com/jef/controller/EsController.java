package com.jef.controller;

import com.alibaba.fastjson.JSON;
import com.jef.constant.CommonConstant;
import com.jef.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author tufujie
 * @date 2023/9/12
 */
@Controller
@RequestMapping("/es")
@RequiredArgsConstructor
@Slf4j
public class EsController {

    private final RestHighLevelClient client;

    private final String INDEX_NAME = "test_index";

    /**
     * 创建索引
     */
    @ResponseBody
    @RequestMapping("/testCreateIndex")
    String testCreateIndex() {
        // 1、创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        try {
            // 2、客户端执行请求
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(createIndexResponse);
        } catch (Exception e) {
            log.error("创建索引异常，异常信息为：{}", e.getMessage(), e);
        }
        return "success";
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/testDeleteIndex")
    String testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response);
        return "success";
    }

    /**
     * 获取索引
     *
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/testExistIndex")
    String testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(INDEX_NAME);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
        return "success";
    }

    /**
     * 添加文档
     *
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/testAddDocument/{id}")
    String testAddDocument(@PathVariable("id") String id) throws IOException {
        User userEntity = new User(30, CommonConstant.USER_NAME);
        // 创建请求
        IndexRequest request = new IndexRequest(INDEX_NAME);
        // 规则 put /kuang_index/_doc/1
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString();
        }
        request.id(id);
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        //添加数据
        request.source(JSON.toJSONString(userEntity), XContentType.JSON);
        //客户端发送请求
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response);
        return "success";
    }

    /**
     * 删除文档
     */
    @ResponseBody
    @RequestMapping("/indexDeleteDocument/{id}")
    public String indexDeleteDocument(@PathVariable("id") String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_NAME, id);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
        return "success";
    }

    /**
     * 更新文档
     */
    @ResponseBody
    @RequestMapping("/indexUpdateDocument/{id}")
    public String indexUpdateDocument(@PathVariable("id") String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, id);
        updateRequest.timeout("1s");
        User userEntity = new User(20, "小然");
        updateRequest.doc(JSON.toJSONString(userEntity), XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
        return "success";
    }

    /**
     * 获取文档
     */
    @ResponseBody
    @RequestMapping("/indexGetDocument/{id}")
    public String indexGetDocument(@PathVariable("id") String id) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX_NAME, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        return getResponse.getSourceAsString();
    }

    /**
     * 批量插入数据
     */
    @ResponseBody
    @RequestMapping("/addList")
    public String addList() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(19, "xiaoming"));
        list.add(new User(16, "xiaoming2"));
        // 批量请求
        for (int i = 0; i < list.size(); i++) {
            bulkRequest.add(
                    new IndexRequest(INDEX_NAME)
                            .id(UUID.randomUUID().toString())
                            .source(JSON.toJSONString(list.get(i)), XContentType.JSON)
            );
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        }
        return "success";
    }

    /**
     * 查询批量数据
     */
    @ResponseBody
    @RequestMapping("/searchList")
    public String searchList() throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.highlighter();
        // 查询条件
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "xiaoming");
        sourceBuilder.query(termQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        StringBuilder result = new StringBuilder();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            result.append(documentFields.getSourceAsString());
        }
        return result.toString();
    }

}