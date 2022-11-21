
public class GameOfLife {
    Server se;
    Communicator co;
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;

    public void createServer(){
        this.se = new Server();
        try {
            this.se.setup(this.portActivationSystem,this.portRegistry,this.fichierConfigPolicy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startServer(TabCellule tabCellule){
        try{
            this.se.start(tabCellule);
            System.out.println("[INFO] SERVER STARTED");
        }catch (Exception e){
            System.err.println("[ERROR] VEUILLEZ VERIFIER QUE LE SERVEUR RMID SOIT BIEN LANCE (Cf README.md)");
        }
    }
    public void createCommunicator(){
       this.co = new Communicator(null,this);
       this.co.connect("localhost",this.portRegistry);
       this.co.createTabCellules();
    }
    public TabCellule initTabCellules(TabCellule tabCellules,int sizeX,int sizeY){

        try {
            Cellule[][] cellules = new Cellule[sizeX][sizeY];
            for (int numRow = 0; numRow < sizeX; numRow++) {
                for (int numCol = 0; numCol < sizeY; numCol++) {
                    cellules[numRow][numCol] = new Cellule(numRow,numCol,0,false);
                }
            }


            tabCellules.setCellules(cellules);
        }catch (Exception e){
            System.err.println("[ERROR] ERREUR LORS DE L'INITIALISATION DES CELLULES");
            e.printStackTrace();
        }
        return tabCellules;
    }

    public static void main(String[] args) {

        GameOfLife jdlv = new GameOfLife();
        int sizeX = 500;
        int sizeY = 500;
        jdlv.portActivationSystem = 50000;
        jdlv.portRegistry = 50001;
        jdlv.fichierConfigPolicy = "jeuDeLaVie.policy";

        // Création du serveur
        jdlv.createServer();

        try {
            // Création de l'objet partagé
            TabCellule tabCellule = jdlv.se.createSharedObject();
            tabCellule = jdlv.initTabCellules(tabCellule,sizeX,sizeY);

            // Démarrage du serveur
            jdlv.se.start(tabCellule);

        }catch (Exception e){
            e.printStackTrace();
        }

        // Création du communicateur (pour faire dialoguer les calculateurs entre eux et envoyer les données au Disaplyer)
        jdlv.createCommunicator();

    }
}
