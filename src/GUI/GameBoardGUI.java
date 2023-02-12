package GUI;

import ChessBoard.Board;
import ChessBoard.Square;
import PieceRelated.Colour;
import PieceRelated.Piece;
import PieceRelated.PieceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//
public class GameBoardGUI extends JPanel implements ActionListener, MouseListener {

    private int clickCount = 1;
    private int[] currentPos;
    private int[] newPos;

    private Board currentBoard;
    private String colour;

    private boolean whitePerspective = true;

    HashMap<String, Color[]> colours = new HashMap<>();

    int boardWidth = 640;
    int boardHeight = 668;

    int tileSize = 80;
    int countX = 0;
    int countY = 0;
    private BufferedImage img;
    private Graphics2D g2;

    private Window window;

    Image whitePawn;
    Image whiteRook;
    Image whiteBishop;
    Image whiteKnight;
    Image whiteQueen;
    Image whiteKing;
    Image blackPawn;

    Image blackRook;

    Image blackBishop;

    Image blackKnight;

    Image blackQueen;

    Image blackKing;

    public GameBoardGUI(String colour) {
        this.colour = colour;
        addColours();

        setCurrentBoard();

        setSize(640, 668);
        setLayout(null);
        setVisible(true);

        addMouseListener(this);

        img = new BufferedImage(640, 668, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) img.getGraphics();

        int delay = 1000; // milliseconds
        ActionListener taskPerformer = new ActionListener() {
            int timesShown = 0;

            public void actionPerformed(ActionEvent evt) {

                getCurrentBoard().checkCheckMate();

                if (getCurrentBoard().isCheckMate() && timesShown == 0) {
                    showCheckMate();
                    timesShown++;
                };
            }
        };
        new Timer(delay, taskPerformer).start();

        int delayTime = 3000; //milliseconds
        ActionListener playComputer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!getCurrentBoard().isWhiteMove()) {
                    try {
                        getCurrentBoard().computerMove();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (FontFormatException e) {
                        throw new RuntimeException(e);
                    }
                    removeAll();
                    drawSquares();
                    drawBoard();
                    drawCheck();
                    revalidate();
                    repaint();
                }
            }
        };
