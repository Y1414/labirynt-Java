import javax.swing.*;
import Coordinates_pack.*;
import Exceptions.InvalidFileExtensionException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MazeFrame extends JFrame implements ActionListener{
    private final JPanel mazePanel;
    private final JPanel buttonsPanel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton clearButton;
    private final JButton fileButton;
    private final JButton saveButton;
    private final JFileChooser fileChooser;
    private final JTextField textField;
    private final Color defaultColor;
    private final Font defaultFont;
    private Maze maze = null;
    private Coordinates mazeSize;
    private Bfs bfsThread;
    private boolean fileMenuOpened = false;
    

    MazeFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        int width = screenWidth / 2;
        int height = (int) ((double) 90 / 100 * screenHeight);

        bfsThread = new Bfs(maze, this);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setLocationRelativeTo(null);

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Mulish-Regular.ttf")));
        } catch (IOException | FontFormatException ignored) {
            
        }

        ImageIcon icon = new ImageIcon("images/icon.png");
        Image iconImage = icon.getImage();

        defaultFont = new Font("Mulish", Font.BOLD, 20);
        

        setIconImage(iconImage);
        
        defaultColor = new Color(255,160,26,255);
        Color secondaryColor = new Color(220, 126, 0, 255);
        

        buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(secondaryColor);

        textField = new JTextField("");
        textField.setFont(defaultFont);
        textField.setBackground(secondaryColor);
        textField.setBorder(null);
        textField.setForeground(Color.white);
        

        startButton = createButton("Start", "Start BFS algorithm animation");
        stopButton = createButton("Stop", "Pause the animation");
        clearButton = createButton("Clear", "Clear BFS paths");
        fileButton = createButton("Load", "Load a maze from file");
        saveButton = createButton("Save", "Save changed maze to file");

        //buttonsPanel.add(textField);

        add(textField, BorderLayout.SOUTH);
        textField.setPreferredSize(new Dimension(300,(int)textField.getPreferredSize().getHeight()));
        add(buttonsPanel, BorderLayout.NORTH);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        
        mazePanel = new JPanel();
        mazePanel.setBackground(secondaryColor);
        add(mazePanel, BorderLayout.CENTER);
        revalidate();
    }

    private void loadMaze(){
        mazePanel.removeAll();
        mazePanel.revalidate();
        mazePanel.repaint();
        revalidate();
        mazePanel.setLayout(new GridLayout(maze.getHeight(), maze.getWidth()));
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
                        if (!bfsThread.isInProgress() && !bfsThread.hasFinished()){
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
                        }else{
                            textField.setText("Clear first!");
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
        textField.setText("");
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

        if (component instanceof JLabel label) {
            label.setBackground(color);
        }
    }


    private JButton createButton (String text, String description){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.addActionListener(this);
        button.setBackground(defaultColor);
        button.setBorderPainted(false);
        button.setFont(defaultFont);
        button.setForeground(Color.white);
        button.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                textField.setText(description);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!fileMenuOpened) {
                    textField.setText("");
                }
                fileMenuOpened = false;
            }
            
        });


        buttonsPanel.add(button);
        return button;
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        

        if(event.getSource() == startButton){
            if (maze == null){
                textField.setText("Load a maze first!");
            }
            else if (maze.getStart() == null || maze.getEnd() == null){
                textField.setText("Place start and end first!");
            }
            else if (bfsThread.hasFinished()){
                textField.setText("Clear the maze first!");
            }
            else if (bfsThread.isStopped()){
                bfsThread.startThread();
            }else if (!bfsThread.isInProgress()){
                bfsThread = new Bfs(maze, this);
                bfsThread.start();
            }
            
        }
        if(event.getSource() == stopButton){
            if (maze == null){
                textField.setText("Load a maze first!");
            }
            else if (bfsThread != null && !bfsThread.isStopped())
                bfsThread.stopThread();
            else
                textField.setText("Start the animation first!");
        }
        if (event.getSource() == clearButton){
            if (maze == null){
                textField.setText("Load a maze first!");
            }
            else if (bfsThread.isStopped()){
                bfsThread = new Bfs(maze, this);
                for (int i=0;i<mazeSize.getY();i++){
                    for (int j=0;j<mazeSize.getX();j++){
                        if (maze.getChar(new Coordinates(j, i)) == ' '){
                            changeLabelColor(j, i, Color.white, maze.getWidth());
                        }
                    }
                }
            }else if(bfsThread.isInProgress()){
                textField.setText("Pause the animation first!");
            }else{
                textField.setText("There is nothing to clear!");
            }
        }
        if (event.getSource() == fileButton){
            
            if (bfsThread.isStopped() || !bfsThread.isInProgress()){
            
                int select = fileChooser.showOpenDialog(null);
                fileMenuOpened = true;

                if (select == JFileChooser.APPROVE_OPTION){
                    bfsThread = new Bfs(maze, this);
                    try {
                        maze = new Maze(Reader.read(fileChooser.getSelectedFile().getAbsolutePath()));
                        mazeSize = new Coordinates(maze.getWidth(), maze.getHeight());
                        loadMaze();
                        textField.setText("File loaded");
                    }catch (InvalidFileExtensionException e){
                        textField.setText("Unknown file format!");
                        revalidate();
                    }

                }

            }else{
                textField.setText("Pause the animation first!");
            }
        }
        if (event.getSource() == saveButton){
            if (maze != null){
                int saveTo = fileChooser.showSaveDialog(null);
                fileMenuOpened = true;
                if (saveTo == JFileChooser.APPROVE_OPTION){

                    try {
                        Writer.write(fileChooser.getSelectedFile().getAbsolutePath(), maze);
                        textField.setText("File saved");
                    } catch (InvalidFileExtensionException e) {
                        textField.setText("Unknown file extension!");
                    }
                }
            }
            else{
                textField.setText("Load a maze first!");
            }
        }
    }
}

    