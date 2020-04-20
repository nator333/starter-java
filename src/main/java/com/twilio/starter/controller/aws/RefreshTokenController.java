package com.twilio.starter.controller.aws;

import com.amazonaws.services.cognitoidp.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.starter.Application.AWS_COGNITO_IDENTITY_PROVIDER;
import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;


public class RefreshTokenController {

    private static final String PASSWORD = "password_examp1E";
    public static String userPoolId = "";
    public static String clientId = "";
    public static String cognitoSession = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Route handlePost = (Request request, Response response) -> {
        // reset session
        cognitoSession = null;
        // Get POST data
        userPoolId = request.queryParams("userPoolId");
        clientId = request.queryParams("clientId");
        String postRefreshToken = request.queryParams("postRefreshToken");

        // Initiate authentication
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", TWILIO_PHONE_NUMBER);
        authParams.put("PASSWORD", PASSWORD);
        authParams.put("REFRESH_TOKEN", postRefreshToken);

        AdminInitiateAuthResult result = AWS_COGNITO_IDENTITY_PROVIDER.adminInitiateAuth(
                new AdminInitiateAuthRequest()
                        .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                        .withClientId(clientId)
                        .withUserPoolId(userPoolId)
                        .withAuthParameters(authParams)
        );

        InitiateAuthController.AuthResult authResult = new InitiateAuthController.AuthResult();
        authResult.accessToken = result.getAuthenticationResult().getAccessToken();
        authResult.refreshToken = postRefreshToken;
        return objectMapper.writeValueAsString(authResult);
    };
}
