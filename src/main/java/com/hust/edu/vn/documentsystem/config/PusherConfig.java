package com.hust.edu.vn.documentsystem.config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
    private final String appId = System.getenv("PUSHER_APP_ID");
    private final String appKey = System.getenv("PUSHER_KEY");
    private final String appSecret = System.getenv("PUSHER_SECRET");
    private final String appCluster = System.getenv("PUSHER_CLUSTER");

    @Bean
    public Pusher pusherConfig(){
        Pusher pusherConfig = new Pusher(appId, appKey, appSecret);
        pusherConfig.setCluster(appCluster);
        pusherConfig.setEncrypted(true);
        return pusherConfig;
    }
}
