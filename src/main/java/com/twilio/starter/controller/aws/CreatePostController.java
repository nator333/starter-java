package com.twilio.starter.controller.aws;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.Maps;
import com.twilio.starter.domain.CreateUploadResponseWrapper;
import com.twilio.starter.domain.JsonResponse;
import com.twilio.starter.utils.FileAccessUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class CreatePostController {
    public static String accessToken = "";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    private static final String POST_API_URL = "https://%s.dev.muzetv.app/posts-api/graph";

    public static Route handlePost = (Request request, Response response) -> {
        FileAccessUtil util = new FileAccessUtil();
        File tempFile = File.createTempFile("temp", ".mp4");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            // Writes bytes from the specified byte array to this file output stream
            fos.write(util.getReadAllBytes("food.mp4"));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        accessToken = request.queryParams("accessToken");
        Integer amount = Integer.parseInt(request.queryParams("amount"));
        String originalPostId = request.queryParams("originalPostId");
        String ticketId = request.queryParams("ticketId");

        for (int i = 0; i < amount; i++) {
            // Get POST data

            // 1. create upload
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("query", "mutation StageUploadMutation($input: StageUploadMutationInput!) { createUpload(input: $input) { ... on StageUploadMutationSuccess { ticket { id uploadURL } } ... on StageUploadMutationFailure { messages { message errorCode } } } }");
            data.put("operationName", "StageUploadMutation");

            Map<String, Object> variables = new LinkedHashMap<>();
            Map<String, Object> input = new LinkedHashMap<>();
            if (!originalPostId.trim().isEmpty()) {
                input.put("id", originalPostId);
            }
            variables.put("input", input);
            data.put("variables", variables);

            HttpContent createUploadContent = new JsonHttpContent(JSON_FACTORY, data);
            HttpRequest createPostRequest = requestFactory.buildPostRequest(
                    new GenericUrl(String.format(POST_API_URL, ticketId)),
                    createUploadContent
            );
            HttpHeaders headers = createPostRequest.getHeaders();
            System.out.println(accessToken);
            headers.set("Authorization", "Bearer " + accessToken);

            String rawResponse = createPostRequest.execute().parseAsString();
            JsonResponse<CreateUploadResponseWrapper> decoded = objectMapper.readValue(rawResponse, new TypeReference<JsonResponse<CreateUploadResponseWrapper>>() {
            });
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(decoded));

            String postId = decoded.getData().getCreateUpload().getTicket().getId();
            String postURL = decoded.getData().getCreateUpload().getTicket().getUploadURL();

            // 2. upload video
            FileContent fileContent = new FileContent("video/mp4", tempFile);
            HttpRequest putRequest = requestFactory.buildPutRequest(
                    new GenericUrl(postURL), fileContent);
            HttpHeaders putHeaders = putRequest.getHeaders();
            putHeaders.set("Content-Type", "video/mp4");
            putHeaders.set("Content-Length", tempFile.length());

            try {
                HttpResponse uploadResponse = putRequest.execute();
                System.out.println(IOUtils.toString(uploadResponse.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed in uploading a video");
                return e.getMessage();
            }

            // 3. Complete upload
            data.clear();
            data.put("query", "mutation CompleteUploadMutation($input: CompleteUploadMutationInput!) { completeUpload(input: $input) { ... on CompleteUploadMutationSuccess { post { id analyticsId createdAt user { id } posterURL previewURL shareURL watchURL originalURL masteredURL duration laughs nextLaugh variants shares externalShares nextExternalShare totalShares nextTotalShares commentsCount { value valueText nextValue nextValueText } punchline punchlineConfig { animation textColor textStyle } musicMeta { id name author visual audio } } } ... on CompleteUploadMutationFailure { messages { message errorCode } } } }");
            data.put("operationName", "CompleteUploadMutation");
            input.clear();
            input.put("ticketId", postId);
            variables.put("input", input);
            data.put("variables", variables);

            HttpContent completeUploadContent = new JsonHttpContent(JSON_FACTORY, data);
            HttpRequest completePostRequest = requestFactory.buildPostRequest(
                    new GenericUrl(String.format(POST_API_URL, ticketId)),
                    completeUploadContent
            );
            HttpHeaders completeHeaders = completePostRequest.getHeaders();
            completeHeaders.set("Authorization", "Bearer " + accessToken);

            rawResponse = completePostRequest.execute().parseAsString();
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rawResponse));

        }

        return "rawResponse";
    };
}
