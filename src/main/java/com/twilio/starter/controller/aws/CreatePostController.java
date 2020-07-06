package com.twilio.starter.controller.aws;

import com.amazonaws.http.apache.request.impl.ApacheHttpRequestFactory;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.twilio.starter.Application.AWS_COGNITO_IDENTITY_PROVIDER;
import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;


public class CreatePostController {
    public static String accessToken = "";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static Route handlePost = (Request request, Response response) -> {
        // Get POST data
        accessToken = request.queryParams("accessToken");
        String originalPostId = request.queryParams("originalPostId");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("query", "mutation StageUploadMutation($input: StageUploadMutationInput!) { createUpload(input: $input) { ... on StageUploadMutationSuccess { ticket { id uploadURL } } ... on StageUploadMutationFailure { messages { message errorCode } } } }");
        data.put("operationName", "StageUploadMutation");

        Map<String, Object> variables = new LinkedHashMap<>();
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", originalPostId);
        variables.put("input", input);
        data.put("variables", variables);

        HttpContent content = new JsonHttpContent(JSON_FACTORY, data);
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        HttpRequest createPostRequest = requestFactory.buildPostRequest(
                new GenericUrl("https://ticket-1234.dev.muzetv.app/posts-api/graph"),
                content
        );
        HttpHeaders headers = createPostRequest.getHeaders();
        headers.set("Authorization", accessToken);

        String rawResponse = createPostRequest.execute().parseAsString();

        System.out.println(objectMapper.writeValueAsString(rawResponse));


        return rawResponse;
    };
}
