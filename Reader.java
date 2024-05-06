
import Exceptions.InvalidFileExtensionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {
    public static ArrayList<char[]> read(String filename) throws InvalidFileExtensionException {
        ArrayList<char[]> list = new ArrayList<>();
        if (filename.endsWith(".txt")) {
            Scanner scanner = null;
            String line;

            try {
                File in = new File(filename);
                scanner = new Scanner(in);
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    list.add(line.toCharArray());
                }
            } catch (FileNotFoundException e) {
                System.err.println("No such file!");
                System.exit(1);
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
        else if (filename.endsWith(".bin")){
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
                int fileID = Integer.reverseBytes(randomAccessFile.readInt());
                if (fileID != 0x52524243){
                    System.err.println("Unknown file ID");
                    return null;
                }
                int escape = randomAccessFile.read();
                int columns = Short.reverseBytes(randomAccessFile.readShort());
                int lines = Short.reverseBytes(randomAccessFile.readShort());
                int entryX = Short.reverseBytes(randomAccessFile.readShort());
                int entryY = Short.reverseBytes(randomAccessFile.readShort());
                int exitX = Short.reverseBytes(randomAccessFile.readShort());
                int exitY = Short.reverseBytes(randomAccessFile.readShort());
                randomAccessFile.skipBytes(12);
                int counter = Integer.reverseBytes(randomAccessFile.readInt());
                randomAccessFile.readInt();
                char separator = (char) randomAccessFile.read();
                char wall = (char) randomAccessFile.read();
                char path = (char) randomAccessFile.read();

                int currentX =1;
                int currentY =1;

                StringBuilder line = new StringBuilder();
                for (int i=0;i<counter;i++){
                    separator = (char) randomAccessFile.read();
                    char value = (char) randomAccessFile.read();
                    int count = randomAccessFile.read();

                    for(int j=0;j<=count;j++){
                        if(currentY == entryY && currentX == entryX) {
                            line.append('P');
                        } else if (currentY == exitY && currentX == exitX) {
                            line.append('K');
                        }else {
                            line.append(value);
                        }
                        if(line.length() == columns){
                            list.add(line.toString().toCharArray());
                            line = new StringBuilder();
                            currentX = 0;
                            currentY ++;
                        }
                        currentX ++;
                    }

                }
                

            } catch (IOException e) {
                System.exit(1);
            }

        }else {
            throw new InvalidFileExtensionException();
        }

        return list;
    }
}
