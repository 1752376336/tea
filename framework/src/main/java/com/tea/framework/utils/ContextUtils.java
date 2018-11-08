package com.tea.framework.utils;

import com.tea.framework.exception.ServiceException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * 配置文件初始化存取bean
 */
public class ContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    public static MessageSource getMessageSource() {
        return ContextUtils.getBean(MessageSource.class);
    }

    public static String getMessage(String code, String[] values) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(getMessageSource());
        String msg = null;
        String defaultMsg = "消息未定义|010101";
        if (values != null) {
            msg = accessor.getMessage(code, values, defaultMsg, LocaleContextHolder.getLocale());
        }
        if (values == null) {
            msg = accessor.getMessage(code, defaultMsg, LocaleContextHolder.getLocale());
        }
        return msg;
    }

    public static String getMessage(ServiceException e) {
        return getMessage(e.getCode(), e.getValues());
    }

}
