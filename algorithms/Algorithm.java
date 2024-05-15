package algorithms;

import javax.swing.*;

public class Algorithm {
    private AlgorithmThread thread;
    private JButton button;
    private boolean stopped;

    public Algorithm(JButton button, AlgorithmThread thread){
        this.button = button;
        this.thread = thread;
    }
    public void setThread(AlgorithmThread thread){
        this.thread = thread;
    }
    public JButton getButton(){
        return button;
    }

    public AlgorithmThread getThread(){
        return thread;
    }
    public void reset(){
        thread.reset();
    }
}
