package engine;
import java.rmi.*;
import java.rmi.server.*;
import compute.*;

public class ComputeEngine extends UnicastRemoteObject implements Compute
{
    public ComputeEngine() throws RemoteException { }

    public Object executeTask(Task t)
    {
        return t.execute();
    }

    public void start(){
        try
        {
            Compute engine = new ComputeEngine(); // engine is a Compute not a ComputeEngine
            Naming.rebind("Compute", engine); // register compute server on this machine
            System.out.println("\u001B[32m"+"[INFO] COMPUTE ENGINE LINKED SUCCESSFULLY"+"\u001B[0m");
        }
        catch (Exception e)
        {
            System.err.println("ComputeEngine exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}