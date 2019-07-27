package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.body.AbstractPhysicBody;
import fr.bdd.deathrace.model.body.FinishingLine;
import fr.bdd.deathrace.model.body.Runner;
import fr.bdd.deathrace.model.world.World;
import fr.bdd.deathrace.view.IUpdatable;
import javafx.scene.input.KeyCode;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Map;

public class RunnerBot implements IUpdatable {

    private Player botPlayer;
    private World world;
    private AbstractList<AbstractPhysicBody> staticObstacle;
    private AbstractPhysicBody goal;
    private AbstractList<FinishingLine> goalList;
    private double distanceFromGoal;
    private boolean finish;
    private double botX;
    private double botY;
    private int difficulty;

    enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }
    
    RunnerBot(World world, Player player, int difficulty) {
        goalList = new ArrayList<>();
        setGoal(new FinishingLine(Long.MAX_VALUE, Long.MAX_VALUE));
        distanceFromGoal = Long.MAX_VALUE;
        setbotPlayer(player);
        setWorld(world);
        finish = false;
        this.difficulty = difficulty;
    }

    /**
     * Fonction de movement de notre ordinateur,
     * il calcule pour le moment le mouvement à faire en fonction du poney
     * controlé par le joueur.
     */
    private void move() {
        if (!finish && !Controller.getInstance().getControllerModel().getRace().isStarted()) {
            return;
        }
        if (Controller.getInstance().getControllerModel()
                .getRace().getArrivals().contains(botPlayer.getRunnerUid())) {
            stop();
            return;
        }
        setBotX(world.getObjects().get(botPlayer.getRunnerUid()).getPosition().x);
        setBotY(world.getObjects().get(botPlayer.getRunnerUid()).getPosition().y);
        setClosestFinishingLine();

        if (getBotX() >= getGoalX()) {
            directionToMove(Direction.LEFT);
        } else {
            directionToMove(Direction.RIGHT);
        }
        if (getBotY() < getGoalY() + world.getSize().getY() / 2) {
            if (ennemyNearFront() && borderUpNear()) {
                directionToMove(Direction.DOWN);
            } else if (ennemyNearFront() && borderDownNear()) {
                directionToMove(Direction.UP);
            } else {
                directionToMove(Direction.DOWN);
            }
        } else {
            if (ennemyNearFront() && borderUpNear()) {
                directionToMove(Direction.DOWN);
            } else if (ennemyNearFront() && borderDownNear()) {
                directionToMove(Direction.UP);
            } else {
                directionToMove(Direction.UP);
            }
        }
    }

    private void moveRight() {
        Controller.getInstance().keyReleasedEvent(KeyCode.LEFT, botPlayer.getRunnerUid());
        Controller.getInstance().keyPressedEvent(KeyCode.RIGHT, botPlayer.getRunnerUid());
    }

    private void moveUp() {
        Controller.getInstance().keyReleasedEvent(KeyCode.DOWN, botPlayer.getRunnerUid());
        Controller.getInstance().keyPressedEvent(KeyCode.UP, botPlayer.getRunnerUid());
    }

    private void moveDown() {
        Controller.getInstance().keyReleasedEvent(KeyCode.UP, botPlayer.getRunnerUid());
        Controller.getInstance().keyPressedEvent(KeyCode.DOWN, botPlayer.getRunnerUid());
    }

    private void moveLeft() {
        Controller.getInstance().keyReleasedEvent(KeyCode.RIGHT, botPlayer.getRunnerUid());
        Controller.getInstance().keyPressedEvent(KeyCode.LEFT, botPlayer.getRunnerUid());
    }

    private void directionToMove(Direction dir) {
        switch (dir) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            default:
                moveRight();
                break;
        }
    }

    private void mayIDodge(Direction dir) {
        Direction tempDir = dir;
        if (ennemyNearFront()) {
            tempDir = dodge(dir);
        }
        directionToMove(tempDir);
    }

    private boolean borderUpNear() {
        return (botY - 10 < 0);
    }

    private boolean borderDownNear() {
        return (botY + getSizeYOfRunner() + 1 > world.getSize().getY());
    }

    private boolean obstacleFront() {
        for (AbstractPhysicBody obstacle : staticObstacle) {
            if (obstacleNearFront(obstacle) && translateY(obstacle) && translateY2(obstacle)) {
                return true;
            }
        }
        return false;
    }

    private boolean translateY(AbstractPhysicBody obstacle) {
        return obstacle.getPosition().getY() + obstacle.getShape().getHeight() < getBotY();
    }

    private boolean translateY2(AbstractPhysicBody obstacle) {
        return getBotY() + getSizeYOfRunner()
                > obstacle.getShape().getHeight() + obstacle.getPosition().getY();
    }

    private boolean obstacleNearFront(AbstractPhysicBody obstacle) {
        return obstacle.getPosition().getX() - getBotX() - difficulty - getSizeXOfRunner() < 0;
    }

    private boolean ennemyNearFront() {
        for (AbstractPhysicBody obstacle :
                Controller.getInstance()
                        .getControllerModel().getRace().getWorld().getObjects().values()) {
            if (translateY(obstacle) && translateY2(obstacle) && obstacleNearFront(obstacle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set all the obstacles to dodge on the map AND the finishign line to reach.
     *
     * @param objects can nbe everything on the map
     */
    private void setStaticObstacle(Map<Integer, AbstractPhysicBody> objects) {
        if (objects != null) {
            staticObstacle = new ArrayList<>();
            for (Map.Entry<Integer, AbstractPhysicBody> entry : objects.entrySet()) {
                AbstractPhysicBody temp = entry.getValue();
                if (!(temp instanceof Runner)) {
                    staticObstacle.add(temp);
                    if (temp instanceof FinishingLine) {
                        goalList.add((FinishingLine) temp);
                    }
                }
            }
        }
    }

    private Direction dodge(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.RIGHT;
            case DOWN:
                return Direction.LEFT;
            case RIGHT:
                return Direction.DOWN;
            case LEFT:
                return Direction.UP;
            default:
                return Direction.RIGHT;
        }
    }

    /**
     * Stop the bot when he reched the goal.
     */
    private void stop() {
        Controller.getInstance().keyReleasedEvent(KeyCode.UP, botPlayer.getRunnerUid());
        Controller.getInstance().keyReleasedEvent(KeyCode.DOWN, botPlayer.getRunnerUid());
        Controller.getInstance().keyReleasedEvent(KeyCode.LEFT, botPlayer.getRunnerUid());
        Controller.getInstance().keyReleasedEvent(KeyCode.RIGHT, botPlayer.getRunnerUid());
        finish = true;
    }

    private void setWorld(World world) {
        this.world = world;
        setStaticObstacle(world.getObjects());
    }

    private void setbotPlayer(Player botPlayer) {
        botPlayer.setPseudo("Bot" + botPlayer.getRunnerUid());
        botPlayer.setBot(true);
        this.botPlayer = botPlayer;
    }

    private double getGoalX() {
        return this.goal.getPosition().getX();
    }

    private double getGoalY() {
        return this.goal.getPosition().getX();
    }

    private void setGoal(FinishingLine goal) {
        this.goal = goal;
    }

    /**
     * Cherche la Tile FINISHINGLINE la plus du player.
     */
    private void setClosestFinishingLine() {
        for (FinishingLine finishingLine : goalList) {
            if (getDistanceFromGoal(finishingLine.getPosition().getX(),
                    finishingLine.getPosition().getY())) {
                setGoal(finishingLine);
            }
        }
    }

    private boolean getDistanceFromGoal(double x, double y) {
        double result = Math.sqrt(Math.pow(getBotX() - x, 2) + Math.pow(getBotY() - y, 2));
        if (result < distanceFromGoal) {
            distanceFromGoal = result;
            return true;
        }
        return false;
    }

    private double getBotX() {
        return botX;
    }

    private void setBotX(double botX) {
        this.botX = botX;
    }

    private double getBotY() {
        return botY;
    }

    private void setBotY(double botY) {
        this.botY = botY;
    }

    private double getSizeXOfRunner() {
        return world.getObjects().get(botPlayer.getRunnerUid()).getShape().getX();
    }

    private double getSizeYOfRunner() {
        return world.getObjects().get(botPlayer.getRunnerUid()).getShape().getY();
    }

    @Override
    public void update() {
        move();
    }
}
