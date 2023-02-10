package Controller;

import ChessBoard.Board;
import ChessBoard.Square;
import GUI.GameBoardGUI;
import GUI.Window;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

// controls game state and makes call to View to update GUI
public class Controller {

    private Board board;

    public static void main(String[] args) throws IOException, FontFormatException {
//        var board = new Board();
//        int count = 0;
//        Scanner move = new Scanner(System.in);
//
//        while (!Objects.equals(move.next(), "quit")) {
//            System.out.println("Input current square selection: ");
//
//            String currentSquare = move.next();
//
//            System.out.println("Input square to move to: ");
//
//            String nextSquare = move.next();
//
//            board.movePiece(currentSquare, nextSquare);
//
//            for (Square[] rank : board.getSquares()) {
//                for (Square square : rank) {
//                    System.out.print(square.toString() + "  ");
//                    count++;
//                }
//                if (count % 8 == 0) System.out.println();
//            }
//        }
        Window window = new Window();

//        Board b = new Board();
//        for (Square[] s : reverse2DArray(b.getSquares())) {
//            System.out.println(Arrays.toString(s));
//        }

//        GameBoardGUI chessGame = new GameBoardGUI("Brown");
//        window.add(chessGame);
//        chessGame.drawBoard();


    }

    private static Square[][] reverse2DArray(Square[][] squares) {

        int left = 0, right = squares.length - 1;

        while (left <= right) {

            Square[] oldLeft = squares[left];
            Square[] oldRight = squares[right];

            squares[left] = oldRight;
            squares[right] = oldLeft;

            left++;
            right--;
        }

        return squares;
    }
}


