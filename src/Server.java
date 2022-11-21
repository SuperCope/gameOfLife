import java.rmi.Naming;
import java.rmi.activation.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server
{
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;

    Registry registry;



    public void setup(int portActivationSystem,int portRegistry,String fichierConfigPolicy){
        this.portActivationSystem = portActivationSystem;
        this.portRegistry = portRegistry;
        this.fichierConfigPolicy = fichierConfigPolicy;
        System.setProperty("java.security.policy", fichierConfigPolicy);
    }
    public void start() throws Exception{
        ActivationGroupDesc agroupdesc = new ActivationGroupDesc(null,null);
        ActivationSystem asystem =(ActivationSystem) Naming.lookup("//:"+portActivationSystem+"/java.rmi.activation.ActivationSystem");
        ActivationGroupID agi =  asystem.registerGroup(agroupdesc);

        ActivationGroup.createGroup(agi,agroupdesc,0);

        ActivationDesc objectDesc = new ActivationDesc(agi,"TabCellulleImpl","",null);

        TabCellule tabCellule = (TabCellule)Activatable.register(objectDesc);

        this.registry = LocateRegistry.createRegistry(portRegistry);

        this.registry.bind("CelluleActivatable", tabCellule);
        System.out.println("SERVER RUNNING");
    }
}


