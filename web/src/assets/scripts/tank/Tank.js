import { AcGameObject } from '@/assets/scripts/AcGameObject';


export class Tank extends AcGameObject {
    constructor(info, gamemap) {
        super();
        this.id = info.id;
        this.row = info.row;
        this.col = info.col;
        this.color = info.color;
        this.next_row = 0;
        this.next_col = 0;

        this.ctx = info.ctx;
        this.gamemap = gamemap;
        this.speed = 10; //一秒走n格

        this.operate = [];

        this.direction = this.id === 1 ? 0 : 2;
        this.x = 0;
        this.y = 0;
        this.vx = 0;
        this.vy = 0;
        this.eps = 0.5;

        this.status = 1; // 0代表死亡, 1代表静止，2代表移动
        this.die_img = new Image();
        this.die_img.src = 'https://s1.ax1x.com/2022/11/09/xzrnb9.png'
        this.L = 0;
        this.controller = "";

        this.bullet_cnt = 30;
        this.bullet_has = 30;
        this.bullet = [];
        this.is_launched = false;
        this.is_continue_move = false;

        this.dir_x = [-1, 0,1,0];
        this.dir_y = [0, 1, 0, -1];


        this.life = 1;
    }

    /*
    add_keydown_listener() {
        this.ctx.canvas.addEventListener("keydown", (e) => {
            for(const tank of this.gamemap.tanks) {
                if(tank.status === 0)
                    return;
            }
            // if(this.controller != "" && e.key !== 'j' && e.key !== 'k' && e.key !== 'l' && e.key !== '1' && e.key !== '2' && e.key !== '3'){
            //     this.vx = 0;
            //     this.vy = 0;
            // }
           
            if(this.id === 1 && "ljk".indexOf(e.key) !== -1) {
                this.controller = "";
                this.vx = 0;
                this.vy = 0;
            }
            else if(this.id === 2 && "123".indexOf(e.key) !==-1) {
                this.controller = "";
                this.vx = 0;
                this.vy = 0;                
            }
                if(this.id === 1 && e.key.indexOf("Arrow") === -1 && "wasd".indexOf(e.key) !== -1) {
                    this.vx = 0;
                    this.vy = 0;
                    this.controller = e.key;
                } else if(this.id === 2 && e.key.indexOf("Arrow") !== -1) {
                    this.vx = 0;
                    this.vy = 0;
                    this.controller = e.key;
                }

            if(this.id === 1 && e.key === "j"){
                if(this.is_launched === true) return;
                this.is_launched = true;
                let d = this.direction + 3;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        if(d === 3) d = 7;
                        this.bullet[i].launch(this.x, this.y, d);
                        break;
                    }
            }
            else if(this.id === 1 && e.key === 'k') {
                if(this.is_launched === true) return;
                this.is_launched = true;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        this.bullet[i].launch(this.x, this.y, this.direction);
                        break;
                    }
            }
            else if(this.id === 1 && e.key === 'l') {
                if(this.is_launched === true) return;
                this.is_launched = true;
                let d = this.direction + 4;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        this.bullet[i].launch(this.x, this.y, d);
                        break;
                    }
            }
            if(this.id === 2 && e.key === "1"){
                if(this.is_launched === true) return;
                this.is_launched = true;
                let d = this.direction + 3;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        if(d === 3) d = 7;
                        this.bullet[i].launch(this.x, this.y, d);
                        break;
                    }
            }
            else if(this.id === 2 && e.key === '2') {
                
                if(this.is_launched === true) return;
                this.is_launched = true;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        this.bullet[i].launch(this.x, this.y, this.direction);
                        break;
                    }
            }
            else if(this.id === 2 && e.key === '3') {
                if(this.is_launched === true) return;
                this.is_launched = true;
                let d = this.direction + 4;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        this.bullet[i].launch(this.x, this.y, d);
                        break;
                    }
            }
        })
        this.ctx.canvas.addEventListener("keyup", (e) => {
            if(e.key === this.controller) {
                this.vx = 0;
                this.vy = 0;
                this.controller = "";
            }
            if(this.id === 1 && (e.key === "j" || e.key === 'k' || e.key === 'l')){
                this.is_launched = false;
                
            }
            else if(this.id === 2 && (e.key === "1" || e.key === '2' || e.key === '3')){
                this.is_launched = false;
            }
        })
    }
    */

    init_tank() {
        this.x = this.row * this.L;
        this.y = this.col * this.L;
    }

    start() {
        this.init_tank();
    }


