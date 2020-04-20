package com.twilio.starter.controller.base;

import com.twilio.rest.api.v2010.account.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;


public class MessageController {

    public static Route handlePost = (Request request, Response response) -> {
        // Get POST data
        String to = request.queryParams("to");

        Message call = Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                "Good luck on your Twilio quest!")
                .create();

        System.out.println(call);

        return "Message incoming!";
    };
}
