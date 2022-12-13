public class GameOfLife {
    Server server;
    Communicator communicator;
    Hashlife hashlife;
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;

    public static void main(String[] args) {
        GameOfLife gOL = new GameOfLife();
        gOL.setParams(50000,50001);
        gOL.setup("dataFiles/sample.mc");
        gOL.init();
        try {
            gOL.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup(String fileName) {
        this.hashlife = new Hashlife();
        this.hashlife.init(fileName);

        this.hashlife.createHashLife();
        this.server = new Server();
    }

    public void init() {
        Object rootNode = this.hashlife.getRootNode();
        // Création et paramétrage du serveur
        this.createServer();
        try {
            this.server.start(rootNode);

            // Création du communicateur (pour faire dialoguer les calculateurs entre eux et envoyer les données au Disaplyer)
            this.communicator = new Communicator(this.hashlife.getRootNode(),1,2);
            this.communicator.createCalculators(rootNode);
            this.communicator.connect("localhost", this.portRegistry);
        } catch (Exception e) {
            System.err.println("[ERROR] IMPOSSIBLE DE DEMARRER LE SERVEUR");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void start() {
        try {
            this.communicator.runTasks();
            this.communicator.runExecutor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParams(int portActivationSystem,int portRegistry) {
        this.portActivationSystem = portActivationSystem;
        this.portRegistry = portRegistry;
        this.fichierConfigPolicy = "gameOfLife.policy";
    }

    public void createServer() {
        // Création du compute engine
        try {
            // Configuration du serveur
            this.server.setup(this.portActivationSystem, this.portRegistry, this.fichierConfigPolicy);
            System.out.println("\u001B[34m" + "[INFO] CREATING THE SERVER..." + "\u001B[0m");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] LA CONFIGURATION DU SERVEUR A ECHOUE");
            System.exit(0);
        }
    }
}
