import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Main {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int width = (int)screenSize.getWidth() / 3;
    public static int height = (int)screenSize.getHeight() / 3;
    private static int lPaddlePos = height / 3;
    private static int rPaddlePos = height / 3;
    private static int[] ballPos = new int[] {width / 2 - height / 32, height / 2 - height / 32};
    private static int[] score = new int[] {0, 0};
    public static void main(String[] args) {
        // Create a new window using the Swing library
        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                System.out.println(e.getKeyCode());
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        ballPos[1] -= 10;
                        break;
                    case KeyEvent.VK_DOWN:
                        ballPos[1] += 10;
                        break;
                    case KeyEvent.VK_LEFT:
                        ballPos[0] -= 10;
                        break;
                    case KeyEvent.VK_RIGHT:
                        ballPos[0] += 10;
                        break;
                }
            }
        });
        gameLoop(frame);
    }
    public static void drawScreen(JFrame frame) {
        frame.getContentPane().add(new Container() {
            public void paint(Graphics g) {
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB) {
                    {
                        Graphics2D g = createGraphics();
                        g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
                        g.setColor(Color.WHITE);
                        g.setFont(g.getFont().deriveFont(70f));

                        g.fillRect(Main.width / 200, lPaddlePos, Main.width / 40, Main.height / 3);
                        g.fillRect(Main.width - Main.width / 200 - Main.width / 40, rPaddlePos, Main.width / 40, Main.height / 3);
                        g.fillOval(ballPos[0], ballPos[1], Main.height / 16, Main.height / 16);
                        g.drawString(score[0] + "   –   " + score[1], Main.width / 2 - g.getFontMetrics().stringWidth(score[0] + "   –   " + score[1]) / 2, 50);
                    }
                };
                g.drawImage(img, 0, 0, null);
            }
        });
        frame.repaint();
    }
    public static void gameLoop(JFrame frame) {
        while (true) {
            drawScreen(frame);
        }
    }
}

class Game {

}