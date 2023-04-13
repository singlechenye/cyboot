package com.cy.usercenter.model.request;

import lombok.Data;

/**
 * @author 86147
 * create  22/3/2023 下午12:13
 */
@Data
public class RegisterBody {
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String password;

    private String checkPassword;
}