    update_operate(op) {
        if(this.status !== 1 || this.is_continue_move) return;
        if(0 <= op && op <= 3) this.is_continue_move = true;
        if(op === 0) {
            this.controller = 'w';
            this.next_row = this.row - 1;
            this.next_col = this.col;
        } else if (op === 1) {
            this.controller = 'd';
            this.next_row = this.row;
            this.next_col = this.col + 1;
        } else if (op === 2) {
            this.controller = 's';
            this.next_row = this.row + 1;
            this.next_col = this.col;
        } else if (op === 3) {
            this.controller = 'a';
            this.next_row = this.row;
            this.next_col = this.col - 1;
        } else if (op === 4 && !this.is_launched) {
            this.controller = ''
            this.is_launched = true;
            let d = this.direction + 3;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        if(d === 3) d = 7;
                        this.bullet[i].launch(this.x, this.y, d);
                        break;
                    }
        } else if (op === 5 && !this.is_launched) {
            this.controller = ''
            this.is_launched = true;
            for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        this.bullet[i].launch(this.x, this.y, this.direction);
                        break;
                    }
        } else if (op === 6 && !this.is_launched) {
            this.controller = ''
            this.is_launched = true;
            let d = this.direction + 4;
                for(let i = 0; i < this.bullet_cnt; ++i)
                    if(!this.bullet[i].is_used) {
                        this.bullet_has--;
                        this.bullet[i].launch(this.x, this.y, d);
                        break;
                    }
        } 
    }

    update_unoperate(op) {
        if(op === 0 && this.controller === 'w') {
            this.controller = '';
            this.is_continue_move = false;
        } else if(op === 1 && this.controller === 'd') {
            this.controller = '';
            this.is_continue_move = false;
        } else if(op === 2 && this.controller === 's') {
            this.controller = '';
            this.is_continue_move = false;
        } else if(op === 3 && this.controller === 'a') {
            this.controller = '';
            this.is_continue_move = false;
        } else if(op === 4 || op === 5 || op === 6) {
            this.is_launched = false;
        }
    }

    update_controller() {
        if(this.status === 0) {
            this.vx = 0;
            this.vy = 0;
            return;
        }
            if(this.gamemap.gamemap[this.next_row][this.next_col] === 1) return;
            this.status = 2;
            if(this.controller === "w"){
                this.vx = -this.speed;
                this.direction = 0;
            }
            else if(this.controller === "d") {
                this.vy = this.speed; 
                this.direction = 1;
            }
            else if(this.controller === "s") {
                this.vx = this.speed;
                this.direction = 2;
            }
            else if(this.controller === "a") {
                this.vy = -this.speed;
                this.direction = 3;
            }
        
    }


    is_collision(rect) {
        for(let wall of this.gamemap.walls) {
            let x1 = wall.r * this.L, y1 = wall.c * this.L, x2 = x1 + this.L, y2 = y1 +this.L;
            
            if(Math.max(rect.x1, x1) >= Math.min(rect.x2, x2) )continue;
            if(Math.max(rect.y1, y1) >= Math.min(rect.y2, y2)) continue; 
            if(this.direction === 0 || this.direction === 2) {
                this.x = x1 - this.dir_x[this.direction] * this.L;
            } else if (this.direction === 1 || this.direction === 3) {
                this.y = y1 - this.dir_y[this.direction] * this.L;
            } 
            return true;
        }
        return false;
    }

    update_move() {
        
            const move_distance = this.speed * this.timedelta / 1000;
            const dx = this.next_row - this.row;
            const dy = this.next_col - this.col;
            const distance = Math.sqrt(dx * dx + dy * dy);

            if(distance <= this.eps) {
                this.status = 1;
                this.row = this.next_row;
                this.col = this.next_col;
            } else {
                this.row += move_distance * (dx / distance);
                this.col += move_distance * (dy / distance);
            }
        
    }

    update() {
        this.update_controller();
        if(this.status === 2)
        this.update_move();
        this.x = this.row * this.L;
        this.y = this.col * this.L;
        if(this.status !== 0) {
            if(this.direction === 0 || this.direction === 2) {
                this.ctx.fillStyle = this.color;
                this.ctx.fillRect(this.y, this.x, this.L / 3, this.L);
                this.ctx.fillStyle = this.color;
                this.ctx.fillRect(this.y  + this.L / 3 * 2, this.x, this.L / 3, this.L);
                this.ctx.fillStyle = 'black';
                this.ctx.arc(this.y + this.L / 2, this.x + this.L / 2, this.L / 3, 0, Math.PI * 2);
                this.ctx.fill();


                if(this.direction === 0 ) {
                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'blue';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2); 
                    this.ctx.lineTo(this.y - this.L / 2, this.x - this.L / 2);  // \
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'black';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L / 2, this.x - this.L / 4 * 3 ); // |
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = '#B37226';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L + this.L / 2, this.x - this.L / 2 ); // /
                    this.ctx.closePath();
                    this.ctx.stroke();
                } else {
                    this.ctx.beginPath();
                    this.ctx.strokeStyle = '#B37226';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y - this.L / 2, this.x + this.L + this.L / 2); // /
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'black';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L / 2, this.x + this.L / 4 * 3 + this.L); // |
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'blue';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L + this.L / 2, this.x + this.L / 4 * 3  + this.L); // \
                    this.ctx.closePath();
                    this.ctx.stroke();
                }
            } else{
                this.ctx.fillStyle = this.color;
                this.ctx.fillRect(this.y, this.x, this.L, this.L / 3);
                this.ctx.fillStyle = this.color;
                this.ctx.fillRect(this.y  , this.x + this.L / 3 * 2, this.L, this.L / 3);
                this.ctx.fillStyle = 'black';
                this.ctx.arc(this.y + this.L / 2, this.x + this.L / 2, this.L / 3, 0, Math.PI * 2);
                this.ctx.fill();

                if(this.direction === 1 ) {
                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'blue';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L / 2 + this.L, this.x - this.L / 2);  // /
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'black';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L / 4 * 3 + this.L, this.x + this.L / 2 ); // -
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = '#B37226';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y + this.L / 2 + this.L, this.x + this.L / 2 + this.L); // \
                    this.ctx.closePath();
                    this.ctx.stroke();
                } else {
                    this.ctx.beginPath();
                    this.ctx.strokeStyle = '#B37226';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y - this.L / 2, this.x - this.L / 2); // \
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'black';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y - this.L / 4 * 3, this.x + this.L / 2); // -
                    this.ctx.closePath();
                    this.ctx.stroke();

                    this.ctx.beginPath();
                    this.ctx.strokeStyle = 'blue';
                    this.ctx.moveTo(this.y + this.L / 2, this.x + this.L / 2);
                    this.ctx.lineTo(this.y - this.L / 2, this.x + this.L + this.L / 2); // /
                     this.ctx.closePath();
                    this.ctx.stroke();
                }
            }

        } else if(this.status === 0) {
            this.ctx.drawImage(this.die_img, this.y, this.x, this.L, this.L);
        }
    }
}
