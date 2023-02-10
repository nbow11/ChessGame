package ChessBoard;

import PieceRelated.Colour;
import PieceRelated.Piece;

public class Square {
    private int[] position;
    private Colour squareColour;
    public Piece currentPiece;
    public String squareName;

    public Square(int[] position, Colour colour) {
        this.position = position;
        this.squareColour = colour;
        this.currentPiece = null;
    }

    public Square(int[] position, Colour colour, Piece currentPiece)
    {
        this.position = position;
        this.squareColour = colour;
        this.currentPiece = currentPiece;
    }

    public String toString()
    {
        if (this.currentPiece == null)
        {
            if (this.squareColour == Colour.WHITE) {
                return "White Square";
            }
            return "Black Square";
        }

        return this.currentPiece.toString();
    }

    public void setPosition(int[] newPosition) { this.position = newPosition; }

    public int[] getPosition() { return this.position; }

    public Colour getSquareColour() { return this.squareColour; }

    public Piece getCurrentPiece() { return this.currentPiece; }

    public void setSquareName(String squareName) { this.squareName = squareName; }

    public String getSquareName() { return this.squareName; }

    public void setCurrentPiece(Piece piece) { this.currentPiece = piece; }

}
