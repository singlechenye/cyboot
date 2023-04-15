package com.cy.usercenter.model.request;

import lombok.Data;

/**
 * @author 86147
 * create  14/4/2023 下午12:27
 */
@Data
public class AdminBody {
    private static final long serialVersionUID = 1L;

    private String account;

    private String password;
}
