package com.tea.api.user.service;

import com.tea.api.user.model.User;
import com.tea.framework.core.service.IService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "用户-接口", description = "系统-用户接口")
@RequestMapping("/api/user")
public interface IUserService  extends IService<User> {

    @ApiOperation("级联-获取用户")
    @ResponseBody
    @RequestMapping(path = "/get/cascade", method = RequestMethod.POST)
    User getCascade(User model);
}
