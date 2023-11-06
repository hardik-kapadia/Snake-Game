package org.example;

import org.example.RL.Policy;
import org.example.RL.SnakeAgent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgentGamePanel extends GamePanel implements ActionListener {

    Policy policy;

    AgentGamePanel(Policy policy) {
        super();
        this.policy = policy;
    }

    AgentGamePanel(Policy policy,int delay) {
        super(delay);
        this.policy = policy;
    }

    @Override
    public void start() {
        applesEaten = 0;
        running = true;
        delay = 100;
        direction = 'R';
        setInitialPosition();
        newApple();
        System.out.println("Started game!");
        timer = new Timer(delay, this);
        System.out.println("Initiated new Timer");
        timer.start();
        System.out.println("Started the timer");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("State is now: "+exportCurrentState()+" with reward: "+exportCurrentState().reward());

        char action = policy.getIdealAction(exportCurrentState());

        if(action != 'N')
            direction = action;

        checkApple();
        move();
        checkCollisions();

        repaint();

        if(!running)
            timer.stop();

    }

}
