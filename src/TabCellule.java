import java.rmi.*;


public interface TabCellule extends Remote {
    int sizeX = -1;
    int sizeY = -1;
    Cellule[][] cellules = null;


    int getSizeX() throws RemoteException;
    int getSizeY() throws RemoteException;
    void setSizeX(int sizeX) throws RemoteException;
    void setSizeY(int sizeY) throws RemoteException;

    Cellule[][] getCellules() throws RemoteException;

    void setCellules(Cellule[][] cellule) throws RemoteException;
}
