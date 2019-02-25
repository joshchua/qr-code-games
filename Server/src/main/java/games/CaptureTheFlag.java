package games;

import models.CaptureTheFlag.Flag;
import models.ScanResult;

/**
 * Class for the game of Capture the flag.
 */
public class CaptureTheFlag extends Game {

    /**
     *Flag for team 1.
     */
    private Flag flag1;

    /**
     *Flag for team 2.
     */
    private Flag flag2;

    /**
     *Name of the base 1.
     */
    private static final String BASE1 = "base1";

    /**
     *Name of the base 2.
     */
    private static final String BASE2 = "base2";

    /**
     *Creates a game of capture the flag.
     */
    public CaptureTheFlag() {
        super();
        this.mGameName = "Capture the Flag";
        flag1 = new Flag(1);
        flag2 = new Flag(2);
    }

    /**
     *Checks if a player scanned another player.
     * @param userName name of the player
     * @param otherPlayer name of the player that has been tagged
     * @return returns a message of the players
     */
    private ScanResult scannedPlayer(String userName, String otherPlayer) {
        if (findTeam(userName) == 1 && findTeam(otherPlayer) == 2 && otherPlayer.equals(flag1.getFlagBearer())) {
            flag1.reset();
            return new ScanResult(userName + " has tagged " + otherPlayer + " and has reset Team 1's flag.");
        }

        if (findTeam(userName) == 2 && findTeam(otherPlayer) == 1 && otherPlayer.equals(flag2.getFlagBearer())) {
            flag2.reset();
            return new ScanResult(userName + " has tagged " + otherPlayer + " and has reset Team 2's flag.");
        }

        return null;
    }

    /**
     *Checks if player scanned a base
     * @param userName name of the player that scanned a base
     * @param base name of the base that has beed scanned
     * @return returns a message for the players
     */
    private ScanResult scannedBase(String userName, String base) {
        // Handle a player tagging enemy team base
        if (findTeam(userName) == 1 && base.equals(BASE2) && !flag2.getIsTaken()) {
            flag2.setFlagBearer(userName);
            return new ScanResult(userName + " has captured Team 2's flag!");
        }

        if (findTeam(userName) == 2 && base.equals(BASE1) && !flag1.getIsTaken()) {
            flag1.setFlagBearer(userName);
            return new ScanResult(userName + " has captured Team 1's flag!");
        }

        // Handle a player tagging their own base
        if (findTeam(userName) == 1 && base.equals(BASE1) && userName.equals(flag2.getFlagBearer())) {
            gameOver();
            return new ScanResult(userName + " has returned Team 2's flag to their base. Game Over!");
        }

        if (findTeam(userName) == 2 && base.equals(BASE2) && userName.equals(flag1.getFlagBearer())) {
            gameOver();
            return new ScanResult(userName + " has returned Team 1's flag to their base. Game Over!");
        }

        return null;
    }

    /**
     * Called when a players has scanned something
     * @param userName name of the player
     * @param scanned string that he has scanned
     * @return returns scanned player or scanned base, otherwise null
     */
    @Override
    public ScanResult handleScan(String userName, String scanned) {

        if (scanned.equals(BASE1) || scanned.equals(BASE2)) {
            return scannedBase(userName, scanned);
        } else if (scanned.equals(flag1.getFlagBearer()) || scanned.equals(flag2.getFlagBearer())) {
            return scannedPlayer(userName, scanned);
        }

        return null;
    }
}
