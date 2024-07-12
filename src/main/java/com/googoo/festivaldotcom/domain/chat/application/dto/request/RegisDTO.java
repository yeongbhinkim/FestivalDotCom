package com.googoo.festivaldotcom.domain.chat.application.dto.request;

import lombok.Getter;


@Getter
public class RegisDTO {
    private Long registration_id;
    private Long festival_id;
    private Long id;

    public void setRegistration_id(Long registration_id) {
        this.registration_id = registration_id;
    }

    public void setFestival_id(Long festival_id) {
        this.festival_id = festival_id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}