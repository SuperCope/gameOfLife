import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.*;

public class TabCellulleImpl extends Activatable implements TabCellule, Serializable {
    int sizeX;
    int sizeY;
    Cellule tabCellules[][];
    public TabCellulleImpl(ActivationID id, MarshalledObject data,int sizeX,int sizeY) throws RemoteException {
        super(id,0);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tabCellules = new Cellule[sizeX][sizeY];
    }
    public TabCellulleImpl(ActivationID id, MarshalledObject data,Cellule tabCellules[][]) throws RemoteException {
        super(id,0);
        this.sizeX = this.tabCellules.length;
        this.sizeY = this.tabCellules.length;
        this.tabCellules = tabCellules;
    }
    public int getSizeX() {
        return this.sizeX;
    }
    public void setSizeX(int sizeX){
        System.out.println("EDITING X");
        this.sizeX = sizeX;
    }
    public void setSizeY(int sizeY){
        this.sizeY = sizeY;
    }
    public int getSizeY(){
        return this.sizeY;
    }

    public String sayHello(String nom) throws RemoteException{
        return "Hello "+nom;
    }
}