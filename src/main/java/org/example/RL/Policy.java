package org.example.RL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Policy {


    Map<SnakeState,Character> learnedPolicy;
    Random random;
    public Policy(Map<SnakeState,Character> learnedPolicy) {
        this.learnedPolicy = learnedPolicy;
        this.random = new Random();
    }

    public char getIdealAction(SnakeState state){

        if(learnedPolicy.containsKey(state))
            return learnedPolicy.get(state);

        List<Character> actions = state.possibleActions();
        return actions.get(random.nextInt(actions.size()));

    }

}
