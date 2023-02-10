package GUI;

import ChessBoard.Square;
import PieceRelated.Piece;
import PieceRelated.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;

public class Window extends JFrame implements ActionListener {

    private GameBoardGUI gameBoard;
    private ControlsAndButtons controlPanel;

    private WelcomeWindow welcomeWindow;

    private Square[][] savedPieces;

    private boolean isWhite;

    private boolean newGameWindowOpen = true;

    private TimerAndMoves timerAndMoves;
    private JButton greenButton = new JButton();
    private JButton brownButton = new JButton();
    private JButton purpleButton = new JButton();
    private JButton blueButton = new JButton();

    String colourForBoard = "Brown";
    Label changeColourLabel;

    public Window() throws IOException, FontFormatException {

        addChangeColour();
        setControlPanel();
        setGameBoard();
        getGameBoard().getCurrentBoard().setWindow(this);

        setTimerAndMoves();

        setWelcomeWindow();

        getControlPanel().setBounds(0, 640, 640, 82);
        getTimerAndMoves().setBounds(640, 0, 260, 750);
        getGameBoard().setBounds(0, 0, 640, 640);
        getWelcomeWindow().setBounds(0, 0, 900, 750);

//        add(getWelcomeWindow());
        add(getControlPanel());
        add(getTimerAndMoves());
        add(getGameBoard());

        JButton newGame = new JButton();
        newGame.setBounds(230, 660, 200, 50);
        newGame.setBackground(Color.lightGray);
        newGame.setVisible(true);
        newGame.setText("New Game");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLayout(null);
        setVisible(true);
//        add(newGame);
//        add(this.changeColourLabel);
//        add(setBlueButton());
//        add(setGreenButton());
//        add(setBrownButton());
//        add(setPurpleButton());
    }


    public void setColourForBoard(String colour) {
        this.colourForBoard = colour;
        destroyAndRedrawBoard();
    }

    public void addGameWindow() {
        getContentPane().removeAll();
        add(getControlPanel());
        add(getTimerAndMoves());
        add(getGameBoard());
        // need to repaint
        repaint();
    }

    public boolean getNewGameWindowOpen() { return this.newGameWindowOpen; }

    public void setNewGameWindowOpen() { this.newGameWindowOpen = false; }

    public void destroyNewGameWindow() { this.remove(welcomeWindow); }

//    public void addActionListeners() {
//        greenButton.addActionListener(e -> setColourForBoard("Green"));
//    }

    public void setGameBoard() {
        this.gameBoard = new GameBoardGUI(getControlPanel().getColourForBoard());

        if (getSavedPieces() != null) {
            getGameBoard().getCurrentBoard().setPiecesSave(getSavedPieces());

            if (!getIsWhite()) {
                getGameBoard().reversePerspective();
            }

        }

        this.gameBoard.getCurrentBoard().setWindow(this);
        this.gameBoard.setWindow(this);
        this.gameBoard.drawBoard();
    }

    public void reversePerspective() { this.isWhite = !this.isWhite; }

    public boolean getIsWhite() { return this.isWhite; }

    public Square[][] getSavedPieces() { return this.savedPieces; }

    public void setControlPanel() throws IOException, FontFormatException {
        this.controlPanel = new ControlsAndButtons();
        getControlPanel().setWindow(this);
    }

    private void setWelcomeWindow() throws IOException, FontFormatException {
        this.welcomeWindow = new WelcomeWindow();
        this.welcomeWindow.setWindow(this);
    }

    private WelcomeWindow getWelcomeWindow() { return this.welcomeWindow; }

    public void setTimerAndMoves() throws IOException, FontFormatException {
        this.timerAndMoves = new TimerAndMoves();
        getTimerAndMoves().setWindow(this);
    }

    public GameBoardGUI getGameBoard() {
        return this.gameBoard;
    }

    public ControlsAndButtons getControlPanel() { return this.controlPanel; }

    public TimerAndMoves getTimerAndMoves() { return this.timerAndMoves; }

    public String getColourForBoard() {
        return this.colourForBoard;
    }

