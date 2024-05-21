package algorithms;

import Coordinates_pack.Coordinates;
import maze.Maze;
import maze.MazeFrame;

import java.awt.*;
import java.util.ArrayList;

public class BruteForce extends AlgorithmThread{
    private final Object lock;
    private boolean stopped = false;
    private boolean inProgress = false;
    private boolean hasFinished = false;
    private ArrayList<Coordinates> visited;
    private ArrayList<Coordinates> visitedTwice;
    private Coordinates current;
    private int direction = 0;


    public BruteForce(Maze maze, MazeFrame frame) {
        super(maze, frame);
        lock = new Object();
    }
    private int countWalls(char[] chars){
        int sum = 0;
        for(char c :chars){
            if (c == 'X')
                sum++;
        }
        return sum;
    }


    private void chooseDirection(){
        char[] ways = maze.getWays(current);
        if (countWalls(ways) == 3){
            direction = (direction+2)%4;
            visitedTwice.add(current);
            return;
        }
        for(int i=0;i<4;i++){
            if (
                      ways[i] != 'X'
                    && !visited.contains(current.moveForward(i))) {
                direction = i;
                return;
            }
        }
        visitedTwice.add(current);
        frame.changeLabelColor(current.getX(), current.getY(), new Color(255,160,26,255),maze.getWidth() );


        for(int i=4;i>0;i--){
            System.out.println(i%4);
            if (ways[i%4] != 'X' && !visitedTwice.contains(current.moveForward(i%4))) {
                direction = i%4;
                return;
            }
        }
        direction = -1;
    }

    private boolean noWay(){
        char[] ways = maze.getWays(current);
        for(int i=0;i<4;i++){
            if (ways[i] != 'X' && !visited.contains(current.moveForward(i)))
                return false;
        }
        return true;
    }


    @Override
    public void run() {

        synchronized (lock) {
            inProgress = true;

            current = maze.getStart();

            visited = new ArrayList<>();
            visitedTwice = new ArrayList<>();

            while (!current.equals(maze.getEnd())){
                try {
                    if (stopped)
                        lock.wait();
                } catch (InterruptedException ignored) {
                System.out.println(current);
                }
                char[] ways = maze.getWays(current);
                if (countWalls(ways) == 3 && !current.equals(maze.getStart())){
                    frame.changeLabelColor(current.getX(), current.getY(), new Color(255,160,26,255),maze.getWidth() );
                }

                visited.add(current);


                chooseDirection();
                if (direction == -1){
                    break;
                }
                current = current.moveForward(direction);

                if (!visited.contains(current)  && !current.equals(maze.getEnd()))
                    frame.changeLabelColor(current.getX(), current.getY(), Color.cyan, maze.getWidth());
                else if (noWay() && !current.equals(maze.getEnd()) && !current.equals(maze.getStart())){
                    frame.changeLabelColor(current.getX(), current.getY(), new Color(255,160,26,255),maze.getWidth() );
                    visitedTwice.add(current);
                }
                frame.revalidate();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            stopped = true;
            inProgress = false;
            hasFinished = true;

        }
    }

    @Override
    public void pause() {
        stopped = true;
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void unpause(){
        stopped = false;
        synchronized (lock) {
            lock.notify();
        }
    }
    @Override
    public boolean isStopped(){
        return stopped;
    }
    @Override
    public boolean isInProgress(){
        return inProgress;
    }

    @Override
    public void reset() {
        stopped = false;
        inProgress = false;
        hasFinished = false;
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public boolean hasFinished(){
        return hasFinished;
    }
}
