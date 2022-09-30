import {AcGameObject} from '@/assets/scripts/AcGameObject';

export class Wall extends AcGameObject {
    constructor(r, c, gamemap){
        super();
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
        this.color = '#B37226';
    }

    update(){
        this.render();
    }

    //障碍物也要每帧渲染 因为浏览器大小可能改变
    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}