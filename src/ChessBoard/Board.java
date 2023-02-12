package ChessBoard;

import GUI.TimerAndMoves;
import PieceRelated.Colour;
import PieceRelated.Piece;
import PieceRelated.PieceType;

import GUI.Window;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Board {
    private List<Piece> whitePieces = new ArrayList<>();
    private List<Piece> blackPieces = new ArrayList<>();

    private List<Piece> piecesTakenWhite = new ArrayList<>();

    private List<Piece> piecesTakenBlack = new ArrayList<>();

    private List<int[][]> moveStack = new ArrayList<>();

    Window window = null;

    public final int height = 8;
    public final int width = 8;

    boolean inCheck = false;

    boolean checkMate = false;

    boolean whiteToMove = true;

    public Square[][] dimensions = new Square[getHeight()][getWidth()];

    public Board() {
        setUpSquares();
        setSquareNames();
        setUpPieces();
        setPiecesOnSquares();
    }

    public int getHeight() { return this.height; }

    public int getWidth() { return this.width; }

    private void setUpSquares() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                int[] position = new int[] { i, j };

                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        dimensions[i][j] = new Square(position, Colour.WHITE);
                    } else {
                        dimensions[i][j] = new Square(position, Colour.BLACK);
                    }
                } else {
                    if (j % 2 != 0) {
                        dimensions[i][j] = new Square(position, Colour.WHITE);
                    } else {
                        dimensions[i][j] = new Square(position, Colour.BLACK);
                    }
                }
            }

        }
    }

    public void setPiecesSave(Square[][] squares) {
        this.dimensions = squares;
    }

    private void setSquareNames() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                HashMap<Integer, String> alphabet = createDictionary();
                int[] rowNums = new int[] { 8, 7, 6, 5, 4, 3, 2, 1 };
                String name = alphabet.get(j) + String.format("{0}", rowNums[i]);
                dimensions[i][j].setSquareName(name);
            }
        }
    }

    private HashMap<Integer, String> createDictionary() {
        HashMap<Integer, String> alphabet = new HashMap<>();
        alphabet.put(0, "a");
        alphabet.put(1, "b");
        alphabet.put(2, "c");
        alphabet.put(3, "d");
        alphabet.put(4, "e");
        alphabet.put(5, "f");
        alphabet.put(6, "g");
        alphabet.put(7, "h");

        return alphabet;
    }

    private HashMap<Character, Integer> createDictionaryLettersFirst() {
        var alphabet = new HashMap<Character, Integer>();
        alphabet.put('a', 0);
        alphabet.put('b', 1);
        alphabet.put('c', 2);
        alphabet.put('d', 3);
        alphabet.put('e', 4);
        alphabet.put('f', 5);
        alphabet.put('g', 6);
        alphabet.put('h', 7);

        return alphabet;
    }

    private void setUpPieces() {
        setWhitePieces();
        setBlackPieces();
    }

    private void setWhitePieces() {
        whitePieces.add(new Piece(new int[] { 7, 0 }, Colour.WHITE, PieceType.ROOK));
        whitePieces.add(new Piece(new int[] { 7, 1 }, Colour.WHITE, PieceType.KNIGHT));
        whitePieces.add(new Piece(new int[] { 7, 2 }, Colour.WHITE, PieceType.BISHOP));
        whitePieces.add(new Piece(new int[] { 7, 3 }, Colour.WHITE, PieceType.QUEEN));
        whitePieces.add(new Piece(new int[] { 7, 4 }, Colour.WHITE, PieceType.KING));
        whitePieces.add(new Piece(new int[] { 7, 5 }, Colour.WHITE, PieceType.BISHOP));
        whitePieces.add(new Piece(new int[] { 7, 6 }, Colour.WHITE, PieceType.KNIGHT));
        whitePieces.add(new Piece(new int[] { 7, 7 }, Colour.WHITE, PieceType.ROOK));

        for (int i = 0; i < getWidth(); i++) {
            whitePieces.add(new Piece(new int[] { 6, i }, Colour.WHITE, PieceType.PAWN));
        }

    }

    private void setBlackPieces() {
        blackPieces.add(new Piece(new int[] { 0, 0 }, Colour.BLACK, PieceType.ROOK));
        blackPieces.add(new Piece(new int[] { 0, 1 }, Colour.BLACK, PieceType.KNIGHT));
        blackPieces.add(new Piece(new int[] { 0, 2 }, Colour.BLACK, PieceType.BISHOP));
        blackPieces.add(new Piece(new int[] { 0, 3 }, Colour.BLACK, PieceType.QUEEN));
        blackPieces.add(new Piece(new int[] { 0, 4 }, Colour.BLACK, PieceType.KING));
        blackPieces.add(new Piece(new int[] { 0, 5 }, Colour.BLACK, PieceType.BISHOP));
        blackPieces.add(new Piece(new int[] { 0, 6 }, Colour.BLACK, PieceType.KNIGHT));
        blackPieces.add(new Piece(new int[] { 0, 7 }, Colour.BLACK, PieceType.ROOK));

        for (int i = 0; i < getWidth(); i++) {
            blackPieces.add(new Piece(new int[] { 1, i }, Colour.BLACK, PieceType.PAWN));
        }
    }

    private void setPiecesOnSquares() {

        for (Piece piece : getWhitePieces()) {
            int yWhite = piece.getYCoordinate(), xWhite = piece.getXCoordinate();
            dimensions[yWhite][xWhite].setCurrentPiece(piece);
        }

        for (Piece piece : getBlackPieces()) {
            int yBlack = piece.getYCoordinate(), xBlack = piece.getXCoordinate();
            dimensions[yBlack][xBlack].setCurrentPiece(piece);
        }

    }

    public void boardToString() {
        for (Square[] rank : getSquares()) {
            for (Square square : rank) {
                System.out.println(square.toString());
            }
        }
    }

    // Method checks if square is empty or if opposite colour exists on square
    public boolean canCaptureOrEmpty(Piece piece, int[] newPosition) {
        int xPos = newPosition[1], yPos = newPosition[0];
        Piece potentialCapture = getSquares()[yPos][xPos].getCurrentPiece();

        if (potentialCapture == null) { return true; }

        if (piece.getType() == PieceType.PAWN) {
            if (piece.getXCoordinate() == newPosition[1]) return false;
        }

        if (isWhiteMove()) {
            if (piece.getColour() == Colour.WHITE
                    && potentialCapture.getColour() == Colour.WHITE) return false;
        } else {
            if (piece.getColour() == Colour.BLACK
                    && potentialCapture.getColour() == Colour.BLACK) return false;
        }

//        if (piece.getColour() == potentialCapture.getColour()) return false;

        return true;
//        else return potentialCapture.getType() != PieceType.KING;
    }

    // check if piece in the same rank when trying to move
    public boolean pieceNotBlockingPawn(Piece piece, int[] currentPosition, int[] newPosition) {

        // get opposite colour pieces
        var pieces
                = piece.getColour() == Colour.WHITE ? getWhitePieces() : getBlackPieces();
        int currentVertical = currentPosition[0], currentHorizontal = currentPosition[1];
        var occupiedSquares = new ArrayList<Piece>();

        for (Piece p : pieces) {
            if (p.getXCoordinate() == currentHorizontal) occupiedSquares.add(p);
        }

        int newVertical = newPosition[0];

        // check if piece directly in front
        for (Piece p : occupiedSquares) {
            if (p.getYCoordinate() == newPosition[0]) return false;

            if (piece.getColour() == Colour.WHITE) {
                if (p.getYCoordinate() < currentVertical && p.getYCoordinate() > newVertical) return false;
            } else {
                if (p.getYCoordinate() > currentVertical && p.getYCoordinate() < newVertical) return false;
            }
        }

        return true;
    }

    public boolean pieceNotBlockingRook(Piece piece, int[] currentPosition, int[] newPosition) {
        int currentY = currentPosition[0], currentX = currentPosition[1];
        int nextY = newPosition[0], nextX = newPosition[1];
        int differenceY = nextY - currentY, differenceX = nextX - currentX;

        if (differenceX > 0) {
            return moveRookRight(currentPosition, newPosition, differenceX);
        } else if (differenceX < 0) {
            return moveRookLeft(currentPosition, newPosition, differenceX);
        } else if (differenceY < 0) {
            return moveRookUp(piece, currentPosition, newPosition, differenceY);
        } else if (differenceY > 0) {
            return moveRookDown(piece, currentPosition, newPosition, differenceY);
        }

        return true;
    }

    private boolean moveRookRight(int[] currentPosition, int[] newPosition, int differenceX) {
        int currentY = currentPosition[0], currentX = currentPosition[1];
        int newX = newPosition[1];
        Piece closestPiece = null;

        for (int i = 1; i < differenceX; i++) {
            if (getSquares()[currentY][currentX + i].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentY][currentX + i].getCurrentPiece();
                break;
            }
        }

        if (closestPiece != null) {
            if (closestPiece.getXCoordinate() < newX) return false;
        }

        return true;

    }

    private boolean moveRookLeft(int[] currentPosition, int[] newPosition, int differenceX) {
        int currentY = currentPosition[0], currentX = currentPosition[1];
        int newX = newPosition[1];
        Piece closestPiece = null;

        for (int i = 1; i < Math.abs(differenceX); i++) {
            if (getSquares()[currentY][currentX - i].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentY][currentX - i].getCurrentPiece();
                break;
            }
        }

        if (closestPiece != null) {
            if (closestPiece.getXCoordinate() > newX) return false;
        }

        return true;

    }

    private boolean moveRookUp(Piece p, int[] currentPosition, int[] newPosition, int differenceY) {
        int currentY = currentPosition[0], currentX = currentPosition[1];
        int newY = newPosition[0];
        Piece closestPiece = null;

        for (int i = 1; i < Math.abs(differenceY); i++) {
            if (getSquares()[currentY - i][currentX].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentY - i][currentX].getCurrentPiece();
                break;
            }
        }

        if (closestPiece != null) {
            if (closestPiece.getColour() == p.getColour()) return false;
            if (closestPiece.getYCoordinate() > newY) return false;
        }

        return true;

    }

    private boolean moveRookDown(Piece p, int[] currentPosition, int[] newPosition, int differenceY) {
        int currentY = currentPosition[0], currentX = currentPosition[1];
        int newY = newPosition[0];
        Piece closestPiece = null;

        for (int i = 1; i < Math.abs(differenceY); i++) {
            if (getSquares()[currentY + i][currentX].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentY + i][currentX].getCurrentPiece();
                break;
            }
        }

        if (closestPiece != null) {
            if (closestPiece.getColour() == p.getColour()) return false;
            if (closestPiece.getYCoordinate() < newY) return false;
        }

        return true;

    }

    public boolean pieceNotBlockingBishop(Piece piece, int[] currentPosition, int[] newPosition) {
        int currentY = currentPosition[0], currentX = currentPosition[1];
        int nextY = newPosition[0], nextX = newPosition[1];
        int differenceY = nextY - currentY, differenceX = nextX - currentX;

        if (differenceX > 0 && differenceY < 0) {
            return topRightBishopMove(currentPosition, newPosition, differenceY, differenceX);
        } else if (differenceX > 0 && differenceY > 0) {
            return bottomRightBishopMove(currentPosition, newPosition, differenceY, differenceX);
        } else if (differenceX < 0 && differenceY < 0) {
            return topLeftBishopMove(currentPosition, newPosition, differenceY, differenceX);
        } else if (differenceX < 0 && differenceY > 0){
            return bottomLeftBishopMove(currentPosition, newPosition, differenceY, differenceX);
        }
        // iterate through each diagonal until newPosition and check if piece there
        // top right -> x greater, y less
        // bottom right -> x greater, y greater
        // top left -> x less, y less
        // bottom left -> x less, y greater

        return true;
    }

    private boolean topRightBishopMove(int[] currentPosition,
                                   int[] newPosition, int differenceY,
                                   int differenceX) {

        int val = Math.abs(Math.min(differenceX, differenceY));
        Piece closestPiece = null;

        for (int i = 1; i < val; i++) {
            if (getSquares()[currentPosition[0] - i][currentPosition[1] + i].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentPosition[0] - i][currentPosition[1] + i].getCurrentPiece();
                break;
            }
        }


        if (closestPiece != null) {
            int pieceY = closestPiece.getYCoordinate(), pieceX = closestPiece.getXCoordinate();
            if (pieceX < newPosition[1] && pieceY > newPosition[0]) {
                return false;
            }
        }

        return true;

    }

    private boolean bottomRightBishopMove(int[] currentPosition,
                                   int[] newPosition, int differenceY,
                                   int differenceX) {
        int val = Math.abs(Math.min(differenceX, differenceY));
        Piece closestPiece = null;

        for (int i = 1; i < val; i++) {
            if (getSquares()[currentPosition[0] + i][currentPosition[1] + i].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentPosition[0] + i][currentPosition[1] + i].getCurrentPiece();
                break;
            }
        }

        if (closestPiece != null) {
            int pieceY = closestPiece.getYCoordinate(), pieceX = closestPiece.getXCoordinate();
            if (pieceX < newPosition[1] && pieceY < newPosition[0]) {
                return false;
            }
        }

        return true;

    }

    private boolean topLeftBishopMove(int[] currentPosition,
                                  int[] newPosition, int differenceY,
                                  int differenceX) {

        int val = Math.abs(Math.min(differenceX, differenceY));
        Piece closestPiece = null;

        for (int i = 1; i < val; i++) {
            if (getSquares()[currentPosition[0] - i][currentPosition[1] - i].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentPosition[0] - i][currentPosition[1] - i].getCurrentPiece();
                break;
            }
        }


        if (closestPiece != null) {
            int pieceY = closestPiece.getYCoordinate(), pieceX = closestPiece.getXCoordinate();
            if (pieceX > newPosition[1] && pieceY > newPosition[0]) {
                return false;
            }
        }

        return true;

    }

    private boolean bottomLeftBishopMove(int[] currentPosition,
                                  int[] newPosition, int differenceY,
                                  int differenceX) {

        int val = Math.abs(Math.min(differenceX, differenceY));
        Piece closestPiece = null;

        for (int i = 1; i < val; i++) {
            if (getSquares()[currentPosition[0] + i][currentPosition[1] - i].getCurrentPiece() != null) {
                closestPiece = getSquares()[currentPosition[0] + i][currentPosition[1] - i].getCurrentPiece();
                break;
            }
        }


        if (closestPiece != null) {
            int pieceY = closestPiece.getYCoordinate(), pieceX = closestPiece.getXCoordinate();
            if (pieceX > newPosition[1] && pieceY < newPosition[0]) {
                return false;
            }
        }

        return true;

    }

    public boolean inCheck(Piece king) {
        var pieces = king.getColour() == Colour.WHITE ? getBlackPieces() : getWhitePieces();

        for (Piece p : pieces) {
            if (canMovePiece(p, king.getPosition())) {
                return true;
            }
        }

        return false;
    }

    public boolean isInCheck() { return this.inCheck; }

    public void setInCheck(boolean b) { this.inCheck = b; }

    public void setCheckMate(boolean b) { this.checkMate = b; }

    public boolean isCheckMate() { return this.checkMate; }

    public void checkInCheck() {
        checkInCheckBlack();

        if (!isInCheck()) {
            checkInCheckWhite();
        }
    }

    private void checkInCheckBlack() {
        for (Piece p : getBlackPieces()) {
            if (p.getType() == PieceType.KING) {
                if (inCheck(p)) {
                    setInCheck(true);
                } else {
                    setInCheck(false);
                }
            }
        }
    }

    private void checkInCheckWhite() {
        for (Piece p : getWhitePieces()) {
            if (p.getType() == PieceType.KING) {
                if (inCheck(p)) {
                    setInCheck(true);
                } else {
                    setInCheck(false);
                }
            }
        }
    }

    // method to check which piece attacking the king.
    public ArrayList<Piece> whichPieceAttackingKing() {
        checkInCheck();

        ArrayList<Piece> piecesAttacking = new ArrayList<>();

        Piece kingInCheck = getKingInCheck();

        if (kingInCheck != null) {
            if (kingInCheck.getColour() == Colour.BLACK) {
                for (Piece p : getWhitePieces()) {
                    if (canMovePiece(p, kingInCheck.getPosition())) {
                        piecesAttacking.add(p);
                    }
                }
            } else {
                for (Piece p : getBlackPieces()) {
                    if (canMovePiece(p, kingInCheck.getPosition())) {
                        piecesAttacking.add(p);
                    }
                }
            }
        }

        return piecesAttacking;

    }

    // Method to check if same horizontal, vertical, or diagonal as piece (handle knight later)
    public boolean canBlockCheck(Piece pieceToMove, Piece king, Piece attackingPiece, int[] newPosition) {
        int[] kingPosition = king.getPosition();
        int[] attackingPiecePosition = attackingPiece.getPosition();

        int differenceY = kingPosition[0] - attackingPiecePosition[0];
        int differenceX = kingPosition[1] - attackingPiecePosition[1];

        if (Arrays.equals(attackingPiecePosition, newPosition)) {
            if (pieceToMove.getType() != PieceType.KING) {
                return true;
            } else {
                return legalMoveKing(pieceToMove, newPosition);
            }

        }
//
//        if (!potentialSquareIsCheck(king, newPosition)) {
//            if (potentialCapture != null) {
//                return potentialCapture.getColour() != king.getColour();
//            }
//
//            return true;
//        }

        // if differenceY < 0 then up
        // if differenceY == 0 then same horizontal
        // if differenceY > 0 then down

        // if differenceX < 0 then left
        // if differenceY == 0 then same vertical
        // if differenceX > 0 then right

        // find the diagonal or horizontal or vertical
        // then check if the piece steps into this location and is between the king
        // and attacking piece

        if (differenceY == 0) {
            return blockCheckHorizontal(kingPosition, attackingPiecePosition, newPosition);
        } else if (differenceX == 0) {
            return blockCheckVertical(kingPosition, attackingPiecePosition, newPosition);
        } else if (differenceY < 0 && differenceX < 0) {
            return blockCheckDiagonalLeftTop(kingPosition, attackingPiecePosition, newPosition);
        } else if (differenceY < 0 && differenceX > 0) {
            return blockCheckDiagonalRightTop(kingPosition, attackingPiecePosition, newPosition);
        } else if (differenceY > 0 && differenceX < 0) {
            return blockCheckDiagonalLeftBottom(kingPosition, attackingPiecePosition, newPosition);
        } else if (differenceY > 0 && differenceX > 0) {
            return blockCheckDiagonalRightBottom(kingPosition, attackingPiecePosition, newPosition);
        }

        return false;
    }

    private boolean blockCheckHorizontal(int[] kingPos, int[] attackingPos, int[] newPos) {
        if (kingPos[0] != newPos[0]) return false;
        if (newPos[1] < kingPos[1] && newPos[1] > attackingPos[1]) return true;
        return newPos[1] > kingPos[1] && newPos[1] < attackingPos[1];
    }

    private boolean blockCheckVertical(int[] kingPos, int[] attackingPos, int[] newPos) {
        if (kingPos[1] != newPos[1]) return false;
        if (newPos[0] < kingPos[0] && newPos[0] > attackingPos[0]) return true;
        return newPos[0] > kingPos[0] && newPos[0] < attackingPos[0];
    }

    private boolean blockCheckDiagonalLeftTop(int[] kingPos, int[] attackingPos, int[] newPos) {

        List<int[]> possibleDiagonalSquares = new ArrayList<>();
        int differenceX = kingPos[1] - attackingPos[1];
        int differenceY = kingPos[0] - attackingPos[0];
        int difference = Math.abs(Math.min(differenceX, differenceY));

        for (int i = 1; i < difference; i++) {
            possibleDiagonalSquares.add(new int[] {attackingPos[0] - i, attackingPos[1] - i});
        }

        for (int[] move : possibleDiagonalSquares) {
            if (Arrays.equals(move, newPos)) return true;
        }

        return false;
    }

    private boolean blockCheckDiagonalRightTop(int[] kingPos, int[] attackingPos, int[] newPos) {

        List<int[]> possibleDiagonalSquares = new ArrayList<>();
        int differenceX = kingPos[1] - attackingPos[1];
        int differenceY = kingPos[0] - attackingPos[0];
        int difference = Math.abs(Math.min(differenceX, differenceY));

        for (int i = 1; i < difference; i++) {
            possibleDiagonalSquares.add(new int[] {attackingPos[0] - i, attackingPos[1] + i});
        }

        for (int[] move : possibleDiagonalSquares) {
            if (Arrays.equals(move, newPos)) return true;
        }

        return false;
    }

    private boolean blockCheckDiagonalLeftBottom(int[] kingPos, int[] attackingPos, int[] newPos) {

        List<int[]> possibleDiagonalSquares = new ArrayList<>();
        int differenceX = kingPos[1] - attackingPos[1];
        int differenceY = kingPos[0] - attackingPos[0];
        int difference = Math.abs(Math.min(differenceX, differenceY));

        for (int i = 1; i < difference; i++) {
            possibleDiagonalSquares.add(new int[] {attackingPos[0] + i, attackingPos[1] - i});
        }

        for (int[] move : possibleDiagonalSquares) {
            if (Arrays.equals(move, newPos)) return true;
        }

        return false;
    }

    private boolean blockCheckDiagonalRightBottom(int[] kingPos, int[] attackingPos, int[] newPos) {

        List<int[]> possibleDiagonalSquares = new ArrayList<>();
        int differenceX = kingPos[1] - attackingPos[1];
        int differenceY = kingPos[0] - attackingPos[0];
        int difference = Math.abs(Math.min(differenceX, differenceY));

        for (int i = 1; i < difference; i++) {
            possibleDiagonalSquares.add(new int[] {attackingPos[0] + i, attackingPos[1] + i});
        }

        for (int[] move : possibleDiagonalSquares) {
            if (Arrays.equals(move, newPos)) return true;
        }

        return false;
    }

    public boolean movingPieceIsCheck(Piece currentPiece, int[] newPosition) {

        // KING NOT IN CHECK SO CAN'T FIND PIECE ATTACKING KING
        // USE DIFFERENT METHOD
        Piece attackingPiece = whichPieceAttackingKing().get(0);

        boolean canTake = false;

        if (canMovePiece(attackingPiece, currentPiece.getPosition())) {
            canTake = true;
        }

        if (canTake) return processMovingPieceIsCheck(currentPiece, attackingPiece);

        return false;

    }

    private boolean processMovingPieceIsCheck(Piece currentPiece, Piece attackingPiece) {

        var pieces = currentPiece.getColour() == Colour.WHITE ? getWhitePieces() : getBlackPieces();
        Piece king = pieces.get(4);

        if (captureHorizontal(currentPiece, attackingPiece)) {
            if (currentPiece.getPosition()[1] < attackingPiece.getPosition()[1]
                    && king.getPosition()[1] < currentPiece.getPosition()[1]) {
                return true;
            } else if (currentPiece.getPosition()[1] > attackingPiece.getPosition()[1]
                    && currentPiece.getPosition()[1] < king.getPosition()[1]) {
                return true;
            }

            return false;
        } else if (captureVertical(currentPiece, attackingPiece)) {
            if (currentPiece.getPosition()[0] < attackingPiece.getPosition()[0]
                    && king.getPosition()[0] < currentPiece.getPosition()[0]) {
                return true;
            } else if (currentPiece.getPosition()[0] > attackingPiece.getPosition()[0]
                    && currentPiece.getPosition()[0] < king.getPosition()[0]) {
                return true;
            }

            return false;
        }

        return false;

    }

    private boolean captureHorizontal(Piece currentPiece, Piece attackingPiece) {
        int[] currentPiecePos = currentPiece.getPosition();
        int[] attackingPiecePos = attackingPiece.getPosition();

        return currentPiecePos[0] == attackingPiecePos[0];
    }

    private boolean captureVertical(Piece currentPiece, Piece attackingPiece) {
        int[] currentPiecePos = currentPiece.getPosition();
        int[] attackingPiecePos = attackingPiece.getPosition();

        return currentPiecePos[1] == attackingPiecePos[1];
    }

    public boolean processCheckOrMove(Piece currentPiece, int[] newPosition) {
        checkInCheck();

        // NOT ABLE TO MOVE OUT OF CHECK IF PIECE PROTECTING

        if (currentPiece.getType() == PieceType.KING) {
            if (potentialSquareIsCheck(currentPiece, newPosition)) return false;
        }

//        if (movingPieceIsCheck(currentPiece, newPosition)) return false;

        if (isInCheck()) {

            if (whichPieceAttackingKing().size() == 1) {
                // check if new position will block the check
                if (legalMoveKing(currentPiece, newPosition)) {
                    return true;
                }

                if (canBlockCheck(currentPiece, getKingInCheck(), whichPieceAttackingKing().get(0), newPosition)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }


        return canMovePiece(currentPiece, newPosition);
    }

    public boolean processAllMoves(Piece currentPiece, int[] newPosition) {
//        checkInCheck();

        if (isCheckMate()) {
            return false;
        }

        return processCheckOrMove(currentPiece, newPosition);

    }

    public boolean canMovePiece(Piece currentPiece, int[] newPosition) {

        if (isPieceProtectingKing(currentPiece, getKing(currentPiece).getPosition())) return false;

        return switch (currentPiece.getType()) {
            case ROOK -> legalMoveRook(currentPiece, newPosition)
                    && pieceNotBlockingRook(currentPiece, currentPiece.getPosition(), newPosition);
            case BISHOP -> legalMoveBishop(currentPiece, newPosition)
                    && pieceNotBlockingBishop(currentPiece, currentPiece.getPosition(), newPosition);
            case QUEEN -> legalMoveQueen(currentPiece, newPosition);
            case KNIGHT -> legalMoveKnight(currentPiece, newPosition);
            case KING -> legalMoveKing(currentPiece, newPosition);
            default -> legalMovePawn(currentPiece, newPosition)
                    && pieceNotBlockingPawn(currentPiece, currentPiece.getPosition(), newPosition);
        };
    }

    public boolean isPieceProtectingKing(Piece piece, int[] kingSquare) {
        int[] piecePosition = piece.getPosition();
        Colour pieceColour = piece.getColour();
        int kingRow = kingSquare[0], kingColumn = kingSquare[1];
        int pieceRow = piecePosition[0], pieceColumn = piecePosition[1];
        // Get the pieces of the opponent colour
        List<Piece> attackingPieces = (pieceColour == Colour.WHITE) ? getBlackPieces() : getWhitePieces();

        // Check if the piece is blocking the path of the attacking pieces
        for (Piece attackingPiece : attackingPieces) {
            int[] attackingPieceSquare = attackingPiece.getPosition();
            int attackingPieceRow = attackingPieceSquare[0];
            int attackingPieceColumn = attackingPieceSquare[1];

            // Check if the attacking piece and the king are in the same row
            if (attackingPiece.getType() == PieceType.ROOK || attackingPiece.getType() == PieceType.QUEEN) {
                if (attackingPieceRow == kingRow && attackingPiece.getType() != PieceType.KING) {
//                    System.out.println(attackingPiece);
                    int minColumn = Math.min(attackingPieceColumn, kingColumn);
                    int maxColumn = Math.max(attackingPieceColumn, kingColumn);
                    if (pieceRow == attackingPieceRow && pieceColumn > minColumn && pieceColumn < maxColumn) {
                        if (isPathClear(attackingPieceSquare, piecePosition)) {
                            return true;
                        }
                    }
                }
            }


            // Check if the attacking piece and the king are in the same column
            if (attackingPiece.getType() == PieceType.ROOK || attackingPiece.getType() == PieceType.QUEEN) {
                if (attackingPieceColumn == kingColumn && attackingPiece.getType() != PieceType.KING) {
//                    System.out.println(attackingPiece);
                    int minRow = Math.min(attackingPieceRow, kingRow);
                    int maxRow = Math.max(attackingPieceRow, kingRow);
                    if (pieceColumn == attackingPieceColumn && pieceRow > minRow && pieceRow < maxRow) {
                        if (isPathClear(attackingPieceSquare, piecePosition)) {
                            return true;
                        }
                    }
                }
            }

            // Check if the attacking piece and the king are in the same diagonal
            if (attackingPiece.getType() == PieceType.BISHOP || attackingPiece.getType() == PieceType.QUEEN) {
                if (Math.abs(attackingPieceRow - kingRow) == Math.abs(attackingPieceColumn - kingColumn)) {
//                    System.out.println(attackingPiece);
                    int minRow = Math.min(attackingPieceRow, kingRow);
                    int maxRow = Math.max(attackingPieceRow, kingRow);
                    int minColumn = Math.min(attackingPieceColumn, kingColumn);
                    int maxColumn = Math.max(attackingPieceColumn, kingColumn);
                    if (pieceRow > minRow && pieceRow < maxRow && pieceColumn > minColumn && pieceColumn < maxColumn) {
                        if (isDiagonalPathClear(attackingPieceSquare, piecePosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    // need to write is path clear functions
    public boolean isPathClear(int[] startSquare, int[] endSquare) {
        int startRow = startSquare[0];
        int startColumn = startSquare[1];
        int endRow = endSquare[0];
        int endColumn = endSquare[1];

        // Check if the start and end squares are in the same row
        if (startRow == endRow) {
            int minColumn = Math.min(startColumn, endColumn);
            int maxColumn = Math.max(startColumn, endColumn);
            for (int i = minColumn + 1; i < maxColumn; i++) {
                if (getSquares()[startRow][i].getCurrentPiece() != null) {
                    return false;
                }
            }
        }

        // Check if the start and end squares are in the same column
        if (startColumn == endColumn) {
            int minRow = Math.min(startRow, endRow);
            int maxRow = Math.max(startRow, endRow);
            for (int i = minRow + 1; i < maxRow; i++) {
                if (getSquares()[i][startColumn].getCurrentPiece() != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isDiagonalPathClear(int[] startSquare, int[] endSquare) {
        int startRow = startSquare[0];
        int startColumn = startSquare[1];
        int endRow = endSquare[0];
        int endColumn = endSquare[1];

        // Check if the start and end squares are not in the same diagonal
        if (Math.abs(startRow - endRow) != Math.abs(startColumn - endColumn)) {
            return false;
        }

        int rowDirection = (startRow < endRow) ? 1 : -1;
        int columnDirection = (startColumn < endColumn) ? 1 : -1;

        int row = startRow + rowDirection;
        int column = startColumn + columnDirection;
        while (row != endRow) {
            if (getSquares()[row][column].getCurrentPiece() != null) {
                return false;
            }
            row += rowDirection;
            column += columnDirection;
        }

        return true;
    }

    public Piece getKingInCheck() {
        Piece king = null;

        for (Piece p : getWhitePieces()) {
            if (p.getType() == PieceType.KING) {
                if (inCheck(p)) king = p;
            }
        }

        for (Piece p : getBlackPieces()) {
            if (p.getType() == PieceType.KING) {
                if (inCheck(p)) king = p;
            }
        }

        return king;
    }

    private Piece getKing(Piece currentPiece) {
        var sameColourPieces = currentPiece.getColour() == Colour.WHITE ? getWhitePieces() : getBlackPieces();

        Piece king = null;

        for (Piece p : sameColourPieces) {
            if (p.getType() == PieceType.KING) {
                king = p;
            }
        }

        return king;
    }

    public boolean legalMovePawn(Piece piece, int[] newPosition) {
        int currentY = piece.getPosition()[0], currentX = piece.getPosition()[1];
        int nextY = newPosition[0], nextX = newPosition[1];
        int differenceY = nextY - currentY;
        int differenceX = nextX - currentX;

//        if (isPieceProtectingKing(piece, getKing(piece).getPosition())) return false;

        if (differenceX != 1 && differenceX != -1 && differenceX != 0) return false;

        if (!canCaptureOrEmpty(piece, newPosition)) return false;

        // check if diagonal capture available
        if (differenceX == 1 || differenceX == -1) return diagonalCaptureAvailable(piece, newPosition);

        if (piece.isFirstMove()) {
            if (piece.getColour() == Colour.BLACK) {
                if (differenceY == 1 || differenceY == 2) return true;
            } else {
                if (differenceY == -1 || differenceY == -2) return true;
            }
        } else {
            if (piece.getColour() == Colour.BLACK) {
                if (differenceY == 1) return true;
            } else {
                if (differenceY == -1) return true;
            }
        }

        return false;
    }

    public boolean diagonalCaptureAvailable(Piece currentPiece, int[] newPosition) {
        ArrayList<int[]> diagonalMoves = getDiagonalMoves(currentPiece);
        Piece potentialCapture = getSquares()[newPosition[0]][newPosition[1]].getCurrentPiece();
        Colour curentPieceColour = currentPiece.getColour();

        if (potentialCapture == null) return false;

        if (Arrays.equals(diagonalMoves.get(0), newPosition)
                && potentialCapture.getColour() != curentPieceColour) {
            return true;
        } else return Arrays.equals(diagonalMoves.get(1), newPosition)
                    && potentialCapture.getColour() != curentPieceColour;

    }

    public ArrayList<int[]> getDiagonalMoves(Piece currentPiece) {
        ArrayList<int[]> diagonalMoves = new ArrayList<>();
        int[] currentPosition = currentPiece.getPosition();
        int[] diagonalLeft = new int[2], diagonalRight = new int[2];

        if (currentPiece.getColour() == Colour.WHITE) {
            try {
                diagonalLeft[0] = currentPosition[0] - 1;
                diagonalLeft[1] = currentPosition[1] - 1;
            } catch (Exception ignored) {}

            try {
                diagonalRight[0] = currentPosition[0] - 1;
                diagonalRight[1] = currentPosition[1] + 1;
            } catch (Exception ignored) {}
        } else {
            try {
                diagonalLeft[0] = currentPosition[0] + 1;
                diagonalLeft[1] = currentPosition[1] + 1;
            } catch (Exception ignored) {}

            try {
                diagonalRight[0] = currentPosition[0] + 1;
                diagonalRight[1] = currentPosition[1] - 1;
            } catch (Exception ignored) {}
        }

        diagonalMoves.add(diagonalLeft);
        diagonalMoves.add(diagonalRight);

        return diagonalMoves;
    }

    public boolean legalMoveRook(Piece piece, int[] newPosition) {
        int currentY = piece.getPosition()[0], currentX = piece.getPosition()[1];
        int nextY = newPosition[0], nextX = newPosition[1];
        boolean sameX = currentX == nextX;
        boolean sameY = currentY == nextY;

        if (!(sameX || sameY)) return false;

        if (!canCaptureOrEmpty(piece, newPosition)) return false;

        return true;
    }

    public boolean legalMoveBishop(Piece piece, int[] newPosition) {
        int currentY = piece.getPosition()[0], currentX = piece.getPosition()[1];
        int nextY = newPosition[0], nextX = newPosition[1];
        boolean sameX = currentX == nextX;
        boolean sameY = currentY == nextY;
        int differenceX = Math.abs(currentX - nextX);
        int differenceY = Math.abs(currentY - nextY);

        if (sameX || sameY) return false;

        if (differenceX != differenceY) return false;

        if (!canCaptureOrEmpty(piece, newPosition)) return false;

        return true;
    }

    public boolean legalMoveKnight(Piece piece, int[] newPosition) {
        int currentY = piece.getPosition()[0], currentX = piece.getPosition()[1];

        if (!canCaptureOrEmpty(piece, newPosition)) return false;

        List<int[]> legalMoves = new ArrayList<>();
        legalMoves.add(new int[] {currentY - 2, currentX + 1});
        legalMoves.add(new int[] {currentY - 2, currentX - 1});
        legalMoves.add(new int[] {currentY + 2, currentX + 1});
        legalMoves.add(new int[] {currentY + 2, currentX - 1});
        legalMoves.add(new int[] {currentY + 1, currentX + 2});
        legalMoves.add(new int[] {currentY + 1, currentX - 2});
        legalMoves.add(new int[] {currentY - 1, currentX + 2});
        legalMoves.add(new int[] {currentY - 1, currentX - 2});

        for (int[] move : legalMoves) {
            if (Arrays.equals(move, newPosition)) return true;
        }

        return false;
    }

    public boolean legalMoveQueen(Piece piece, int[] newPosition) {
        Square newSquare = getSquares()[newPosition[0]][newPosition[1]];

        for (Square[] rank : getSquares()) {
            for (Square square : rank) {
                if (legalMoveBishop(piece, square.getPosition())
                        && pieceNotBlockingBishop(piece, piece.getPosition(), square.getPosition())) {
                    if (Objects.equals(square, newSquare)) {
                        return true;
                    }
                } else if (legalMoveRook(piece, square.getPosition())
                        && pieceNotBlockingRook(piece, piece.getPosition(), square.getPosition())) {
                    if (Objects.equals(square, newSquare)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean legalMoveKing(Piece piece, int[] newPosition) {
        int[] currentPosition = piece.getPosition();
        int differenceY = Math.abs(currentPosition[0] - newPosition[0]);
        int differenceX = Math.abs(currentPosition[1] - newPosition[1]);

        if (!canCaptureOrEmpty(piece, newPosition)) return false;

        if (Arrays.equals(newPosition, new int[]{7, 6}) && piece.isFirstMove()
                && piece.getColour() == Colour.WHITE
                && getSquares()[7][5].getCurrentPiece() == null
                && getSquares()[7][6].getCurrentPiece() == null) {
            return true;
        }

        if (Arrays.equals(newPosition, new int[]{7, 2}) && piece.isFirstMove()
                && piece.getColour() == Colour.WHITE
                && getSquares()[7][1].getCurrentPiece() == null
                && getSquares()[7][2].getCurrentPiece() == null
                && getSquares()[7][3].getCurrentPiece() == null) {
            return true;
        }

        if (Arrays.equals(newPosition, new int[]{0, 6}) && piece.isFirstMove()
                && piece.getColour() == Colour.BLACK
                && getSquares()[0][5].getCurrentPiece() == null
                && getSquares()[0][6].getCurrentPiece() == null) {
            return true;
        }

        if (Arrays.equals(newPosition, new int[]{0, 2}) && piece.isFirstMove()
                && piece.getColour() == Colour.BLACK
                && getSquares()[0][1].getCurrentPiece() == null
                && getSquares()[0][2].getCurrentPiece() == null
                && getSquares()[0][3].getCurrentPiece() == null) {
            return true;
        }

        if (Math.abs(piece.getPosition()[0] - newPosition[0]) > 1
                || Math.abs(piece.getPosition()[1] - newPosition[1]) > 1) return false;

        if (differenceY == 1 && differenceX == 1) return true;
        if (differenceY == 1 && differenceX == 0) return true;
        if (differenceY == 0 && differenceX == 1) return true;

        return false;
    }

    public boolean potentialSquareIsCheck(Piece piece, int[] newPosition) {
        if (piece.getType() != PieceType.KING) return false;

        var pieces = piece.getColour() == Colour.WHITE ? getBlackPieces() : getWhitePieces();

        checkInCheck();

        if (isInCheck()) {
            if (Arrays.equals(whichPieceAttackingKing().get(0).getPosition(), newPosition)) {
                if (countAttackersToSquare(pieces, newPosition, whichPieceAttackingKing().get(0)) <= 1) {
                    return false;
                }
            }
        }

        for (Piece p : pieces) {
            if (p.getType() == PieceType.PAWN) {
                if (newPosition[0] == p.getYCoordinate() + 1
                        && newPosition[1] == p.getXCoordinate() - 1
                        || newPosition[0] == p.getYCoordinate() + 1
                        && newPosition[1] == p.getXCoordinate() + 1) {
                    return true;
                } else if (newPosition[0] == p.getYCoordinate() - 1
                        && newPosition[1] == p.getXCoordinate() - 1
                        || newPosition[0] == p.getYCoordinate() - 1
                        && newPosition[1] == p.getXCoordinate() + 1) {
                    return true;
                }
            } else if (canMovePiece(p, newPosition)) {
                    return true;
            }
        }

        return false;
    }

    public int countAttackersToSquare(List<Piece> pieces, int[] newPosition, Piece attackingPiece) {

        // FIX THIS METHOD TO REGISTER MORE ATTACKERS

        int count = 0;

        List<Piece> attackers = new ArrayList<>();

        for (Piece p : pieces) {
            if (canMovePiece(p, newPosition)) {
                attackers.add(p);
                count++;
            } else if (newPosition[0] == p.getYCoordinate() + 1
                    && newPosition[1] == p.getXCoordinate() - 1
                    || newPosition[0] == p.getYCoordinate() + 1
                    && newPosition[1] == p.getXCoordinate() + 1) {
                attackers.add(p);
                count++;
            } else if (newPosition[0] == p.getYCoordinate() - 1
                    && newPosition[1] == p.getXCoordinate() - 1
                    || newPosition[0] == p.getYCoordinate() - 1
                    && newPosition[1] == p.getXCoordinate() + 1) {
                attackers.add(p);
                count++;
            }
        }

        boolean isEqual = false;

        // adds bishop and knight to attackers because they were not being counted
        // as attacking the square
        for (Piece p : attackers) {
            if (Objects.equals(p, attackingPiece)) {
                isEqual = true;
            }
        }

        if (isEqual == false) {
            count++;
        }

        return count;
    }

    public boolean checkIfCanMove(Piece currentPiece) {

        checkInCheck();

        if (isInCheck()) {
                // check if new position will block the check
            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    if (legalMoveKing(currentPiece, new int[] {i, j})) {
                        return false;
                    }

                    return !canBlockCheck(currentPiece, getKingInCheck(),
                            whichPieceAttackingKing().get(0), new int[] {i, j});
                }
            }

            return false;
        }

        return false;
    }

    public void checkCheckMate() {

        Piece kingInCheck = null;

        if (getKingInCheck() != null) {
            kingInCheck = getKingInCheck();
        }

        boolean canMove = false;

        List<Piece> pieces = null;

        if (kingInCheck != null) {
            pieces = kingInCheck.getColour() == Colour.WHITE ? getWhitePieces() : getBlackPieces();
        }

        if (kingInCheck != null) {
            for (Square[] rank : getSquares()) {
                for (Square square : rank) {
                    for (Piece p : pieces) {
                        if (p.getType() == PieceType.KING) {
                            if (processCheckOrMove(p, square.getPosition())) {
//                            if (canBlockCheck(p, kingInCheck, whichPieceAttackingKing().get(0), square.getPosition())
//                                    && !potentialSquareIsCheck(p, square.getPosition())) {
                                canMove = true;
                            }
                        }
                        else {
                            if (canBlockCheck(p, kingInCheck,
                                    whichPieceAttackingKing().get(0), square.getPosition())
                                    && canMovePiece(p, square.getPosition())) {
                                canMove = true;
                            }
                        }
                    }
                }
            }
        }
//        System.out.println(canMovePiece(getSquares()[0][0].getCurrentPiece(), new int[] {1, 5}));

        if (!canMove && kingInCheck != null) {
            setCheckMate(true);
        }

    }

    private void castleRightWhite() {
        int [] newRookPos = new int[] {7, 5};
        Piece whiteRookRight = getWhitePieces().get(7);
        int[] oldPosRook = whiteRookRight.getPosition();
        whiteRookRight.movePiece(newRookPos);
        getSquares()[newRookPos[0]][newRookPos[1]].setCurrentPiece(whiteRookRight);
        getSquares()[oldPosRook[0]][oldPosRook[1]].setCurrentPiece(null);
    }

    private void castleRightBlack() {
        int [] newRookPos = new int[] {0, 5};
        Piece blackRookRight = getBlackPieces().get(7);
        int[] oldPosRook = blackRookRight.getPosition();
        blackRookRight.movePiece(newRookPos);
        getSquares()[newRookPos[0]][newRookPos[1]].setCurrentPiece(blackRookRight);
        getSquares()[oldPosRook[0]][oldPosRook[1]].setCurrentPiece(null);
    }

    private void castleLeftWhite() {
        int [] newRookPos = new int[] {7, 3};
        Piece whiteRookRight = getWhitePieces().get(0);
        int[] oldPosRook = whiteRookRight.getPosition();
        whiteRookRight.movePiece(newRookPos);
        getSquares()[newRookPos[0]][newRookPos[1]].setCurrentPiece(whiteRookRight);
        getSquares()[oldPosRook[0]][oldPosRook[1]].setCurrentPiece(null);
    }

    private void castleLeftBlack() {
        int [] newRookPos = new int[] {0, 3};
        Piece blackRookRight = getBlackPieces().get(0);
        int[] oldPosRook = blackRookRight.getPosition();
        blackRookRight.movePiece(newRookPos);
        getSquares()[newRookPos[0]][newRookPos[1]].setCurrentPiece(blackRookRight);
        getSquares()[oldPosRook[0]][oldPosRook[1]].setCurrentPiece(null);
    }

    // takes user input and converts to working positions
    // If valid move, moves the piece on the board.
    public void movePiece(String currentSquare, String newSquare) {
        int[] currentPos = processSquareName(currentSquare);
        int[] newPos = processSquareName(newSquare);

        Piece currentPiece = getSquares()[currentPos[0]][currentPos[1]].getCurrentPiece();

        if (currentPiece.getType() != PieceType.PAWN) {
            currentPiece.movePiece(newPos);
            // removes piece from current square and moves to new square
            getSquares()[newPos[0]][newPos[1]].setCurrentPiece(currentPiece);
            getSquares()[currentPos[0]][currentPos[1]].setCurrentPiece(null);
        } else {
            if (canMovePiece(currentPiece, newPos))
            {
                currentPiece.movePiece(newPos);
                // removes piece from current square and moves to new square
                getSquares()[newPos[0]][newPos[1]].setCurrentPiece(currentPiece);
                getSquares()[currentPos[0]][currentPos[1]].setCurrentPiece(null);
            }
        }
    }

    public boolean isWhiteMove() { return this.whiteToMove; }

    public void setWhiteToMove(boolean b) { this.whiteToMove = b; }

    public void movePieceClicked(int[] currentPos, int[] newPos) throws IOException, FontFormatException {
        Piece currentPiece = getSquares()[currentPos[0]][currentPos[1]].getCurrentPiece();
        Piece newPiece = null;

//        if (isPieceProtectingKing(currentPiece, getKing(currentPiece).getPosition())) System.out.println(currentPiece);

        // handles not being able to capture king
        boolean kingPresent = false;

        if (getSquares()[newPos[0]][newPos[1]].getCurrentPiece() != null) {
            newPiece = getSquares()[newPos[0]][newPos[1]].getCurrentPiece();
            kingPresent = newPiece.getType() == PieceType.KING;
        }

        if (isWhiteMove() && currentPiece.getColour() == Colour.WHITE
                || !isWhiteMove() && currentPiece.getColour() == Colour.BLACK) {
            if (processAllMoves(currentPiece, newPos) && !kingPresent) {

                castleMove(currentPiece, currentPos, newPos);

                // promote pawn if reaches end of the board
                if ((newPos[0] == 0 || newPos[0] == 7) && currentPiece.getType() == PieceType.PAWN) {
                    currentPiece.changeType(PieceType.QUEEN);
                    currentPiece.movePiece(newPos);
                } else {
                    currentPiece.movePiece(newPos);
                }

                setWhiteToMove(!isWhiteMove());

                var moves = new int[][]{newPos, currentPos};
                getMoveStack().add(moves);

                // removes piece from current square and moves to new square
                getSquares()[currentPos[0]][currentPos[1]].setCurrentPiece(null);
                getSquares()[newPos[0]][newPos[1]].setCurrentPiece(currentPiece);

                if (newPiece != null) {
                    if (newPiece.getColour() == Colour.WHITE) {
                        getWhitePieces().remove(newPiece);
                        getPiecesTakenWhite().add(newPiece);
                    } else {
                        getBlackPieces().remove(newPiece);
                        getPiecesTakenBlack().add(newPiece);
                    }
                }

                getWindow().destroyAndRedrawTimerAndMoves();
            }
        }

//        checkInCheck();

//        if (whichPieceAttackingKing().size() != 0) {
//            if (whichPieceAttackingKing().get(0).getType() == PieceType.QUEEN) {
//                System.out.println(Arrays.toString(whichPieceAttackingKing().get(0).getPosition()));
//            }
//        }
    }

    public int checkQueenOnBoard() {
        int count = 0;

        for (Piece p : getWhitePieces()) {
            if (p.getType() == PieceType.BISHOP) {
                count++;
            }
        }

        return count;
    }

    private void castleMove(Piece currentPiece, int[] currentPos, int[] newPos) {

        if (currentPiece.getType() == PieceType.KING) {
            if (currentPiece.getColour() == Colour.WHITE) {
                if (newPos[0] == 7 && newPos[1] == 6) {
                    castleRightWhite();
                } else if (newPos[0] == 7 && newPos[1] == 2) {
                    castleLeftWhite();
                }
            } else {
                if (newPos[0] == 0 && newPos[1] == 6) {
                    castleRightBlack();
                } else if (newPos[0] == 0 && newPos[1] == 2) {
                    castleLeftBlack();
                }
            }
        }
    }

    public void computerMove() throws IOException, FontFormatException {
        Piece pieceToMove = null;

        Random rand = new Random();
        int n = rand.nextInt(getBlackPieces().size());

        pieceToMove = getBlackPieces().get(n);

        int y = rand.nextInt(8);
        int x = rand.nextInt(8);

        int[] newPosition = {y, x};

        if (!isInCheck()) {
            while (!processAllMoves(pieceToMove, newPosition)) {
                n = rand.nextInt(getBlackPieces().size());
                y = rand.nextInt(8);
                x = rand.nextInt(8);
                pieceToMove = getBlackPieces().get(n);
                newPosition = new int[]{y, x};
            }
        } else {
            while (!(canBlockCheck(pieceToMove, getKingInCheck(), whichPieceAttackingKing().get(0), newPosition)
                    && !potentialSquareIsCheck(pieceToMove, newPosition))
                    || !(canBlockCheck(pieceToMove, getKingInCheck(),
                    whichPieceAttackingKing().get(0), newPosition)
                    && canMovePiece(pieceToMove, newPosition))) {
                n = rand.nextInt(getBlackPieces().size());
                y = rand.nextInt(8);
                x = rand.nextInt(8);
                pieceToMove = getBlackPieces().get(n);
                newPosition = new int[]{y, x};
            }
        }

        if (processAllMoves(pieceToMove, newPosition)) {
            if (isInCheck()) {
                if (canBlockCheck(pieceToMove, getKingInCheck(), whichPieceAttackingKing().get(0), newPosition)
                        && !potentialSquareIsCheck(pieceToMove, newPosition)) {
                    movePieceClicked(pieceToMove.getPosition(), newPosition);
                } else if (canBlockCheck(pieceToMove, getKingInCheck(),
                        whichPieceAttackingKing().get(0), newPosition)
                        && canMovePiece(pieceToMove, newPosition)) {
                    movePieceClicked(pieceToMove.getPosition(), newPosition);
                }
            }
            else {
                movePieceClicked(pieceToMove.getPosition(), newPosition);
            }
        }

    }

    public List<Piece> getPiecesTakenWhite() { return this.piecesTakenWhite; }

    public List<Piece> getPiecesTakenBlack() { return this.piecesTakenBlack; }

    private HashMap<Integer, Integer> ReversedNumbers() {
        var reversed = new HashMap<Integer, Integer>();
        reversed.put(1, 7);
        reversed.put(2, 6);
        reversed.put(3, 5);
        reversed.put(4, 4);
        reversed.put(5, 3);
        reversed.put(6, 2);
        reversed.put(7, 1);
        reversed.put(8, 0);

        return reversed;
    }

    public int[] processSquareName(String squareName) {
        var alphabet = createDictionaryLettersFirst();
        var nums = ReversedNumbers();
        int xAxis = Integer.parseInt(String.valueOf(squareName.charAt(1)));
        char yAxis = squareName.charAt(0);

        int vertical = nums.get(xAxis);
        int across = alphabet.get(yAxis);

        return new int[] { vertical, across };
    }

    public List<Piece> getWhitePieces() { return whitePieces; }

    public List<Piece> getBlackPieces() { return blackPieces; }

    public Square[][] getSquares() { return dimensions; }

    public void setWindow(Window w) {
        this.window = w;
    }

    public Window getWindow() { return this.window; }

    // create method to step back moves
    public List<int[][]> getMoveStack() {
        return this.moveStack;
    }

    public void setMoveStack(List<int[][]> moveHistory) {
        this.moveStack = moveHistory;
    }

    private int[][] getLastMove() {
        var lastMove = getMoveStack().get(getMoveStack().size() - 1);
        getMoveStack().remove(lastMove);

        return lastMove;
    }

    public void stepBackMove() {
        int[][] lastMove = getLastMove();
        int[] currentPos = lastMove[0];
        int[] oldPos = lastMove[1];

        Piece currentPiece = getSquares()[currentPos[0]][currentPos[1]].getCurrentPiece();
        currentPiece.movePiece(oldPos);

        if (currentPiece.getMovesMade() == 2) {
            currentPiece.setFirstMove(true);
            currentPiece.setMovesMade(0);
        }

        getSquares()[currentPos[0]][currentPos[1]].setCurrentPiece(null);
        getSquares()[oldPos[0]][oldPos[1]].setCurrentPiece(currentPiece);
    }



}
