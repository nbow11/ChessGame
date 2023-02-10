package GUI;

import ChessBoard.Board;
import PieceRelated.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TimerAndMoves extends JPanel implements ActionListener {

    Image img;
    private Graphics2D g2;

    private Label whiteTimer;

    private Label selectLabel;

    private Label moveToLabel;

    private Label playerToMove;

    private boolean whiteToMove = true;
    Font coolvetica = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("coolvetica.otf"));

    Window window;

    JTextField selectPieceField;
    JTextField movePieceField;

    String selectedSquare;
    String moveToSquare;

    JButton submitButton;

    JButton resetButton;

    Color darkGrey = new Color(	34, 47, 62);
    Color lighterGrey = new Color(53, 59, 72);
    Color electricBlue = new Color(89,203,232);

    public TimerAndMoves() throws IOException, FontFormatException {
        setSize(260, 750);
        setLayout(null);
        setVisible(true);
        setBackground(lighterGrey);
        setSelectPieceField();
        setMovePieceField();
        setSubmitButton();
        setResetButton();
        addSelectPieceLabel();
        addMoveToLabel();
        setPlayerToMoveLabel();
        add(this.selectPieceField);
        add(this.movePieceField);
        add(getSubmitButton());
        add(this.selectLabel);
        add(this.moveToLabel);
        add(this.playerToMove);
//        this.add(this.resetButton);
//        add(addWhiteTimer());

        img = new BufferedImage(260, 750, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) img.getGraphics();
    }

    private void setSelectPieceField() {
        this.selectPieceField = new JTextField();
        this.selectPieceField.setBounds(180, 275, 40, 25);
    }

    private void setMovePieceField() {
        this.movePieceField = new JTextField();
        this.movePieceField.setBounds(180, 305, 40, 25);
    }

    private void setSubmitButton() {
        this.submitButton = new JButton("submit");
        this.submitButton.setBounds(80, 340, 100, 25);
        this.submitButton.addActionListener(this);
    }

    private void setResetButton() {
        this.resetButton = new JButton("reset");
        this.resetButton.setBounds(80, 370, 100, 25);
        this.resetButton.addActionListener(this);
    }

    private void setPlayerToMoveLabel() {
        this.playerToMove = new Label();

        if (getWhiteToMove()) {
            this.playerToMove = new Label("WHITE TO MOVE");
        } else {
            this.playerToMove = new Label("BLACK TO MOVE");
        }

        this.playerToMove.setBounds(40, 665, 200, 30);
        this.playerToMove.setFont(coolvetica.deriveFont(Font.TRUETYPE_FONT, 25));
        this.playerToMove.setForeground(Color.WHITE);
        this.playerToMove.setBackground(lighterGrey);
    }

    private JButton getSubmitButton() { return this.submitButton; }

    private JTextField getSelectPieceField() { return this.selectPieceField; }

    private JTextField getMovePieceField() { return this.movePieceField; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getSubmitButton()) {
            selectedSquare = getSelectPieceField().getText();
            moveToSquare = getMovePieceField().getText();

            getSelectPieceField().setText("");
            getMovePieceField().setText("");

            movePiece();
            getWindow().destroyAndRedrawBoard();
            setWhiteToMove(!getWhiteToMove());
            this.remove(getPlayerToMove());
            setPlayerToMoveLabel();
            this.add(getPlayerToMove());
        }
    }

    private String getSelectedSquare() { return this.selectedSquare; }

    private String getMoveToSquare() { return this.moveToSquare; }

    private void movePiece() {
        getWindow().getGameBoard().getCurrentBoard().movePiece(getSelectedSquare(), getMoveToSquare());
    }

    public Label addWhiteTimer() throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("coolvetica.otf"));
        this.whiteTimer = new Label("white");
        this.whiteTimer.setBounds(30, 550, 200, 25);
        this.whiteTimer.setFont(font.deriveFont(Font.TRUETYPE_FONT, 20));
        this.whiteTimer.setForeground(Color.WHITE);
        this.whiteTimer.setBackground(darkGrey);

        return this.whiteTimer;
    }

    private void addSelectPieceLabel() throws IOException, FontFormatException {
        this.selectLabel = new Label("select piece at: ");
        this.selectLabel.setBounds(40, 270, 200, 30);
        this.selectLabel.setFont(coolvetica.deriveFont(Font.TRUETYPE_FONT, 20));
        this.selectLabel.setForeground(Color.WHITE);
        this.selectLabel.setBackground(darkGrey);
    }

    private void addMoveToLabel() throws IOException, FontFormatException {
        this.moveToLabel = new Label("move piece to: ");
        this.moveToLabel.setBounds(40, 300, 200, 30);
        this.moveToLabel.setFont(coolvetica.deriveFont(Font.TRUETYPE_FONT, 20));
        this.moveToLabel.setForeground(Color.WHITE);
        this.moveToLabel.setBackground(darkGrey);
    }

    private void addPlayerToMoveLabel() {
        //add label like 'white to move' after each move
    }

    protected void paintComponent(Graphics g) {
        Color brown = new Color(181, 136, 99);
        Color lightBrown = new Color(240, 217, 181);
        Color electricBlue = new Color(89,203,232);

        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(brown);
        g2d.drawRoundRect(18, 25, 225, 600, 2, 2);
        g2d.setColor(darkGrey);
        g2d.fillRoundRect(18, 25, 225, 600, 2, 2);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(brown);
        g2d.drawRect(24, 33, 212, 115);
        g2d.setColor(new Color(240, 217, 181));
        g2d.fillRect(24, 33, 212, 115);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(brown);
        g2d.drawRect(24, 503, 212, 115);
        g2d.setColor(new Color(240, 217, 181));
        g2d.fillRect(24, 503, 212, 115);

//        drawWhiteTakenArea(g2d);


        addWhitePiecesTaken(g);
        addBlackPiecesTaken(g);

    }

    private void drawWhiteTakenArea(Graphics2D g2d) {
        Color brown = new Color(181, 136, 99);
        Color lightBrown = new Color(240, 217, 181);

//        g2d.setStroke(new BasicStroke(2));
//        g2d.setColor(new Color(89,203,232));
//        g2d.drawRect(25, 540, 212, 75);
//        g2d.setColor(lightBrown);
//        g2d.fillRect(25, 540, 212, 75);

        int x = 26;
        int y = 440;
        int level = 1;

//        g2d.drawRect(x, 540, 40, 40);
//        g2d.setColor(lightBrown);
//        g2d.fillRect(x, 540, 40, 40);
//
//        g2d.setStroke(new BasicStroke(2));
//        g2d.setColor(brown);
//        g2d.drawRect(x + 42, 540, 40, 40);
//        g2d.setColor(brown);
//        g2d.fillRect(x + 42, 540, 40, 40);
//
//        g2d.setStroke(new BasicStroke(2));
//        g2d.setColor(lightBrown);
//        g2d.drawRect(x + 84, 540, 40, 40);
//        g2d.setColor(lightBrown);
//        g2d.fillRect(x + 84, 540, 40, 40);

        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(brown);
                g2d.drawRect(x, y, 40, 40);
                g2d.setColor(brown);
                g2d.fillRect(x, y, 40, 40);
            } else {
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(lightBrown);
                g2d.drawRect(x, y, 40, 40);
                g2d.setColor(lightBrown);
                g2d.fillRect(x, y, 40, 40);
            }

            x += 42;
        }

        int newX = 26;

        for (int i = 0; i < 5; i++) {
            if (i % 2 != 0) {
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(brown);
                g2d.drawRect(newX, y + 42, 40, 40);
                g2d.setColor(brown);
                g2d.fillRect(newX, y + 42, 40, 40);
            } else {
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(lightBrown);
                g2d.drawRect(newX, y + 42, 40, 40);
                g2d.setColor(lightBrown);
                g2d.fillRect(newX, y + 42, 40, 40);
            }

            x += 42;
        }

