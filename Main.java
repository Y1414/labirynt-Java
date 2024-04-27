
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        ArrayList<char[]> list = Reader.read();
        Maze maze = new Maze(list);
        new MazeFrame(maze);
    }   
}