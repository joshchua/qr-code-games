package games;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaptureTheFlagTest {
    CaptureTheFlag c = new CaptureTheFlag();

    @Test
    void getGameName() {
        assertEquals(c.getGameName(), "Capture the Flag");
    }




    @Test
    void scannedPlayerWithFlag() {
        c.joinLobby("tom");
        c.joinLobby("jack");
        c.joinLobby("zac");
        c.joinLobby("joe");
        c.switchTeam("zac");
        c.switchTeam("joe");

        //player scanned himself
        assertNull(c.handleScan("tom","tom"));

        //team2 scanned team2
        assertNull(c.handleScan("zac","joe"));

        //team1 scanned team2 with flag
        c.handleScan("zac","base1");
        assertNull(c.handleScan("joe","zac"));
        assertEquals(c.handleScan("tom","zac").getMessage(),
                "tom has tagged zac and has reset Team 1's flag.");

        //team2 scanned team1 with flag
        c.handleScan("tom","base2");
        assertEquals(c.handleScan("joe","tom").getMessage(),
                "joe has tagged tom and has reset Team 2's flag.");

    }
    @Test
    void scannedOwnBase() {
        c.joinLobby("zac");
        assertNull(c.handleScan("zac", "base1"));
    }
        @Test
        void scannedBase2WithFlag1() {
        c.joinLobby("zac");
        c.switchTeam("zac");
        c.handleScan("zac","base1");
        assertEquals(c.handleScan("zac","base2").getMessage(),
                "zac has returned Team 1's flag to their base. Game Over!");
    }
    @Test
    void scannedBase1WithFlag2() {
        c.joinLobby("zac");
        c.handleScan("zac","base2");
        assertEquals(c.handleScan("zac","base1").getMessage(),
                "zac has returned Team 2's flag to their base. Game Over!");
    }



}