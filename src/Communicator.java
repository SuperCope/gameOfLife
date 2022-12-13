import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Communicator {
    Object tabCellulles;
    Registry registry;
    ExecutorService executor;
    Calculator[] calculators;
    int generation = 0;
    int generationTarget = 400;
    final int MAX_CELLS_PROCESS = 10;


    public Communicator(Object tabCellulles,ExecutorService executor){
        this.tabCellulles = tabCellulles;
        this.executor = executor;
    }

    public void connect(String machine,int port){
        try {
            this.registry = LocateRegistry.getRegistry(machine , port);

            System.out.println("\u001B[32m"+"[INFO] CONNECTED TO THE SERVER"+"\u001B[0m");
        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE SE CONNECTER AU SERVEUR");
            e.printStackTrace();
            System.exit(0);

        }
    }
}
