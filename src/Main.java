import javax.swing.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();
        // Create a new window using the Swing library
        JFrame frame = new JFrame("Hello World!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Set the background color of the window to black
        frame.getContentPane().setBackground(Color.BLACK);
        // Draw a white rectangle in the middle of the window
        frame.getContentPane().add(new Container() {
            public void paint(Graphics g) {
                ((Graphics2D)g).addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
//                g.setColor(Color.WHITE);
                g.fillRect(width / 200, height / 3, width / 40, height / 3);
                g.fillRect(width - width / 200 - width / 40, height / 3, width / 40, height / 3);
                g.fillOval(width / 2 - height / 32, height / 2 - height / 32, height / 16, height / 16);  // ball
                g.setFont(g.getFont().deriveFont(70f));
                g.drawString("0   –   0", width / 2 - g.getFontMetrics().stringWidth("0   –   0") / 2, 50);
            }
        });
    }
}