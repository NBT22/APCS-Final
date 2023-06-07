import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public int height = (int) screenSize.getHeight();
    public int width = (int) screenSize.getWidth();
    private int lPaddlePos;
    private int rPaddlePos;
    private final int[] lPaddleTarget;
    private final int[] rPaddleTarget;
    private final int paddleSpeed = Settings.loadSettings().speed;
    private int[] ballPos = new int[]{width / 2 - height / 64, height / 2 - height / 64};
    private double[] ballVelocity;
    private int speed = 5;
    private final int[] score = new int[]{0, 0};
    private final int players;
    private final JFrame frame;
    private final JPanel panel;

    public Game(int players) {
        this.players = players;
        ballVelocity = new double[]{players == 1 ? -5 : (Math.random() > 0.5 ? 5 : -5), (Math.random() > 0.5 ? (int) (Math.random() * 2) + 1 : (int) (Math.random() * 2) - 2)};
        positionPaddles();
        lPaddleTarget = new int[]{0, lPaddlePos};
        rPaddleTarget = new int[]{0, rPaddlePos};
        frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, lPaddlePos, height / 64, height / 5);
                g2d.fillRect(width - height / 64, rPaddlePos, height / 64, height / 5);
                g2d.fillOval(ballPos[0], ballPos[1], height / 32, height / 32);
                g2d.setFont(new Font("Arial", Font.BOLD, height / 16));
                g2d.drawString(Integer.toString(score[0]), width / 4, height / 8);
                g2d.drawString(Integer.toString(score[1]), width * 3 / 4, height / 8);
            }
        };
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        switch (players) {
            case 0 -> frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    }
                }
            });
            case 1 -> frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> lPaddlePos -= 10;
                        case KeyEvent.VK_DOWN -> lPaddlePos += 10;
                        case KeyEvent.VK_ESCAPE -> System.exit(0);
                    }
                }
            });
            default -> frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> lPaddlePos -= 10;
                        case KeyEvent.VK_DOWN -> lPaddlePos += 10;
                        case KeyEvent.VK_PAGE_UP, KeyEvent.VK_W -> rPaddlePos -= 10;
                        case KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_S -> rPaddlePos += 10;
                        case KeyEvent.VK_ESCAPE -> System.exit(0);
                    }
                }
            });
        }
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                panel.repaint();
                if (score[0] == 10 || score[1] == 10) {
                    timer.cancel();
                    endGame(score[0] == 10 ? 1 : 2);
                }
            }
        }, 0, 10);
    }

    private void endGame(int winner) {
        GameOver.main(new String[]{Integer.toString(winner)});
        frame.dispose();
    }

    private int[] calculatePosition() {
        double[] velocity = new double[]{ballVelocity[0], ballVelocity[1]};
        int[] pos = new int[]{ballPos[0], ballPos[1]};
        pos[0] += velocity[0];
        pos[1] += velocity[1];
        while (pos[0] < width - height / 64 - height / 32 && pos[0] > height / 64) {
            if (pos[1] < 0 || pos[1] > height - height / 32) {
                velocity[1] *= -1;
            }
            pos[0] += velocity[0];
            pos[1] += velocity[1];
        }
        return pos;
    }

    private void update() {
        if (players == 1) {
            lPaddlePos = MouseInfo.getPointerInfo().getLocation().y - height / 10;
        }
        if (ballPos[1] < 0 || ballPos[1] > height - height / 32) {
            ballVelocity[1] *= -1;
        }
        if ((ballPos[0] <= height / 64) && ballPos[1] > lPaddlePos - height / 32 && ballPos[1] < lPaddlePos + height / 5) {
            double angle = Math.toRadians(-(90 + ((((ballPos[1] - lPaddlePos + height / 64) / (height / 5d)) - 0.5) * 90)));
            ballVelocity[0] = -speed * Math.sin(angle);
            ballVelocity[1] = -speed * Math.cos(angle);
            if (speed < 100) speed++;
            if (players < 2) {
                rPaddleTarget[1] = calculatePosition()[1] - (int) (Math.random() * (height / 5)) + height / 64;
                if (rPaddleTarget[1] < rPaddlePos) rPaddleTarget[0] = 0;
                else rPaddleTarget[0] = 1;
            }
        } else if (ballPos[0] >= width - height / 64 - height / 32 && ballPos[1] > rPaddlePos - height / 32 && ballPos[1] < rPaddlePos + height / 5) {
            double angle = Math.toRadians(-(90 + ((((ballPos[1] - rPaddlePos + height / 64) / (height / 5d)) - 0.5) * 90)));
            ballVelocity[0] = speed * Math.sin(angle);
            ballVelocity[1] = -speed * Math.cos(angle);
            if (speed < 100) speed++;
            if (players == 0) {
                lPaddleTarget[1] = calculatePosition()[1] - (int) (Math.random() * (height / 5)) + height / 64;
                if (lPaddleTarget[1] < lPaddlePos) lPaddleTarget[0] = 0;
                else lPaddleTarget[0] = 1;
            }
        } else if (ballPos[0] < 0) {
            score[1]++;
            ballPos = new int[]{width / 2 - height / 32, height / 2 - height / 32};
            speed = 5;
            ballVelocity = new double[]{-speed, (Math.random() > 0.5 ? (int) (Math.random() * 2) + 1 : (int) (Math.random() * 2) - 2)};
            positionPaddles();
        } else if (ballPos[0] > width - height / 32) {
            score[0]++;
            ballPos = new int[]{width / 2 - height / 32, height / 2 - height / 32};
            speed = 5;
            ballVelocity = new double[]{(players == 1 ? -speed : speed), (Math.random() > 0.5 ? (int) (Math.random() * 2) + 1 : (int) (Math.random() * 2) - 2)};
            positionPaddles();
        }
        if (rPaddleTarget[1] != rPaddlePos && players < 2) {
            if (rPaddleTarget[0] == 0) {
                if (rPaddleTarget[1] < rPaddlePos) {
                    if (paddleSpeed >= 100) rPaddlePos = rPaddleTarget[1];
                    else rPaddlePos -= (10 * paddleSpeed) / (200 - speed);
                }
                else rPaddlePos = rPaddleTarget[1];
            } else if (rPaddleTarget[0] == 1) {
                if (rPaddleTarget[1] > rPaddlePos) {
                    if (paddleSpeed >= 100) rPaddlePos = rPaddleTarget[1];
                    else rPaddlePos += (10 * paddleSpeed) / (200 - speed);
                }
                else rPaddlePos = rPaddleTarget[1];
            }
        }
        if (lPaddleTarget[1] != lPaddlePos && players == 0) {
            if (lPaddleTarget[0] == 0) {
                if (lPaddleTarget[1] < lPaddlePos) {
                    if (paddleSpeed >= 100) lPaddlePos = lPaddleTarget[1];
                    else lPaddlePos -= (10 * paddleSpeed) / (200 - speed);
                }
                else lPaddlePos = lPaddleTarget[1];
            } else if (lPaddleTarget[0] == 1) {
                if (lPaddleTarget[1] > lPaddlePos) {
                    if (paddleSpeed >= 100) lPaddlePos = lPaddleTarget[1];
                    else lPaddlePos += (10 * paddleSpeed) / (200 - speed);
                }
                else lPaddlePos = lPaddleTarget[1];
            }
        }
        ballPos[0] += ballVelocity[0];
        ballPos[1] += ballVelocity[1];
    }

    private void positionPaddles() {
        int[] pos = calculatePosition();
        if (pos[0] < width / 2) {
            lPaddlePos = players == 0 ? pos[1] - (int) (Math.random() * (height / 5)) + height / 64 : 2 * height / 5;
            rPaddlePos = 2 * height / 5;
        } else {
            lPaddlePos = 2 * height / 5;
            rPaddlePos = players < 2 ? pos[1] - (int) (Math.random() * (height / 5)) + height / 64 : 2 * height / 5;
        }
    }
}
