package com.twilio.starter.domain.sessionStart;

import com.twilio.starter.domain.createUpload.Ticket;

public class SessionStartResponse {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}