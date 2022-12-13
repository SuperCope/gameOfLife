import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Node extends Remote {
    Object sw = 0;
    Object nw = 0;
    Object ne = 0;
    Object se = 0;
    int depth = -1;
    int area = -1;
    int id = -1;

    public void setSw(Object c) throws RemoteException;
    public void setNw(Object c) throws RemoteException;
    public void setNe(Object c) throws RemoteException;
    public void setSe(Object c) throws RemoteException;
    public Object getSw() throws RemoteException;
    public Object getNw() throws RemoteException;
    public Object getNe() throws RemoteException;
    public Object getSe() throws RemoteException;
    public void setArea(int area) throws RemoteException;
    public void setDepth(int depth) throws RemoteException;
    public int getArea() throws RemoteException;
    public int getDepth() throws RemoteException;
//    @Override
//    public boolean equals(Object o);
//    @Override
//    public int hashCode();
//    public String toString();
}
