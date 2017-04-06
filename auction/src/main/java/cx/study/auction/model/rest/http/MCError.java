package cx.study.auction.model.rest.http;

public enum MCError {

	//======================= 服务器错误 ↓ ==============================//
	SUCCESS(0, "成功"),
	WRONG_NAME_OR_PWD(-1, "账号或密码错误"),
	WRONG_JSON_TO_OBJECT(-2, "Json数据格式错误"),
	WRONG_TELEPHONE(-3, "手机号码不存在"),
	WRONG_CAPTCHA_CREATE(-4, "验证码生成错误"),
	WRONG_TELEPHONE_CAPTCHA(-5, "验证码验证失败"),
	WRONG_PASSWORD_RESET(-6, "密码重置是失败"),
	WRONG_USER_EXIST(-7, "用户已存在"),
	WRONG_LOGIN_CLOSE(-8, "账户不可用"),
	WRONG_LOGIN_OTHER(-9, "登录信息其他错误"),
	WRONG_APPLY_OPERATION(-10, "请求信息格式错误"),
	WRONG_PWD(-11, "密码错误"),
	WRONG_TIMEOUT(-12, "登录超时"),
	WRONG_UPLOAD(-13, "上传数据失败"),
	WRONG_CREATEFILE(-14, "创建文件失败"),
	WRONG_WRITEFILE(-15, "写文件失败"),
	WRONG_CLOUSE_OUTPUTSTREAM(-16, "关闭输出流失败"),
	WRONG_EMPTY_FIELDSTYLE(-17, "字段校验配置文件不存在"),
	WRONG_EMPTY_FIELDS(-18, "字段校验配置不存在"),
	WRONG_VERIFY_FAIL(-19, "字段校验失败"),
	WRONG_AVATAR_UPDATE_FAIL(-20, "更新头像失败"),
	WRONG_USER_EMPTY(-21, "无此用户"),
	FAILURE(-22, "操作失败"),
	RECORD_EXIST(-23, "该记录已存在"),
	RECORD_NOTEXIST(-24, "该记录不存在"),
	RECORD_NONEW(-25, "没有更新记录"),
	WRONG_PARAMETER(-26, "请求参数有误"),
	USER_NOTEXIST(-27, "该患者不存在"),
	RECORD_ISDELETE(-28, "该记录已删除"),
	//======================= 服务器错误 ↑ ==============================//


	//======================= 自定义错误 ↓ ==============================//
	ERROR_EMPTY(2, "数据为空"),
	ERROR_JSON(3, "数据格式错误"),
	ERROR_UNKNOW(4, "unknow error"),
	ERROR_NET_POOR(5, "您的网络不给力哦~"),
	ERROR_RESPONSE(6, "返回格式错误"),
	ERROR_HTTP(7, "请求服务器错误"),
	UNSUPPORTED_ENCODING(8, "不支持的HTTP编码格式"),
	PARSE_EXCEPTION(9, "HTTP返回参数错误"),
	IO_EXCEPTION(10, "IO 错误"),
	ERROR_TYPE(11, "数据缺少必须项"),
	ERROR_OPERATING(12, "数据正在操作"),
	ERROR_TIMEOUT(13, "链接服务器超时"),
	//======================= 自定义错误 ↑ ==============================//

	UNKONWN(Integer.MIN_VALUE, "未知错误");

	private int code;
	private String info;

	MCError(int code, String info) {
		this.code = code;
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

	public static MCError valueOf(int code) {
		for (MCError error : MCError.values()) {
			if (code == error.getCode()) {
				return error;
			}
		}
		return UNKONWN;
	}

}