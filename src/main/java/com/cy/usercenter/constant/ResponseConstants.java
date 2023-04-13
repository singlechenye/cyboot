package com.cy.usercenter.constant;

public interface ResponseConstants {

    public static final int CODE_SUCCESS = 200000;
    public static final String MSG_SUCCESS = "成功";

    public static final int CODE_AUTH_ERROR = 400001;
    public static final String MSG_AUTH_ERROR = "管理员认证失败, 拒绝访问";

    public static final int CODE_BUSSINESS_ERROR = 400002;
    public static final String MSG_BUSSINESS_ERROR = "业务繁忙";

    public static final int CODE_SERVER_ERROR = 400003;
    public static final String MSG_SERVER_ERROR = "网络操作失败，请稍后重试";

    public static final int CODE_NOT_FOUND_ERROR = 400004;
    public static final String MSG_NOT_FOUND_ERROR = "服务器没有此接口";

    public static final int CODE_UNKNOWN_ERROR = 400005;
    public static final String MSG_UNKNOWN_ERROR = "未知错误";

    public static final int CODE_NOT_LOGIN_ERROR = 400006;
    public static final String MSG_NOT_LOGIN_ERROR = "请先登录";

    public static final int CODE_METHOD_ERROR = 400007;
    public static final String MSG_METHOD_ERROR = "请求方法错误";

    public static final int CODE_PARAMETER_ERROR = 400008;
    public static final String MSG_PARAMETER_ERROR = "缺少参数";

    public static final int CODE_SYSTEM_ERROR = 400010;
    public static final String MSG_SYSTEM_ERROR = "系统错误";

    public static final int CODE_EXIST_ERROR = 400011;
    public static final String MSG_EXIST_ERROR = "注册用户已存在";

    public static final int CODE_NOT_EXIST_ERROR = 400012;
    public static final String MSG_NOT_EXIST_ERROR = "登录用户不存在";


}
