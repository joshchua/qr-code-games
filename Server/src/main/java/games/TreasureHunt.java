package games;

import models.ScanResult;

import java.util.HashMap;
import java.util.Map;


/**
 * Class for the game of Scavenger Hunt.
 */
public class TreasureHunt extends Game {

    /**
     * Map containing all treasures.
     */
    private Map<String, Integer> map;

    /**
     * Number of treasures.
     */
    private int numOfTreasures;

    /**
     * Creates a new Treasure Hunt game.
     *
     * @param numOfTreasures Number of treasures
     */
    public TreasureHunt(final int numOfTreasures) {
        super();
        this.mGameName = "Treasure Hunt";
        this.numOfTreasures = numOfTreasures;
        map = new HashMap<String, Integer>();

        for (int i = 1; i <= numOfTreasures; i++) {
            map.put("treasure" + i, 0);
        }
    }

    /**
     * Checks if a team has won.
     *
     * @return Returns -1 if no winner. Returns 1 if team 1 wins and returns 2
     * if team 2 wins.
     */
    public int checkWin() {
        int team1 = 0, team2 = 0;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 3) {
                team1++;
                team2++;
            } else if (entry.getValue() == 2) {
                team2++;
            } else if (entry.getValue() == 1) {
                team1++;
            }
        }

        if (team1 == numOfTreasures) {
            return 1;
        }
        if (team2 == numOfTreasures) {
            return 2;
        }

        return -1;
    }

    /**
     * Checks the scanned QR code. Checks if it is a valid treasure. If it is,
     * then apply game logic.
     *
     * @param userName name of the player that scanned the object
     * @param scanned name of the scanned object
     * @return The game result
     */
    @Override
    public ScanResult handleScan(final String userName, final String scanned) {
        if (!map.containsKey(scanned) || !isPlaying()) {
            return null;
        }

        String message = "";

        if (map.get(scanned) == 0) {
            map.put(scanned, findTeam(userName));
            message = userName + " found " + scanned + ".";
        } else if ((map.get(scanned) == 1 && findTeam(userName) == 2)
                || (map.get(scanned) == 2 && findTeam(userName) == 1)) {
            map.put(scanned, 3);
            message = userName + " found " + scanned + ".";
        }

        int win = checkWin();

        if (win > -1) {
            message += " Game Over! ";
            if (win == 1) {
                message += "Team 1";
            } else {
                message += "Team 2";
            }
            message += " found all of the treasure!";
            gameOver();
        }

        if (message.equals("")) {
            return null;
        } else {
            return new ScanResult(message);
        }
    }


}
