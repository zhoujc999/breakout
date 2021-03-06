package Breakout;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Paddle {
    private ImageView paddle;
    private int xVel;


    /**
     * Constructor of the Paddle class.
     */
    public Paddle() {
        new Paddle(180, 500);
    }
    public Paddle(double x, double y) {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(Game.PADDLE_PATH));
        paddle = new ImageView(image);
        paddle.setX(x);
        paddle.setY(y);
        paddle.setFitWidth(Game.PADDLE_WIDTH);
        paddle.setFitHeight(Game.PADDLE_HEIGHT);
        xVel = Game.PADDLE_SPEED;
    }

    public ImageView getView() {
        return paddle;
    }

    public double getMinX() {
        return paddle.getBoundsInParent().getMinX();
    }

    public double getMaxX() {
        return paddle.getBoundsInParent().getMaxX();
    }

    public double getMinY() {
        return paddle.getBoundsInParent().getMinY();
    }

    public double getMaxY() {
        return paddle.getBoundsInParent().getMaxY();
    }

    public void setWidth(double x) {
        paddle.setFitWidth(x);
    }

    public void setY(double y) {
        paddle.setY(y);
    }

    public void setSpeed(double times) {
        xVel *= times;
    }

    public void move(int direction) {
        if (getMinX() + xVel * direction * Game.SECOND_DELAY > 0 && getMaxX() + xVel * direction * Game.SECOND_DELAY < Game.WIDTH) {
            paddle.setX(getMinX() + xVel * direction * Game.SECOND_DELAY);
        }
    }
}
