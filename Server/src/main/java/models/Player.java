package models;

public class Player {

    private String mUserName;

    private int mState;

    private int mTeam;

    public Player(String userName, int team) {
        mUserName = userName;
        mTeam = team;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public int getState() {
        return mState;
    }

    public int getTeam() {
        return mTeam;
    }
}
