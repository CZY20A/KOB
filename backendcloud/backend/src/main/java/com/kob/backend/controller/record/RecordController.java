package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/record/")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @GetMapping("/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return recordService.getList(page);
    }

    @GetMapping("/getlistByUsername/")
    public JSONObject getListByUsername(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        String username = data.get("username");
        return recordService.getListByUsername(page, username);
    }
}
