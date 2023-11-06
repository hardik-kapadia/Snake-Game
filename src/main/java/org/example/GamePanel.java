package org.example;

import org.example.RL.SnakeAgent;
import org.example.RL.SnakeState;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 15;
    int delay = 100;

    java.util.List<Integer> x = new ArrayList<>();

    java.util.List<Integer> y = new ArrayList<>();

    int applesEaten;

    int appleX;
    int appleY;

    char direction; // R,L,U,D;
    boolean running;

    int initialSize = 6;

    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
    }

    GamePanel(int delay) {
        this.delay = delay;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
    }

    public void start(){
        startGame();
    }

    public void startGame() {

        applesEaten = 0;
        running = true;
        delay = 100;
        direction = 'R';
        setInitialPosition();
        newApple();
        timer = new Timer(delay, this);
        timer.start();
    }

    public SnakeState startManualGame(int delay) {

        applesEaten = 0;
        running = true;
        this.delay = delay;
        direction = 'R';
        setInitialPosition();
        newApple();

        return exportCurrentState();
    }

    public void startForAgent(SnakeAgent agent) {
        applesEaten = 0;
        running = true;
        delay = 100;
        direction = 'R';
        setInitialPosition();
        newApple();
    }

    public void setInitialPosition() {

        x.clear();
        y.clear();

        int prevX = SCREEN_WIDTH / 2;

        for (int i = 0; i < initialSize; i++) {
            x.add(prevX);
            y.add(SCREEN_HEIGHT / 2);

            prevX -= UNIT_SIZE;
        }

    }

    public SnakeState exportCurrentState() {

        return SnakeState.builder()
                .applesEaten(applesEaten)
                .appleX(appleX)
                .appleY(appleY)
                .x(x).y(y)
                .direction(direction)
                .running(running)
                .build();
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {

        for (int i = x.size() - 1; i > 0; i--) {
            x.set(i, x.get(i - 1));
            y.set(i, y.get(i - 1));
        }

        switch (direction) {
            case 'R':
                x.set(0, x.get(0) + UNIT_SIZE);
                break;
            case 'L':
                x.set(0, x.get(0) - UNIT_SIZE);
                break;
            case 'U':
                y.set(0, y.get(0) - UNIT_SIZE);
                break;
            case 'D':
                y.set(0, y.get(0) + UNIT_SIZE);
                break;
        }

    }

    public void checkApple() {

        if (x.get(0) == appleX && y.get(0) == appleY) {
            x.add(-1);
            y.add(-1);
            applesEaten++;
            delay = Math.max(0,delay-1);
            newApple();
        }

    }

    public void checkCollisions() {

        // checks if head collides with body
        for (int i = x.size() - 1; i > 0; i--)
            if (x.get(0).equals(x.get(i)) && y.get(0).equals(y.get(i))) {
                running = false;
                break;
            }

        // checks if it is, going out left or right
        if (x.get(0) < 0 || x.get(0) > SCREEN_WIDTH)
            running = false;

        // checks if it is going above or below
        if (y.get(0) < 0 || y.get(0) > SCREEN_HEIGHT)
            running = false;

        if (!running)
            timer.stop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) throws IOException {

        if (running) {

            InputStream iis = getClass().getClassLoader().getResourceAsStream("apple.png");
            Image apple = ImageIO.read(iis);

            g.drawImage(apple, appleX, appleY, UNIT_SIZE, UNIT_SIZE, null);

            for (int i = 0; i < x.size(); i++) {
                if (i == 0)
                    g.setColor(Color.green);
                else
                    g.setColor(new Color(45, 180, 0));

                g.fillRect(x.get(i), y.get(i), UNIT_SIZE, UNIT_SIZE);
            }

            showScore(g);

        } else {
            gameOver(g);
        }

    }

    public void showScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize() + 5);
    }

    public void gameOver(Graphics g) {

        showScore(g);
        g.setColor(Color.RED);
        g.setFont(new Font("Ink free", Font.BOLD, 80));
        FontMetrics metricsOver = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metricsOver.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 3);

    }

    public SnakeState act(char action) {

        if(action != 'N')
            direction = action;

        checkApple();
        move();
        checkCollisions();

        Timer t = new Timer(this.delay,e -> repaint());
        t.setInitialDelay(this.delay);
        t.setRepeats(false);
        t.start();
        return exportCurrentState();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            checkApple();
            move();
            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
                case KeyEvent.VK_ENTER:
                    if (!running)
                        startGame();
            }
        }
    }
}
