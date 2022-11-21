public interface CelluleInterface {
    int row = -1;
    int col = -1;
    int generation = -1;
    boolean alive = false;

    int getRow();
    int getCol();
    int getGeneration();
    boolean getAlive();
    void setRow(int row);
    void setCol(int col);
    void setGeneration(int generation);
    void setAlive(boolean alive);

}
