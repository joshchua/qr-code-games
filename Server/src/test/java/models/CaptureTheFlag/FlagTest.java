package models.CaptureTheFlag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlagTest {
    Flag f = new Flag(1);
    @Test
    void getTeam() {
        assertEquals(f.getTeam(),1);
    }
}