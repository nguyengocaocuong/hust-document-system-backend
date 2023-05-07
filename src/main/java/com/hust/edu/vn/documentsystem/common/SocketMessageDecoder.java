package com.hust.edu.vn.documentsystem.common;

import com.google.gson.Gson;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

public class SocketMessageDecoder implements Decoder.Text<FirebaseMessage> {

    private static final Gson gson = new Gson();

    @Override
    public FirebaseMessage decode(String s) throws DecodeException {
        return gson.fromJson(s, FirebaseMessage.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
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