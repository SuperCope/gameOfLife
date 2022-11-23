package compute;
import java.rmi.*;

public interface Compute extends Remote
{
    Object executeTask(Task t) throws RemoteException;
}