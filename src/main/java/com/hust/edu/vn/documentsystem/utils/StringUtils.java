package com.hust.edu.vn.documentsystem.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;
public class StringUtils {
    public static String removeDiacritics(String text) {
        if(text == null) return null;
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("[^\\p{L}\\d\\s]", "").toLowerCase();
    }
}
