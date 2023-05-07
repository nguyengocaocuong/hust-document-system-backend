package com.hust.edu.vn.documentsystem.service;


import java.io.IOException;

public interface GoogleLanguageService {
    boolean detectBabComment(String comment) throws IOException;
}
