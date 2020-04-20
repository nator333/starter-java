package com.twilio.starter;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.twilio.Twilio;
import com.twilio.starter.controller.base.*;
import com.twilio.starter.controller.aws.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;

public class Application {

    private static final String ACCOUNT_SID = "***";
    private static final String AUTH_TOKEN = "***";
    public static final String TWILIO_PHONE_NUMBER = "***";

    public static final AWSCognitoIdentityProvider AWS_COGNITO_IDENTITY_PROVIDER;

    static {
        AWSCognitoIdentityProviderClientBuilder builder =
                AWSCognitoIdentityProviderClientBuilder.standard();
        AWS_COGNITO_IDENTITY_PROVIDER = builder.build();
    }

    public static void main(String[] args) {
        initExceptionHandler((e) -> System.out.println("Uh-oh"));
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        staticFileLocation("/public");

        // hello.html file is in resources/templates directory
        get("/", IndexController.handlePost, new ThymeleafTemplateEngine());
        post("/call", CallController.handlePost);
        post("/message", MessageController.handlePost);
        post("/hello", TwimlController.handlePost);
        post("/sms", SmsReplyController.handlePost);
        get("/status", StatusController.handlePost);

        // AWS
        post("/initiateAuth", InitiateAuthController.handlePost);
        post("/authLogin", AuthLoginController.handlePost);
        post("/refreshToken", RefreshTokenController.handlePost);


        get("/login", (req, res) -> {
            System.out.println(req.body());
            return null;
        });
    }
}
