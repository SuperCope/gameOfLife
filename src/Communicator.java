import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Communicator {
    TabCellule tabCellulles;
    GameOfLife gameOfLife;
    Registry registry;
    Calculator[] calculators;


    public Communicator(TabCellule tabCellulles, GameOfLife gameOfLife){
        this.tabCellulles = tabCellulles;
    }
    public void connect(String machine,int port){
        try {
            this.registry = LocateRegistry.getRegistry(machine , port);

            System.out.println("[INFO] CONNECTED TO THE SERVER");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createTabCellules(){
        try {
            // On récupère l'objet partagé du serveur
            this.tabCellulles = (TabCellule) this.registry.lookup("InitialTabCells");
            // On l'initialise

            // On pousse l'objet sur le serveur
            this.pushTabCellules();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pushTabCellules(){
        try {
            // On pousse l'objet sur le serveur
            this.registry.bind("TabCells", this.tabCellulles);
            System.out.println("[INFO] BINDING SUCCESS");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public TabCellule getTabCellulles(){
        return this.tabCellulles;
    }
    public GameOfLife getJeuDeLaVie(){
        return this.gameOfLife;
    }

}
