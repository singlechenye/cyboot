package com.cy.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 86147
 * create  14/4/2023 下午12:17
 */
@Data
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String account;

    private String password;

}
