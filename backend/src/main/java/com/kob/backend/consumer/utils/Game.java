package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    private Integer rows;
    private Integer cols;
    private Integer inner_walls_count;
    private boolean[][] g;
    private static int[] dirx = {-1, 0, 1, 0};
    private static int[] diry = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new boolean[rows][cols];
    }



    public boolean[][] getG(){
        return g;
    }


    public boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;
        for(int i = 0; i < 4; ++i){
            int x = sx + dirx[i], y = sy + diry[i];
            if(x < 0 || x >= rows || y < 0 || y >= cols) continue;
            if(g[x][y] == false && check_connectivity(x, y, tx, ty) == true) {
                g[sx][sy] = false;
                return true;
            }
        }
        g[sx][sy] = false;
        return false;
    }


    private boolean draw() { //画地图

        for(int i = 0; i < rows; ++i)
            for(int j = 0; j < cols; ++j)
                g[i][j] = false;

        for(int i = 0; i < cols; ++i) g[0][i] = g[rows - 1][i] = true;
        for(int i = 0; i < rows; ++i) g[i][0] = g[i][cols - 1] = true;

        Random random = new Random();
        for(int i = 0; i < inner_walls_count / 2; ++i) {
            for(int j = 0; j < 1000; ++j){
                int r = random.nextInt(rows), c = random.nextInt(cols);
                if(r == 1 && c == cols - 2 || r == rows - 2 && c == 1 ) continue;
                if(g[r][c] == true || g[rows - 1 - r][cols - 1- c]) continue;
                g[r][c] = g[rows - 1 - r][cols - 1 - c] = true;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        //暴力出奇迹，直到两只蛇的起点都能连通为止
        for(int i = 0; i < 1000; ++i)
            if(draw() == true)
                break;
    }
}