//        var blackMoves = new Timer(delayTime, playComputer);
//        blackMoves.start();
//
//        if (getCurrentBoard().isCheckMate()) {
//            blackMoves.stop();
//        }
    }

    public void drawBoard() {
        drawSquares();

        if (isWhitePerspective()) {
            drawNums();
            drawLetters();
        } else {
            drawNumsReversed();
            drawLettersReversed();
        }

        loadWhitePawns();
        loadWhiteRooks();
        loadWhiteKing();
        loadWhiteQueen();
        loadWhiteBishops();
        loadWhiteKnights();
        loadBlackPawns();
        loadBlackRooks();
        loadBlackBishops();
        loadBlackKnights();
        loadBlackQueen();
        loadBlackKing();
    }

    public void showCheckMate() {
        JOptionPane.showMessageDialog(this, "Checkmate");
    }

    public void paintComponent(Graphics g) {

        g.drawImage(img, 0, 0, getBoardWidth(), getBoardHeight(), null);
//        drawPieces(g);
        if (isWhitePerspective()) {
            drawPieces(g);
        } else {
            drawPiecesBlackPerspective(g);
        }
//        drawWhitePieces(g);
//        drawBlackPawns(g);

    }

    public void reversePerspective() { this.whitePerspective = !this.whitePerspective; }

    public void setCurrentBoard() { this.currentBoard = new Board(); }

    public Board getCurrentBoard() { return this.currentBoard; }

    private int getBoardWidth() { return this.boardWidth; }

    private int getBoardHeight() { return this.boardHeight; }

    public void addColours() {

        Color green = new Color(118, 150, 86);
        Color lightGreen = new Color(238, 238, 210);

        Color blue = new Color(75, 115, 153);
        Color cream = new Color(234, 233, 210);

        Color purple = new Color(136, 119, 183);
        Color white = new Color(239, 239, 239);

        Color brown = new Color(181, 136, 99);
        Color lightBrown = new Color(240, 217, 181);

        colours.put("Green", new Color[]{green, lightGreen});
        colours.put("Blue", new Color[]{blue, cream});
        colours.put("Purple", new Color[]{purple, white});
        colours.put("Brown", new Color[]{brown, lightBrown});

    }

    public String getBoardColour() { return this.colour; }

    public Color getColour(int index) { return this.colours.get(getBoardColour())[index]; }

    private int getTileSize() { return this.tileSize; }

    private void drawSquares() {

        int countX = 0, countY = 0;

        HashMap<Colour, Color> squareColour = new HashMap<>();
        squareColour.put(Colour.WHITE, getColour(1));
        squareColour.put(Colour.BLACK, getColour(0));

        for (Square[] rank : getCurrentBoard().getSquares()) {

            if (countX == (getTileSize() * 8)) {
                countY += getTileSize();
                countX = 0;
            }

            for (Square square : rank) {
                g2.setColor(squareColour.get(square.getSquareColour()));
                g2.fillRect(countX, countY, getTileSize(), getTileSize());
                countX += getTileSize();
            }
        }

    }

    private void drawPieces(Graphics g) {

        // these positions have to be linked to current positions of the pieces
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {
                Piece currentPiece = square.getCurrentPiece();
                Image currentPieceImage;

                if (currentPiece != null && currentPiece.getColour() == Colour.WHITE) {
                    currentPieceImage = getWhitePieceImage(currentPiece.getType());
                    g.drawImage(currentPieceImage,
                            currentPiece.getXCoordinate() * getTileSize(),
                            currentPiece.getYCoordinate() * getTileSize(),
                            getTileSize(), getTileSize(), null);
                }

                else if (currentPiece != null && currentPiece.getColour() == Colour.BLACK) {
                    currentPieceImage = getBlackPieceImage(currentPiece.getType());
                    g.drawImage(currentPieceImage,
                            currentPiece.getXCoordinate() * getTileSize(),
                            currentPiece.getYCoordinate() * getTileSize(),
                            getTileSize(), getTileSize(), null);
                }
            }
        }
    }

    public void drawPiecesBlackPerspective(Graphics g) {

        var reversedNums = reversedNumbers();

        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {
                Piece currentPiece = square.getCurrentPiece();
                Image currentPieceImage;

                if (currentPiece != null && currentPiece.getColour() == Colour.WHITE) {
                    currentPieceImage = getWhitePieceImage(currentPiece.getType());
                    g.drawImage(currentPieceImage,
                            reversedNums.get(currentPiece.getXCoordinate()) * getTileSize(),
                            reversedNums.get(currentPiece.getYCoordinate()) * getTileSize(),
                            getTileSize(), getTileSize(), null);
                }

                else if (currentPiece != null && currentPiece.getColour() == Colour.BLACK) {
                    currentPieceImage = getBlackPieceImage(currentPiece.getType());
                    g.drawImage(currentPieceImage,
                            reversedNums.get(currentPiece.getXCoordinate()) * getTileSize(),
                            reversedNums.get(currentPiece.getYCoordinate()) * getTileSize(),
                            getTileSize(), getTileSize(), null);
                }

            }
        }
    }

    private HashMap<Integer, Integer> reversedNumbers() {
        HashMap<Integer, Integer> reversed = new HashMap<>();
        reversed.put(7, 0);
        reversed.put(6, 1);
        reversed.put(5, 2);
        reversed.put(4, 3);
        reversed.put(3, 4);
        reversed.put(2, 5);
        reversed.put(1, 6);
        reversed.put(0, 7);
        return reversed;
    }

    public boolean isWhitePerspective() { return this.whitePerspective; }

    public void drawNums() {

        int numCountX = 3;
        int numCountY = 20;
        for (int i = 8; i >= 1; i--) {
            if (i != 8) {
                numCountY += tileSize;
            }
            if (i % 2 == 0) {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(0));
                g2.drawString(Integer.toString(i), numCountX, numCountY);
            } else {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(1));
                g2.drawString(Integer.toString(i), numCountX, numCountY);
            }
        }
    }

    public void drawNumsReversed() {

        int numCountX = 3;
        int numCountY = 20;
        for (int i = 1; i <= 8; i++) {
            if (i != 1) {
                numCountY += tileSize;
            }
            if (i % 2 != 0) {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(0));
                g2.drawString(Integer.toString(i), numCountX, numCountY);
            } else {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(1));
                g2.drawString(Integer.toString(i), numCountX, numCountY);
            }
        }
    }

    private void drawLetters() {

        int lettersCountX = 66;
        int lettersCountY = 635;

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};

        for (int i = 0; i <= 7; i++) {
            if (i != 0) {
                lettersCountX += tileSize;
            }

            if (i % 2 == 0) {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(1));
                g2.drawString(letters[i], lettersCountX, lettersCountY);
            } else {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(0));
                g2.drawString(letters[i], lettersCountX, lettersCountY);
            }

        }

    }

    private void drawLettersReversed() {
        int lettersCountX = 66;
        int lettersCountY = 635;

        String[] letters = {"h", "g", "f", "e", "d", "c", "b", "a"};

        for (int i = 0; i <= 7; i++) {
            if (i != 0) {
                lettersCountX += tileSize;
            }

            if (i % 2 == 0) {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(1));
                g2.drawString(letters[i], lettersCountX, lettersCountY);
            } else {
                Font nums = new Font("Sans Serif", Font.BOLD, 17);
                g2.setFont(nums);
                g2.setColor(getColour(0));
                g2.drawString(letters[i], lettersCountX, lettersCountY);
            }

        }
    }

    private void loadWhitePawns() {
        String path = "src/PieceImages/whitePawn.png";
        File pawnFile = new File(path);
        try {
            whitePawn = ImageIO.read(pawnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWhiteRooks() {
        String path = "src/PieceImages/whiteRook.png";
        File rookFile = new File(path);
        try {
            whiteRook = ImageIO.read(rookFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWhiteQueen() {
        String path = "src/PieceImages/whiteQueen.png";
        File queenFile = new File(path);
        try {
            whiteQueen = ImageIO.read(queenFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWhiteKing() {
        String path = "src/PieceImages/whiteKing.png";
        File kingFile = new File(path);
        try {
            whiteKing = ImageIO.read(kingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWhiteBishops() {
        String path = "src/PieceImages/whiteBishop.png";
        File bishopFile = new File(path);
        try {
            whiteBishop = ImageIO.read(bishopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWhiteKnights() {
        String path = "src/PieceImages/whiteKnight.png";
        File knightFile = new File(path);
        try {
            whiteKnight = ImageIO.read(knightFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackPawns() {
        String path = "src/PieceImages/blackPawn.png";
        File blackPawnFile = new File(path);
        try {
            blackPawn = ImageIO.read(blackPawnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackRooks() {
        String path = "src/PieceImages/blackRook.png";
        File blackRookFile = new File(path);
        try {
            blackRook = ImageIO.read(blackRookFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackKnights() {
        String path = "src/PieceImages/blackKnight.png";
        File blackKnightFile = new File(path);
        try {
            blackKnight = ImageIO.read(blackKnightFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackBishops() {
        String path = "src/PieceImages/blackBishop.png";
        File blackBishopFile = new File(path);
        try {
            blackBishop = ImageIO.read(blackBishopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackQueen() {
        String path = "src/PieceImages/blackQueen.png";
        File blackQueenFile = new File(path);
        try {
            blackQueen = ImageIO.read(blackQueenFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackKing() {
        String path = "src/PieceImages/blackKing.png";
        File blackKingFile = new File(path);
        try {
            blackKing = ImageIO.read(blackKingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getWhitePawn() { return this.whitePawn; }

    private Image getWhiteRook() { return this.whiteRook; }

    private Image getWhiteKnight() { return this.whiteKnight; }

    private Image getWhiteBishop() { return this.whiteBishop; }

    private Image getWhiteQueen() { return this.whiteQueen; }

    private Image getWhiteKing() { return this.whiteKing; }

    private Image getBlackPawn() { return this.blackPawn; }

    private Image getBlackRook() { return this.blackRook; }

    private Image getBlackKnight() { return this.blackKnight; }

    private Image getBlackBishop() { return this.blackBishop; }

    private Image getBlackQueen() { return this.blackQueen; }

    private Image getBlackKing() { return this.blackKing; }

    public Image getWhitePieceImage(PieceType pieceType) {
        Image imageRequested = null;

        switch (pieceType) {
            case PAWN -> imageRequested = getWhitePawn();
            case ROOK -> imageRequested = getWhiteRook();
            case KNIGHT -> imageRequested = getWhiteKnight();
            case BISHOP -> imageRequested = getWhiteBishop();
            case QUEEN -> imageRequested = getWhiteQueen();
            case KING -> imageRequested = getWhiteKing();
        }

        return imageRequested;
    }

    public Image getBlackPieceImage(PieceType pieceType) {
        Image imageRequested = null;

        switch (pieceType) {
            case PAWN -> imageRequested = getBlackPawn();
            case ROOK -> imageRequested = getBlackRook();
            case KNIGHT -> imageRequested = getBlackKnight();
            case BISHOP -> imageRequested = getBlackBishop();
            case QUEEN -> imageRequested = getBlackQueen();
            case KING -> imageRequested = getBlackKing();
        }

        return imageRequested;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Point point = e.getPoint();
        int xCoordinate = point.x / 80;
        int yCoordinate = point.y / 80;
        int[] position = new int[] {yCoordinate, xCoordinate};
        Piece currentPiece = getCurrentBoard().getSquares()[yCoordinate][xCoordinate].getCurrentPiece();

//        getCurrentBoard().checkCheckMate();

        if (getClickCount() == 1) {

            setCurrentPos(position);
            this.clickCount++;

//            removeAll();
//            drawSquares();
//            drawBoard();
//            this.removeAll();
            redrawBoard();

//            if (currentPiece != null) {
//                if (getCurrentBoard().isWhiteMove() && currentPiece.getColour() == Colour.WHITE
//                        || !getCurrentBoard().isWhiteMove() && currentPiece.getColour() == Colour.BLACK) {
//                    if (currentPiece.getType() == PieceType.PAWN) {
//                        processMoveCirclesPawn(currentPiece);
//                    } else if (currentPiece.getType() == PieceType.ROOK) {
//                        processMoveCirclesRook(currentPiece);
//                    } else if (currentPiece.getType() == PieceType.BISHOP) {
//                        processMoveCirclesBishop(currentPiece);
//                    } else if (currentPiece.getType() == PieceType.QUEEN) {
//                        processMoveCirclesQueen(currentPiece);
//                    } else if (currentPiece.getType() == PieceType.KNIGHT) {
//                        processMoveCirclesKnight(currentPiece);
//                    } else {
//                        processMoveCirclesKing(currentPiece);
//                    }
//                }
//            }

            if (currentPiece != null) {
                if (currentPiece.getType() == PieceType.PAWN) {
                    processMoveCirclesPawn(currentPiece);
                } else if (currentPiece.getType() == PieceType.ROOK) {
                    processMoveCirclesRook(currentPiece);
                } else if (currentPiece.getType() == PieceType.BISHOP) {
                    processMoveCirclesBishop(currentPiece);
                } else if (currentPiece.getType() == PieceType.QUEEN) {
                    processMoveCirclesQueen(currentPiece);
                } else if (currentPiece.getType() == PieceType.KNIGHT) {
                    processMoveCirclesKnight(currentPiece);
                } else {
                    processMoveCirclesKing(currentPiece);
                }
            }

//            if (getCurrentBoard().isCheckMate()) {
//                JOptionPane.showMessageDialog(this, "Checkmate");
//            };

            drawCheck();

            setFocus(xCoordinate, yCoordinate);
//            revalidate();
//            repaint();

        } else if (getClickCount() == 2) {

            setNewPos(position);

            if (Arrays.equals(getCurrentPos(), getNewPos())) {
                redrawBoard();
            } else {
                try {
                    getCurrentBoard().movePieceClicked(getCurrentPos(), getNewPos());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (FontFormatException ex) {
                    throw new RuntimeException(ex);
                }
                redrawBoard();
            }

//            getCurrentBoard().checkInCheck();
//
//            if (getCurrentBoard().isInCheck()) {
//                getCurrentBoard().checkCheckMate();
//            }

//            System.out.println(getCurrentBoard().isCheckMate());
            drawCheck();
//            if (getCurrentBoard().isInCheck()) {
//                System.out.println(getCurrentBoard().whichPieceAttackingKing());
////                System.out.println(getCurrentBoard().getSquares()[1][5].getCurrentPiece());
//            }

//            if (getCurrentBoard().isInCheck()) {
//                System.out.println(getCurrentBoard().canBlockCheck(
//                        getCurrentBoard().getSquares()[0][0].getCurrentPiece(),
//                        getCurrentBoard().getKingInCheck(),
//                        getCurrentBoard().whichPieceAttackingKing().get(0), new int[] {1, 5})
//                        && getCurrentBoard().legalMoveRook(getCurrentBoard().getSquares()[0][0].getCurrentPiece(), new int[] {1, 5}));
//            }


//            if (getCurrentBoard().isCheckMate()) {
//                JOptionPane.showMessageDialog(this, "Checkmate");
//            };

            this.clickCount = 1;

        }

    }

    public void setWindow(Window window) { this.window = window; }

    public Window getWindow() { return this.window; }

    void drawCheck() {
        drawBlackCheck();
        drawWhiteCheck();
    }

    private void drawBlackCheck() {
        for (Piece p : getCurrentBoard().getBlackPieces()) {
            if (p.getType() == PieceType.KING) {
                if (getCurrentBoard().inCheck(p)) {
                    g2.setColor(new Color(255, 204, 0));
                    g2.fillRect(p.getXCoordinate() * 80, p.getYCoordinate() * 80, getTileSize(), getTileSize());
                }
            }
        }
    }

    private void drawWhiteCheck() {
        for (Piece p : getCurrentBoard().getWhitePieces()) {
            if (p.getType() == PieceType.KING) {
                if (getCurrentBoard().inCheck(p)) {
                    g2.setColor(new Color(255,204,0));
                    g2.fillRect(p.getXCoordinate() * 80, p.getYCoordinate() * 80, getTileSize(), getTileSize());
                }
            }
        }
    }

    private void moveCircles(int x, int y, Colour c) {
        g2.setColor(Color.darkGray);

        if (c == Colour.WHITE) {
            if (getCurrentBoard().getSquares()[y][x].getCurrentPiece() != null) {
                g2.setColor(new Color(255, 87, 51));
                g2.fillOval(x * 80, y * 80, getTileSize(), getTileSize());
            } else {
                g2.fillOval((x * getTileSize()) + 29, ((y) * getTileSize()) + 29, 22, 22);
            }
        } else {
            g2.fillOval((x * getTileSize()) + 29, ((y) * getTileSize()) + 29, 22, 22);
        }

    }

    public void processMoveCirclesPawn(Piece piece) {
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {

                if (getCurrentBoard().isInCheck()) {
                    if (getCurrentBoard().canBlockCheck(piece, getCurrentBoard().getKingInCheck(),
                            getCurrentBoard().whichPieceAttackingKing().get(0), square.getPosition())
                            && getCurrentBoard().legalMovePawn(piece, square.getPosition())
                            && getCurrentBoard().pieceNotBlockingPawn(piece, piece.getPosition(), square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                } else {
                    if (getCurrentBoard().processCheckOrMove(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                }
            }
        }
    }

    public void processMoveCirclesRook(Piece piece) {
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {

                if (getCurrentBoard().isInCheck()) {
                    if (getCurrentBoard().canBlockCheck(piece, getCurrentBoard().getKingInCheck(),
                            getCurrentBoard().whichPieceAttackingKing().get(0), square.getPosition())
                            && getCurrentBoard().legalMoveRook(piece, square.getPosition())
                            && getCurrentBoard().pieceNotBlockingRook(piece, piece.getPosition(), square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                } else {
                    if (getCurrentBoard().processCheckOrMove(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                }

            }
        }
    }

    public void processMoveCirclesBishop(Piece piece) {
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {

                if (getCurrentBoard().isInCheck()) {
                    if (getCurrentBoard().canBlockCheck(piece, getCurrentBoard().getKingInCheck(),
                            getCurrentBoard().whichPieceAttackingKing().get(0), square.getPosition())
                            && getCurrentBoard().legalMoveBishop(piece, square.getPosition())
                            && getCurrentBoard().pieceNotBlockingBishop(piece, piece.getPosition(), square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                } else {
                    if (getCurrentBoard().processCheckOrMove(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                }

            }
        }
//        && getCurrentBoard().pieceNotBlockingBishop(piece, piece.getPosition(), square.getPosition())
    }

    public void processMoveCirclesKnight(Piece piece) {
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {

                if (getCurrentBoard().isInCheck()) {
                    if (getCurrentBoard().canBlockCheck(piece, getCurrentBoard().getKingInCheck(),
                            getCurrentBoard().whichPieceAttackingKing().get(0), square.getPosition())
                            && getCurrentBoard().legalMoveKnight(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                } else {
                    if (getCurrentBoard().processCheckOrMove(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                }
            }
        }
    }

    public void processMoveCirclesQueen(Piece piece) {
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {

                if (getCurrentBoard().isInCheck()) {
                    if (getCurrentBoard().canBlockCheck(piece, getCurrentBoard().getKingInCheck(),
                            getCurrentBoard().whichPieceAttackingKing().get(0), square.getPosition())
                            && getCurrentBoard().legalMoveQueen(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                } else {
                    if (getCurrentBoard().processCheckOrMove(piece, square.getPosition())) {
                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                    }
                }

            }
        }
    }

    public void processMoveCirclesKing(Piece piece) {
        for (Square[] rank : getCurrentBoard().getSquares()) {
            for (Square square : rank) {

//                if (getCurrentBoard().isInCheck()) {
//                    if (getCurrentBoard().canBlockCheck(getCurrentBoard().getKingInCheck(),
//                            getCurrentBoard().whichPieceAttackingKing().get(0), square.getPosition())
//                            && getCurrentBoard().legalMoveQueen(piece, square.getPosition())) {
//                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
//                    }
//                } else {
//                    if (getCurrentBoard().legalMoveKing(piece, square.getPosition())
//                            && !getCurrentBoard().potentialSquareIsCheck(piece, square.getPosition())) {
//                        moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
//                    }
                if (getCurrentBoard().processAllMoves(piece, square.getPosition())) {
                    moveCircles(square.getPosition()[1], square.getPosition()[0], Colour.WHITE);
                }
//                }

            }
        }
    }

    public void redrawBoard() {
        removeAll();

        // All this method is doing is redrawing over the top every time I think.
        // because move circles stay after piece is moved
        // may have to click in window and get piece to move (make method in window instead
        // of the panel)

        drawSquares();
        drawBoard();

        getWindow().remove(getWindow().getControlPanel());
        try {
            getWindow().setControlPanel();
            getWindow().getControlPanel().setBounds(0, 640, 640, 82);
            getWindow().add(getWindow().getControlPanel());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (FontFormatException ex) {
            throw new RuntimeException(ex);
        }

        revalidate();
        repaint();
    }

    void setFocus(int x, int y) {
        Color lightGrey = new Color(169, 169, 240);
        g2.setColor(lightGrey);
        g2.fillRect(x * 80, y * 80, getTileSize(), getTileSize());
    }

    public int getClickCount() { return this.clickCount; }

    public void incrementClickCount() { this.clickCount += 1; }

    public void setClickCount(int i) { this.clickCount = 1; }

    public void setCurrentPos(int[] currentPos) { this.currentPos = currentPos; }

    void setNewPos(int[] newPos) { this.newPos = newPos; }

    public int[] getCurrentPos() { return this.currentPos; }

    int[] getNewPos() { return this.newPos; }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        point = e.ge
////        if (e.getSource() == cont)
//    }
}
