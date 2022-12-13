import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Calculator implements Callable {

    Object node;
    FutureTask<String> task;
    Hashlife hashLife;

    public Calculator(Object node)  {
        this.node = node;
        this.task = new FutureTask<String>(this);
        this.hashLife = new Hashlife();
    }

    public String call() {
        process();
        // return the thread name executing this callable task
        return Thread.currentThread().getName();
    }

    public void process(){
        this.hashLife.setRootNode(this.node);
        this.hashLife.run();
    }

    public Object getNode(){
        return node;
    }

    public FutureTask<String> getFutureTask(){
        return this.task;
    }

    public void setFutureTask(FutureTask<String> task){
        this.task = task;
    }
}
