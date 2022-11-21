public class CelluleImpl implements Cellule {
    int row;
    int col;
    int generation;
    boolean alive;

    public CelluleImpl(int row,int col,int generation,boolean alive){
        this.row = row;
        this.col = col;
        this.generation = generation;
        this.alive = alive;
    }

    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public int getGeneration(){
        return this.generation;
    }
    public boolean getAlive(){
        return this.alive;
    }
    public void setRow(int row){
        this.row = row;
    }
    public void setCol(int col){
        this.col = col;
    }
    public void setGeneration(int generation){
        this.generation = generation;
    }
    public void setAlive(boolean alive){
        this.alive =alive;
    }
}
