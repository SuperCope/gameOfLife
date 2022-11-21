
public class GameOfLife {
    Server se;
    Communicator co;
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;

    public void createServer(){
        try {
            // Configuration du serveur
            this.se.setup(this.portActivationSystem,this.portRegistry,this.fichierConfigPolicy);
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
    public TabCellule initTabCellules(TabCellule tabCellules,int sizeX,int sizeY){

        try {
            // Création des cellules
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
            System.exit(0);

        }
        return tabCellules;
    }

    public static void main(String[] args) {

        GameOfLife jdlv = new GameOfLife();
        int sizeX = 500;
        int sizeY = 500;
        jdlv.portActivationSystem = 50000;
        jdlv.portRegistry = 50001;
        jdlv.fichierConfigPolicy = "gameOfLife.policy";

        // Création et paramétrage du serveur
        jdlv.se = new Server();
        jdlv.createServer();

        try {
            // Création de l'objet partagé
            TabCellule tabCellule = jdlv.se.createSharedObject();
            tabCellule = jdlv.initTabCellules(tabCellule,sizeX,sizeY);

            // Démarrage du serveur
            jdlv.se.start(tabCellule);

        }catch (Exception e){
            System.err.println("[ERROR] IMPOSSIBLE DE DEMARRER LE SERVEUR");

            e.printStackTrace();
            System.exit(0);

        }

        // Création du communicateur (pour faire dialoguer les calculateurs entre eux et envoyer les données au Disaplyer)
        jdlv.createCommunicator();

        // Création des calculateurs
        jdlv.co.initCalculators();

    }
}
