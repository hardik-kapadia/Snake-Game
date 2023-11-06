package org.example;

import org.example.RL.Policy;
import org.example.RL.SnakeAgent;

import javax.swing.*;

public class AgentGameFrame extends JFrame {


    public AgentGamePanel agp;

    public AgentGameFrame(Policy policy){
        this.agp = new AgentGamePanel(policy);
    }

    public AgentGameFrame(Policy policy, int delay){
        this.agp = new AgentGamePanel(policy, delay);
    }

    public void play() {
        this.add(agp);
        this.setTitle("Agent Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void start() {
        this.agp.start();
    }

}
