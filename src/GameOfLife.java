import client.ComputeMyTask;
import engine.ComputeEngine;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameOfLife {
    Server se;
    Communicator co;
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;
    final int MAX_CELLS_PROCESS = 10;
    int nbCalculators;
    int sizeX;
    int sizeY;
    int size;


    public void createServer(){
        // Création du compute engine
        /* ComputeEngine ce = null;
        try {
            System.out.println("\u001B[34m"+"[INFO] LINKING THE COMPUTE ENGINE..."+"\u001B[0m");
            ce = new ComputeEngine();
            ce.start();

        }catch (Exception e){
            e.printStackTrace();
        }*/
        try {
            // Configuration du serveur
            this.se.setup(this.portActivationSystem,this.portRegistry,this.fichierConfigPolicy,null);
            System.out.println("\u001B[34m"+"[INFO] CREATING THE SERVER..."+"\u001B[0m");
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("[ERROR] LA CONFIGURATION DU SERVEUR A ECHOUE");
            System.exit(0);

        }
    }

    public void createCommunicator(){
        // Création du communicateur
        ExecutorService executorService = Executors.newFixedThreadPool(this.nbCalculators);
       this.co = new Communicator(null,executorService);

       // Connexion du communicateur au serveur
       this.co.connect("localhost",this.portRegistry);
    }


    public void setParams(){
        this.nbCalculators = 4;
        this.sizeX = 4000;
        this.sizeY = 4000;
        this.portActivationSystem = 50000;
        this.portRegistry = 50001;
        this.fichierConfigPolicy = "gameOfLife.policy";
    }
    public void init(){
        // Création et paramétrage du serveur
        this.se = new Server();
        this.createServer();

        try {
            // Création des objets partagé
            TabCellule tabCellule = this.se.createSharedObject();



            // Démarrage du serveur
            this.se.start(tabCellule);

        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE DEMARRER LE SERVEUR");

            e.printStackTrace();
            System.exit(0);

        }

        // Création du communicateur (pour faire dialoguer les calculateurs entre eux et envoyer les données au Disaplyer)
        this.createCommunicator();


        // Création des calculateurs
        this.co.createCalculators(nbCalculators,sizeX,sizeY);

    }
    public static void main(String[] args) {

        GameOfLife jdlv = new GameOfLife();
        jdlv.setParams();
        jdlv.init();
        try {
            jdlv.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        this.co.generation = 1;
        this.co.generationTarget = 2;

        while(this.co.generation<this.co.generationTarget){
            // On récupère les cellules
            this.co.tabCellulles  = (TabCellule) this.co.registry.lookup("TabCells"+(this.co.generation-1));

            // On redécoupe
/*            for(int numCalculateur= 0;numCalculateur<nbCalculators;numCalculateur++) {
                Cellule[][] cellules = new Cellule[sizeX/nbCalculators][sizeY/nbCalculators];
                for (int i = 0; i < sizeX/nbCalculators; i++) {
                    for (int j = 0; j < sizeY/nbCalculators; j++) {


                        cellules[i][j] = this.co.tabCellulles.getCellules()[i][j];

                    }
                }
                this.co.calculators[numCalculateur].setCellules(cellules);
            }*/

            while(!this.co.analyseNeightbors()){
                // On applique les règles du jeu de la vie
            }
            // On réassemble les tableaux de cellules des calculateurs
            HashSet<Cellule> nouvCellules = new HashSet<Cellule>(sizeX);

//            for(int numCalculator = 0;numCalculator<nbCalculators;numCalculator++){
//                Cellule[][] cellulesCalculateur = this.co.calculators[numCalculator].getCellules();
//
//                for(int row = 0;row<cellulesCalculateur.length;row++){
//                    for(int col = 0;col<cellulesCalculateur[0].length;col++){
//
//                        nouvCellules[row][col] = cellulesCalculateur[row][col];
//
//                    }
//                }
//            }
            this.co.tabCellulles.setCellules(nouvCellules);

            // On met à jour le registry
            this.co.registry.bind("TabCells"+this.co.generation, this.co.tabCellulles);


            // On met à jour la génération
            this.co.generation++;


        }
    }

}
