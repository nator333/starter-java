package com.twilio.starter.controller.base;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import spark.Request;
import spark.Response;
import spark.Route;


public class SmsReplyController {

    public static Route handlePost = (Request request, Response response) -> {
        response.type("application/xml");
        Body body = new Body
                .Builder("TwilioQuest rules")
                .build();
        Message sms = new Message
                .Builder()
                .body(body)
                .action("/status")
                .build();
        MessagingResponse twiml = new MessagingResponse
                .Builder()
                .message(sms)
                .build();
        return twiml.toXml();
    };

}
