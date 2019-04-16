package games;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreasureHuntTest {
    TreasureHunt t = new TreasureHunt(3);

    @Test
    void getGameName() {
        assertEquals(t.getGameName(), "Treasure Hunt");
    }


    @Test
    void scannedNonTreasure(){
        c.joinLobby("tom");
        c.joinLobby("zac");
        c.switchTeam("zac");

        assertNull(t.handleScan("tom","randomStuff12345"));
    }
}

    @Test
    void scannedTreasureToWinTeam1(){
        c.joinLobby("tom");
        c.joinLobby("zac");
        c.switchTeam("zac");

        t.handleScan("tom","treasure3");
        t.handleScan("tom","treasure2");
        assertEquals(t.handleScan("tom","treasure1").getMessage(), "tom found treasure1." +
                "\n Game over! Team 1 found all of the treasure!");

    }

    @Test
    void scannedTreasureToWinTeam2(){
        c.joinLobby("tom");
        c.joinLobby("zac");
        c.switchTeam("zac");

        t.handleScan("zac","treasure3");
        t.handleScan("zac","treasure2");
        assertEquals(t.handleScan("zac","treasure1").getMessage(), "zac found treasure1." +
                "\n Game over! Team 2 found all of the treasure!");

    }

    @Test
    void scannedSameTreasure() {
        c.joinLobby("tom");
        c.joinLobby("zac");
        c.switchTeam("zac");

        assertEquals(t.handleScan("tom","treasure1").getMessage(), "tom found treasure1.");

        assertEquals(t.handleScan("zac","treasure1").getMessage(), "zac found treasure1.");
}