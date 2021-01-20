let w=0, h=0;
let lastTimestamp = 0;
let myX=0, myY=0;
const pressedKeys = {};
const sprite = new Image();
let time = 0;
let lastDir;
let score;

//fixes the width and height of the canvas to those of the window we are currently in
function fixSize(){
    w=window.innerWidth;
    h=window.innerHeight;
    const canvas = document.getElementById('gameCanvas');
    canvas.width = w;
    canvas.height = h;
}

//defines basic functions, constants and variables
function pageLoad(){
    //defines an event 'fixSize' which will resize a part of the page (the canvas)
    window.addEventListener("resize", fixSize);
    fixSize();
    //gives 'sprite' an image
    sprite.src = "img/sprite.png";
    //calls the animation 'redraw' to initiate the sprite on the page
    window.requestAnimationFrame(redraw);
    //spawns an enemy
    spawnEnemy();
    //defines events for when keys are pressed
    window.addEventListener("keydown", event => pressedKeys[event.key] = true);
    window.addEventListener("keyup", event => pressedKeys[event.key] = false);
    window.requestAnimationFrame(frame);
    //calls the function addTime (to be used as our timer)
    addTime();
}

//draws the sprite and erases behind it
function redraw(myX, myY, whiteX, whiteY, direction){
    const canvas = document.getElementById('gameCanvas');
    const context = canvas.getContext('2d');
    //draws a 100*100 red square at the specified coordinates
    context.drawImage(sprite, myX, myY+1, 100, 100);
    //erases in a different spot based on which direction we are moving in
    if (direction === "down"){
        context.clearRect(whiteX, whiteY+1, 100, 2);
    }
    if (direction === "right"){
        context.clearRect(whiteX, whiteY+1, 2, 100);
    }
    if(direction === "up"){
        context.clearRect(whiteX, whiteY+100, 100, 2);
    }
    else{
        context.clearRect(whiteX+100, whiteY+1, 2, 100);
    }
}

function shoot(direction,X,Y, movedX, movedY){
    const canvas = document.getElementById('gameCanvas');
    const context = canvas.getContext('2d');
        if(direction==="right"){
            context.fillStyle = 'black';
            context.beginPath();
            context.arc(X+100,Y+50, 5, 0, 2*Math.PI);
            context.fill();
            if(X<canvas.width){
                //used to show how far the 'bullet' has moved
                movedX++;
                //recursive call to continue shooting
                registerHit(X,Y);
                //console.log(window.enemyX,window.enemyY);
               shoot(direction, X+1, Y,movedX, movedY);
            }else{
                //clears the path of the bullet when it reaches the edge (same points repeated for other directions, with different values for x, y, width, height)
                //waits 100ms before clearing the path
                setTimeout(()=>{context.beginPath();
                context.clearRect(X-movedX+100,Y,movedX,100);
                context.closePath();},100);
            }
        }
        if(direction==="down"){
            context.fillStyle = 'black';
            context.beginPath();
            context.arc(X+50,Y+100, 5, 0, 2*Math.PI);
            context.fill();
            if(Y<canvas.height){
                movedY++;
                shoot(direction, X, Y+1,movedX, movedY);
            }else{
                setTimeout(()=>{context.beginPath();
                context.clearRect(X,Y-movedY+100,100,movedY);
                context.closePath();}, 100);

            }
        }
        if(direction==="left"){
            context.fillStyle = 'black';
            context.beginPath();
            context.arc(X,Y+50, 5, 0, 2*Math.PI);
            context.fill();
            //shoot(direction, X-1, Y);
            if(X>0){
                movedX++;
                shoot(direction, X-1, Y,movedX, movedY);
            }else{
                setTimeout(()=>{context.beginPath();
                context.clearRect(0,Y,movedX,100);
                context.closePath();},100);
            }
        }
        if(direction==="up"){
            context.fillStyle = 'black';
            context.beginPath();
            context.arc(X+50,Y, 5, 0, 2*Math.PI);
            context.fill();
            if(Y>0){
                movedY++;
                shoot(direction, X, Y-1,movedX,movedY);
            }else{
                setTimeout(()=>{context.beginPath();
                context.clearRect(X,0,100,movedY);
                context.closePath();},100);
            }
        }
}
function Enemy(spawnY, X, Y, enemyHit){
    window.enemySpawnY = spawnY;
    window.enemyY = Y;
    window.enemyX = X;
    window.enemyHit = enemyHit;
}
function spawnEnemy(){
    const canvas = document.getElementById("gameCanvas");
    const context = canvas.getContext('2d');
    //generates a random position on the right edge of the screen for an enemy to spawn on
    let initialY = Math.ceil(Math.random()*canvas.height);
    let enemy = new Enemy(initialY, canvas.width, initialY, false);
    console.log(canvas.height);
    //draws the enemy sprite at its initial position
    context.drawImage(sprite, canvas.width, enemySpawnY, 20, 20);
    //begins to move the enemy
    moveEnemy(enemySpawnY, 0, enemy);
}

