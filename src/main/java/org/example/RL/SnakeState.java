package org.example.RL;

import java.util.List;

public class SnakeState {

    char direction;

    List<Integer> x;
    List<Integer> y;

    int appleX;
    int appleY;

    int applesEaten;

    boolean running;

    private double distanceToApple(){
        return Math.sqrt((x.get(0) - appleX)*(x.get(0) - appleX) + (y.get(0) - appleY)*(y.get(0) - appleY));
    }

    private char directionToApple() {
        int yDist = appleY - y.get(0);
        int xDist = appleX - x.get(0);

        if(yDist > xDist)
            return yDist > 0 ? 'U' : 'D';

        return xDist > 0 ? 'R' : 'L';

    }

    // Parameters to consider: Distance to apple, isAlive, number of apples eaten, direction to apple
    public double getRewardForState() {

        if(!running)
            return 120;

        double reward = 0;

        reward -= (2 * distanceToApple() / 850);

        if(directionToApple() != direction)
            reward -= 5;

        reward += applesEaten * 10;

        return reward;
    }

}
