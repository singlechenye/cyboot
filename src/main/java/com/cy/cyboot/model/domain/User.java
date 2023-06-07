package com.cy.cyboot.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private Integer userStatus;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private Integer userRole;

    public User() {
    }

    public User(Long id, String username, String password, String phone, String email, Integer userStatus, Date createTime, Date updateTime, Integer isDelete, Integer userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.userStatus = userStatus;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDelete = isDelete;
        this.userRole = userRole;
    }
}