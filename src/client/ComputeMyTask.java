package client;

import compute.*;
import java.rmi.Naming;

public class ComputeMyTask {

    public void start(){
        try {
            // args[0]
            String name = "//127.0.0.1/Compute";


            Compute comp = (Compute) Naming.lookup(name);
            MyTask myTask = new MyTask();
            comp.executeTask(myTask);
            System.out.println("\u001B[32m"+"[INFO] BINDED SUCCESSFULLY THE COMPUTE ENGINE"+"\u001B[0m");
            // Exec
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
