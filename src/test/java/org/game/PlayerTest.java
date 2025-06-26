package org.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getMarker_shouldReturnCorrectMarkerForX() {
        Player player = new Player('X');
        assertEquals('X', player.getMarker(), "Marker should be 'X'");
    }

    @Test
    void getMarker_shouldReturnCorrectMarkerForO() {
        Player player = new Player('O');
        assertEquals('O', player.getMarker(), "Marker should be 'O'");
    }

    @Test
    void getMarker_shouldReturnCorrectMarkerForCustomChar() {
        Player player = new Player('A');
        assertEquals('A', player.getMarker(), "Marker should be 'A'");
    }
}