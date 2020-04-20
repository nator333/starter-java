package com.twilio.starter.controller.base;

import com.twilio.rest.api.v2010.account.Call;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URI;

import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;

public class CallController {

    public static Route handlePost = (Request request, Response response) -> {
        // Get POST data
        String to = request.queryParams("to");

        Call call = Call.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                URI.create("http://demo.twilio.com/docs/voice.xml"))
                .create();

        return "Call is inbound!";
    };

}
