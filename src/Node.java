import java.rmi.Remote;
import java.util.Objects;

public interface RootNode extends Remote {
    Object sw = 0;
    Object nw = 0;
    Object ne = 0;
    Object se = 0;
    int depth = -1;
    int area = -1;
    int id = -1;

    public void setSw(Object c);
    public void setNw(Object c);
    public void setNe(Object c);
    public void setSe(Object c);
    public Object getSw();
    public Object getNw();
    public Object getNe();
    public Object getSe();
    public void setArea(int area);
    public void setDepth(int depth);
    public int getArea();
    public int getDepth();
    @Override
    public boolean equals(Object o);
    @Override
    public int hashCode();
    public String toString();
}
