package com.kob.backend.service.impl.game;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.GameInfoMapper;
import com.kob.backend.pojo.GameInfo;
import com.kob.backend.service.game.GameInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameInfoServiceImpl implements GameInfoService {

    @Autowired
    GameInfoMapper gameInfoMapper;

    @Override
    public GameInfo getGameInfo(Integer id) {
        return gameInfoMapper.selectById(id);
    }

    @Override
    public List<GameInfo> getGameInfoPage(Integer page, Integer size) {
         return gameInfoMapper.getGameInfoPage(page, size);
    }

    @Override
    public List<GameInfo> getGameInfos() {
        return gameInfoMapper.selectList(new QueryWrapper<>());
    }
}
