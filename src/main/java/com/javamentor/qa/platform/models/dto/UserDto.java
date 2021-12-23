package com.javamentor.qa.platform.models.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String fullName;
    private String linkImage;
    private String city;
    private Long reputation;
}
