package com.example.demoelasticsearch.constant;

/**
 * @Author liull
 * @Date 2024/3/10 16:07
 **/
public class ElasticsearchConstant {

    public static final String CREATE_INDEX_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"info\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"email\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"name\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"firstName\": {\n" +
            "            \"type\": \"keyword\"\n" +
            "          },\n" +
            "          \"secondName\": {\n" +
            "            \"type\": \"keyword\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String ADD_DOCUMENT_TEMPLATE = "{\n" +
            "          \"name\" : {\n" +
            "            \"firstName\" : \"知夏\",\n" +
            "            \"secondName\" : \"盛\"\n" +
            "          },\n" +
            "          \"info\" : \"小美女一个\",\n" +
            "          \"age\" : 10,\n" +
            "          \"email\" : \"17722223334@163.com\"\n" +
            "        }";
}
