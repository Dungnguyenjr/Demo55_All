package com.tn.dto;

import com.tn.entity.AccountRole;
import lombok.Data;

@Data
public class AccountDTO {

    private Integer id;

    private String username;

    private String fullName;

    private AccountRole role;

    private boolean isActive;

    private String departmentName;
}
