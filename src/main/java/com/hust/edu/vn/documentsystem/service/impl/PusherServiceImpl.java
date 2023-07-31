package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.service.PusherService;
import com.pusher.rest.Pusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PusherServiceImpl implements PusherService {
    private final Pusher pusher;

    public PusherServiceImpl(Pusher pusher) {
        this.pusher = pusher;
    }


    @Override
    public void triggerChanel(String channel, String event, Object data) {
        pusher.trigger(channel, event, data);
    }
}
