package games;

import models.CaptureTheFlag.Flag;
import models.ScanResult;

public class CaptureTheFlag extends Game {

    private Flag flag1;
    private Flag flag2;

    private static final String BASE1 = "base1";
    private static final String BASE2 = "base2";


    public CaptureTheFlag() {
        super();
        this.mGameName = "Capture the Flag";
        flag1 = new Flag(1);
        flag2 = new Flag(2);
    }

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

        if (findTeam(userName) == 2 && base.equals(BASE2) && userName.equals(flag2.getFlagBearer())) {
            gameOver();
            return new ScanResult(userName + " has returned Team 1's flag to their base. Game Over!");
        }

        return null;
    }

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