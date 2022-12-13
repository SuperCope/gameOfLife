import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.concurrent.*;

public class Communicator {
    Object rootNode;
    Registry registry;
    ExecutorService executor;
    Calculator[] calculators;
    int generation;
    int generationTarget;


    public Communicator(Object rootNode, int generation, int generationTarget) {
        this.calculators = new Calculator[4];
        this.rootNode = rootNode;
        this.executor = Executors.newFixedThreadPool(this.calculators.length);
        this.generation = generation;
        this.generationTarget = generationTarget;

    }


    public Node connect(String machine, int port) {
        Node rootNode = null;
        try {
            this.registry = LocateRegistry.getRegistry(machine, port);

            rootNode = (Node) this.registry.lookup("rootNode");

            System.out.println("\u001B[32m" + "[INFO] CONNECTED TO THE SERVER" + "\u001B[0m");
        } catch (Exception e) {
            System.err.println("[ERROR] IMPOSSIBLE DE SE CONNECTER AU SERVEUR");
            e.printStackTrace();
            System.exit(0);

        }
        return rootNode;
    }

    public void createCalculators(Object rootNode) {
        try {
            Object nw = ((Node) rootNode).getNw();
            this.calculators[0] = new Calculator(nw);

            Object ne = ((Node) rootNode).getNe();
            this.calculators[1] = new Calculator(ne);

            Object sw = ((Node) rootNode).getSw();
            this.calculators[2] = new Calculator(sw);

            Object se = ((Node) rootNode).getSe();
            this.calculators[3] = new Calculator(se);
            System.out.println("\u001B[32m" + "[INFO] CALCULATORS SUCCESSFULLY CREATED" + "\u001B[0m");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void runTasks() {
        for (Calculator calculator : this.calculators) {
            this.executor.execute(calculator.getFutureTask());
        }
    }
    public void runExecutor(){
        while(this.generation <= this.generationTarget){
            try{
                boolean isReady = false;

                for (Calculator calculator : this.calculators) {
                    if (calculator.task.isDone()) {
                        isReady = true;
                    } else {
                        isReady = false;
                    }
                }
                if(isReady){
                    this.executor.shutdown();
                    this.executor = Executors.newFixedThreadPool(this.calculators.length);
                    this.buildRootNode();
                    this.pushToServer();

                    for (Calculator calculator : this.calculators) {
                        calculator.setFutureTask(new FutureTask<String>(calculator));
                    }
                    this.generation++;
                    this.runTasks();
                }


            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void buildRootNode() {
        Object nw = this.calculators[0].getNode();
        Object ne = this.calculators[1].getNode();
        Object sw = this.calculators[2].getNode();
        Object se = this.calculators[3].getNode();

        if (nw != (Object) 0) ((ServerNode) this.rootNode).setNw(nw);
        if (ne != (Object) 0) ((ServerNode) this.rootNode).setNe(ne);
        if (se != (Object) 0) ((ServerNode) this.rootNode).setSe(se);
        if (sw != (Object) 0) ((ServerNode) this.rootNode).setSw(sw);

    }

    public void pushToServer() {
        try {
            // On pousse l'objet sur le serveur
            this.registry.bind("rootNode" + this.generation, (Node)this.rootNode);
            System.out.println("\u001B[32m" + "[INFO] CALCULATOR BINDED SUCCESSFULLY" + "\u001B[0m");
        } catch (Exception e) {
            System.err.println("[ERROR] LE BIND A ECHOUE");
            e.printStackTrace();
            System.exit(0);

        }
    }
}
