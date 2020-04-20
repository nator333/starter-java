package com.twilio.starter.controller.base;

import spark.*;

import java.util.HashMap;
import java.util.Map;

import static com.twilio.starter.Application.TWILIO_PHONE_NUMBER;

public class IndexController {

    public static TemplateViewRoute handlePost = (Request request, Response response) -> {
        Map<String, String> map = new HashMap<>();
        map.put("phone", TWILIO_PHONE_NUMBER);
        map.put("password", "password_examp1E");
        return new ModelAndView(map, "index");
    };

}
