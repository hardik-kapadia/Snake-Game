package org.example.RL;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
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

    public List<Character> getPossibleActions() {

        if(!running)
            new ArrayList<>();

        switch (direction) {
            case 'R':
            case 'L':
                return List.of('U','D','N');
            case 'U':
            case 'D':
                return List.of('R','L','N');
        }

        return new ArrayList<>();

    }

    // Parameters to consider: Distance to apple, isAlive, number of apples eaten, direction to apple
    public double getRewardForState() {

        if(!running)
            return -120;

        double reward = 0;

        reward -= (2 * distanceToApple() / 850);

        if(directionToApple() != direction)
            reward -= 5;

        reward += applesEaten * 10;

        return reward;
    }

    @Override
    public boolean equals(Object state) {

        if(! (state instanceof  SnakeState))
            return false;

        SnakeState s = (SnakeState) state;

        if (!s.running ){
           if(!running)
               return true;
        }

        if(!s.x.equals(x))
            return false;

        if(!s.y.equals(y))
            return false;

        if(s.direction != direction)
            return false;

        if(s.running != running)
            return false;

        if(s.applesEaten != applesEaten)
            return false;

        return s.appleX == appleX && s.appleY == appleY;

    }

    @Override
    public int hashCode() {

        int hash = 0;

        if(!running)
            return 0;

        hash += (direction - 'A') * 133;
        hash += appleX * 25;
        hash += appleY * 25;
        hash += x.hashCode() * 99;
        hash += y.hashCode() * 99;
        hash += applesEaten*10;

        return hash;

    }


}
