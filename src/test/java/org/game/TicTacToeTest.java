package org.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;
    private TicTacToe game;

    @BeforeEach
    void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        game = new TicTacToe();
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
        game.closeScanner(); // Ensure scanner is closed
    }

    private void provideInput(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        // Re-initialize the scanner in TicTacToe with the new input stream
        try {
            Field scannerField = TicTacToe.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            Scanner oldScanner = (Scanner) scannerField.get(game);
            if (oldScanner != null) {
                oldScanner.close(); // Close existing scanner if any
            }
            scannerField.set(game, new Scanner(inputStream));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail("Failed to mock scanner: " + e.getMessage());
        }
    }

    private Object invokePrivateMethod(String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        Method method = TicTacToe.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true); // Allow access to private method
        return method.invoke(game, args);
    }

    @Test
    void makeMove_shouldReturnTrueForValidEmptyCell() {
        // Initially Player 1 ('X') is the current player
        assertTrue(game.makeMove(0, 0), "Move should be successful for an empty cell");
    }

    @Test
    void makeMove_shouldReturnFalseForInvalidCoordinates() {
        assertFalse(game.makeMove(3, 0), "Move should fail for out-of-bounds row");
        String expectedOutput = "Invalid coordinates! Please choose a row and colum between 0 and 2.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
        outputStreamCaptor.reset(); // Clear output for next assertion

        assertFalse(game.makeMove(0, -1), "Move should fail for out-of-bounds column");
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void makeMove_shouldReturnFalseForOccupiedCell() {
        game.makeMove(0, 0); // Player X places a marker
        outputStreamCaptor.reset(); // Clear output from previous move

        assertFalse(game.makeMove(0, 0), "Move should fail for an occupied cell");
        String expectedOutput = "Cell (0,0) is already occupied. Please choose an empty cell.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void switchCurrentPlayer_shouldSwitchFromXtoO() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to get the initial currentPlayer marker
        Field currentPlayerField = TicTacToe.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        Player initialPlayer = (Player) currentPlayerField.get(game);
        assertEquals('X', initialPlayer.getMarker(), "Initial player should be X");

        game.switchCurrentPlayer();

        Player switchedPlayer = (Player) currentPlayerField.get(game);
        assertEquals('O', switchedPlayer.getMarker(), "Player should switch to O");
        assertTrue(outputStreamCaptor.toString().contains("It's now Player O's turn."));
    }

    @Test
    void switchCurrentPlayer_shouldSwitchFromOtoX() throws NoSuchFieldException, IllegalAccessException {
        // Set current player to O first
        Field currentPlayerField = TicTacToe.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        Field player2Field = TicTacToe.class.getDeclaredField("player2");
        player2Field.setAccessible(true);
        currentPlayerField.set(game, player2Field.get(game)); // Set currentPlayer to player2 (O)

        Player initialPlayer = (Player) currentPlayerField.get(game);
        assertEquals('O', initialPlayer.getMarker(), "Initial player should be O for this test setup");

        game.switchCurrentPlayer();

        Player switchedPlayer = (Player) currentPlayerField.get(game);
        assertEquals('X', switchedPlayer.getMarker(), "Player should switch to X");
        assertTrue(outputStreamCaptor.toString().contains("It's now Player X's turn."));
    }

    @Test
    void hasWinner_shouldReturnTrueWhenWinnerExists() throws NoSuchFieldException, IllegalAccessException {
        // Simulate a winning condition for player X
        Board board = new Board();
        board.place(0, 0, 'X');
        board.place(0, 1, 'X');
        board.place(0, 2, 'X');

        // Inject this board into the game instance
        Field boardField = TicTacToe.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);

        // Current player is X by default after game initialization
        assertTrue(game.hasWinner(), "hasWinner should return true when Player X has a winning row");
    }

    @Test
    void hasWinner_shouldReturnFalseWhenNoWinner() throws NoSuchFieldException, IllegalAccessException {
        // Simulate a board with no winner
        Board board = new Board();
        board.place(0, 0, 'X');
        board.place(0, 1, 'O');
        board.place(0, 2, 'X');
        board.place(1, 0, 'O');
        board.place(1, 1, 'X');
        board.place(1, 2, 'O');

        // Inject this board into the game instance
        Field boardField = TicTacToe.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);

        // Current player is X by default after game initialization
        assertFalse(game.hasWinner(), "hasWinner should return false when no winner exists for current player X");

        // Switch player and check again
        game.switchCurrentPlayer(); // Current player becomes O
        assertFalse(game.hasWinner(), "hasWinner should return false when no winner exists for current player O");
    }

    @Test
    void closeScanner_shouldCloseScanner() throws NoSuchFieldException, IllegalAccessException {
        // Get the scanner field using reflection
        Field scannerField = TicTacToe.class.getDeclaredField("scanner");
        scannerField.setAccessible(true); // Make the private field accessible

        Scanner scanner = (Scanner) scannerField.get(game);
        assertNotNull(scanner, "Scanner should not be null before closing");

        game.closeScanner();

        game.closeScanner(); // Calling again should not cause issues.
        assertTrue(true, "Calling closeScanner multiple times should not throw an exception.");
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "2"})
    void getValidInput_shouldReturnValidInteger(String input) throws Exception {
        provideInput(input + "\n");
        Object result = invokePrivateMethod("getValidInput", new Class[]{String.class}, new Object[]{"Enter row (0-2): "});
        assertEquals(Integer.parseInt(input), result, "getValidInput should return the valid integer");
        assertFalse(outputStreamCaptor.toString().contains("Please enter a number between 0 and 2."));
        assertFalse(outputStreamCaptor.toString().contains("Invalid input. Please enter a number."));
    }

    @Test
    void getValidInput_shouldHandleInvalidNonIntegerInput() throws Exception {
        provideInput("abc\n1\n"); // First invalid, then valid
        Object result = invokePrivateMethod("getValidInput", new Class[]{String.class}, new Object[]{"Enter row (0-2): "});
        assertEquals(1, result, "getValidInput should eventually return the valid integer after invalid input");
        assertTrue(outputStreamCaptor.toString().contains("Invalid input. Please enter a number."));
        assertFalse(outputStreamCaptor.toString().contains("Please enter a number between 0 and 2."));
    }

    @Test
    void getValidInput_shouldHandleOutOfRangeIntegerInput() throws Exception {
        provideInput("5\n-1\n0\n"); // First out of range, then out of range, then valid
        Object result = invokePrivateMethod("getValidInput", new Class[]{String.class}, new Object[]{"Enter row (0-2): "});
        assertEquals(0, result, "getValidInput should eventually return the valid integer after out-of-range input");
        assertTrue(outputStreamCaptor.toString().contains("Please enter a number between 0 and 2."));
        assertFalse(outputStreamCaptor.toString().contains("Invalid input. Please enter a number."));
    }

    @Test
    void askToPlayAgain_shouldReturnTrueForYes() throws Exception {
        provideInput("y\n");
        Boolean result = (Boolean) invokePrivateMethod("askToPlayAgain", new Class[]{}, new Object[]{});
        assertTrue(result, "askToPlayAgain should return true for 'y'");
        assertFalse(outputStreamCaptor.toString().contains("Invalid input. Please enter 'y' or 'n':"));
    }

    @Test
    void askToPlayAgain_shouldReturnFalseForNo() throws Exception {
        provideInput("n\n");
        Boolean result = (Boolean) invokePrivateMethod("askToPlayAgain", new Class[]{}, new Object[]{});
        assertFalse(result, "askToPlayAgain should return false for 'n'");
        assertFalse(outputStreamCaptor.toString().contains("Invalid input. Please enter 'y' or 'n':"));
    }

    @Test
    void askToPlayAgain_shouldHandleInvalidInputThenValid() throws Exception {
        provideInput("invalid\nY\n"); // Test case-insensitivity and retry
        Boolean result = (Boolean) invokePrivateMethod("askToPlayAgain", new Class[]{}, new Object[]{});
        assertTrue(result, "askToPlayAgain should return true after invalid input followed by 'Y'");
        assertTrue(outputStreamCaptor.toString().contains("Invalid input. Please enter 'y' or 'n':"));
    }

    @Test
    void start_shouldHandleWinScenario() throws NoSuchFieldException, IllegalAccessException {
        // Simulate moves for X to win: (0,0), (1,0), (0,1), (1,1), (0,2)
        // Then simulate 'n' to stop playing.
        String simulatedInput = "0\n0\n" + // X places at (0,0)
                "1\n0\n" + // O places at (1,0)
                "0\n1\n" + // X places at (0,1)
                "1\n1\n" + // O places at (1,1)
                "0\n2\n" + // X places at (0,2) - X wins
                "n\n";    // Don't play again

        provideInput(simulatedInput);
        game.start();

        String output = outputStreamCaptor.toString();
        // Check for win message
        assertTrue(output.contains("Player X wins!"), "Output should contain win message for Player X");
        // Check for game end message
        assertTrue(output.contains("Thanks for playing!"), "Output should contain 'Thanks for playing!' message");
        // Ensure "It's a draw!" is not present
        assertFalse(output.contains("It's a draw!"), "Output should not contain 'It's a draw!' message");

        // Verify the board is cleared if played again (though in this case we said 'n')
        // This is hard to assert without exposing Board internals or running multiple games.
    }

    @Test
    void start_shouldHandleDrawScenario() throws NoSuchFieldException, IllegalAccessException {
        // Simulate moves for a draw:
        // X: (0,0), (0,2), (1,1), (2,1), (1,0)
        // O: (0,1), (1,2), (2,0), (2,2)
        // (0,0)X (0,1)O (0,2)X
        // (1,0)X (1,1)X (1,2)O
        // (2,0)O (2,1)X (2,2)O
        String simulatedInput = "0\n0\n" + // X at (0,0)
                "0\n1\n" + // O at (0,1)
                "0\n2\n" + // X at (0,2)
                "1\n2\n" + // O at (1,2)
                "1\n1\n" + // X at (1,1)
                "2\n0\n" + // O at (2,0)
                "2\n1\n" + // X at (2,1)
                "2\n2\n" + // O at (2,2)
                "1\n0\n" + // X at (1,0) - Draw
                "n\n";    // Don't play again

        provideInput(simulatedInput);
        game.start();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("It's a draw!"), "Output should contain 'It's a draw!' message");
        assertFalse(output.contains("wins!"), "Output should not contain any win messages");
        assertTrue(output.contains("Thanks for playing!"), "Output should contain 'Thanks for playing!' message");
    }

    @Test
    void start_shouldAllowMultipleRounds() throws NoSuchFieldException, IllegalAccessException {
        // Round 1: X wins
        // X: (0,0), (0,1), (0,2)
        // O: (1,0), (1,1)
        // Then 'y' to play again

        // Round 2: Draw
        // X: (0,0), (0,2), (1,1), (2,1), (1,0)
        // O: (0,1), (1,2), (2,0), (2,2)
        // Then 'n' to stop
        String simulatedInput = "0\n0\n" + // R1: X (0,0)
                "1\n0\n" + // R1: O (1,0)
                "0\n1\n" + // R1: X (0,1)
                "1\n1\n" + // R1: O (1,1)
                "0\n2\n" + // R1: X (0,2) - X wins R1
                "y\n" +    // Play again

                "0\n0\n" + // R2: X at (0,0)
                "0\n1\n" + // R2: O at (0,1)
                "0\n2\n" + // R2: X at (0,2)
                "1\n2\n" + // R2: O at (1,2)
                "1\n1\n" + // R2: X at (1,1)
                "2\n0\n" + // R2: O at (2,0)
                "2\n1\n" + // R2: X at (2,1)
                "2\n2\n" + // R2: O at (2,2)
                "1\n0\n" + // R2: X at (1,0) - Draw R2
                "n\n";    // Stop

        provideInput(simulatedInput);
        game.start();

        String output = outputStreamCaptor.toString();

        // Verify Round 1 win
        assertTrue(output.contains("Player X wins!"), "Output should contain win message for Player X in first round");

        // Verify Round 2 draw
        assertTrue(output.indexOf("It's a draw!") > output.indexOf("Do you want to play again? (y/n): y"));
        assertTrue(output.contains("It's a draw!"), "Output should contain 'It's a draw!' message in second round");

        // Verify final message
        assertTrue(output.contains("Thanks for playing!"), "Output should contain 'Thanks for playing!' message");
    }
}