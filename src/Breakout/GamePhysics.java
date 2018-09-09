package Breakout;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;

public class GamePhysics {
    private ScreenController sc;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;



    public GamePhysics(ScreenController screenController) {
        sc = screenController;
        getScreenElements();
    }


    public void getScreenElements() {
        ball = sc.getBall();
        paddle = sc.getPaddle();
        bricks = sc.getBricks();
    }

    public void moveScreenElements() {
        sc.moveBall();
        sc.movePaddle();
    }

    public void collisionEffects() {
        ballOutTop();
        ballWithWall();
        ballWithPaddle();
        ballWithBricks();
    }


    private void ballWithWall() {
        double ballMinX = ball.getMinX();
        double ballMaxX = ball.getMaxX();
        if (ballMinX <= 0 || ballMaxX >= Game.WIDTH) {
            ball.xBounce();
        }
    }

    public boolean ballOutBottom() {
        return ball.getMaxY() >= Game.HEIGHT;
    }


    private void ballOutTop() {
        if (ball.getMinY() <= 0) {
            ball.yBounce();
        }
    }

    private void ballWithPaddle() {
        double ballMinX = ball.getMinX();
        double ballMaxX = ball.getMaxX();
        double ballMinY = ball.getMinY();
        double ballMaxY = ball.getMaxY();
        double paddleMinX = paddle.getMinX();
        double paddleMaxX = paddle.getMaxX();
        double paddleMinY = paddle.getMinY();
        double paddleMaxY = paddle.getMaxY();

        double paddleHalf = Game.PADDLE_WIDTH / 2;

        // check for hit on the upper edge
        if (ball.getYVel() > 0 && ballMaxY >= paddleMinY && ballMinY <= paddleMinY && ((ballMaxX >= paddleMinX) && (ballMaxX <= paddleMaxX) || (ballMinX >= paddleMinX && ballMinX <= paddleMaxX))) {
            ball.yBounce();

            if (ball.getXVel() > 0 && (paddleMinX + paddleHalf) > ballMaxX) {
                ball.xBounce();
            }
            if (ball.getXVel() < 0 && (paddleMinX + paddleHalf) < ballMinX) {
                ball.xBounce();
            }
        }

    }

    private void ballWithBricks() {
        ArrayList<Brick> bricksToBeRemoved = new ArrayList<>();
        for (Brick brick: bricks) {

            ballWithBrick(brick);
            if (brick.toBeRemoved()) {
                bricksToBeRemoved.add(brick);
            }

        }
        bricks.removeAll(bricksToBeRemoved);

    }

    private void ballWithBrick(Brick b) {
        double ballMinX = ball.getMinX();
        double ballMaxX = ball.getMaxX();
        double ballMinY = ball.getMinY();
        double ballMaxY = ball.getMaxY();
        double brickMinX = b.getMinX();
        double brickMaxX = b.getMaxX();
        double brickMinY = b.getMinY();
        double brickMaxY = b.getMaxY();

        // check for hit on the upper edge
        if (ball.getYVel() > 0 && ballMaxY >= brickMinY && ballMinY <= brickMinY && ((ballMaxX >= brickMinX) && (ballMaxX <= brickMaxX) || (ballMinX >= brickMinX && ballMinX <= brickMaxX))) {
            ball.yBounce();
            b.gotHit();
        }
        // check for hit on the lower edge
        else if (ball.getYVel() < 0 && ballMinY <= brickMaxY && ballMaxY >= brickMaxY && ((ballMaxX >= brickMinX) && (ballMaxX <= brickMaxX) || (ballMinX >= brickMinX && ballMinX <= brickMaxX))) {
            ball.yBounce();
            b.gotHit();
        }
        // check for hit on the left edge
        else if (ball.getXVel() > 0 && ballMaxX >= brickMinX && ballMinX <= brickMinX && ((ballMaxY >= brickMinY && ballMaxY <= brickMaxY) || (ballMinY >= brickMinY && ballMinY <= brickMaxY))) {
            ball.xBounce();
            b.gotHit();
        }
        // check for hit on the right edge
        else if (ball.getXVel() < 0 && ballMinX <= brickMaxX && ballMaxX >= brickMaxX && ((ballMaxY >= brickMinY && ballMaxY <= brickMaxY) || (ballMinY >= brickMinY && ballMinY <= brickMaxY))) {
            ball.xBounce();
            b.gotHit();
        }
        if (b.toBeRemoved()) {
            sc.removeBrick(b);
        }



    }
}


