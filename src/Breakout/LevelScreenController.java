package Breakout;

import java.util.Scanner;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import java.util.ArrayList;
import javafx.scene.text.Text;
import javafx.scene.text.Font;



public class LevelScreenController {

    private Scene scene;
    private Group root;
    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks;
    private ArrayList<Powerup> powerups;
    private int paddleDirection;
    private int numPermBlocks;
    private Text levelText;
    private Text lifeText;
    private Text powerText;

    private int level;
    private int life;
    private int power;

    public LevelScreenController(int gameLevel, int gameLife, int gamePower) {
        root = new Group();
        // create a place to see the shapes
        scene = new Scene(root, Game.WIDTH, Game.HEIGHT, Game.BACKGROUND);
        level = gameLevel;
        life = gameLife;
        power = gamePower;
        numPermBlocks = 0;
        setPaddle();
        setBall();
        setBricks();
        setLevelText();
        setLifeText();
        setPowerText();
        paddleDirection = 0;
    }


    public Scene getScene() {
        return scene;
    }

    public Group getGroup() {
        return root;
    }


    public void setLevel(int l) {
        level = l;
        numPermBlocks = 0;
        resetBallPaddle();
        removeAllBricks();
        removeAllPowerups();
        setBricks();
        changeLevelText(l);
    }


    public void resetBallPaddle() {
        resetPaddle();
        resetBall();
    }


