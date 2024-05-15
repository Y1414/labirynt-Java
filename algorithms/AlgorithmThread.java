package algorithms;

import maze.*;

public abstract class  AlgorithmThread extends Thread {
    public final Maze maze;
    public final MazeFrame frame;

    public AlgorithmThread(Maze maze, MazeFrame frame) {
        this.maze = maze;
        this.frame = frame;
    }

    public abstract void pause();
    public abstract void unpause();
    public abstract boolean isStopped();
    public abstract boolean hasFinished();
    public abstract boolean isInProgress();
    public abstract void reset();
}