function moveEnemy(Y,moved){
    const canvas = document.getElementById("gameCanvas");
    const context = canvas.getContext('2d');
    //removes the current enemy and spawns a new one if the previous enemy reaches the edge of the screen.
    if (enemyHit === false){
        if (moved === canvas.width){
            context.clearRect(0, Y, 2, 20);
            spawnEnemy();
        }
        else{
            //console.log("moving");
            moved++;
            //draws the enemy sprite 1 pixel to the left of where it previously was
            context.drawImage(sprite, canvas.width-moved, Y, 20, 20);
            enemyX--;
            //clears the previous position of the enemy (doesn't work at present)
            context.clearRect((canvas.Width-moved)+2, Y, 20, 20);
            //waits before a recursive call to move the enemy again
            setTimeout(()=>{moveEnemy(Y,moved);},100);
        }
    }
}

function registerHit(shotX,shotY){
    const canvas = document.getElementById("gameCanvas");
    const context = canvas.getContext('2d');
    let hit = false;
    //while (hit===false){
        for (i=0; i<=20;i++){
            for (j=0; j<=20; j++){
                if(shotX===enemyY+i&&shotY===enemyX+j){
                    hit = true;
                    console.log(hit);
                    context.clearRect(enemyX, enemyY, 20,20);
                }
            }
        }
    //}
}

function frame(timeStamp) {
    //decides when to move on a frame
    if (lastTimestamp === 0) lastTimestamp = timeStamp;
    const frameLength = (timeStamp - lastTimestamp) / 1000;
    lastTimestamp = timeStamp;

    inputs();
    //processes(frameLength);
    //outputs();

    window.requestAnimationFrame(frame);

    function inputs() {
        const canvas = document.getElementById('gameCanvas');
        const lastX = myX;
        const lastY = myY;
        //sends different values to redraw the sprite based on which key has been pressed
        if (pressedKeys["ArrowUp"]) {
            if (myY > 0) {
                lastDir = "up";
                myY--;
                redraw(myX, myY, lastX, lastY, "up");
            }
        }
        if (pressedKeys["ArrowDown"]) {
            if (myY < canvas.height - 1) {
                myY++;
                lastDir = "down";
                redraw(myX, myY, lastX, lastY, "down");
            }
        }
        if (pressedKeys["ArrowLeft"]) {
            if (myX > 0) {
                lastDir = "left";
                myX--;
                redraw(myX, myY, lastX, lastY, "left");
            }
        }
        if (pressedKeys["ArrowRight"]) {
            if (myX < canvas.width - 1) {
                lastDir = "right";
                myX++;
                redraw(myX, myY, lastX, lastY, "right");
            }
        }
        if (pressedKeys["b"]) {
            let movedX = 0;
            let movedY = 0;
            shoot(lastDir, myX, myY,movedX, movedY)
        }
    }
}
    function addTime() {
        //increments the timer every addTime is called
        time += 1;
        setTimeout(() => addTime(), 1000);
        //addTime();
    }
