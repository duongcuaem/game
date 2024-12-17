package com.game.lyn.dto.requestDTO;

import java.util.Date;
import lombok.*;

@Data // Lombok tự tạo getter, setter, toString, equals, hashCode
public class UserRequestDTO {
    private String username;
    private String password;
    private Integer banPass;
    private Boolean banLogin;
    private String token;
    private String lastDate;
    private String lastLogin;
    private Date regDate;
    private Integer fail;
    private Boolean lock;
}