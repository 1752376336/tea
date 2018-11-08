package com.tea.framework.utils;

import com.tea.framework.exception.ServiceException;
import com.tea.framework.metadata.consts.ResultCode;
import com.tea.framework.metadata.model.Response;

public class ResponseBeanUtils {

    public static Response ok() {
        return ok(null);
    }

    public static Response ok(Object body) {
        Response response = build(ResultCode.Default.SUCCESS);
        response.setBody(body);
        return response;
    }

    public static Response build(ServiceException e) {
        return build(e.getCode(), e.getValues());
    }


    public static Response build(String buildCode) {
        return build(buildCode, null, null);
    }

    public static Response build(String buildCode, Object body, String... values) {
        String tip = ContextUtils.getMessage(buildCode, values);
        String[] tips = tip.split("\\|");
        String code = tips[1];
        String message = tips[0];
        if (code.equalsIgnoreCase("010101")) {
            message += " - " + buildCode;
        }
        Response res = new Response();
        res.setCode(code);
        res.setMessage(message);
        res.setBody(body);
        return res;
    }


}
