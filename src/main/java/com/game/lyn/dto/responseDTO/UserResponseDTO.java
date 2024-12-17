package com.game.lyn.dto.responseDTO;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private Set<String> roles; // Danh sách tên quyền
    private Integer banPass;
    private Boolean banLogin;
    private String lastDate;
    private String lastLogin;
    private Date regDate;
    private Boolean lock;
}
