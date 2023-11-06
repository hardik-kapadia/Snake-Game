package org.example;

import org.example.RL.SnakeAgent;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

    public GamePanel gp;

    public GameFrame(){
        this.gp = new GamePanel();
    }

    public GameFrame(int delay){
        this.gp = new GamePanel(delay);
    }

    public void play() {
        this.add(gp);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void start() {
        this.gp.start();
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }

}
