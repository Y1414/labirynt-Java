package Coordinates_pack;
import java.util.ArrayDeque;

public class ListOfPiairsOfCoordinates extends ArrayDeque<PairOfCordinates>{

    public boolean contains(Coordinates coordinates){
        for (PairOfCordinates pair : this){
            if (pair.getCurrent().equals(coordinates)){
                return true;
            }
        }
        return false;
    }

    public Coordinates getPrevoiusCoordinates(Coordinates currentCoordinates) {
        for (PairOfCordinates pair : this){
            if (pair.getCurrent().equals(currentCoordinates)){
                return pair.getPrevious();
            }
        }
        return null;
    }
    
}
