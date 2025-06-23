package org.game;

public class TicTacToe {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;

    public TicTacToe() {
        board = new Board();
        player1 = new Player('X');
        player2 = new Player('O');

        currentPlayer = player1;
    }

    public void start() {
        System.out.println("TicTacToe game started!");

        board.print();
    }

    public boolean makeMove(int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            System.out.println("Invalid coordinates! Please choose a row and colum between 0 and 2.");
            return false;
        }

        if (board.isCellEmpty(x, y)) {
            board.place(x, y, currentPlayer.getMarker());
            System.out.println("Player " + currentPlayer.getMarker() + " placed at (" + x + "," + y + ")");
            board.print();
            return true;
        } else {
            System.out.println("Cell (" + x + "," + y + ") is already occupied. Please choose an empty cell.");
            return false;
        }
    }

    public void switchCurrentPlayer() {

        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
        System.out.println("It's now Player " + currentPlayer.getMarker() + "'s turn.");
    }

    public void hasWinner() {}

}
