package com.javamentor.qa.platform.models.dto;

import java.time.LocalDateTime;

//Просто образец Dto
public class UserTestDto {
    public Long id;
    public String email;
    public String fullName;
    public String linkImage;
    public String city;
    public Long reputation;


    public UserTestDto(Long id, String email, String fullName, String linkImage, String city, Long reputation) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.linkImage = linkImage;
        this.city = city;
        this.reputation = reputation;


    }

    public UserTestDto() {
    }
}
