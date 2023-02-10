package PieceRelated;

public class Piece {
    public int[] position;
    public Colour colour;
    public PieceType type;
    public boolean firstMove;

    int movesMade = 0;

    public Piece(int[] position, Colour colour, PieceType type) {
        this.position = position;
        this.colour = colour;
        this.type = type;
        this.firstMove = true;
    }

    public void movePiece(int[] newPosition) {
        this.position = newPosition;
        this.firstMove = false;
        this.movesMade += 1;
    }

    public int getYCoordinate() { return this.position[0]; }

    public int getXCoordinate() { return this.position[1]; }

    public String toString() {
        return this.colour.toString() + " " + this.type.toString();
    }

    public void setPosition(int[] newPosition) { this.position = newPosition; }

    public int[] getPosition() { return this.position; }

    public Colour getColour() { return this.colour; }

    public PieceType getType() { return this.type; }

    public void changeType(PieceType type) { this.type = type; }

    public boolean isFirstMove() { return this.firstMove; }

    public void setFirstMove(boolean b) { this.firstMove = b; }

    public int getMovesMade() { return this.movesMade; }

    public void setMovesMade(int moves) { this.movesMade = moves; }
}

