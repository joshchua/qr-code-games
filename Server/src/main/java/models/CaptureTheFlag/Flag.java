package models.CaptureTheFlag;

public class Flag {

    private int flagTeam;
    private boolean flagTaken;
    private String flagBearer;

    public Flag(){
        flagTeam = 0;
        flagTaken = false;
        flagBearer = null;
    }
    public Flag(int team){
        flagTeam = team;
        flagTaken = false;
        flagBearer = null;
    }

    public int getTeam(){
        return flagTeam;
    }

    public boolean getIsTaken(){
        return flagTaken;
    }

    public String getFlagBearer() {
        return flagBearer;
    }

    public void setFlagBearer(String flagBearer) {
        this.flagBearer = flagBearer;
        flagTaken = true;
    }

    public void reset() {
        this.flagBearer = null;
        flagTaken = false;
    }
}