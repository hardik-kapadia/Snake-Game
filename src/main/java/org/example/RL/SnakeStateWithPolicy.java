package org.example.RL;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

import java.util.List;

@JsonDeserialize
public class SnakeStateWithPolicy extends SnakeState{

    public char action;

    SnakeStateWithPolicy(){
        super();
    }

    SnakeStateWithPolicy(char direction, List<Integer> x, List<Integer> y, int appleX, int appleY, int applesEaten, boolean running, char action) {
        super(direction, x, y, appleX, appleY, applesEaten, running);
        this.action = action;
    }

    public static SnakeStateWithPolicy fromStateAndAction(SnakeState state, char action) {
        return new SnakeStateWithPolicy(state.direction,state.x,state.y,state.appleX,state.appleY,state.applesEaten,state.running,action);
    }

    public SnakeState state() {
        return this;
    }

    public char action() {
        return this.action;
    }
}
