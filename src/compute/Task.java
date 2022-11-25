package compute;
import java.io.Serializable;
import java.util.concurrent.Callable;

public interface Task extends Serializable, Callable
{
    Object execute();
    public String call();
}