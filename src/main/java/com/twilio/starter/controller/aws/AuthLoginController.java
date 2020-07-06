package com.twilio.starter.controller.aws;

import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.twilio.starter.Application.AWS_COGNITO_IDENTITY_PROVIDER;
import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;


public class AuthLoginController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Route handlePost = (Request request, Response response) -> {
        // Signup
        Map<String, String> responses = new HashMap<>();
        System.out.println("body============");
        System.out.println(request.body());
        System.out.println("clientId============");
        System.out.println(InitiateAuthController.clientId);
        System.out.println("userPoolId============");
        System.out.println(InitiateAuthController.userPoolId);
        System.out.println("cognitoSession============");
        System.out.println(InitiateAuthController.cognitoSession);

        TwilioRequestBody requestBody = readBody(request.body());
        String answer = requestBody.body.substring(0, 6);
        System.out.println("answer============");
        System.out.println(answer);

        responses.put("ANSWER", answer);
        responses.put("USERNAME", InitiateAuthController.phone);

        AdminRespondToAuthChallengeResult result =
                AWS_COGNITO_IDENTITY_PROVIDER.adminRespondToAuthChallenge(
                        new AdminRespondToAuthChallengeRequest()
                                .withChallengeName("CUSTOM_CHALLENGE")
                                .withClientId(InitiateAuthController.clientId)
                                .withUserPoolId(InitiateAuthController.userPoolId)
                                .withSession(InitiateAuthController.cognitoSession)
                                .withChallengeResponses(responses)
                );

        InitiateAuthController.resultType = result.getAuthenticationResult();
        /*Map<String, String> map = new HashMap<>();
        map.put("accessToken", InitiateAuthController.resultType.getAccessToken());
        map.put("refreshToken", InitiateAuthController.resultType.getRefreshToken());
        map.put("phone", TWILIO_PHONE_NUMBER);
        map.put("password", "password_examp1E");*/
        return null;
    };

    static class TwilioRequestBody{
        @JsonProperty(value = "ToCountry")
        private String toCountry;
        @JsonProperty(value = "ToState")
        private String toState;
        @JsonProperty(value = "SmsMessageSid")
        private String smsMessageSid;
        @JsonProperty(value = "NumMedia")
        private String numMedia;
        @JsonProperty(value = "ToCity")
        private String toCity;
        @JsonProperty(value = "FromZip")
        private String fromZip;
        @JsonProperty(value = "SmsSid")
        private String smsSid;
        @JsonProperty(value = "FromState")
        private String fromState;
        @JsonProperty(value = "SmsStatus")
        private String smsStatus;
        @JsonProperty(value = "FromCity")
        private String fromCity;
        @JsonProperty(value = "Body")
        private String body;

        @JsonProperty(value = "FromCountry")
        private String fromCountry;
        @JsonProperty(value = "To")
        private String to;
        @JsonProperty(value = "ToZip")
        private String toZip;
        @JsonProperty(value = "NumSegments")
        private String numSegments;
        @JsonProperty(value = "MessageSid")
        private String messageSid;
        @JsonProperty(value = "AccountSid")
        private String accountSid;
        @JsonProperty(value = "From")
        private String from;
        @JsonProperty(value = "ApiVersion")
        private String apiVersion;

        public String getToCountry() {
            return toCountry;
        }

        public void setToCountry(String toCountry) {
            this.toCountry = toCountry;
        }

        public String getToState() {
            return toState;
        }

        public void setToState(String toState) {
            this.toState = toState;
        }

        public String getSmsMessageSid() {
            return smsMessageSid;
        }

        public void setSmsMessageSid(String smsMessageSid) {
            this.smsMessageSid = smsMessageSid;
        }

        public String getNumMedia() {
            return numMedia;
        }

        public void setNumMedia(String numMedia) {
            this.numMedia = numMedia;
        }

        public String getToCity() {
            return toCity;
        }

        public void setToCity(String toCity) {
            this.toCity = toCity;
        }

        public String getFromZip() {
            return fromZip;
        }

        public void setFromZip(String fromZip) {
            this.fromZip = fromZip;
        }

        public String getSmsSid() {
            return smsSid;
        }

        public void setSmsSid(String smsSid) {
            this.smsSid = smsSid;
        }

        public String getFromState() {
            return fromState;
        }

        public void setFromState(String fromState) {
            this.fromState = fromState;
        }

        public String getSmsStatus() {
            return smsStatus;
        }

        public void setSmsStatus(String smsStatus) {
            this.smsStatus = smsStatus;
        }

        public String getFromCity() {
            return fromCity;
        }

        public void setFromCity(String fromCity) {
            this.fromCity = fromCity;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getFromCountry() {
            return fromCountry;
        }

        public void setFromCountry(String fromCountry) {
            this.fromCountry = fromCountry;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getToZip() {
            return toZip;
        }

        public void setToZip(String toZip) {
            this.toZip = toZip;
        }

        public String getNumSegments() {
            return numSegments;
        }

        public void setNumSegments(String numSegments) {
            this.numSegments = numSegments;
        }

        public String getMessageSid() {
            return messageSid;
        }

        public void setMessageSid(String messageSid) {
            this.messageSid = messageSid;
        }

        public String getAccountSid() {
            return accountSid;
        }

        public void setAccountSid(String accountSid) {
            this.accountSid = accountSid;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public void setApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
        }
    }

    public static TwilioRequestBody readBody(String query) throws JsonProcessingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }

        String json = objectMapper.writeValueAsString(query_pairs);
        System.out.println(json);
        return objectMapper.readValue(json, TwilioRequestBody.class);
    }
}
