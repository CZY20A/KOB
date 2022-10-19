import { AcGameObject } from '@/assets/scripts/AcGameObject';
import { Cell } from './Cell';

//蛇的移动方式:蛇头到目的地，尾部(若是蛇会变成的回合则不动，否则删除即可)
export class Snake extends AcGameObject{
    constructor(info, gamemap){
        super();

        this.id = info.id;
        this.color =info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; //存放蛇的身体，cells[0]存放蛇头
        this.next_cell = null; //下一步的目标位置

        this.speed = 5; //蛇每秒走5个格子
        this.direction = -1; //-1表示没有指令, 0、1、2、3表示上右下左
        this.status = "idle"; //idle表示静止, move表示正在移动, die表示死亡

        //四个方向上右下左
        this.dr = [-1, 0, 1, 0]; //行的偏移量
        this.dc = [0, 1, 0, -1]; //列的偏移量

        this.step = 0; //当前的回合数
        this.eps = 1e-2; //误差(小数二分常用)

        this.eye_direction = 0;
        if(this.id === 1) this.eye_direction = 2;  //左下角的蛇初始朝上，右上角的蛇朝下

        //蛇眼睛的偏移量(上右下左)
        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [
            [-1, -1],
            [1, -1],
            [1, 1],
            [-1, 1],
        ];
    }

    start(){

    }

    set_direction(d){
        this.direction = d;
    }

    //检测当前回合蛇的长度是否增加:前十回合每一回合蛇都变长, 后十回合每三回合变长一次
    check_tail_increasing(){
        if(this.step <= 10) return true;
        if(this.step % 3 === 1) return true;
        return false;
    }

    //将蛇的状态变为走下一步
    next_step(){
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1;
        this.status = 'move';
        this.step++;

        const k = this.cells.length;
        for(let i = k; i > 0; --i) this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1])); //深层拷贝，否则赋值的是引用  

    }

    update_move(){
        const move_distance = this.speed  * this.timedelta / 1000; //每两帧之间走过的距离
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if(distance < this.eps){
            this.cells[0] = this.next_cell; //cells[0]的r和c没变所以要重新赋值头部
            this.next_cell = null;
            this.status = 'idle';
            this.direction = -1;

            if(!this.check_tail_increasing()){
                this.cells.pop();
            }
        }else{
            this.cells[0].x += move_distance * (dx / distance); // 相当于斜边乘cos
            this.cells[0].y += move_distance * (dy / distance); //相当于斜边乘sin

            if(!this.check_tail_increasing()){
                const k = this.cells.length;
                const tail = this.cells[k-1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                const tail_distance = Math.sqrt(tail_dx * tail_dx + tail_dy * tail_dy);
                tail.x += move_distance * tail_dx / tail_distance;
                tail.y += move_distance * tail_dy / tail_distance;
            }
        }
    }

    update(){
        if(this.status === 'move'){
            this.update_move();
        }
        this.render();
    }

    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if(this.status === 'die'){
            ctx.fillStyle = 'white';
        }
        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2 , true);
            ctx.fill();
        }

        //美化蛇的各节点之间的连接:在半圆中间通过矩形将空白部分连接起来
        for(let i = 1; i < this.cells.length; ++i){
            const a = this.cells[i-1], b = this.cells[i];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
            if(a.x === b.x){
                ctx.fillRect( (a.c+0.1) * L , Math.min(a.y, b.y) * L, L * 0.8 , Math.abs(a.y - b.y) * L);
            }else if(a.y === b.y){
                ctx.fillRect(Math.min(a.x, b.x) * L , (a.r+0.1) * L,Math.abs(a.x - b.x) * L ,  L * 0.8);
            }
        }

        ctx.fillStyle ='black';
        for(let i = 0; i < 2; ++i){
            const eye_x = this.eye_dx[this.eye_direction][i] * 0.15 + this.cells[0].x;
            const eye_y = this.eye_dy[this.eye_direction][i] * 0.15 + this.cells[0].y;
            ctx.beginPath();
            ctx.arc(eye_x * L, eye_y * L, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}