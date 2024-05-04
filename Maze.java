
import java.util.ArrayList;

import Coordinates_pack.Coordinates;

public class Maze{
    private final ArrayList<char[]> mazeData;
    private Coordinates start = null;
    private Coordinates end = null;
    private final int width;
    private final int height;

    public Maze(ArrayList<char[]> list){
        this.mazeData = list;
        setStartAndEnd();
        width = mazeData.getFirst().length;
        height = mazeData.size();
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public void setStart(Coordinates coordinates){
        start = coordinates;
    }
    public void setEnd(Coordinates coordinates){
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
        
        if (newCoordinates.getX() >= 0 && newCoordinates.getX() < mazeData.getFirst().length) {
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
        
        if (newCoordinates.getX() >= 0 && newCoordinates.getX() < mazeData.getFirst().length) {
            return getChar(newCoordinates);
        } else {
            return 'X';
        }
    }
    
    public char[] getWays(Coordinates coordinates){
        return new char[]{checkUp(coordinates), checkRight(coordinates), checkDown(coordinates), checkLeft(coordinates)};
    }

    public ArrayList<char[]> getMazeData() {
        return mazeData;
    }


    public void changeChar(Coordinates coordinates, char c){
        mazeData.get(coordinates.getY())[coordinates.getX()] = c;
    }

    
    @Override
    public String toString (){
        StringBuilder maze = new StringBuilder();
        for (char[] mazeDatum : mazeData) {
            maze.append(new String(mazeDatum)).append("\n");
        }

        return maze.toString();
    }

}