    public JButton setBlueButton() {
        this.blueButton = new JButton("Blue");
        this.blueButton.setBounds(10, 683, 60, 25);
        this.blueButton.addActionListener(this);
        this.blueButton.setFocusable(false);
        return this.blueButton;
    }

    public JButton setGreenButton() {
        this.greenButton = new JButton("Green");
        this.greenButton.setBounds(65, 683, 60, 25);
        this.greenButton.addActionListener(this);
        this.greenButton.setFocusable(false);
        return this.greenButton;
    }

    public JButton setBrownButton() {
        this.brownButton = new JButton("Brown");
        this.brownButton.setBounds(120, 683, 70, 25);
        this.brownButton.addActionListener(this);
        this.brownButton.setFocusable(false);
        return this.brownButton;
    }

    public JButton setPurpleButton() {
        this.purpleButton = new JButton("Purple");
        this.purpleButton.setBounds(186, 683, 70, 25);
        this.purpleButton.addActionListener(this);
        this.purpleButton.setFocusable(false);
        return this.purpleButton;
    }

    public Label addChangeColour() {
        this.changeColourLabel = new Label("Change board colour");
        this.changeColourLabel.setBounds(35, 653, 300, 25);
        this.changeColourLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
        this.changeColourLabel.setForeground(Color.DARK_GRAY);

        return changeColourLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.blueButton) {
            destroyAndRedrawBoard();
//            setColourForBoard("Blue");
        } else if (e.getSource() == this.greenButton) {
            destroyAndRedrawBoard();
//            setColourForBoard("Green");
        } else if (e.getSource() == this.brownButton) {
            destroyAndRedrawBoard();
//            setColourForBoard("Brown");
        } else if (e.getSource() == this.purpleButton) {
            destroyAndRedrawBoard();
//            setColourForBoard("Purple");
        }

//        destroyAndRedrawBoard();
//        this.remove(this.gameBoard);
//        setGameBoard();
//
////        getGameBoard().getCurrentBoard().movePiece("e2", "e4");
//
//        this.add(getGameBoard());
//        this.revalidate();
//        this.repaint();

    }

    public void setSavedPieces(Square[][] squares) {
        this.savedPieces = squares;
    }

    public void resetBoard() {
        setSavedPieces(null);
        this.remove(this.gameBoard);
        setGameBoard();
        this.add(getGameBoard());

        this.revalidate();
        this.repaint();
    }

    public void destroyAndRedrawBoard() {
        // save pieces after changing colour
        setSavedPieces(getGameBoard().getCurrentBoard().getSquares());
        this.remove(this.gameBoard);
        setGameBoard();
        this.add(getGameBoard());
        this.revalidate();
        this.repaint();
    }

    public void destroyAndRedrawTimerAndMoves() throws IOException, FontFormatException {
        this.remove(this.timerAndMoves);
        setTimerAndMoves();
        getTimerAndMoves().setBounds(640, 0, 260, 750);
        getTimerAndMoves().setWindow(this);
        this.add(getTimerAndMoves());
        this.revalidate();
        this.repaint();
    }

    public void destroyAndRedrawControlPanel() throws IOException, FontFormatException {
        this.remove(this.controlPanel);
        setControlPanel();
        getControlPanel().setBounds(0, 640, 640, 82);
        getControlPanel().setWindow(this);
        this.add(getControlPanel());
        this.revalidate();
        this.repaint();
    }

    private void repaintWindow() {
        addChangeColour();
//        setControlPanel();
//        setTimerAndMoves();
//        setWelcomeWindow();

//        getControlPanel().setBounds(0, 640, 640, 82);
//        getTimerAndMoves().setBounds(640, 0, 260, 750);
        getGameBoard().setBounds(0, 0, 640, 640);
//        getWelcomeWindow().setBounds(0, 0, 900, 750);

//        add(getWelcomeWindow());
//        add(getControlPanel());
//        add(getTimerAndMoves());
        add(getGameBoard());

        JButton newGame = new JButton();
        newGame.setBounds(230, 660, 200, 50);
        newGame.setBackground(Color.lightGray);
        newGame.setVisible(true);
        newGame.setText("New Game");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLayout(null);
        setVisible(true);
    }

}
