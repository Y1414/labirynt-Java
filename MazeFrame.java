import javax.swing.*;
import Coordinates_pack.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MazeFrame extends JFrame implements ActionListener{
    private JPanel mazePanel;
    private JPanel buttonsPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton clearButton;
    private int height = 1000;
    private int width = 1000;
    private Maze maze;
    private Coordinates mazeSize;
    private Bfs bfsThread;



    MazeFrame(Maze inMaze){

        maze = inMaze;
        mazeSize = new Coordinates(maze.getWidth(), maze.getHeight());
        bfsThread = new Bfs(maze, this);
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setLocationRelativeTo(null);
        
        buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(220,126,0,255));

        startButton = createButton("Start");
        stopButton = createButton("Stop");
        clearButton = createButton("Clear");
        

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
                       
                        Coordinates clickCoordinates = new Coordinates(labelX, labelY);
                        if (!bfsThread.isInProgress()){
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


    private JButton createButton (String text){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.addActionListener(this);
        button.setBackground(new Color(255,160,26,255));
        button.setBorderPainted(false);
        buttonsPanel.add(button);
        return button;
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == startButton){
            if (bfsThread.isStopped()){
                bfsThread.startThread();
            }else{
                bfsThread = new Bfs(maze, this);
                bfsThread.start();
            }
        }
        if(event.getSource() == stopButton && bfsThread != null && bfsThread.isAlive()){
            bfsThread.stopThread();
        }
        if (event.getSource() == clearButton && bfsThread.isStopped()){
            bfsThread = new Bfs(maze, this);
            for (int i=0;i<mazeSize.getY();i++){
                for (int j=0;j<mazeSize.getX();j++){
                    if (maze.getChar(new Coordinates(j, i)) == ' '){
                        changeLabelColor(j, i, Color.white, maze.getWidth());
                    }
                }
            }
        }
    }
}

    
