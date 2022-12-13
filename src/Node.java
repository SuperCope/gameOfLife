import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public class Node {
    Object sw = 0;
    Object nw = 0;
    Object ne = 0;
    Object se = 0;
    int depth;
    int area;
    int id;
    boolean canonical;

    public Node() {
        this.depth = 1;
    }



    public Node(Object sw, Object se, Object nw, Object ne,int id) {
        this.sw = sw;
        this.se = se;
        this.nw = nw;
        this.ne = ne;


        this.id = id;
        this.canonical = false;



        if(this.sw.getClass()==Node.class){
            this.depth = (Node.class.cast(this.sw)).getDepth() + 1;

        }



        if (this.getSw() instanceof Node && getSw() != (Object) 0) {
            this.area += ((Node )getSw()).getArea();
        }
        if (this.getSe() instanceof Node && getSe() != (Object) 0) {
            this.area += ((Node )getSe()).getArea();
        }
        if (this.getNw() instanceof  Node && getNw() !=(Object) 0) {
            this.area += ((Node )getNw()).getArea();
        }
        if (this.getNe() instanceof Node &&  getNe() != (Object) 0) {
            this.area += ((Node )getNe()).getArea();
        }
    }
    public Node(Object sw, Object se, Object nw, Object ne) {
        this.sw = sw;
        this.se = se;
        this.nw = nw;
        this.ne = ne;
        this.canonical = true;

        this.depth = 1;

        if (getSw() != (Object) 0) {
            this.area++;
        }
        if (getSe() != (Object) 0) {
            this.area++;
        }
        if (getNw() != (Object) 0) {
            this.area++;
        }
        if (getNe() != (Object) 0) {
            this.area++;
        }
    }

    public void setSw(Object c) {
        this.sw = c;
    }

    public void setNw(Object c) {
        this.nw = c;
    }

    public void setNe(Object c) {
        this.ne = c;
    }

    public void setSe(Object c) {
        this.se = c;
    }

    public Object getSw() {
        return this.sw;
    }

    public Object getNw() {
        return nw;
    }

    public Object getNe() {
        return ne;
    }

    public Object getSe() {
        return se;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getArea() {
        return this.area;
    }

    public int getDepth() {
        return this.depth;
    }


    // Overriding equals() to compare two Complex objects
    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }
        if (o != null) {
            Function.identity();
            if (this.getSw() == ((Node) o).getSw() && this.getSe() == ((Node) o).getSe() && this.getNw() == ((Node) o).getNw() && this.getNe() == ((Node) o).getNe()) {
                return true;
            }
        }

        return false;

    }

    @Override
    public int hashCode() {
        Object sw3 = 0;
        Object se3 = 0;
        Object nw3 = 0;
        Object ne3 = 0;


        if(this.sw!=(Object) 0){
            sw3 = this.sw;
        }
        if(this.se!=(Object) 0){
            se3 = this.se;
        }
        if(this.nw!=(Object) 0){
            nw3 = this.nw;
        }
        if(this.ne!=(Object) 0){
            ne3 = this.ne;
        }

        Object[] nu =  {sw3,se3, nw3,ne3,this.id};

            return Objects.hash(nu);
        }




    /*public String toString() {
        String display = "";
        if(this.nw != null){
            display+="X";
        }else{
            display+=".";
        }
        display+= " ";
        if(this.ne != null){
            display+="X";
        }else{
            display+=".";
        }
        display+= "\n";
        if(this.sw != null){
            display+="X";
        }else{
            display+=".";
        }
        display+= " ";
        if(this.se != null){
            display+="X";
        }else{
            display+=".";
        }

        return display;
    }
*/
}
