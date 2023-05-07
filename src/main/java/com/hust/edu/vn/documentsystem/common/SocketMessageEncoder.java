package com.hust.edu.vn.documentsystem.common;

import com.google.gson.Gson;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class SocketMessageEncoder implements Encoder.Text<FirebaseMessage> {

    private static final Gson gson = new Gson();

    @Override
    public String encode(FirebaseMessage firebaseMessage) throws EncodeException {
        return gson.toJson(firebaseMessage);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // TODO
    }

    @Override
    public void destroy() {
        // TODO
    }
}