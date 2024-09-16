package com.game.lyn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDTO {

    private String status;
    private String title;
    private String token;

    public ResponseDTO(String status, String title, String token) {
        this.status = status;
        this.title = title;
        this.token = token;
    }
}
