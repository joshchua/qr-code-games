package models.ScavengerHunt;

import java.util.ArrayList;

public class Findables {

    private ArrayList<String> findablesArrayList;



    public Findables(){
    ArrayList<String> findablesArrayList = new ArrayList<String>();
}

    public void addToFindables(String input){
    findablesArrayList.add(input);
    }

    public ArrayList<String> getFindablesList(){
        return findablesArrayList;
    }

    public boolean findablesContains (String input) {
        if(findablesArrayList.contains(input))
            return true;
        return false;
    }


}
