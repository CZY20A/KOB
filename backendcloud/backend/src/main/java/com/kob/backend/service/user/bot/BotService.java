package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;
import java.util.Map;

public interface BotService {
    Map<String, String> add(Bot bot);

    Map<String, String> remove(Integer id);

    Map<String, String> update(Bot bot);

    List<Bot> getList();
}
