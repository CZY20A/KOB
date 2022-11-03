package com.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * 当前支持java语言
 * input参数是本局对战的所有信息该参数由系统提供,格式为：
 * 地图信息#我的x坐标#我的y坐标#(我的操作)#对手的x坐标#对手的y坐标#(对手的操作)
 * 地图共13行14列，地图信息为一个13*14的一维字符串，若该处为墙则为1，否则为0
 */
public class Bot implements Supplier<Integer> {

    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean checkTailIncreasing(int steps) { //检验当前回合蛇是否会变长
        if(steps <= 10)
            return true;
        else if(steps % 3 == 1)
            return true;
        return false;
    }

    public List<Cell> getCells(int sx, int sy, String steps) { //蛇的身体有哪些
        List<Cell> cells = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;

        cells.add(new Cell(x, y));
        int step = 0;
        for(int i = 1 ; i < steps.length() - 1; ++i) {
            int d = steps.charAt(i) - '0';
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

    public Integer nextMove(String input) { //返回值:0123分别表示上右下左
        String[] strs = input.split("#");
        int[][] g = new int[13][14]; // 墙的信息
        for(int i = 0, k = 0; i < 13; ++i){
            for(int j = 0; j < 14; ++j, ++k) {
                if(strs[0].charAt(k) == '1'){
                    g[i][j] = 1;
                }
            }
        }

        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);

        for(Cell c : aCells) g[c.x][c.y] = 1;
        for(Cell c : bCells) g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        for(int i = 0; i < 4; ++i) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if(x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
