package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    private LocalDateTime dateRegister;
    private Long totalAnswers;
    private Long totalVotesOnAnswers;

    public UserDto(Long id, String email, String fullName, String linkImage, String city, Long reputation, LocalDateTime dateRegister) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.linkImage = linkImage;
        this.city = city;
        this.reputation = reputation;
        this.dateRegister = dateRegister;
    }

}