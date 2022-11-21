import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Communicator {
    TabCellule tabCellulles;
    JeuDeLaVie jeuDeLaVie;


    public Communicator(TabCellule tabCellulles,JeuDeLaVie jeuDeLaVie){
        this.tabCellulles = tabCellulles;
    }
    public void connect(String machine,int port){
        try {
            Registry registry = LocateRegistry.getRegistry(machine , port);
            this.tabCellulles = (TabCellule) registry.lookup("CelluleActivatable");
            this.tabCellulles.setSizeX(4);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public TabCellule getTabCellulles(){
        return this.tabCellulles;
    }
    public JeuDeLaVie getJeuDeLaVie(){
        return this.jeuDeLaVie;
    }

}
