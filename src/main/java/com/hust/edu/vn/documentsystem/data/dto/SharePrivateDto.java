package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SharePrivate} entity
 */
@Data
public class SharePrivateDto implements Serializable {
    private  Long id;
    private  UserDto user;
    private  Date sharedAt;
    private  Date expirationTime;
}