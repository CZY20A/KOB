import { AcGameObject } from '../scripts/AcGameObject'
import { Wall } from './Wall';
import { Snake } from './Snake';

//13x13地图
export class GameMap extends AcGameObject{
    constructor(ctx, parent){ //画布 画布的父元素(即包住画图的标签,用于动态修改画布长宽)
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //一个单位的长度(可能因为浏览器的大小而改变)

        this.rows = 13;
        this.cols = 14;  //行列奇偶不同才能保证决策的公平(两条蛇不会走到一个格子，否则劣势方可以鱼死网破)

        this.walls = [];
        this.innert_walls_count = 20; //障碍物的个数

        this.snakes = [
            new Snake({id:0, color: '#4876EC', r: this.rows-2, c:1}, this),
            new Snake({id:1, color: '#F94848', r: 1, c: this.cols - 2}, this),
        ]
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
                g[r][c] = g[this.rows - r - 1][this.cols - c - 1] = true;
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

    add_listening_events(){
        this.ctx.canvas.focus();
        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener('keydown', e => {
            if(e.key === 'w') snake0.set_direction(0);
            else if(e.key === 'd') snake0.set_direction(1);
            else if(e.key === 's') snake0.set_direction(2);
            else if(e.key === 'a') snake0.set_direction(3);
            else if(e.key === 'ArrowUp') snake1.set_direction(0);
            else if(e.key === 'ArrowRight') snake1.set_direction(1);
            else if(e.key === 'ArrowDown') snake1.set_direction(2);
            else if(e.key === 'ArrowLeft') snake1.set_direction(3);
        })
    }

    //暴力出奇迹直到两条蛇连通为止!
    start(){
        for(let i = 0; i < 1000; ++i)
            if(this.create_walls())
                break;
        this.add_listening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows)); //整数不会有浮点数的间隔
        this.ctx.canvas.width = this.cols * this.L;
        this.ctx.canvas.height = this.L * this.rows;
    }

    //两条蛇是否准备完毕进入下一个回合
    check_ready(){
        for(const snake of this.snakes){
            if(snake.status !== "idle") return false;
            if(snake.direction === -1) return false;
        }
        return true;
    }

    //让两条蛇进入下一回合
    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    //检测目标位置是否合法:没有撞到两条蛇的身体和障碍物
    check_valid(cell){
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c)
                return false;
        }
        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){ //当蛇尾不增加时，蛇尾会随着前进而改变位置故蛇尾不用判断
                k--;
            }
            for(let i = 0; i < k; ++i){
                if(cell.r === snake.cells[i].r && cell.c === snake.cells[i].c) 
                    return false;
            }
        }
        return true;
    }


    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
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