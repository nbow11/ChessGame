package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WelcomeWindow extends JPanel implements ActionListener {

    Label welcomeLabel;
    JButton newGameButton;

    JButton loadGameButton;

    Window window;

    Color darkGrey = new Color(	34, 47, 62);
    Color lighterGrey = new Color(53, 59, 72);
    Color electricBlue = new Color(89,203,232);
    private BufferedImage img;
    private Graphics2D g2;


    public WelcomeWindow() throws IOException, FontFormatException {
        setSize(900, 750);
        setLayout(null);
        setVisible(true);
        setBackground(lighterGrey);

        setNewGameButton();
        add(this.newGameButton);

        setLoadGameButton();
        add(this.loadGameButton);

        setWelcomeLabel();
        add(this.welcomeLabel);

        img = new BufferedImage(640, 82, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) img.getGraphics();
    }

    public void setWindow(Window window) { this.window = window; }

    public Window getWindow() { return this.window; }

    private void setWelcomeLabel() throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("coolvetica.otf"));
        this.welcomeLabel = new Label("welcome to chess");
        this.welcomeLabel.setBounds(336, 300, 227, 25);
        this.welcomeLabel.setFont(font.deriveFont(Font.TRUETYPE_FONT, 30));
        this.welcomeLabel.setForeground(Color.WHITE);
        this.welcomeLabel.setBackground(darkGrey);
    }

    private void setNewGameButton() {
        this.newGameButton = new JButton("new game");
        this.newGameButton.setBounds(345, 350, 100, 45);
        this.newGameButton.addActionListener(this);
        this.newGameButton.setFocusable(false);
    }

    private void setLoadGameButton() {
        this.loadGameButton = new JButton("load game");
        this.loadGameButton.setBounds(450, 350, 100, 45);
        this.loadGameButton.addActionListener(this);
        this.loadGameButton.setFocusable(false);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(electricBlue);
        g2d.drawRoundRect(20, 20, 860, 680, 2, 2);
        g2d.setColor(darkGrey);
        g2d.fillRoundRect(20, 20, 860, 680, 2, 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.newGameButton) {
            getWindow().addGameWindow();
        }
    }
}
