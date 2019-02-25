package games;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
import models.ScanResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

 class GameTest {
    Game g = new Game() {
        @Override
        public ScanResult handleScan(String userName, String scanned) {
            return null;
        }
    };

    @Test
    void getGameName() {
        g.mGameName = "game";
        assertTrue(g.getGameName().equals("game"));
    }

    @Test
    void getGameCode() {
        assertTrue(g.getGameCode().matches("[A-Za-z0-9]+"));
        assertEquals(g.getGameCode().length(),5);
    }

    @Test
    void switchTeam1to2(){
        g.mPlayers.add("tom");
        g.mPlayerTeam.add(1);
        g.switchTeam("tom");
        assertEquals(g.mPlayerTeam.get(0), 2);
    }
     @Test
     void switchTeam2to1(){
         g.mPlayers.add("tim");
         g.mPlayerTeam.add(2);
         g.switchTeam("tim");
         assertEquals(g.mPlayerTeam.get(0), 1);
     }

     @Test
     void joinLobby() {
         g.joinLobby("Jack");
         assertEquals(g.mPlayers.get(0), "Jack");
         assertEquals(g.mPlayerTeam.get(0), 1);
         assertEquals(g.mPlayerState.get(0),0 );
     }


    @Test
    void getPlayers() {
        g.joinLobby("tom");
        g.joinLobby("tim");
        ArrayList<String> players = new ArrayList<String>();
        players.add("tom");
        players.add("tim");
        assertEquals(g.getPlayers(),players);
    }

    @Test
    void findTeam() {
        g.joinLobby("tim");
        g.joinLobby("tom");
        g.switchTeam("tom");
        assertEquals(g.findTeam("tim"),1);
        assertEquals(g.findTeam("tom"),2);
        assertEquals(g.findTeam("jack"),-1);
    }

    @Test
    void start() {
        g.start();
        assertTrue(g.mIsStarted);
    }

    @Test
    void gameOver() {
        g.gameOver();
        assertFalse(g.mIsStarted);
    }

    @Test
    void isPlaying() {
        assertFalse(g.isPlaying());
        g.start();
        assertTrue(g.isPlaying());
    }
}