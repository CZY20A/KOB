import { AcGameObject } from '@/assets/scripts/AcGameObject';
import { Tank } from '@/assets/scripts/tank/Tank.js';
import { Bullet } from './Bullet';
import { TankWall } from './TankWall';

export class TankGameMap extends AcGameObject {
    constructor(ctx, parent, store){
        super();
        this.ctx = ctx;
        this.parent = parent;

        this.is_end = false;

        this.store = store;
        this.rows = 15;
        this.cols = 15;
        this.gamemap = store.state.pk.gamemap;

        this.walls = [];
        this.walls_cnt = 30;

        this.tanks = [
            new Tank({id:1, row:this.rows-2, col:1, color:'red', ctx:this.ctx}, this),
            new Tank({id:2, row:1, col:this.cols-2, color:'green', ctx:this.ctx}, this)
        ];

        this.dir_x = [-1, 0, 1, 0];
        this.dir_y = [0, 1, 0, -1];
        this.L = 1;

        
    }

    // init_map() {
    //     for(let i = 0; i < this.rows; ++i)
    //         for(let j = 0; j < this.cols; ++j)
    //             this.gamemap[i][j] = 0;
    //     for(let i = 0; i < this.rows; ++i) {
    //         this.gamemap[i][0] = this.gamemap[i][this.rows - 1] = this.gamemap[0][i] = this.gamemap[this.cols - 1][i] = 1;
    //     }
    //     for(let i = 0; i < this.walls_cnt / 2; ++i) {
    //         for(let j = 0; j < 6000; ++j) {
    //             let r = parseInt(Math.random() * this.rows), c = parseInt(Math.random() * this.cols);
    //             if(this.gamemap[r][c]) continue;
    //             if(r === this.rows - 2 && c === 1 || r === 1 && c === this.cols - 2) continue;
    //             let flag = false;
    //             for(let i = 0; i < 4; ++i) {
    //                 let rr = r + this.dir_x[i] * 2, cc = c + this.dir_y[i] * 2;
    //                 if(rr >= 0 && rr <= this.rows - 1 && cc >= 0 && cc <= this.cols - 1 && this.gamemap[rr][cc])
    //                 {
    //                     flag = true;
    //                     break;
    //                 }
    //             }
    //             if(flag) continue;
    //             if(this.gamemap[r][c] === 0) {
    //                 this.gamemap[r][c] = this.gamemap[this.rows - 1 - r][this.cols - 1 - c] = 1;
    //                 break;
    //             }
    //         }
    //     }
    // }

    // check_valid(x, y, g) {
    //     if(x === 1 && y === this.cols-2) return true;
    //     let xx, yy;
    //     g[x][y] = 1;
    //     for(let i = 0; i < 4; ++i) {
    //         xx = x + this.dir_x[i];
    //         yy = y + this.dir_y[i];
    //         if( g[xx][yy] === 0)
    //             return this.check_valid(xx, yy, g);
    //     }
    //     return false;
    // }

    draw_map() {
        
                for(let i = 0; i < this.rows; ++i)
                    for(let j = 0; j < this.cols; ++j)
                        if(this.gamemap[i][j] === 1) {
                            this.walls.push(new TankWall(i, j, this));
                        }
        
          
    }

    add_listener() {
        this.ctx.canvas.focus();

        this.ctx.canvas.addEventListener('keydown', e => {
            for(const t of this.tanks) {
                if(t.status === 0) return;
            }
            let userId = parseInt(this.store.state.user.id);
            if(userId === this.store.state.pk.a_id && this.tanks[0].status === 2) return;
            if(userId === this.store.state.pk.b_id && this.tanks[1].status === 2) return;
            let d = -1;
            if (e.key === 'w') d = 0;
            else if (e.key === 'd') d = 1;
            else if (e.key === 's') d = 2;
            else if (e.key === 'a') d = 3;
            else if (e.key === 'ArrowUp') d = 0;
            else if (e.key === 'ArrowRight') d = 1;
            else if (e.key === 'ArrowDown') d = 2;
            else if (e.key === 'ArrowLeft') d = 3;
            else if (e.key === 'j') d = 4;
            else if (e.key === 'k') d = 5;
            else if(e.key === 'l') d = 6;

            if (d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "operate",
                    operate: d,
                }))
            }
        })

        this.ctx.canvas.addEventListener('keyup', e => {
            let d = -1;
            if (e.key === 'w') d = 0;
            else if (e.key === 'd') d = 1;
            else if (e.key === 's') d = 2;
            else if (e.key === 'a') d = 3;
            else if (e.key === 'ArrowUp') d = 0;
            else if (e.key === 'ArrowRight') d = 1;
            else if (e.key === 'ArrowDown') d = 2;
            else if (e.key === 'ArrowLeft') d = 3;
            else if (e.key === 'j') d = 4;
            else if (e.key === 'k') d = 5;
            else if(e.key === 'l') d = 6;

            if(d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "unoperate",
                    operate: d,
                }))
            }
        })
    }

    send_result() {
        if(this.is_end) return;
        for(const t of this.tanks) {
            if(t.status === 0)  {
                let loser = t.id === 1 ? 'A' : 'B';
                this.is_end = true;
                this.store.state.pk.socket.send(JSON.stringify({
                    event: 'result',
                    loser: loser
                }))
            }
        }
    }

    start() {
        
        this.add_listener();
        this.update_size();
        this.draw_map();
        for(let i = 0; i < 30; ++i) {
            this.tanks[0].bullet.push(new Bullet({L:this.L, tank:this.tanks[0]},this.ctx, this));
            this.tanks[1].bullet.push(new Bullet({L:this.L, tank:this.tanks[1]},this.ctx, this));
        }
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;

        for(let tank of this.tanks) tank.L = this.L;
    }

    render() {     
        this.ctx.fillStyle = "#FFFDFC";
        this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height)
    }

    update() {
        this.send_result();
        this.update_size();
        this.render();

    }
}
