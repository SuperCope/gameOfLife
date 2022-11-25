import client.ComputeMyTask;
import client.MyTask;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Calculator implements Callable {
    int id;
    Cellule[][] cellules;
    ComputeMyTask computer;
    FutureTask<String> task;
    public Calculator(int id, Cellule[][] cellules,ComputeMyTask computer){
        this.id =id;
        this.cellules = cellules;
        this.computer = computer;
        this.task = new FutureTask<String>(this);

    }
    public String call () {
        // System.out.println("APPEL");
        process();
        // return the thread name executing this callable task
        return Thread.currentThread ().getName ();
    }
    public void process(){
        Cellule[][] svg = new Cellule[this.cellules.length][this.cellules[0].length].clone();
        for(int row = 0;row<this.cellules.length;row++){

            for(int col = 0;col<this.cellules[0].length;col++){

                Cellule c = this.cellules[row][col];
                svg[row][col] = new Cellule(c.getRow(),c.getCol(),-c.getGeneration(),c.isAlive());

                if(c.isAlive()){
                    // System.out.println("["+id+"]  ALIVE!!! EN "+row+" PAR "+col+" VOISINES : "+hasAliveNeightbors(c));

                }
                if(!c.isAlive() &&  hasAliveNeightbors(c)==3){
                    // System.out.println("["+id+"] ET ME VOILA ! EN "+row+" PAR "+col);
                    svg[row][col].setAlive(true);
                }

                if(c.isAlive() &&  (hasAliveNeightbors(c)!=2) && (hasAliveNeightbors(c)!=3)){
                    // System.out.println("["+id+"] DEAD!!! EN "+row+" PAR "+col);
                    svg[row][col].setAlive(false);

                }


            }

        }
        this.cellules =svg;
    }
    public int hasAliveNeightbors(Cellule c){

        int nbNeighborsAlive = 0;
        for(int row = c.getRow()-1;row<=c.getRow()+1;row++){
            for(int col = c.getCol()-1;col<=c.getCol()+1;col++){

                if(row >= 0 && row < this.cellules.length && col >= 0 && col < this.cellules[0].length){

                    Cellule c2 = this.cellules[row][col];

                    if(c2 != c && c2.isAlive()){
                        nbNeighborsAlive++;
                    }
                }


            }
        }

        return nbNeighborsAlive;
    }
    public FutureTask<String> getFutureTask(){
        return this.task;
    }
    public void setFutureTask(FutureTask<String> futureTask){
        this.task = futureTask;
    }
    public void setCellules(Cellule[][] cellules){
        this.cellules = cellules;
    }
    public Cellule[][] getCellules(){
        return this.cellules;
    }
}
