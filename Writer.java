import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    public static void write(String filename, Maze maze){
        File file = new File(filename);
        try(FileWriter writer = new FileWriter(file); BufferedWriter bWriter = new BufferedWriter(writer)){
            bWriter.write(maze.toString());
        }catch (IOException e){
            System.exit(1);
        }
    }
}
