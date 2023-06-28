package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import lombok.*;

@Data
public class FavoriteSubjectModel {
    private Long id;
    private NotificationType notificationType = NotificationType.ALL;
}
