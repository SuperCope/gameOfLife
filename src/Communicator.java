import client.ComputeMyTask;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Communicator {
    TabCellule tabCellulles;
    Registry registry;
    Calculator[] calculators;
    int generation = 0;
    int generationTarget = 400;
    final int MAX_CELLS_PROCESS = 10;
    ExecutorService executor;


    public Communicator(TabCellule tabCellulles,ExecutorService executor){
        this.tabCellulles = tabCellulles;
        this.executor = executor;
    }
    public void connect(String machine,int port){
        try {
            this.registry = LocateRegistry.getRegistry(machine , port);

            this.tabCellulles = (TabCellule) this.registry.lookup("TabCells0");

            System.out.println("\u001B[32m"+"[INFO] CONNECTED TO THE SERVER"+"\u001B[0m");
        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE SE CONNECTER AU SERVEUR");
            e.printStackTrace();
            System.exit(0);

        }
    }
    public boolean analyseNeightbors(){

        this.executor = Executors.newFixedThreadPool(this.calculators.length);
        for(int numCalculator = 0;numCalculator<this.calculators.length;numCalculator++){
            // System.out.println("LAUNCH");
            this.executor.execute(this.calculators[numCalculator].getFutureTask());
        }
        while (true){
            try{
                boolean end = true;
                for(int numCalculator = 0;numCalculator<this.calculators.length;numCalculator++){
                    if(!this.calculators[numCalculator].getFutureTask().isDone()){
                        end = false;
                    }
                }
                if(end){
                    System.out.println("END OF GENERATION"+this.generation);
                    this.executor.shutdown();
                    for(int numCalculator = 0;numCalculator<this.calculators.length;numCalculator++) {
                        this.calculators[numCalculator].setFutureTask(new FutureTask<String>(this.calculators[numCalculator]));
                    }
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void createCalculators(int nbCalculators,int sizeX,int sizeY){

        System.out.println("\u001B[34m"+"[INFO] CREATING THE CALCULATORS..."+"\u001B[0m");

        try {

            HashSet<Cellule> cellulesTot = new HashSet<Cellule>(sizeX);
            this.calculators = new Calculator[nbCalculators];
            for(int numCalculateur= 0;numCalculateur<nbCalculators;numCalculateur++) {
                HashSet<Cellule> cellules = new HashSet<Cellule>(sizeX/nbCalculators);



                calculators[numCalculateur] = new Calculator(numCalculateur,cellules,null);

            }


            this.tabCellulles.setCellules(cellulesTot);



            System.out.println("\u001B[32m"+"[INFO] CALCULATORS CREATED"+"\u001B[0m");


        }catch (Exception e){
            System.err.println("[ERROR] ERREUR LORS DE LA CREATION DES CALCULATEURS");
            e.printStackTrace();
            System.exit(0);

        }

    }

    public void updateTabCellules(){

        try {
            // On pousse l'objet sur le serveur
            this.registry.bind("TabCells0", this.tabCellulles);
            System.out.println("\u001B[32m"+"[INFO] BINDED SUCCESSFULLY"+"\u001B[0m");
        }catch (Exception e){
            System.err.println("[ERROR] LE BIND A ECHOUE");
            e.printStackTrace();
            System.exit(0);

        }
    }



    public boolean allNeighborsSeen() {
        return false;
    }
}
