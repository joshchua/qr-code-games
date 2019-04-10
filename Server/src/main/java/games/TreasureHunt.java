package games;

import models.TreasureHunt.Findables;
import models.ScanResult;


/**
 * Class for the game of Treasure Hunt.
 */
public class TreasureHunt extends Game {

    /**
     * String for game start code for use in "handleScan' when treasure has all been
     * added, GAMESTART command needs to be implemented client side
     */
    private static final String GAMESTARTCODE = "GAMESTART";


    /**
     * Findables object holds arraylist for treasures(As Strings) to be loaded into
     */
    private Findables treasure;
    /**
     * This object represents the treasures that team 1 has found so far
     */
    private Findables team1Treasure;
    /**
     * This object represents the treasures that team 2 has found so far
     */
    private Findables team2Treasure;

    public TreasureHunt(int treasures) {
        super();
        this.mGameName = "Scavenger Hunt";
        Findables treasure = new Findables();
        Findables team1Treasure = new Findables();
        Findables team2Treasure = new Findables();
    }

    /**
     * Adds the item to the treasure arraylist before game starts
     * @param username is player string
     * @param item is scanned string representing treasure
     * @return code for display to clients**
     */
    private ScanResult loadTreasureScan(String username, String item){
        if(treasure.findablesContains(item) == false) {
            treasure.addToFindables(item);
            return new ScanResult(username + " has added " + item + "to the search!");
        }
        return null;
    }

    /**
     * Game logic for when game has started, checks team, conditions and updates Findables arraylists for teams
     * declares winner if applicable
     * @param username is player string
     * @param item is scanned string representing treasure
     * @return code for display to clients**
     */
    private ScanResult scannedTreasure(String username, String item) {
            if (treasure.findablesContains(item) && findTeam(username) == 1) {
                if (team1Treasure.findablesContains(item)) {
                    return null;
                }
                if (team1Treasure.findablesContains(item) == false){
                    team1Treasure.addToFindables(item);
                    if(treasure.equals(team1Treasure)){
                        gameOver();
                        return new ScanResult(username + " found the last piece of treasure for Team 1!");
                    }
                    return new ScanResult(username + " found " + item + " for Team 1!");
                }
            }

            if (treasure.findablesContains(item) && findTeam(username) == 2) {
                if (team1Treasure.findablesContains(item)) {
                    return null;
                }
                if (team2Treasure.findablesContains(item) == false){
                    team2Treasure.addToFindables(item);
                    if(treasure.equals(team2Treasure)){
                        gameOver();
                        return new ScanResult(username + " found the last piece of treasure for Team 2!");
                    }
                    return new ScanResult(username + " found " + item + " for Team 2!");
                }
            }
            return null;
    }

    /**
     * checks if game code for starting game is present
     * if game is not started executes load treasure logic
     * if game is started executes game logic
     * @param userName name of the player that scanned the object
     * @param scanned name of the scanned object
     * @return
     */
    @Override
    public ScanResult handleScan(String userName, String scanned) {
        if(!mIsStarted && scanned == GAMESTARTCODE)
        if(!mIsStarted){
            return loadTreasureScan(userName, scanned);
        }

        if(mIsStarted){
            return  scannedTreasure(userName, scanned);
        }

        return null;
    }


}
