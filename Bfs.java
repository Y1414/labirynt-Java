import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import java.awt.*;
import Coordinates_pack.Coordinates;
import Coordinates_pack.ListOfPiairsOfCoordinates;
import Coordinates_pack.PairOfCordinates;

public class Bfs extends Thread{
    private Maze maze;
    private MazeFrame frame;
    private Object lock;
    private boolean stopped = false;
    private boolean inProgress = false;

    public Bfs(Maze maze, MazeFrame frame) {
        this.maze = maze;
        this.frame = frame;
        lock = new Object();
    }
    @Override
    public void run(){
        synchronized (lock){
            inProgress = true;
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
                try {
                    if (stopped) 
                        lock.wait(); 

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (maze.getChar(currentCoordinates) == ' '){
                    frame.changeLabelColor(currentCoordinates.getX(), currentCoordinates.getY(), new Color(255,160,26,255),maze.getWidth() );
                    frame.revalidate();
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

                bfsWait();
            }

            currentCoordinates = maze.getEnd();
            while(currentCoordinates != null){
                try {
                    if (stopped) {
                        lock.wait(); 
                    } else {
                        Thread.sleep(0, 1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (maze.getChar(currentCoordinates) == ' '){
                    frame.changeLabelColor(currentCoordinates.getX(), currentCoordinates.getY(), Color.cyan,maze.getWidth() );
                    frame.revalidate();
                }
                currentCoordinates = visited.getPrevoiusCoordinates(currentCoordinates);
                bfsWait();

            }
            stopped = true;
            inProgress = false;
        }
    }
    private void bfsWait(){
        try {
            Thread.sleep(0, 1); 
        } catch (InterruptedException e) {

        }
    }

    public void stopThread() {
        stopped = true;
        synchronized (lock) {
            lock.notify();
        }
    }
    public void startThread(){
        stopped = false;
        synchronized (lock) {
            lock.notify();
        }
    }
    public boolean isStopped(){
        return stopped;
    }
    public boolean isInProgress(){
        return inProgress;
    }
}