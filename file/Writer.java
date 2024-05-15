package file;

import Exceptions.InvalidFileExtensionException;
import maze.Maze;

import java.io.*;
import java.util.ArrayList;

public class Writer {
    public static void write(String filename, Maze maze) throws InvalidFileExtensionException {
        if (!(filename.endsWith(".txt") || filename.endsWith(".bin"))) {
            throw new InvalidFileExtensionException();
        }

        try (RandomAccessFile file = new RandomAccessFile(filename, "rw")){
            file.setLength(0);
            if (filename.endsWith(".txt")) {
                file.writeBytes(maze.toString());
            } else if (filename.endsWith(".bin")) {

                file.writeInt(Integer.reverseBytes(0x52524243));
                file.write(0x1B);

                file.writeShort(Short.reverseBytes((short) maze.getWidth()));
                file.writeShort(Short.reverseBytes((short) (maze.getHeight() + 1)));

                file.writeShort(Short.reverseBytes((short) (maze.getStart().getX() + 1)));
                file.writeShort(Short.reverseBytes((short) (maze.getStart().getY() + 1)));
                file.writeShort(Short.reverseBytes((short) (maze.getEnd().getX() + 1)));
                file.writeShort(Short.reverseBytes((short) (maze.getEnd().getY() + 1)));

                file.write(new byte[12]);

                int counter = 1;
                int solutionOffset = 0;
                char separator = '#';
                char wall = 'X';
                char path = ' ';
                file.writeInt(Integer.reverseBytes(counter));

                file.writeInt(solutionOffset);
                file.write(separator);
                file.write(wall);
                file.write(path);


                ArrayList<char[]> mazeData = maze.getMazeData();
                char value = mazeData.getFirst()[0];

                int count = -1;
                for(int i=0;i<maze.getHeight();i++){
                    for(int j=0;j< maze.getWidth();j++){
                        char currentChar = mazeData.get(i)[j];
                        if (currentChar == 'P' || currentChar == 'K'){
                            currentChar = ' ';
                        }
                        if (currentChar  == ' '|| currentChar == 'X') {
                            if (value != currentChar || count == 255) {
                                file.write(separator);
                                file.write(value);
                                file.write(count);
                                count = 0;
                                value = currentChar;
                                counter++;
                            } else {
                                count++;
                            }
                        }
                    }
                }
                file.write(separator);
                file.write(value);
                file.write(count);
                file.seek(29);
                file.writeInt(Integer.reverseBytes(counter));
            }

        }catch (IOException e){
            System.exit(1);
        }
    }
}
