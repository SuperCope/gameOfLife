import javafx.concurrent.Task;

import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;

public class JeuDeLaVie {
    Server se;
    Communicator co;
    int portActivationSystem;
    int portRegistry;
    String fichierConfigPolicy;
    int sizeX;
    int sizeY;
    public void createServer(){
        this.se = new Server();
        try {
            this.se.setup(this.portActivationSystem,this.portRegistry,this.fichierConfigPolicy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startServer(){
        try{
            System.out.println("OK");
            this.se.start();
            System.out.println("OK 2");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createSharer(){

       this.co = new Communicator(null,this);
       this.co.connect("localhost",this.portRegistry);
    }

    public static void main(String[] args) {

        JeuDeLaVie jdlv = new JeuDeLaVie();
        jdlv.portActivationSystem = 50000;
        jdlv.portRegistry = 50001;
        jdlv.fichierConfigPolicy = "jeuDeLaVie.policy";
        jdlv.createServer();
        jdlv.startServer();
        jdlv.createSharer();

    }
}
