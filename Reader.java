import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {
    public static ArrayList<char[]> read() {
        Scanner scanner = null;
        String line = "";
        ArrayList<char[]> list = new ArrayList<char[]>();
        try {
            File in = new File("50.txt");
            scanner = new Scanner(in);
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                list.add(line.toCharArray());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Brak pliku");
            System.exit(1);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return list;
    }
}
