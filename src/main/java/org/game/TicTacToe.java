package org.game;

import java.util.Scanner;

public class TicTacToe {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;
    private Scanner scanner;

    public TicTacToe() {
        board = new Board();
        player1 = new Player('X');
        player2 = new Player('O');

        currentPlayer = player1;

        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("TicTacToe game started!");

        while (true) {
            System.out.println("\nCurrent board state: ");
            board.print();
            System.out.println("\nPlayer " + currentPlayer.getMarker() + ", enter your move (row column): ");
            int row = -1;
            int col = -1;

            while (true) {
                System.out.print("Enter row (0-2): ");
                if (scanner.hasNextInt()) {
                    row = scanner.nextInt();
                    if (row >= 0 && row <= 2) {
                        break;
                    } else {
                        System.out.println("Invalid row. Please enter a number between 0 and 2.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            while (true) {
                System.out.print("Enter column (0-2): ");
                if (scanner.hasNextInt()) {
                    col = scanner.nextInt();
                    if (col >= 0 && col <= 2) {
                        break;
                    } else {
                        System.out.println("Invalid column. Please enter a number between 0 and 2.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }
            boolean moveMade = makeMove(row, col);

            if (moveMade) {
                switchCurrentPlayer();
            }
        }
    }

    public boolean makeMove(int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            System.out.println("Invalid coordinates! Please choose a row and colum between 0 and 2.");
            return false;
        }

        if (board.isCellEmpty(x, y)) {
            board.place(x, y, currentPlayer.getMarker());
            System.out.println("Player " + currentPlayer.getMarker() + " placed at (" + x + "," + y + ")");
            System.out.println("Current board state: ");
            board.print();
            return true;
        } else {
            System.out.println("Cell (" + x + "," + y + ") is already occupied. Please choose an empty cell.");
            System.out.println("Current board state: ");
            board.print();
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

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
            System.out.println("Scanner closed.");
        }
    }
}
