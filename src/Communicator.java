import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Communicator {
    TabCellule tabCellulles;
    GameOfLife gameOfLife;
    Registry registry;
    Calculator[] calculators;
    final int MAX_CELLS_PROCESS = 1000;


    public Communicator(TabCellule tabCellulles, GameOfLife gameOfLife){
        this.tabCellulles = tabCellulles;
    }
    public void connect(String machine,int port){
        try {
            this.registry = LocateRegistry.getRegistry(machine , port);

            this.tabCellulles = (TabCellule) this.registry.lookup("InitialTabCells");

            System.out.println("\u001B[32m"+"[SUCCESS] CONNECTED TO THE SERVER"+"\u001B[0m");
        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE SE CONNECTER AU SERVEUR");
            e.printStackTrace();
            System.exit(0);

        }
    }

    public void initCalculators(){
        try {
            int nbCells = this.tabCellulles.getSizeX() * this.tabCellulles.getSizeY();
            int nbCalculators = (int) (Math.ceil(nbCells / MAX_CELLS_PROCESS));

            calculators = new Calculator[nbCalculators];
            for(int numCalculator = 0;numCalculator<nbCalculators;numCalculator++){
                calculators[numCalculator] = new Calculator(numCalculator);
            }
        }catch (Exception e){
            System.err.println("[ERROR] ERREUR LORS DE LA CREATION DES CALCULATEURS");
            e.printStackTrace();
            System.exit(0);

        }
    }

    public void pushTabCellules(){

        try {
            // On pousse l'objet sur le serveur
            this.registry.bind("TabCells", this.tabCellulles);
            System.out.println("\u001B[32m"+"[SUCCESS] BINDED SUCCESSFULLY"+"\u001B[0m");
        }catch (Exception e){
            System.err.println("[ERROR] LE BIND A ECHOUE");
            e.printStackTrace();
            System.exit(0);

        }
    }
    public TabCellule getTabCellulles(){
        return this.tabCellulles;
    }
    public GameOfLife getJeuDeLaVie(){
        return this.gameOfLife;
    }

}
