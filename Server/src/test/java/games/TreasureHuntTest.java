package games;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreasureHuntTest {
    TreasureHunt game;

    @BeforeEach
    public void init() {
        game =  new TreasureHunt(3);
        game.start();
    }

    @Test
    void getGameName() {
        assertEquals(game.getGameName(), "Treasure Hunt");
    }


    @Test
    void scannedNonTreasure() {
        game.joinLobby("tom");
        game.joinLobby("zac");
        game.switchTeam("zac");

        assertNull(game.handleScan("tom","randomStuff12345"));
    }

    @Test
    void scannedTreasureToWinTeam1() {
        game.joinLobby("tom");
        game.joinLobby("zac");
        game.switchTeam("zac");

        game.handleScan("tom","treasure3");
        game.handleScan("tom","treasure2");
        assertEquals(game.handleScan("tom","treasure1").getMessage(), "tom found treasure1." +
                " Game Over! Team 1 found all of the treasure!");

    }

    @Test
    void scannedTreasureToWinTeam2() {
        game.joinLobby("tom");
        game.joinLobby("zac");
        game.switchTeam("zac");

        game.handleScan("zac","treasure3");
        game.handleScan("zac","treasure2");
        assertEquals(game.handleScan("zac","treasure1").getMessage(), "zac found treasure1." +
                " Game Over! Team 2 found all of the treasure!");

    }

    @Test
    void scannedSameTreasure() {
        game.joinLobby("tom");
        game.joinLobby("zac");
        game.switchTeam("zac");
        game.handleScan("tom", "treasure1");

        assertEquals(game.handleScan("zac", "treasure1").getMessage(), "zac found treasure1.");
    }

    @Test
    void scannedSameTreasure2() {
        game.joinLobby("tom");
        game.joinLobby("zac");
        game.switchTeam("zac");
        game.handleScan("zac", "treasure1");

        assertEquals(game.handleScan("tom", "treasure1").getMessage(), "tom found treasure1.");
    }
}