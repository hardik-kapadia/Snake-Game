package org.example;

import org.example.RL.SnakeAgent;

import javax.swing.*;

public class AgentGameFrame extends JFrame {


    public AgentGamePanel agp;
    public SnakeAgent agent;

    public AgentGameFrame(SnakeAgent agent){
        this.agent = agent;
        this.agp = new AgentGamePanel(agent);
    }

    public AgentGameFrame(SnakeAgent agent, int delay){
        this.agent = agent;
        this.agp = new AgentGamePanel(agent, delay);
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
