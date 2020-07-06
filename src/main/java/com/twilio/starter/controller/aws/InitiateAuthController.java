package com.twilio.starter.controller.aws;

import com.amazonaws.services.cognitoidp.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.starter.domain.AuthResult;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.starter.Application.AWS_COGNITO_IDENTITY_PROVIDER;
import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;


public class InitiateAuthController {

    private static final String PASSWORD = "password_examp1E";
    public static String userPoolId = "";
    public static String clientId = "";
    public static String username = "";
    public static String cognitoSession = null;
    public static String phone = "";
    public static AuthenticationResultType resultType = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        return objectMapper.writeValueAsString(authResult);
    };
}
