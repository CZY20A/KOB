import { AcGameObject } from '@/assets/scripts/AcGameObject';

export class Bullet extends AcGameObject {
    constructor(info, ctx, gamemap){
        super();
        this.L = info.L;
        this.ctx = ctx;
        this.x = 0;
        this.y = 0;
        this.vx = 0;
        this.vy = 0;
        this.direction = 0; //八个方向: 0123上右下左, 4567-右上 右下 左下 左上
        this.speed = 5; //一帧n格
        this.is_used = false;
        this.use_time = 0;
        this.tank = info.tank;

        this.gamemap = gamemap;

        this.dir_x = [-1, 0, 1, 0, -1, 1, 1, -1];
        this.dir_y = [0, 1, 0, -1, 1, 1, -1, -1];
    }
    
    update_controller() {
        if(this.is_used) {
            this.vx = this.speed * this.dir_x[this.direction];
            this.vy = this.speed * this.dir_y[this.direction];
        }
        else {
            this.vx = 0;
            this.vy = 0;
            this.x = this.y = 0;
            this.use_time = 0;
        }
    }

    launch(x, y, direction) {
        this.x = x + this.L/2;
        this.y = y + this.L/2;
        this.is_used = true;
        this.direction = direction;
    }
    
    is_attack(){
        for(const tank of this.gamemap.tanks) {
            let x1 = tank.x, y1 = tank.y, x2 = tank.x + this.L, y2 = tank.y + this.L;
            if(Math.max(x1, this.x) > Math.min(x2, this.x)) continue;
            if(Math.max(y1, this.y) > Math.min(y2, this.y)) continue;
            tank.status = 0;
            tank.life--;
            for(const t of this.gamemap.tanks) {
                for(const b of t.bullet) {
                    b.is_used = false;
                }
            }
            if(tank.life != 0) {
                setTimeout(() => {
                    tank.status = 1;
                    for(const t of this.gamemap.tanks) {
                        if(t.id === 1) {
                            t.row = (this.gamemap.rows-2);
                            t.col = 1;
                        } else {
                            t.row = 1;
                            t.col = (this.gamemap.cols - 2);
                        }
                    }
                }, 1000);
            }
            return true;
        }
        return false;
    }


    is_collision(rect) {
        for(let wall of this.gamemap.walls) {
            let x1 = wall.r * this.L, y1 = wall.c * this.L, x2 = x1 + this.L, y2 = y1 +this.L;
            if(Math.max(x1, rect.x1) > Math.min(x2, rect.x2)) continue;
            if(Math.max(y1, rect.y1) > Math.min(y2, rect.y2)) continue;
            if(this.direction < 4) {
                if(this.direction === 0) {
                    this.x = x2;
                } else if(this.direction === 1) {
                    this.y = y1;
                } else if(this.direction === 2) {
                    this.x = x1;
                } else {
                    this.y = y2;
                }
                 this.direction = ( this.direction + 2 ) % 4;
            } else if(this.direction >= 4) {
                if(this.direction === 4) {
                    
                    //弹右边的墙和上边的墙是不一样的
                    if(rect.last_y < y1) {
                        this.direction = 7; 
                        this.y = y1;
                    }
                    else{
                        this.direction = 5;
                        this.x = x2;
                    }
                } else if(this.direction === 5) {
                    //弹右边的墙和下边的墙是不一样的
                    if(rect.last_y < y1){
                         this.direction = 6;
                         this.y = y1;
                    }
                    else{
                         this.direction = 4;
                         this.x = x1;
                    }
                } else if(this.direction === 6) {
                    //弹左边的墙和下边的墙是不一样的
                    if(rect.last_y > y2) {
                        this.direction = 5;
                        this.y = y2;
                    }
                    else{
                        this.direction = 7; 
                        this.x =x1;
                    }
                } else {
                    //弹左边的墙和上边的墙是不一样的
                    if(rect.last_y > y2) {
                        this.direction = 4;
                        this.y = y2;
                    }
                    else{
                        this.direction = 6;
                        this.x = x2;
                    }
                }  
            }
            return true;
            
        }
        return false;
    }


    update_move() {
        if(this.is_used) {
           
            let last_x = this.x, last_y = this.y;
            this.x += this.vx * this.L * this.timedelta / 1000;
            this.y += this.vy * this.L * this.timedelta / 1000;
            this.use_time += this.timedelta;
            
            this.ctx.beginPath();
            this.ctx.strokeStyle = "black";
            this.ctx.arc(this.y, this.x, this.L / 10, 0, Math.PI * 2, true);
            this.ctx.fill();
            this.ctx.stroke();

            if(this.use_time >= 200)
                  this.is_attack();
            this.is_collision({x1:this.x, y1:this.y, x2:this.x, y2:this.y, last_x, last_y});

            if(this.use_time >= 8000.0) {
                this.is_used = false;
                this.use_time = 0;
            }
        }

    }

    update() {
        this.update_controller();
        this.update_move();
    }
}