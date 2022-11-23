import client.ComputeMyTask;
import engine.ComputeEngine;

public class GameOfLife {
    Server se;
    Communicator co;
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;
    final int MAX_CELLS_PROCESS = 1000;


    public void createServer(){
        try {
            // Configuration du serveur
            this.se.setup(this.portActivationSystem,this.portRegistry,this.fichierConfigPolicy);
            System.out.println("\u001B[34m"+"[INFO] CREATING THE SERVER..."+"\u001B[0m");
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("[ERROR] LA CONFIGURATION DU SERVEUR A ECHOUE");
            System.exit(0);

        }
    }

    public void createCommunicator(){
        // Création du communicateur
       this.co = new Communicator(null,this);

       // Connexion du communicateur au serveur
       this.co.connect("localhost",this.portRegistry);
    }


    public static void main(String[] args) {

        GameOfLife jdlv = new GameOfLife();
        int nbCalculators = 4;
        int sizeX = jdlv.MAX_CELLS_PROCESS;
        int sizeY = jdlv.MAX_CELLS_PROCESS;
        jdlv.portActivationSystem = 50000;
        jdlv.portRegistry = 50001;
        jdlv.fichierConfigPolicy = "gameOfLife.policy";

        // Création et paramétrage du serveur
        jdlv.se = new Server();
        jdlv.createServer();

        try {
            // Création de l'objet partagé
            TabCellule tabCellule = jdlv.se.createSharedObject();

            // Démarrage du serveur
            jdlv.se.start(tabCellule);

        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE DEMARRER LE SERVEUR");

            e.printStackTrace();
            System.exit(0);

        }

        // Création du communicateur (pour faire dialoguer les calculateurs entre eux et envoyer les données au Disaplyer)
        jdlv.createCommunicator();


        // Création du compute engine
        try {
            System.out.println("\u001B[34m"+"[INFO] LINKING THE COMPUTE ENGINE..."+"\u001B[0m");
            ComputeEngine ce = new ComputeEngine();
            ce.start();

        }catch (Exception e){
            e.printStackTrace();
        }

        // Création du client pour le compute engine
        try {
            System.out.println("\u001B[34m"+"[INFO] BINDING THE COMPUTE ENGINE..."+"\u001B[0m");
            ComputeMyTask cmt = new ComputeMyTask();
            cmt.start();

        }catch (Exception e){
            e.printStackTrace();
        }

        // Création des calculateurs
        jdlv.co.initCalculators(nbCalculators,sizeX,sizeY);




    }
}
