package com.cy.usercenter.model.request;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author 86147
 * create  22/3/2023 下午12:13
 */
@Data
public class LoginBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String password;


}
