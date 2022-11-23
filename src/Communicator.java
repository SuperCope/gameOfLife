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

            System.out.println("\u001B[32m"+"[INFO] CONNECTED TO THE SERVER"+"\u001B[0m");
        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE SE CONNECTER AU SERVEUR");
            e.printStackTrace();
            System.exit(0);

        }
    }

    public void initCalculators(int nbCalculators,int sizeX,int sizeY){
        System.out.println("\u001B[34m"+"[INFO] CREATING THE CALCULATORS..."+"\u001B[0m");

        try {

            Cellule[][] cellulesTot = new Cellule[sizeX*nbCalculators][sizeY*nbCalculators];
            this.calculators = new Calculator[nbCalculators];
            for(int numCalculateur= 0;numCalculateur<nbCalculators;numCalculateur++) {
                Cellule[][] cellules = new Cellule[sizeX][sizeY];
                for (int i = 0; i < MAX_CELLS_PROCESS; i++) {
                    for (int j = 0; j < MAX_CELLS_PROCESS; j++) {
                        int indexTotX = numCalculateur * MAX_CELLS_PROCESS + i;
                        int indexTotY = numCalculateur * MAX_CELLS_PROCESS + j;

                        cellules[i][j] = new Cellule(i,j,0,false);
                        cellulesTot[indexTotX][indexTotY] = cellules[i][j];
                    }
                }

                calculators[numCalculateur] = new Calculator(numCalculateur,cellules);

            }
            this.tabCellulles.setCellules(cellulesTot);
            System.out.println("\u001B[32m"+"[INFO] CALCULATORS CREATED"+"\u001B[0m");


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
            System.out.println("\u001B[32m"+"[INFO] BINDED SUCCESSFULLY"+"\u001B[0m");
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