    private void setPaddle() {
        paddle = new Paddle((Game.WIDTH - Game.PADDLE_WIDTH) / 2, 5.0 / 6 * Game.HEIGHT);
        root.getChildren().add(paddle.getView());
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddleDirection(int direction) {
        paddleDirection = direction;
    }

    public void movePaddle() {
        paddle.move(paddleDirection);
    }


    private void resetPaddle() {
        root.getChildren().remove(paddle.getView());
        setPaddle();
    }


    private void setBallStartState() {
        double paddleMiddle = (getPaddle().getMinX() + getPaddle().getMaxX()) / 2 - Game.BALL_SIZE / 2;
        int dir = Game.getRandomInRange(-1, 1);
        int xRandVel = Game.getRandomInRange(30, 80);
        if (dir == 0) {
            xRandVel *= -1;
        }
        int yRandVel = (int) Math.sqrt(Game.BALL_SPEED * Game.BALL_SPEED - xRandVel * xRandVel);
        ball.setX(paddleMiddle);
        ball.setY(getPaddle().getMinY() - (double) Game.BALL_SIZE);
        ball.setXVel(xRandVel);
        ball.setYVel(-yRandVel);
    }

    private void setBall() {
        ball = new Ball();
        setBallStartState();
        root.getChildren().add(ball.getView());
    }

    public Ball getBall() {
        return ball;
    }

    public void moveBall() {
        ball.move();
    }

    private void resetBall() {
        root.getChildren().remove(ball.getView());
        setBall();
    }

    private void setBricks() {
        bricks = new ArrayList<>();
        powerups = new ArrayList<>();
        var bricksType = new ArrayList<Integer>();
        int initialXPos = (Game.WIDTH - 3 * (Game.BRICK_WIDTH + 2 * Game.BRICK_XGAP)) / 2;
        int brickXPos = initialXPos;
        int brickYPos = (Game.HEIGHT - 5 * (Game.BRICK_HEIGHT + 2 * Game.BRICK_YGAP)) / 2;
        int xNum = 0;
        switch (level) {
            case 1:
                readFileInput(Game.LEVELONE_PATH, bricksType);
                break;
            case 2:
                readFileInput(Game.LEVELTWO_PATH, bricksType);
                break;
            case 3:
                readFileInput(Game.LEVELTHREE_PATH, bricksType);
                break;
            default:
                readFileInput(Game.LEVELONE_PATH, bricksType);
                break;
        }

        for (Integer brickType : bricksType) {
            Brick brick = new Brick(brickXPos + Game.BRICK_XGAP, brickYPos + Game.BRICK_YGAP, brickType);
            if (brickType == 3) {
                numPermBlocks++;
            } else if (brickType == 2) {
                setPowerup(brick);
            }
            root.getChildren().add(brick.getView());
            bricks.add(brick);
            xNum++;
            if (xNum < 3) {
                brickXPos += (Game.BRICK_WIDTH + 2 * Game.BRICK_XGAP);
            } else {
                xNum = 0;
                brickXPos = initialXPos;
                brickYPos += (Game.BRICK_HEIGHT + 2 * Game.BRICK_YGAP);
            }

        }
    }

    private void readFileInput(String levelPath, ArrayList<Integer> brickList) {
        Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream(levelPath));
        try {
            while (sc.hasNextInt()) {
                brickList.add(sc.nextInt());
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
    }

    public ArrayList getBricks() {
        return bricks;
    }

    public void removeBrick(Brick b) {
        root.getChildren().remove(b.getView());
    }

    private void removeAllBricks() {
        for (Brick brick : bricks) {
            root.getChildren().remove(brick.getView());
        }
        bricks.clear();
    }

    public void clearNumPermBlocks() {
        numPermBlocks = 0;
    }

    public boolean allBricksRemoved() {
        return bricks.size() == numPermBlocks;
    }


    private void setPowerup(Brick b) {
        int randInt = Game.getRandomInRange(0, 30);
        Powerup powerup;
        if (randInt < 10) {
            powerup = new Powerup(1, b);
        } else if (randInt < 20) {
            powerup = new Powerup(2, b);
        } else {
            powerup = new Powerup(3, b);
        }
        root.getChildren().add(powerup.getView());
        powerups.add(powerup);

    }

    public ArrayList getPowerups() {
        return powerups;
    }

    public void movePowerups() {
        for (Powerup p : powerups) {
            p.move();
        }
    }

    public void removePowerup(Powerup p) {
        root.getChildren().remove(p.getView());
    }

    private void removeAllPowerups() {
        for (Powerup powerup : powerups) {
            root.getChildren().remove(powerup.getView());
        }
        powerups.clear();
    }


    private void setLevelText() {
        levelText = new Text(Game.WIDTH * 4 / 5, Game.HEIGHT / 30, "Level: " + level);
        Font font = new Font(Game.TEXT_SIZE);
        levelText.setFont(font);
        root.getChildren().add(levelText);
    }

    public void changeLevelText(int l) {
        level = l;
        levelText.setText("Level: " + level);
    }

    private void setLifeText() {
        lifeText = new Text(0, Game.HEIGHT / 30, "Lives: " + life);
        Font font = new Font(Game.TEXT_SIZE);
        lifeText.setFont(font);
        root.getChildren().add(lifeText);
    }

    public void changeLifeText(int l) {
        life = l;
        lifeText.setText("Lives: " + life);
    }

    private void setPowerText() {
        powerText = new Text(Game.WIDTH * 2 / 5, Game.HEIGHT / 30, "Power: " + power);
        Font font = new Font(Game.TEXT_SIZE);
        powerText.setFont(font);
        root.getChildren().add(powerText);
    }


    public void changePowerText(int p) {
        power = p;
        powerText.setText("Power: " + power);
    }


    public void wonScreen() {

        root.getChildren().clear();
        // create a place to see the shapes
        Text wonText = new Text(Game.WIDTH * 1 / 5, 2 * Game.HEIGHT / 5, "YOU WON!");
        Font font = new Font(30);
        wonText.setFont(font);
        root.getChildren().add(wonText);
    }

    public void lostScreen() {

        root.getChildren().clear();
        // create a place to see the shapes
        Text lostText = new Text(Game.WIDTH * 1 / 5, 2 * Game.HEIGHT / 5, "YOU LOST!");
        Font font = new Font(30);
        lostText.setFont(font);
        root.getChildren().add(lostText);
    }
}
