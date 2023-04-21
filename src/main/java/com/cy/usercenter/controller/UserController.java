package com.cy.usercenter.controller;

import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.request.LoginBody;
import com.cy.usercenter.model.request.RegisterBody;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.util.ResponseUtil;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.util.ExceptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/register")
    public Response<Long> register(@RequestBody RegisterBody registerBody){
        if(Objects.isNull(registerBody)) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        String userAccount = registerBody.getUserAccount();
        String password = registerBody.getPassword();
        String checkPassword = registerBody.getCheckPassword();
        long register = userService.register(userAccount, password, checkPassword);
        return ResponseUtil.success(register);

    }

    /**
     * @return Response<User>
     */
    @RequestMapping("/login")
    public Response<Object> login(@RequestBody LoginBody loginBody){
        if (Objects.isNull(loginBody)) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        String userAccount = loginBody.getUserAccount();
        String password = loginBody.getPassword();
        String jwt = userService.login(userAccount, password);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",jwt);
        return ResponseUtil.success(tokenMap);
    }

    /**
     *
     * @param request
     * @return Response<Integer>
     */
    @RequestMapping("/logout")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    public void Logout( HttpServletRequest request){
        userService.Logout();
    }

    @PostMapping("/dump")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    public Response<Boolean> dump(){
        boolean b = userService.dump();
        return ResponseUtil.success(b);
    }
}
