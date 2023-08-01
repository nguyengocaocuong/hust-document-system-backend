package com.hust.edu.vn.documentsystem.config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
    private final String appId = System.getenv("1523503");
    private final String appKey = System.getenv("070ff19e8a1a4c8d4553");
    private final String appSecret = System.getenv("88eac97d6be356957e07");
    private final String appCluster = System.getenv("ap1");

    @Bean
    public Pusher pusher(){
        Pusher pusher = new Pusher(appId, appKey, appSecret);
        pusher.setCluster(appCluster);
        pusher.setEncrypted(true);
        return pusher;
    }
}
