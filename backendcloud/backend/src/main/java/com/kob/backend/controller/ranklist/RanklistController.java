package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.ranklist.RanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ranklist/")
public class RanklistController {

    @Autowired
    private RanklistService ranklistService;

    @GetMapping("/getlist/")
    public JSONObject getlist(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return ranklistService.getlist(page);
    }
}
