package com.tea.framework.exception;


import com.tea.framework.metadata.consts.ResultCode;
import com.tea.framework.metadata.model.Response;
import com.tea.framework.utils.ResponseBeanUtils;
import lombok.Data;

@Data
public class ServiceException extends RuntimeException {


    private String code;//system.error.search
    private String[] values;//自动设置占位符

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
        this.overrideException(null);
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.overrideException(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.overrideException(cause);
    }

    public ServiceException code(String code) {
        this.setCode(code);
        return this;
    }

    public ServiceException values(String... values) {
        this.setValues(values);
        return this;
    }

    private void overrideException(Throwable cause) {
        this.setCode(ResultCode.Default.ERROR_INNER);
        if (cause != null && isCausedBy(cause, ServiceException.class)) {
            //内部异常则覆盖
            ServiceException inner = ((ServiceException) cause);
            this.setCode(inner.getCode());
            this.setValues(inner.getValues());
        }
    }

    /**
     * 判断底层异常工具方法
     */
    public boolean isCausedBy(Throwable cause, Class<? extends Exception>... causeExceptionClasses) {
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    public Response toResponse() {
        return ResponseBeanUtils.build(this);
    }
}
