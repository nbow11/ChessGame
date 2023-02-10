package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ControlsAndButtons extends JPanel implements ActionListener {
    private JButton greenButton;
    private JButton brownButton;
    private JButton purpleButton;
    private JButton blueButton;
    private JButton resetBoard;
    private JButton flipBoardButton;
    private JButton saveGameButton;

    private JButton takeBackMove;

    Label changeColourLabel;

    Label timerLabel;
    Label flipBoardLabel;

    Label takeBack;

    Color lighterGrey = new Color(53, 59, 72);
    Color darkGrey = new Color(	34, 47, 62);
    Color electricBlue = new Color(89,203,232);

    Color brown = new Color(181, 136, 99);

    Window window;

    private BufferedImage img;
    private Graphics2D g2;

    String colourForBoard = "Brown";

    public ControlsAndButtons() throws IOException, FontFormatException {

        setSize(640, 82);
        setLayout(null);
        setVisible(true);
        setFlipBoard();
        setBackground(lighterGrey);
        setSaveGameButton();
        setFlipBoardLabel();
        setTakeBackMove();
        setTakeBackMoveLabel();

        add(addChangeColour());
//        add(addTimerLabel());

        add(setBlueButton());
        add(setGreenButton());
        add(setBrownButton());
        add(setPurpleButton());
        add(setResetBoard());
        add(this.flipBoardButton);
        add(this.saveGameButton);
        add(this.flipBoardLabel);
        add(this.takeBackMove);
        add(this.takeBack);

        img = new BufferedImage(640, 82, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) img.getGraphics();

    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(brown);
        g2d.drawRoundRect(120, 12, 400, 60, 2, 2);
        g2d.setColor(darkGrey);
        g2d.fillRoundRect(120, 12, 400, 60, 2, 2);

    }

    public JButton setBlueButton() {

        ImageIcon blue = new ImageIcon("/Users/nathanbow/Desktop/ChessJava/ChessClone/src/GUI/Images/blueS.png");
        this.blueButton = new JButton(blue);
        this.blueButton.setBounds(270, 20, 45, 45);
        this.blueButton.addActionListener(this);
        return this.blueButton;
    }

    public JButton setGreenButton() {
        ImageIcon green = new ImageIcon("/Users/nathanbow/Desktop/ChessJava/ChessClone/src/GUI/Images/greenS.png");
        this.greenButton = new JButton(green);
        this.greenButton.setBounds(330, 20, 45, 45);
        this.greenButton.addActionListener(this);
        this.greenButton.setFocusable(false);
        return this.greenButton;
    }

    public JButton setBrownButton() {
        ImageIcon brown = new ImageIcon("/Users/nathanbow/Desktop/ChessJava/ChessClone/src/GUI/Images/brownS.png");
        this.brownButton = new JButton(brown);
        this.brownButton.setBounds(390, 20, 45, 45);
        this.brownButton.addActionListener(this);
        this.brownButton.setFocusable(false);
        return this.brownButton;
    }

    public JButton setPurpleButton() {
        ImageIcon purple = new ImageIcon("/Users/nathanbow/Desktop/ChessJava/ChessClone/src/GUI/Images/purpleS.png");
        this.purpleButton = new JButton(purple);
        this.purpleButton.setBounds(450, 20, 45, 45);
        this.purpleButton.addActionListener(this);
        this.purpleButton.setFocusPainted(false);
        this.purpleButton.setBackground(Color.black);
        return this.purpleButton;
    }

    private JButton setResetBoard() {
        this.resetBoard = new JButton("reset game");
        this.resetBoard.setBounds(10, 45, 100, 25);
        this.resetBoard.addActionListener(this);
        this.resetBoard.setFocusPainted(false);
        this.resetBoard.setBackground(Color.black);
        return this.resetBoard;
    }

    private void setSaveGameButton() {
        this.saveGameButton = new JButton("save game");
        this.saveGameButton.setBounds(10, 15, 100, 25);
        this.saveGameButton.addActionListener(this);
        this.saveGameButton.setFocusPainted(false);
        this.saveGameButton.setBackground(Color.black);
    }

    private void setFlipBoard() {
        this.flipBoardButton = new JButton(
                new ImageIcon("/Users/nathanbow/Desktop/ChessJava/ChessClone/src/GUI/Images/flipB.png")
        );
        this.flipBoardButton.setBounds(527, 10, 50, 50);
        this.flipBoardButton.setFocusPainted(false);
        this.flipBoardButton.addActionListener(this);
        this.flipBoardButton.setVisible(true);
    }

    private void setFlipBoardLabel() throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("coolvetica.otf"));
        this.flipBoardLabel = new Label("flip board");
        this.flipBoardLabel.setBounds(527, 60, 55, 15);
        this.flipBoardLabel.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13));
        this.flipBoardLabel.setForeground(Color.WHITE);
        this.flipBoardLabel.setBackground(lighterGrey);
    }

    private void setTakeBackMove() {
        this.takeBackMove = new JButton(
                new ImageIcon("/Users/nathanbow/Desktop/ChessJava/ChessClone/src/GUI/Images/bacK.png")
        );
        this.takeBackMove.setBounds(587, 10, 50, 50);
        this.takeBackMove.setFocusPainted(false);
        this.takeBackMove.addActionListener(this);
        this.takeBackMove.setVisible(true);
    }

    private void setTakeBackMoveLabel() throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("coolvetica.otf"));
        this.takeBack = new Label("take back");
        this.takeBack.setBounds(587, 60, 60, 15);
        this.takeBack.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13));
        this.takeBack.setForeground(Color.WHITE);
        this.takeBack.setBackground(lighterGrey);
    }

    public Label addChangeColour() throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("coolvetica.otf"));
        this.changeColourLabel = new Label("change colour:");
        // x = 230, y = 12
        this.changeColourLabel.setBounds(140, 27, 110, 25);
        this.changeColourLabel.setFont(font.deriveFont(Font.TRUETYPE_FONT, 17));
        this.changeColourLabel.setForeground(Color.WHITE);
        this.changeColourLabel.setBackground(darkGrey);

        return changeColourLabel;
    }

    public Label addTimerLabel() {
        this.timerLabel = new Label("Timer");
        this.timerLabel.setBounds(500, 10, 300, 25);
        this.timerLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
        this.timerLabel.setForeground(Color.DARK_GRAY);

        return timerLabel;
    }

    public void setColourForBoard(String colour) {
        colourForBoard = colour;
    }

    public String getColourForBoard() { return colourForBoard; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.blueButton) {
            setColourForBoard("Blue");
//            getWindow().getGameBoard().getCurrentBoard().movePiece("e2", "e4");
            getWindow().destroyAndRedrawBoard();
        } else if (e.getSource() == this.greenButton) {
            setColourForBoard("Green");
//            getWindow().getGameBoard().getCurrentBoard().movePiece("g1", "f3");
            getWindow().destroyAndRedrawBoard();
        } else if (e.getSource() == this.brownButton) {
            setColourForBoard("Brown");
            getWindow().destroyAndRedrawBoard();
        } else if (e.getSource() == this.purpleButton) {
            setColourForBoard("Purple");
            getWindow().destroyAndRedrawBoard();
        } else if (e.getSource() == this.resetBoard) {
            getWindow().resetBoard();
            // FIX THIS
            try {
                getWindow().destroyAndRedrawControlPanel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (FontFormatException ex) {
                throw new RuntimeException(ex);
            }
            getWindow().revalidate();
            getWindow().repaint();
        } else if (e.getSource() == this.flipBoardButton) {
            getWindow().reversePerspective();
            getWindow().destroyAndRedrawBoard();
            getWindow().revalidate();
            getWindow().repaint();
        } else if (e.getSource() == this.takeBackMove) {
            getWindow().getGameBoard().getCurrentBoard().stepBackMove();

            // save previous moves
            var previousMoves = getWindow().getGameBoard().getCurrentBoard().getMoveStack();
            getWindow().destroyAndRedrawBoard();
            getWindow().getGameBoard().getCurrentBoard().setMoveStack(previousMoves);
            getWindow().revalidate();
            getWindow().repaint();
        }
    }

    public void setWindow(Window window) { this.window = window; }

    public Window getWindow() { return this.window; }

}
