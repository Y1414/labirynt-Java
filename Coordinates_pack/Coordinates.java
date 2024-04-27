package Coordinates_pack;
public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addCoordinates(Coordinates coordinates){
        this.x += coordinates.getX();
        this.y += coordinates.getY();
        
    }

    public Coordinates moveForward (int direction){
        Coordinates newCoordinates = new Coordinates(x,y);
        if (direction == 0){
            newCoordinates.addCoordinates(new Coordinates(0, -1));
            return newCoordinates;
        }
        if (direction == 1){
            newCoordinates.addCoordinates(new Coordinates(1, 0));
            return newCoordinates;
        }
        if (direction == 2){
            newCoordinates.addCoordinates(new Coordinates(0, 1));
            return newCoordinates;
        }
        if (direction == 3){
            newCoordinates.addCoordinates(new Coordinates(-1, 0));
            return newCoordinates;
        }
        return newCoordinates;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj){
        Coordinates other = (Coordinates) obj;
        if (other.x == x && other.y == y){
            return true;
        }
        return false;
    }
}