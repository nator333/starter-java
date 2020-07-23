package com.twilio.starter.controller.aws;

import com.amazonaws.services.cognitoidp.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.twilio.starter.domain.AuthResult;
import com.twilio.starter.domain.createUpload.CreateUploadResponseWrapper;
import com.twilio.starter.domain.JsonResponse;
import com.twilio.starter.domain.sessionStart.SessionStartResponseWrapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

import static com.twilio.starter.Application.AWS_COGNITO_IDENTITY_PROVIDER;


public class InitiateAuthController {

    private static final String PASSWORD = "password_examp1E";
    public static String userPoolId = "";
    public static String clientId = "";
    public static String username = "";
    public static String cognitoSession = null;
    public static String phone = "";
    public static AuthenticationResultType resultType = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    private static final String USER_API_URL = "https://%s.dev.muzetv.app/users-api/graph";

    public static Route handlePost = (Request request, Response response) -> {
        // reset session
        cognitoSession = null;
        // Get POST data
        userPoolId = request.queryParams("userPoolId");
        clientId = request.queryParams("clientId");
        username = request.queryParams("username");
        phone = request.queryParams("phone");

        // Initiate authentication
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", phone);
        authParams.put("PASSWORD", PASSWORD);

        AdminInitiateAuthResult result = AWS_COGNITO_IDENTITY_PROVIDER.adminInitiateAuth(
                new AdminInitiateAuthRequest()
                        .withAuthFlow(AuthFlowType.CUSTOM_AUTH)
                        .withClientId(clientId)
                        .withUserPoolId(userPoolId)
                        .withAuthParameters(authParams)
        );

        // Get Session
        cognitoSession = result.getSession();
        System.out.println("cognitoSession============");
        System.out.println(cognitoSession);

        while (resultType == null) Thread.sleep(250);

        System.out.println("Received answer============");
        System.out.println(objectMapper.writeValueAsString(resultType));
        
        AuthResult authResult = new AuthResult();
        authResult.accessToken = resultType.getAccessToken();
        authResult.refreshToken = resultType.getRefreshToken();

        // Start session API
        String userSub = request.queryParams("userSub").trim();
        String ticketId = request.queryParams("ticketId");
        if (!userSub.isEmpty() && !ticketId.isEmpty()) {
            System.out.println("cognitoUserId============");
            System.out.println(userSub);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("query", "mutation SessionStartMutation($input: SessionStartMutationInput!) { sessionStart(input: $input) { ... on SessionStartMutationSuccess { user { id name avatarURL biography followCount followerCount variantCount viewCount confirmedInterests } } ... on SessionStartMutationFailure { errors { message }} } }");
            data.put("operationName", "SessionStartMutation");

            Map<String, Object> variables = new LinkedHashMap<>();
            Map<String, Object> input = new LinkedHashMap<>();
            input.put("cognitoUserId", userSub);
            variables.put("input", input);
            data.put("variables", variables);

            HttpContent createUploadContent = new JsonHttpContent(JSON_FACTORY, data);
            HttpRequest createPostRequest = requestFactory.buildPostRequest(
                    new GenericUrl(String.format(USER_API_URL, ticketId)),
                    createUploadContent
            );
            HttpHeaders headers = createPostRequest.getHeaders();
            headers.set("Authorization", "Bearer " + authResult.accessToken);

            String rawResponse = createPostRequest.execute().parseAsString();
            System.out.println(rawResponse);
            JsonResponse<SessionStartResponseWrapper> decoded = objectMapper.readValue(rawResponse, new TypeReference<JsonResponse<SessionStartResponseWrapper>>() {
            });
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(decoded));
        }

        return objectMapper.writeValueAsString(authResult);
    };
}
