package org.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void isCellEmpty_shouldReturnTrueForEmptyCell() {
        assertTrue(board.isCellEmpty(0, 0), "Cell (0,0) should be empty initially");
    }

    @Test
    void isCellEmpty_shouldReturnFalseForOccupiedCell() {
        board.place(0, 0, 'X');
        assertFalse(board.isCellEmpty(0, 0), "Cell (0,0) should not be empty after placing 'X'");
    }

    @Test
    void place_shouldPlaceMarkerCorrectly() {
        board.place(1, 1, 'O');

        assertFalse(board.isCellEmpty(1, 1), "Cell (1,1) should not be empty after placing 'O'");
    }

    @Test
    void place_shouldPlaceDifferentMarkerAtDifferentPosition() {
        board.place(2, 0, 'X');
        assertFalse(board.isCellEmpty(2, 0), "Cell (2,0) should not be empty after placing 'X'");
        // You could add a helper method to Board to inspect cell content directly for more robust tests.
    }

    @Test
    void isFull_shouldReturnFalseForEmptyBoard() {
        assertFalse(board.isFull(), "Board should not be full when empty");
    }

    @Test
    void isFull_shouldReturnFalseForPartiallyFilledBoard() {
        board.place(0, 0, 'X');
        board.place(0, 1, 'O');
        assertFalse(board.isFull(), "Board should not be full when partially filled");
    }

    @Test
    void isFull_shouldReturnTrueForFullBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.place(i, j, 'X');
            }
        }
        assertTrue(board.isFull(), "Board should be full when all cells are occupied");
    }

    @Test
    void clear_shouldClearAllCells() {
        board.place(0, 0, 'X');
        board.place(1, 1, 'O');
        assertFalse(board.isCellEmpty(0, 0), "Cell (0,0) should be occupied before clear");
        assertFalse(board.isCellEmpty(1, 1), "Cell (1,1) should be occupied before clear");

        board.clear();
        assertTrue(board.isCellEmpty(0, 0), "Cell (0,0) should be empty after clear");
        assertTrue(board.isCellEmpty(1, 1), "Cell (1,1) should be empty after clear");
        // The isFull() check after clear is a bit of a trick; it means no ' ' were found.
        // A better check would be to iterate and confirm all are ' '.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(board.isCellEmpty(i, j), "All cells should be empty after clear");
            }
        }
    }

    @Test
    void hasThreeInRow_shouldReturnTrueForRowWin() {
        board.place(0, 0, 'X');
        board.place(0, 1, 'X');
        board.place(0, 2, 'X');
        assertTrue(board.hasThreeInRow('X'), "Should detect a win in the first row");
    }

    @Test
    void hasThreeInRow_shouldReturnTrueForColumnWin() {
        board.place(0, 1, 'O');
        board.place(1, 1, 'O');
        board.place(2, 1, 'O');
        assertTrue(board.hasThreeInRow('O'), "Should detect a win in the second column");
    }

    @Test
    void hasThreeInRow_shouldReturnTrueForMainDiagonalWin() {
        board.place(0, 0, 'X');
        board.place(1, 1, 'X');
        board.place(2, 2, 'X');
        assertTrue(board.hasThreeInRow('X'), "Should detect a win in the main diagonal");
    }

    @Test
    void hasThreeInRow_shouldReturnTrueForAntiDiagonalWin() {
        board.place(0, 2, 'O');
        board.place(1, 1, 'O');
        board.place(2, 0, 'O');
        assertTrue(board.hasThreeInRow('O'), "Should detect a win in the anti-diagonal");
    }

    @Test
    void hasThreeInRow_shouldReturnFalseForNoWin() {
        board.place(0, 0, 'X');
        board.place(0, 1, 'O');
        board.place(0, 2, 'X');
        assertFalse(board.hasThreeInRow('X'), "Should not detect a win with no three in a row");
        assertFalse(board.hasThreeInRow('O'), "Should not detect a win with no three in a row");
    }

    @Test
    void hasThreeInRow_shouldReturnFalseForIncompleteRow() {
        board.place(0, 0, 'X');
        board.place(0, 1, 'X');
        assertFalse(board.hasThreeInRow('X'), "Should not detect a win with an incomplete row");
    }

    @Test
    void hasThreeInRow_shouldReturnFalseForIncompleteColumn() {
        board.place(0, 1, 'X');
        board.place(1, 1, 'X');
        assertFalse(board.hasThreeInRow('X'), "Should not detect a win with an incomplete column");
    }
}