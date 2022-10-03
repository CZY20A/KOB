//蛇的每一个关节
export class Cell{
    constructor(r, c){
        this.r = r;
        this.c = c;

        //圆形要在方块的中间画
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}