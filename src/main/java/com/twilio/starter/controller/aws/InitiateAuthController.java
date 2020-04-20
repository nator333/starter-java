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


public class InitiateAuthController {

    private static final String PASSWORD = "password_examp1E";
    public static String userPoolId = "";
    public static String clientId = "";
    public static String username = "";
    public static String cognitoSession = null;
    public static AuthenticationResultType resultType = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static class AuthResult {
        public String accessToken = null;
        public String refreshToken = null;
    }

    public static Route handlePost = (Request request, Response response) -> {
        // reset session
        cognitoSession = null;
        // Get POST data
        userPoolId = request.queryParams("userPoolId");
        clientId = request.queryParams("clientId");
        username = request.queryParams("username");

        // Signup
        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(new AttributeType().withName("picture").withValue("dummy_path"));
        attributes.add(new AttributeType().withName("preferred_username").withValue(username));
        attributes.add(new AttributeType().withName("phone_number").withValue(TWILIO_PHONE_NUMBER));

        try {
            AWS_COGNITO_IDENTITY_PROVIDER.signUp(
                    new SignUpRequest()
                            .withClientId(clientId)
                            .withUsername(TWILIO_PHONE_NUMBER)
                            .withPassword(PASSWORD)
                            .withUserAttributes(attributes)
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Initiate authentication
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", TWILIO_PHONE_NUMBER);
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

        while (resultType == null) {
            Thread.sleep(1000);
        }

        System.out.println("Received answer============");
        AuthResult authResult = new AuthResult();
        authResult.accessToken = resultType.getAccessToken();
        authResult.refreshToken = resultType.getRefreshToken();
        return objectMapper.writeValueAsString(authResult);
    };
}
