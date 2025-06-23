package org.game;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        TicTacToe game = new TicTacToe();
        game.start();

        game.makeMove(0, 0);

        game.switchCurrentPlayer();
        game.makeMove(0, 1);

        game.switchCurrentPlayer();
        game.makeMove(0, 2);

        game.switchCurrentPlayer();
        game.makeMove(0, 3);
    }
}