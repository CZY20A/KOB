package com.kob.backend.service.game;

import com.kob.backend.pojo.GameInfo;

import java.util.List;

public interface GameInfoService {
    public GameInfo getGameInfo(Integer id);

    public List<GameInfo> getGameInfoPage(Integer page, Integer size);

    public List<GameInfo> getGameInfos();
}
