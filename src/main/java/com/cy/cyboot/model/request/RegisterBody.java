package com.cy.cyboot.model.request;

import lombok.Data;

@Data
public class RegisterBody {
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String checkPassword;
}
