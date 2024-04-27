
import java.util.ArrayList;

import Coordinates_pack.Coordinates;

public class Maze{
    private ArrayList<char[]> mazeData;
    private Coordinates start = null;
    private Coordinates end = null;
    private int width;
    private int height;

    public Maze(ArrayList<char[]> list){
        this.mazeData = list;
        setStartAndEnd();
        width = mazeData.get(0).length;
        height = mazeData.size();
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public void setStart(Coordinates coordinates){
        if (coordinates == null)
            start = null;
        else
            start = coordinates;
    }
    public void setEnd(Coordinates coordinates){
        if (coordinates == null)
            end = null;
        else
            end = coordinates;
    }

    public void setStartAndEnd (){
        for (int i=0;i<mazeData.size();i++){
            for(int j=0;j<mazeData.get(i).length;j++){
                if (mazeData.get(i)[j] == 'P'){
                    start = new Coordinates(j, i);
                }
                else if (mazeData.get(i)[j] == 'K'){
                    end = new Coordinates(j, i);
                }
            }
        }
    }

    public char getChar(Coordinates coordinates){
        return mazeData.get(coordinates.getY())[coordinates.getX()];
    }

    public Coordinates getStart(){
        return start;
    }

    public Coordinates getEnd(){
        return end;
    }

    private char checkUp(Coordinates coordinates){
        Coordinates newCoordinates = new Coordinates(coordinates.getX(), coordinates.getY());
        newCoordinates.addCoordinates(new Coordinates(0, -1));
        
        if (newCoordinates.getY() >= 0 && newCoordinates.getY() < mazeData.size()) {
            return getChar(newCoordinates);
        } else {
            return 'X';
        }
    }
    
    private char checkRight(Coordinates coordinates){
        Coordinates newCoordinates = new Coordinates(coordinates.getX(), coordinates.getY());
        newCoordinates.addCoordinates(new Coordinates(1, 0));
        
        if (newCoordinates.getX() >= 0 && newCoordinates.getX() < mazeData.get(0).length) {
            return getChar(newCoordinates);
        } else {
            return 'X';
        }
    }
    
    private char checkDown(Coordinates coordinates){
        Coordinates newCoordinates = new Coordinates(coordinates.getX(), coordinates.getY());
        newCoordinates.addCoordinates(new Coordinates(0, 1));
        
        if (newCoordinates.getY() >= 0 && newCoordinates.getY() < mazeData.size()) {
            return getChar(newCoordinates);
        } else {
            return 'X';
        }
    }
    
    private char checkLeft(Coordinates coordinates){
        Coordinates newCoordinates = new Coordinates(coordinates.getX(), coordinates.getY());
        newCoordinates.addCoordinates(new Coordinates(-1, 0));
        
        if (newCoordinates.getX() >= 0 && newCoordinates.getX() < mazeData.get(0).length) {
            return getChar(newCoordinates);
        } else {
            return 'X';
        }
    }
    
    public char[] getWays(Coordinates coordinates){
        char[] ways = {checkUp(coordinates), checkRight(coordinates), checkDown(coordinates), checkLeft(coordinates)};
        return ways;
    }

    public void changeChar(Coordinates coordinates, char c){
        mazeData.get(coordinates.getY())[coordinates.getX()] = c;
    }

    
    @Override
    public String toString (){
        String maze = "Start: " + start.toString() + "\n" +
                      "End: " + end.toString() + "\n";
        for (int i=0;i<mazeData.size();i++){
            maze += new String( mazeData.get(i)) + "\n" ;
        }

        return maze;
    }

}
