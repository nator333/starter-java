package com.twilio.starter.controller.aws;

import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.starter.domain.SignUpAuthResult;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

import static com.twilio.starter.Application.AWS_COGNITO_IDENTITY_PROVIDER;


public class SingUpController {

    private static final String PASSWORD = "password_examp1E";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Route handlePost = (Request request, Response response) -> {
        // Get POST data
        String userPoolId = request.queryParams("userPoolId");
        String clientId = request.queryParams("clientId");
        String username = request.queryParams("username");
        String phone = request.queryParams("phone");

        // Signup
        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(new AttributeType().withName("picture").withValue("dummy_path"));
        attributes.add(new AttributeType().withName("preferred_username").withValue(username));
        attributes.add(new AttributeType().withName("phone_number").withValue(phone));

        String userSub = "";
        try {
            SignUpResult signUpResult = AWS_COGNITO_IDENTITY_PROVIDER.signUp(
                    new SignUpRequest()
                            .withClientId(clientId)
                            .withUsername(phone)
                            .withPassword(PASSWORD)
                            .withUserAttributes(attributes)
            );
            userSub = signUpResult.getUserSub();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        System.out.println("cognitoSession============");
        System.out.println(userSub);

        SignUpAuthResult authResult = new SignUpAuthResult();
        authResult.userSub = userSub;
        return objectMapper.writeValueAsString(authResult);
    };
}
