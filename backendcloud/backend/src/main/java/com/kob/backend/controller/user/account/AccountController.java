package com.kob.backend.controller.user.account;

import com.kob.backend.pojo.RegisterUser;
import com.kob.backend.service.user.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/account/")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map){
        String username = map.get("username");
        String password = map.get("password");
        return accountService.getToken(username, password);
    }

    @GetMapping("/info/")
    public Map<String, String> getInfo(){
        return accountService.info();
    }

    @PostMapping("/register/")
    public Map<String, String> register(RegisterUser registerUser){
        return accountService.register(registerUser);
    }

    @PostMapping ("/changeUsername/")
    public Map<String, String>  changeUsername(String username, Integer id) {
        return accountService.changeUsername(username, id);
    }

    @PostMapping("/changeImage/")
    public Map<String, String> changeImage(String url, Integer id) {
        return accountService.changeImage(url, id);
    }
}
