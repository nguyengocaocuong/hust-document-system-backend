package com.hust.edu.vn.documentsystem.utils;

import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DocumentUtils {
    private final Storage storage;
    private final Environment environment;

    @Autowired
    public DocumentUtils(Storage storage, Environment environment) {
        this.storage = storage;
        this.environment = environment;
    }


}
