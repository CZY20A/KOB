package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class BotinfoController {

    @RequestMapping("getbotinfo/")
    public Map<String, List<String>> getBotInfo(){
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new LinkedList<>();
        List<String> list1 = new LinkedList<>();
        list.add("sword");
        list.add("tiger");
        list.add("apple");
        list1.add("1500");
        list1.add("2100");
        map.put("bots", list);
        map.put("rating", list1);
        return map;
    }

}
