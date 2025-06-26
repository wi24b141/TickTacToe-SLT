package org.game;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        TicTacToe game = new TicTacToe();

        try {
            game.start();
        } finally {
            game.closeScanner();
        }
    }
}