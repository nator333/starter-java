package com.twilio.starter.controller.base;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import spark.Request;
import spark.Response;
import spark.Route;

public class StatusController {


    public static Route handlePost = (Request request, Response response) -> {
        System.out.println(request.body());
        return null;
    };

}
