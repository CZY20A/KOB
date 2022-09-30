import { AcGameObject } from '../scripts/AcGameObject'
import { Wall } from './Wall';

//13x13地图
export class GameMap extends AcGameObject{
    constructor(ctx, parent){ //画布 画布的父元素(即包住画图的标签,用于动态修改画布长宽)
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //一个单位的长度(可能因为浏览器的大小而改变)

        this.rows = 13;
        this.cols = 13;

        this.walls = [];
        this.innert_walls_count = 20; //障碍物的个数
    }

    //Flod_fill算法判断是否连通(简单算法题)
    check_connectivity(g, sx, sy, tx, ty){
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true; //防止来回走
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1]; //上右下左
        for(let i = 0; i < 4; ++i){
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;
        }
        return false;
    }

    create_walls() {
        const g = []; //二维数组,该位置是否有墙
        for(let r = 0; r < this.rows; ++r){
            g[r] = [];
            for(let c = 0; c < this.cols; ++c){
                g[r][c] = false;
            }
        }

        //给四周加上障碍物
        for(let r = 0; r < this.rows; ++r) { 
            g[r][0] = g[r][this.cols-1] = true;
        }
        for(let c = 0; c < this.cols; ++c) { 
            g[0][c] = g[this.rows - 1][c] = true;
        }

        //创建随机障碍物
        for(let i = 0; i < this.innert_walls_count / 2; ++i){
            for(let j = 0; j < 1000; ++j){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[c][r] ) continue;
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols-2) continue; //避免覆盖到蛇的位置
                g[r][c] = g[c][r] = true;
                break;
            }
        }

        const copy_g = JSON.parse(JSON.stringify(g)); //通过转换成string再转换为json达到copy对象的效果
        if(!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false; //两条蛇不连通不要生成

        for(let r = 0; r < this.rows; ++r){
            for(let c = 0; c < this.cols; ++c){
                if(g[r][c]){
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
    }

    //暴力出奇迹直到两条蛇连通为止!
    start(){
        for(let i = 0; i < 1000; ++i)
            if(this.create_walls())
                break;
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows)); //整数不会有浮点数的间隔
        this.ctx.canvas.width = this.cols * this.L;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update(){
        this.update_size();
        this.render()
    }

    render(){
        const color_even = '#AAD751', color_odd = '#A2D149'; //偶数格和奇数格的颜色
        for(let r = 0; r < this.rows; ++r)
            for(let c = 0; c < this.cols; ++c){
                if((r + c) % 2 === 0){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L); //canvas坐标系横着是x 竖着是y
            }
    }
}