//        int other = 55;
//        for (int i = 0; i < 8; i++) {
//            if (i % 2 != 0) {
//                g2d.drawRect(other, 540, 30, 30);
//                g2d.setColor(lightBrown);
//                g2d.fillRect(other, 540, 30, 30);
//            }
//            other += 30;
//        }
    }

    public void setWindow(Window window) { this.window = window; }

    public Window getWindow() { return this.window; }

    private boolean getWhiteToMove() { return this.whiteToMove; }

    private void setWhiteToMove(boolean b) { this.whiteToMove = b; }

    private Label getPlayerToMove() { return this.playerToMove; }

    private Board getCurrentBoard() {
        return getWindow().getGameBoard().getCurrentBoard();
    }

    private GameBoardGUI getGameBoardGUI() {
        return getWindow().getGameBoard();
    }

    public void addWhitePiecesTaken(Graphics g) {
        var piecesTaken = getCurrentBoard().getPiecesTakenWhite();

        int x = 21;
        int y = 30;
        int count = 0;

        for (Piece piece : piecesTaken) {
            var pieceImage = getGameBoardGUI().getWhitePieceImage(piece.getType());
            Image resized = pieceImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT);
            g.drawImage(resized, x, y, null);

            count++;
            x += 30;

            if (count == 7 || count == 14) {
                y += 40;
                x = 21;
            }
        }
    }

    private void addBlackPiecesTaken(Graphics g) {
        var piecesTaken = getCurrentBoard().getPiecesTakenBlack();

        int x = 21;
        int y = 500;
        int count = 0;

        for (Piece piece : piecesTaken) {
            var pieceImage = getGameBoardGUI().getBlackPieceImage(piece.getType());
            Image resized = pieceImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT);
            g.drawImage(resized, x, y, null);

            count++;
            x += 30;

            if (count == 7 || count == 14) {
                y += 40;
                x = 21;
            }
        }
    }

}
