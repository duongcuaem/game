package com.game.lyn.dto.responseDTO;



public class AuthDTO {

    private String status;
    private String title;
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthDTO(String status, String title, String token) {
        this.status = status;
        this.title = title;
        this.token = token;
    }
}