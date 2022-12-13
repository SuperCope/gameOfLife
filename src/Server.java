import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;
    Registry registry;

    public void setup(int portActivationSystem, int portRegistry, String fichierConfigPolicy) {
        this.portActivationSystem = portActivationSystem;
        this.portRegistry = portRegistry;
        this.fichierConfigPolicy = fichierConfigPolicy;
        System.setProperty("java.security.policy", fichierConfigPolicy);
    }

    public void start(Object tabCellule) throws Exception {
        this.registry = LocateRegistry.createRegistry(portRegistry);

    }

    public Object createSharedObject() {
        Object tabCellule = null;
        try {
            ActivationGroupDesc agroupdesc = new ActivationGroupDesc(null, null);
            ActivationSystem asystem = (ActivationSystem) Naming.lookup("//:" + portActivationSystem + "/java.rmi.activation.ActivationSystem");
            ActivationGroupID agi = asystem.registerGroup(agroupdesc);

            ActivationGroup.createGroup(agi, agroupdesc, 0);

//            ActivationDesc objectDesc = new ActivationDesc(agi, "Object", "", null);
//
//            tabCellule = Activatable.register(objectDesc);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] IMPOSSIBLE DE DEMARRER LE SERVEUR");
            System.exit(0);
        }
        return tabCellule;
    }

}


