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
    private static final String POST_API_URL = "https://ticket-1234.dev.muzetv.app/posts-api/graph";

    public static Route handlePost = (Request request, Response response) -> {
        FileAccessUtil util = new FileAccessUtil();
        File tempFile = File.createTempFile("temp", ".mp4");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            // Writes bytes from the specified byte array to this file output stream
            fos.write(util.getReadAllBytes("basketball.mp4"));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        for (int i = 0; i < 100; i++) {
            // Get POST data
            accessToken = request.queryParams("accessToken");
            String originalPostId = request.queryParams("originalPostId");

            System.out.println(request.attributes());
            System.out.println(request.body());

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
                    new GenericUrl(POST_API_URL),
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
            Map<String, Object> parameters = Maps.newHashMap();
            //parameters.put("contentType", printRequest.getContentType());

            MultipartContent multiContent = new MultipartContent();
            multiContent.addPart(new MultipartContent.Part(new UrlEncodedContent(parameters)));
            multiContent.addPart(
                    new MultipartContent.Part(
                            new FileContent(
                                    "video/mp4", tempFile
                            )
                    )
            );

            try {
                HttpResponse uploadResponse = requestFactory.buildPutRequest(
                        new GenericUrl(postURL), multiContent).execute();
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
                    new GenericUrl(POST_API_URL),
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
