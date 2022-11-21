import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.*;

public class TabCellulleImpl extends Activatable implements TabCellule, Serializable {
    int sizeX;
    int sizeY;
    Cellule cellules[][];
    public TabCellulleImpl(ActivationID id, MarshalledObject data) throws RemoteException {
        super(id,0);
    }

    public int getSizeX() {
        return this.sizeX;
    }
    public Cellule[][] getCellules() {
        return this.cellules;
    }
    public void setCellules(Cellule[][] cellules) {

        this.cellules = cellules;
        this.sizeX = cellules.length;
        this.sizeY = cellules[0].length;
    }
    public void setSizeX(int sizeX){
        this.sizeX = sizeX;
    }
    public void setSizeY(int sizeY){
        this.sizeY = sizeY;
    }
    public int getSizeY(){
        return this.sizeY;
    }


}