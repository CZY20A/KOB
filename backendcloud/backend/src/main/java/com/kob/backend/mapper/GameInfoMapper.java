package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.GameInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("GameMapper")
public interface GameInfoMapper extends BaseMapper<GameInfo> {
    @Select("select * from game_info limit ${((page - 1) * size).intValue()}, #{size}")
    public List<GameInfo> getGameInfoPage(@Param("page") int page, @Param("size") int size);
}
