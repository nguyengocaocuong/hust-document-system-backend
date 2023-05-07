package com.hust.edu.vn.documentsystem.common;

import com.hust.edu.vn.documentsystem.common.type.SocketMessageType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FirebaseMessage {
    private SocketMessageType type;
    private Object body;
    private List<String> userEmails = new ArrayList<>();
}
