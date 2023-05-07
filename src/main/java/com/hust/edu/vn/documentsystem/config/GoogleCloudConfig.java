package com.hust.edu.vn.documentsystem.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.translate.v3.TranslationServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage storage() {
        StorageOptions options = StorageOptions.getDefaultInstance();
        return options.getService();
    }

    @Bean
    public TranslationServiceClient translationServiceClient() throws IOException {
        return  TranslationServiceClient.create();
    }

}
