import java.util.ArrayList;

public class Node {
    Node sw = null;
    Node nw = null;
    Node ne = null;
    Node se = null;
    int depth;
    int area;

    public Node(){
        this.depth = 1;
        if(sw.getSw()!=null){
            this.area++;
        }
        if(sw.getSe()!=null){
            this.area++;
        }
        if(sw.getNw()!=null){
            this.area++;
        }
        if(sw.getNe()!=null){
            this.area++;
        }
    }
    public Node(Node sw,Node se,Node nw,Node ne){
        this.sw = sw;
        this.se = se;
        this.nw = nw;
        this.ne = ne;

        if(sw.getSw() == this.getSw() && sw.getSe() == this.getSe() && sw.getNw() == this.getNw() && sw.getNe() == this.getNe()){
            this.depth = this.sw.getDepth()+1;
            this.area = sw.getArea() + se.getArea() + nw.getArea() + ne.getArea();
        }else{
            this.depth = 1;
            if(sw.getSw()!=null){
                this.area++;
            }
            if(sw.getSe()!=null){
                this.area++;
            }
            if(sw.getNw()!=null){
                this.area++;
            }
            if(sw.getNe()!=null){
                this.area++;
            }
        }

    }

    public void setSw(Node c){
        this.sw = c;
    }
    public void setNw(Node c){
        this.nw = c;
    }
    public void setNe(Node c){
        this.ne = c;
    }
    public void setSe(Node c){
        this.se = c;
    }

    public Node getSw(){
        return this.sw;
    }
    public Node getNw(){
        return nw;
    }
    public Node getNe(){
        return ne;
    }
    public Node getSe(){
        return se;
    }

    public void setArea(int area){
        this.area = area;
    }
    public void setDepth(int depth){
        this.depth = depth;
    }

    public int getArea(){
        return this.area;
    }
    public int getDepth(){
        return this.depth;
    }



}
