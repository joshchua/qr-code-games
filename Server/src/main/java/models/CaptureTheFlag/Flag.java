package models.CaptureTheFlag;

public class Flag {

    /**
     * The team owning the flag
     */
    private int flagTeam;

    /**
     * If the flag is taken
     */
    private boolean flagTaken;

    /**
     * Who is holding the flag
     */
    private String flagBearer;

    /**
     * Creates a new flag
     *
     * @param team The team owning the flag
     */
    public Flag(int team){
        flagTeam = team;
        flagTaken = false;
        flagBearer = null;
    }

    /**
     * Gets the team owning the flag
     *
     * @return
     */
    public int getTeam(){
        return flagTeam;
    }

    /**
     * If the flag is taken
     *
     * @return If the flag is taken
     */
    public boolean getIsTaken(){
        return flagTaken;
    }

    /**
     * Gets the holder of the flag
     *
     * @return The holder of the flag
     */
    public String getFlagBearer() {
        return flagBearer;
    }

    /**
     * Sets the holder of the flag
     *
     * @param flagBearer The holder of the flag
     */
    public void setFlagBearer(String flagBearer) {
        this.flagBearer = flagBearer;
        flagTaken = true;
    }

    /**
     * Resets the state of the flag
     */
    public void reset() {
        this.flagBearer = null;
        flagTaken = false;
    }
}