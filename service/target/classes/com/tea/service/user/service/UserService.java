package com.tea.service.user.service;

import com.tea.api.user.model.User;
import com.tea.api.user.service.IUserService;
import com.tea.framework.core.service.impl.GenericService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class UserService extends GenericService<User> implements IUserService {
    @Override
    public User getCascade(@RequestBody User model) {
        model = this.get(model);
        return model;
    }
}
