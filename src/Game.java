import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public int height = (int) screenSize.getHeight();
    public int width = (int) screenSize.getWidth();
    private int lPaddlePos = 2 * height / 5;
    private int rPaddlePos = 2 * height / 5;
    private int[] ballPos = new int[]{width / 2 - height / 64, height / 2 - height / 64};
    //    private double[] ballVelocity = new double[] {(Math.random() > 0.5 ? 5 : -5), (Math.random() > 0.5 ? (int)(Math.random() * 2) + 1 : (int)(Math.random() * 2) - 2)};
    private double[] ballVelocity = new double[]{-5, 0};
    private int speed = 5;

    private int[] score = new int[]{0, 0};
    private JFrame frame;
    private JPanel panel;

    public Game() {
        frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.pack();
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
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        lPaddlePos -= 10;
                        break;
                    case KeyEvent.VK_DOWN:
                        lPaddlePos += 10;
                        break;
                    case KeyEvent.VK_PAGE_UP:
                    case KeyEvent.VK_W:
                        rPaddlePos -= 10;
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                    case KeyEvent.VK_S:
                        rPaddlePos += 10;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                }
            }
        });
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                panel.repaint();
                if (score[0] == 10 || score[1] == 10) timer.cancel();
            }
        }, 0, 10);
    }

    /**
     * Uses a racast to calculate the position of the ball when it gets to the other side of the screen
     */
    private double[] calculatePosition() {
        double[] pos = new double[]{ballPos[0] + ballVelocity[0], ballPos[1] + ballVelocity[1]};
        while (pos[0] < width - height / 64 - height / 32 && pos[0] > height / 64) {
            pos[0] += ballVelocity[0];
            pos[1] += pos[1] > height - height / 32 || pos[1] < 0 ? -ballVelocity[1] : ballVelocity[1];
        }
        return pos;
    }

    private void update() {
        if (ballPos[1] < 0 || ballPos[1] > height - height / 32) {
            ballVelocity[1] = -ballVelocity[1];
        }
        if ((ballPos[0] <= height / 64) && ballPos[1] > lPaddlePos - height / 32 && ballPos[1] < lPaddlePos + height / 5) {
            double angle = Math.toRadians(-(90 + ((((ballPos[1] - lPaddlePos + height / 64) / (height / 5d)) - 0.5) * 90)));
            ballVelocity[0] = -speed * Math.sin(angle);
            ballVelocity[1] = -speed * Math.cos(angle);
            if (speed < 100) speed++;
            System.out.println(Arrays.toString(ballPos));
            System.out.println(Arrays.toString(calculatePosition()));
        }
        else if (ballPos[0] >= width - height / 64 - height / 32 && ballPos[1] > rPaddlePos - height / 32 && ballPos[1] < rPaddlePos + height / 5) {
            double angle = Math.toRadians(-(90 + ((((ballPos[1] - rPaddlePos + height / 64) / (height / 5d)) - 0.5) * 90)));
            ballVelocity[0] = speed * Math.sin(angle);
            ballVelocity[1] = -speed * Math.cos(angle);
            if (speed < 100) speed++;
            System.out.println(Arrays.toString(ballPos));
            System.out.println(Arrays.toString(calculatePosition()));
        }
        else if (ballPos[0] < 0) {
            score[1]++;
            ballPos = new int[]{width / 2 - height / 32, height / 2 - height / 32};
            speed = 5;
            ballVelocity = new double[]{-speed, (Math.random() > 0.5 ? (int) (Math.random() * 2) + 1 : (int) (Math.random() * 2) - 2)};
        }
        else if (ballPos[0] > width - height / 32) {
            score[0]++;
            ballPos = new int[]{width / 2 - height / 32, height / 2 - height / 32};
            ballVelocity = new double[]{speed, (Math.random() > 0.5 ? (int) (Math.random() * 2) + 1 : (int) (Math.random() * 2) - 2)};
            speed = 5;
        }
        ballPos[0] += ballVelocity[0];
        ballPos[1] += ballVelocity[1];
    }
}
