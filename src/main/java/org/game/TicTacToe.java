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

    private int getValidInput(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value >= 0 && value <= 2) {
                    return value;
                } else {
                    System.out.println("Please enter a number between 0 and 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    private boolean askToPlayAgain() {
        System.out.print("Do you want to play again? (y/n): ");
        while (true) {
            String input = scanner.next().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.print("Invalid input. Please enter 'y' or 'n': ");
            }
        }
    }



    public void start() {
        System.out.println("TicTacToe game started!");

        boolean playAgain = true;

        while (playAgain) {
            board.clear();
            currentPlayer = player1;

            while (true) {
                System.out.println("\nCurrent board state: ");
                board.print();
                System.out.println("\nPlayer " + currentPlayer.getMarker() + ", enter your move:");

                int row = getValidInput("Enter row (0-2): ");
                int col = getValidInput("Enter column (0-2): ");

                boolean moveMade = makeMove(row, col);

                if (moveMade) {
                    if (hasWinner()) {
                        System.out.println("Player " + currentPlayer.getMarker() + " wins!");
                        board.print();
                        break;
                    }

                    if (board.isFull()) {
                        System.out.println("It's a draw!");
                        board.print();
                        break;
                    }

                    switchCurrentPlayer();
                }
            }

            playAgain = askToPlayAgain();
        }

        closeScanner();
        System.out.println("Thanks for playing!");
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

    public boolean hasWinner() {
        char marker = currentPlayer.getMarker();
        return board.hasThreeInRow(marker);
    }


    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }


}
