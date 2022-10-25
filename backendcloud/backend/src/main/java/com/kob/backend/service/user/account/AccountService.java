package com.kob.backend.service.user.account;

import com.kob.backend.pojo.RegisterUser;
import com.kob.backend.pojo.User;

import java.util.Map;

public interface AccountService {
    Map<String, String> info();

    Map<String, String> getToken(String username, String password);

    Map<String, String> register(RegisterUser user);
}
