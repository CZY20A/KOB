package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    private boolean checkTailIncreasing(int steps) { //检验当前回合蛇是否会变长
        if(steps <= 10)
            return true;
        else if(steps % 3 == 1)
            return true;
        return false;
    }

    public List<Cell> getCells() { //蛇的身体有哪些
        List<Cell> cells = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;

        cells.add(new Cell(x, y));
        int step = 0;
        for(int d : steps) {
            x += dx[d];
            y += dy[d];
            step++;
            cells.add(new Cell(x, y));
            if(!checkTailIncreasing(step)){
                cells.remove(0);
            }
        }

        return cells;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for(int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}
