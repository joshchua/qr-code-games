package games;

import models.TreasureHunt.Findables;
import models.ScanResult;


/**
 * Class for the game of Scavenger Hunt.
 */
public class TreasureHunt extends Game {

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

    public TreasureHunt(int countTreasure) {
        super();
        this.mGameName = "Scavenger Hunt";

        Findables treasure = new Findables();

        for(int i = 1; i <= countTreasure; i++){
            treasure.addToFindables("treasure" + i);

        }
        
        Findables team1Treasure = new Findables();
        Findables team2Treasure = new Findables();
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
                if(treasure.getFindablesSize() == team1Treasure.getFindablesSize()){
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
                if(treasure.getFindablesSize() == team2Treasure.getFindablesSize()){
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
        return  scannedTreasure(userName, scanned);
    }


}
