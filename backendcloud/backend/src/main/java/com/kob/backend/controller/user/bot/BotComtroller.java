package com.kob.backend.controller.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.service.impl.user.bot.BotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/bot/")
public class BotComtroller {
    @Autowired
    BotServiceImpl botService;

    @PostMapping("/add/")
    public Map<String, String> add(Bot bot){
        return botService.add(bot);
    }

    @PostMapping("/remove/")
    public Map<String, String> remove(Integer id){
        return botService.remove(id);
    }

    @PostMapping("/update/")
    public Map<String, String> update(Bot bot){
        return botService.update(bot);
    }

    @GetMapping("/getlist/")
    public List<Bot> getList() {
        return botService.getList();
    }

    @GetMapping("/getlistByGameId")
    public List<Bot> getListByGameId(Integer gameId) {
        return botService.getListByGameId(gameId);
    }
}
