package com.hust.edu.vn.documentsystem.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"email", "password"})
public class AuthenticationModel {
    @JsonProperty
    @NotBlank
    private String email;
    @JsonProperty
    @NotBlank
    @Size(min = 8)
    private String password;
}
