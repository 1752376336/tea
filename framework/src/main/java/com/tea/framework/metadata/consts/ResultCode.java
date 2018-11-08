package com.tea.framework.metadata.consts;


public interface ResultCode {

    interface Secure {
        String ERROR = "secure.authorize.error";
        String ERROR_DESCRIPTION = "secure.authorize.error.description";
        String ERROR_URI = "secure.authorize.error.uri";
        String INVALID_REQUEST = "secure.authorize.invalid.request";
        String INVALID_CLIENT = "secure.authorize.invalid.client";
        String INVALID_GRANT = "secure.authorize.invalid.grant";
        String UNAUTHORIZED_CLIENT = "secure.authorize.unauthorized.client";
        String UNSUPPORTED_GRANT_TYPE = "secure.authorize.unsupported.grant_type";
        String INVALID_SCOPE = "secure.authorize.invalid.scope";
        String INSUFFICIENT_SCOPE = "secure.authorize.insufficient.scope";
        String INVALID_REFRESH_TOKEN = "secure.authorize.invalid.refresh.token";
        String INVALID_TOKEN = "secure.authorize.invalid.token";
        String REDIRECT_URI_MISMATCH = "secure.authorize.redirect_uri.mismatch";
        String UNSUPPORTED_RESPONSE_TYPE = "secure.authorize.unsupported.response_type";
        String ACCESS_DENIED = "secure.authorize.access_denied";
        String CAPTCHA_VALID_FAIL = "secure.authorize.captcha.valid.fail";//验证码错误
        String FREQUENT_SMS_REQUESTS = "secure.authorize.frequent.sms.requests";//短信请求频繁
        String MAIL_VALID_FAIL = "secure.authorize.mail.valid.fail";//邮箱校验失败
        String MAIL_ALREADY_EXISTS = "secure.authorize.mail.already.exists";//邮箱已存在
        String MOBILE_ALREADY_EXISTS = "secure.authorize.mobile.already.exists";//手机号已存在
        String CAPTCHA_VALID_OVERDUE = "secure.authorize.captcha.valid.overdue";//验证码过期
        String TEMPLATE_NOT_FOUND = "secure.authorize.template.not.found";//模板未找到
        String MAIL_NOT_FOUND = "secure.authorize.mail.not.found";//邮箱未找到
        String CHANNEL_NOT_FOUND = "secure.authorize.channel.not.found";//渠道未找到
        String FREQUENT_MAIL_REQUESTS = "secure.authorize.frequent.mail.requests";//邮件请求频繁
        String ROLE_NOT_FOUND = "secure.authorize.role.not.found";//获取默认角色失败
        String PARAMETER_NOT_COMPLETE = "secure.authorize.parameter.not.complete";//参数不全
        String MOBILE_NOT_FOUND = "secure.authorize.mobile.not.found";//手机号未找到
    }

    interface Default {
        String SUCCESS = "system.operation.success";
        String SUCCESS_ASYNC = "system.operation.success.async";
        String OPERATE_TIMEOUT = "system.operation.timeout";
        String ERROR_INNER = "system.error.inner";
        String ERROR_DETAIL = "system.error.detail";
        String ERROR_PARAMETER_REQUIRED = "system.error.required.parameter";
        String ERROR_SEARCH_CONDITION = "system.error.search.condition";
        String ERROR_PARAMETER = "system.error.parameter";
        String ERROR_SAVE = "system.error.save";
        String ERROR_UPDATE = "system.error.update";
        String ERROR_DELETE = "system.error.delete";
        String ERROR_SEARCH = "system.error.search";
        String REMOTE_INVOKE_HOST_ERROR = "system.remote.invoke.host.error";
        String REMOTE_INVOKE_UNKNOWN_ERROR = "system.remote.invoke.unknown.error";
        String REMOTE_INVOKE_CONNECT_ERROR = "system.remote.invoke.connect.error";
        String ERROR_HTTP_STATUS = "system.error.http.status";
        String ERROR_AUTHORITY_VERIFY_TIMEOUT = "system.error.authority.verify.timeout";
        String ERROR_AUTHORITY_VERIFY_INVALID = "system.error.authority.verify.fail";
        String ERROR_AUTHORITY_PARAMETER_INVALID = "system.error.authority.parameter.fail";

        String ERROR_INDEX_MISSING = "system.error.index.missing";

        String ERROR_IO = "system.error.io";
        String ERROR_OPENXML4J = "system.error.openxml4j";
        String ERROR_SAX = "system.error.sax";

        String ERROR_FILE = "system.error.file";
        String ERROR_FILE_SIZE = "system.error.file.size";
        String ERROR_FILE_SUFFIX = "system.error.file.suffix";


        String ERROR_ACCESS_LIMIT = "system.error.access.limit";

        String ERROR_CSRF_TOKEN = "system.error.csrf.token";

        String ERROR_MEDIA_NOT_ACCEPT = "system.error.request.media.not_accept";
        String ERROR_METHOD_NOT_SUPPORT = "system.error.request.method.not_support";
        String ERROR_MEDIA_NOT_SUPPORT = "system.error.request.media.not_support";
        String ERROR_REQUEST_BODY_NOT_READABLE = "system.error.request.body.not_readable";
        String ERROR_REQUEST_PARAM_BINDING = "system.error.request.param.bind";

        String ERROR_DFS_CREATE = "system.error.dfs.operation.create";
        String ERROR_DFS_READ = "system.error.dfs.operation.read";
        String ERROR_DFS_DELETE = "system.error.dfs.operation.delete";

        String ERROR_SSH_CONNECT = "system.error.ssh.connect";
        String ERROR_SSH_DISCONNECT = "system.error.ssh.disconnect";
        String ERROR_SSH_LOGIN = "system.error.ssh.login";
        String ERROR_SSH_COMMAND = "system.error.ssh.command";

        String ERROR_TELNET_CONNECT = "system.error.telnet.connect";
        String ERROR_TELNET_DISCONNECT = "system.error.telnet.disconnect";
        String ERROR_TELNET_READER = "system.error.telnet.reader";
        String ERROR_TELNET_WRITER = "system.error.telnet.writer";
        String ERROR_TELNET_LOGIN = "system.error.telnet.login";
        String ERROR_TELNET_COMMAND = "system.error.telnet.command";

        String ERROR_JSON = "system.error.json";
    }

    interface Validator {
        String ERROR_FIELD_REQUIRED = "system.validator.field.required";
        String ERROR_MOBILE_INVALID = "system.validator.mobile.invalid";
        String ERROR_EMAIL_INVALID = "system.validator.email.invalid";
        String ERROR_INTEGER_INVALID = "system.validator.integer.invalid";
        String ERROR_FLOAT_INVALID = "system.validator.float.invalid";
        String ERROR_LENGTH_INVALID = "system.validator.length.invalid";
        String ERROR_IPV4_INVALID = "system.validator.ipv4.invalid";
        String ERROR_URL_INVALID = "system.validator.url.invalid";
        String ERROR_DATE_INVALID = "system.validator.date.invalid";
    }
}
