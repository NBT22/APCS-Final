import javax.swing.JFrame;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create a new window using the Swing library
        JFrame frame = new JFrame("Hello World!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        // Set the window's size
        frame.setSize(400, 400);
        // Set the background color of the window to black
        frame.getContentPane().setBackground(java.awt.Color.BLACK);
        // Draw a white rectangle in the middle of the window
        frame.getContentPane().add(new Container() {
            public void paint(Graphics g) {
                g.setColor(java.awt.Color.WHITE);
                g.fillRect(0, 100, 50, 200);
            }
        });

    }
}