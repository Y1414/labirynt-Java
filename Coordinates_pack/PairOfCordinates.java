package Coordinates_pack;

public class PairOfCordinates{
    private Coordinates currentCoordinates;
    private Coordinates previousCoordinates;
    
    public PairOfCordinates(Coordinates current, Coordinates prevoius){
        currentCoordinates = current;
        previousCoordinates = prevoius;
    }

    public Coordinates getCurrent(){
        return currentCoordinates;
    }
    public Coordinates getPrevious(){
        return previousCoordinates;
    }
}