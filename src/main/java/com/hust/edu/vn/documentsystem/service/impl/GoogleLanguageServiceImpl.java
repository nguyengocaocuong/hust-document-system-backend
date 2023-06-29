package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Document.Type;
import com.hust.edu.vn.documentsystem.service.GoogleLanguageService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleLanguageServiceImpl implements GoogleLanguageService {
    @Override
    public boolean detectBabComment(String comment) throws IOException {
        LanguageServiceClient language = LanguageServiceClient.create();
        Document doc = Document.newBuilder().setContent(comment).setType(Type.PLAIN_TEXT).build();
        Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
        return sentiment.getScore() < 0;
    }
}
