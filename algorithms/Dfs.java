package algorithms;

import Coordinates_pack.Coordinates;
import maze.Maze;
import maze.MazeFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Dfs extends AlgorithmThread{
    private final Object lock;
    private boolean stopped = false;
    private boolean inProgress = false;
    private boolean hasFinished = false;

    public Dfs(Maze maze, MazeFrame frame) {
        super(maze, frame);
        lock = new Object();
    }

    @Override
    public void run(){
        synchronized (lock){
            inProgress = true;
            ArrayList<Coordinates> visited = new ArrayList<>();
            Stack<Coordinates> stack = new Stack<>();
            stack.add(new Coordinates(maze.getStart().getX(), maze.getStart().getY()));
            while (!stack.isEmpty() && !stack.contains(maze.getEnd())){
                try {
                    if (stopped)
                        lock.wait();
                } catch (InterruptedException ignored) {

                }
                Coordinates current = stack.getFirst();
                if (maze.getChar(current) == ' ') {
                    frame.changeLabelColor(current.getX(), current.getY(), new Color(255, 160, 26, 255), maze.getWidth());
                    frame.revalidate();
                }

                visited.add(current);
                char[] ways = maze.getWays(current);
                boolean hasChildren = false;
                for (int i=0;i<4;i++){
                    if((ways[i] == ' ' || ways[i] == 'P' || ways[i] == 'K') && !visited.contains(current.moveForward(i))) {
                        stack.addFirst(current.moveForward(i));
                        hasChildren = true;
                        break;
                    }
                }
                if (!hasChildren){
                    stack.removeFirst();
                }else {
                    try {
                        Thread.sleep(10, 0);
                    } catch (InterruptedException ignored) {

                    }
                }

            }

            for(Coordinates current : stack){
                try {
                    if (stopped)
                        lock.wait();
                } catch (InterruptedException ignored) {

                }
                if (maze.getChar(current) == ' ') {
                    frame.changeLabelColor(current.getX(), current.getY(), Color.cyan, maze.getWidth());
                    frame.revalidate();
                }
                try {
                    Thread.sleep(10,0);
                } catch (InterruptedException ignored) {

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
