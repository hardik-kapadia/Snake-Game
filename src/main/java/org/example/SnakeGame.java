package org.example;

import org.example.RL.SnakeAgent;
import org.example.RL.SnakeState;

import java.util.Map;

public class SnakeGame {
    public static void main(String[] args) {
//        GameFrame gf = new GameFrame();
//        gf.play();
//        gf.start();
        SnakeAgent agent = new SnakeAgent();
        agent.runAll(100);

        Map<SnakeState,Character> policy = agent.learnPolicy();



    }
}
