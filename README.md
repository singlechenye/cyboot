# **cyboot**

## 项目介绍:

一个开箱即用的通用后端模板,封装好了spring security鉴权以及权限管理,利用jwt和redis的联动实现单点登录,以及knife4j的接口文档生成

## 使用指南:

### 关于权限管理

com/cy/cyboot/model/security/CustomUserDetails.java

在此java类中通过修改以下方法代码块即可自定义数据库字段与权限的关联

```java
public CustomUserDetails(User user) {
        this.user = user;
        Integer userRole = user.getUserRole();
        authorities =new ArrayList<>();
        if (userRole==0) authorities.add(new SimpleGrantedAuthority("normal"));
        else if (userRole==1) authorities.add(new SimpleGrantedAuthority("admin"));
    }
```

### 关于数据库配置

在application.yml中对redis以及数据库进行配置

### 关于接口编写

默认利用jwt配合spring security拦截除login以及register以外的所有接口不能在未登录情况下访问

接口编写时通过添加PreAuthorize注解即可实现权限认证以及拦截

```java
    @GetMapping("/info")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    public Response<User> Info( HttpServletRequest request){
        User user = userService.Info();
        return ResponseUtil.success(user);
    }
```

### 关于异常处理

com/cy/cyboot/constant/ResponseConstants.java

目前已经定义的响应枚举,可于此类自定义编写异常

```java
    SUCCESS(200000,"成功"),
    NOT_LOGIN_ERROR(400002,"用户未登录"),PARAMETER_ERROR(400003,"参数错误"),
    REGISTER_EXIST_ERROR(400004,"注册用户已存在"),LOGIN_NOT_EXIST_ERROR(400005,"登录用户不存在"),
    SYSTEM_ERROR(400006,"系统错误"),LOGIN_PASSWORD_WRONG_ERROR(400007,"登录密码错误"),
    AUTH_LACK_ERROR(400008,"权限不足"),AUTH_CHECK_ERROR(400008,"登录验证失败");

```

com/cy/cyboot/exception/CustomExceptionHandler.java

在此文件中可自定义更多的异常处理方法

```java
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AppException.class)
    public Response appExceptionHandler(AppException appException){
        appException.printStackTrace();
        int code = appException.getCode();
        String msg = appException.getMsg();
        return ResponseUtil.error(code,msg);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Response authExceptionHandler(Exception exception){
        exception.printStackTrace();
        int code = ResponseConstants.AUTH_CHECK_ERROR.getCode();
        String msg = ResponseConstants.AUTH_CHECK_ERROR.getMsg();
        return ResponseUtil.error(code,msg);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response accessExceptionHandler(Exception exception){
        exception.printStackTrace();
        int code = ResponseConstants.AUTH_LACK_ERROR.getCode();
        String msg = ResponseConstants.AUTH_LACK_ERROR.getMsg();
        return ResponseUtil.error(code,msg);
    }
}
```

