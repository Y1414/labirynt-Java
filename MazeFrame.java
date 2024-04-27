import javax.swing.*;
import Coordinates_pack.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class MazeFrame extends JFrame implements ActionListener{
    JPanel mazePanel;
    JPanel buttonsPanel;
    JButton startButton;
    int height = 1000;
    int width = 1000;
    Maze maze;

    MazeFrame(Maze inMaze){

        maze = inMaze;
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setLocationRelativeTo(null);
        
        buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(Color.gray);
        startButton = new JButton("start");
        startButton.setFocusable(false);
        startButton.addActionListener(this);
        buttonsPanel.add(startButton);

        add(buttonsPanel, BorderLayout.NORTH);


        mazePanel = new JPanel(new GridLayout(maze.getHeight(), maze.getWidth()));
        add(mazePanel, BorderLayout.CENTER);

        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                JLabel label = makeLabel(maze.getChar(new Coordinates(col, row)));
                final int labelX = col;
                final int labelY = row;

                label.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                    
                        
                    }
        
                    @Override
                    public void mousePressed(MouseEvent e) {
        
                    }
        
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //System.out.println("Kliknięto na pole: (" + labelX + ", " + labelY + ")");
                        Coordinates clickCoordinates = new Coordinates(labelX, labelY);
                        if (maze.getStart() == null && maze.getChar(clickCoordinates) != 'K'){
                            changeLabelColor(labelX, labelY, Color.green, maze.getWidth());
                            maze.changeChar(new Coordinates(labelX, labelY), 'P');
                            maze.setStart(clickCoordinates);
                        }
                        else if (maze.getEnd() == null && maze.getChar(clickCoordinates) != 'P'){
                            changeLabelColor(labelX, labelY, Color.red, maze.getWidth());
                            maze.changeChar(new Coordinates(labelX, labelY), 'K');
                            maze.setEnd(clickCoordinates);
                        }


                        else if (maze.getChar(clickCoordinates) == ' '){
                            changeLabelColor(labelX, labelY, Color.black, maze.getWidth());
                            maze.changeChar(new Coordinates(labelX, labelY), 'X');
                        }else if (maze.getChar(clickCoordinates) == 'X'){
                            changeLabelColor(labelX, labelY, Color.white, maze.getWidth());
                            maze.changeChar(new Coordinates(labelX, labelY), ' ');
                        }else if (maze.getChar(clickCoordinates) == 'P'){
                            changeLabelColor(labelX, labelY, Color.black, maze.getWidth());
                            maze.changeChar(new Coordinates(labelX, labelY), 'X');
                            maze.setStart(null);
                        }else if (maze.getChar(clickCoordinates) == 'K'){
                            changeLabelColor(labelX, labelY, Color.black, maze.getWidth());
                            maze.changeChar(new Coordinates(labelX, labelY), 'X');
                            maze.setEnd(null);
                        }
                    }
        
                    @Override
                    public void mouseEntered(MouseEvent e) {
        
                    }
        
                    @Override
                    public void mouseExited(MouseEvent e) {
        
                    }
                    
                });


                mazePanel.add(label);
            }
        }
        revalidate();
    }


    private JLabel makeLabel(char c) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        
        switch (c) {
        case 'X':
            label.setBackground(Color.BLACK);
            break;
        case 'P':
            label.setBackground(Color.green);
            break;
        case 'K':
            label.setBackground(Color.red);
            break;
        default:
            label.setBackground(Color.WHITE);
            break;
        }
        label.setOpaque(true);
       
        

        return label;
    }
    public void changeLabelColor(int x, int y, Color color, int mazeWidth) {
        int index = y * mazeWidth + x;
        Component component = mazePanel.getComponent(index);
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setBackground(color);
        }
    }


    public void bfs(){
        new Thread(() -> {
            ListOfPiairsOfCoordinates visited = new ListOfPiairsOfCoordinates();
            ArrayDeque<Coordinates> queue = new ArrayDeque<Coordinates>();
            

            Coordinates currentCoordinates = new Coordinates(maze.getStart().getX(), maze.getStart().getY()) ;
            queue.addLast(currentCoordinates);
            visited.addFirst(new PairOfCordinates(currentCoordinates, null));

            while (!queue.contains(maze.getEnd())){

                try{
                    currentCoordinates = queue.removeFirst();
                }catch (NoSuchElementException e){
                    break;
                }



                if (maze.getChar(currentCoordinates) == ' '){
                    changeLabelColor(currentCoordinates.getX(), currentCoordinates.getY(), Color.orange,maze.getWidth() );
                    revalidate();
                }
            
                char ways[] = maze.getWays(currentCoordinates); 

                for (int i=0;i<4;i++){
                    if (ways[i] != 'X'){
                        if (!visited.contains(currentCoordinates.moveForward(i)) && !queue.contains(currentCoordinates.moveForward(i))){
                            queue.addLast(currentCoordinates.moveForward(i));
                            visited.addFirst( new PairOfCordinates(currentCoordinates.moveForward(i), currentCoordinates));
                        }
                    }
                }
                
                try {
                    Thread.sleep(0, 1); 
                } catch (InterruptedException e) {

                }
            }

            currentCoordinates = maze.getEnd();
            while(currentCoordinates != null){
                if (maze.getChar(currentCoordinates) == ' '){
                    changeLabelColor(currentCoordinates.getX(), currentCoordinates.getY(), Color.cyan,maze.getWidth() );
                    revalidate();
                }
                currentCoordinates = visited.getPrevoiusCoordinates(currentCoordinates);
                try {
                    Thread.sleep(0, 1); 
                } catch (InterruptedException e) {

                }

            }


        }).start();
    }
    


    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == startButton){
            bfs();
        }
    }
}

    
