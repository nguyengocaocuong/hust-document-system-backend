package com.hust.edu.vn.documentsystem.event;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NotifyEvent extends ApplicationEvent {
    private NotificationType notificationType;
    private Object data;

    public NotifyEvent(NotificationType notificationType, Object data){
        super(data);
        this.notificationType = notificationType;
        this.data = data;
    }
}
