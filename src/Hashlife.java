import java.util.ArrayList;

public class Hashlife {
    ArrayList<Node> memoNodes;
    ArrayList<Node> memoRes;
    int generation = 0;
    int worldDepth = 0;
    boolean fastForward = false;
    Node[][] CM = new Node[6][6];
    Node[][] CMr = new Node[6][6];

    Node[] CNList = {
            new Node(null,null,null,null),
            new Node(null,null,null,new Node()),
            new Node(null,null,new Node(),null),
            new Node(null,null,new Node(),new Node()),
            new Node(null,new Node(),null,null),
            new Node(null,new Node(),null,new Node()),
            new Node(null,new Node(),new Node(),null),
            new Node(null,new Node(),new Node(),new Node()),
            new Node(new Node(),null,null,null),
            new Node(new Node(),null,null,new Node()),
            new Node(new Node(),null,new Node(),null),
            new Node(new Node(),null,new Node(),new Node()),
            new Node(new Node(),new Node(),null,null),
            new Node(new Node(),new Node(),null,new Node()),
            new Node(new Node(),new Node(),new Node(),null),
            new Node(new Node(),new Node(),new Node(),new Node())
    };
    public Node pickCannonical(Node sw, Node se, Node nw, Node ne) {

        Node candidat = null;
        for(int i = 0;i<CNList.length;i++){
            if(sw==CNList[i].getSw() && se==CNList[i].getSe() && nw==CNList[i].getNw() && ne==CNList[i].getNe()){
                candidat = CNList[i];
            }
        }
        return candidat;
    }
    Node createNode(Node sw,Node se,Node nw,Node ne){
        Node node;
        if(sw==null){
            node = this.pickCannonical(sw,se,nw,ne);
        }else{
            node = new Node(sw,se,nw,ne);
        }

        if(memoRes.contains(node)){
            return memoNodes.get(memoNodes.indexOf(node));
        }
        memoNodes.set(memoNodes.indexOf(node),node);
        return node;
    }

    Node generateCanonical0(int depth){
        if(depth==0){
            return null;
        }
        Node n1 = CNList[0];
        for(int i = 2;i<depth+1;i++){
            n1 = this.createNode(n1,n1,n1,n1);
        }
        return n1;
    }

    Node addBorder(Node n){
        int depth = n.getDepth();
        Node nodeBorder =  generateCanonical0(depth-1);
        Node resSW = createNode(nodeBorder,nodeBorder,nodeBorder,n.getSw());
        Node resSE = createNode(nodeBorder,nodeBorder,n.getSe(),nodeBorder);
        Node resNW = createNode(nodeBorder,n.getNw(),nodeBorder,nodeBorder);
        Node resNE = createNode(n.getNe(),nodeBorder,nodeBorder,nodeBorder);

        return createNode(n.getSw(),n.getSe(),n.getNw(),n.getNe());
    }

    Node getCenterNode(Node node){
        return createNode(node.getSw().getNe(),node.getSe().getNw(),node.getNw().getSe(),node.getNe().getSw());
    }

    void processAuxiliaryMatrix(){
        Node[][] M = CM;
        Node[][] Mr = CMr;
        Node[] neighbours = new Node[9];
        for(int i = 1;i<5;i++){
            for(int j = 1;j<5;j++){
                neighbours[0] = M[i-1][j-1];
                neighbours[1] = M[i][j-1];
                neighbours[2] = M[i+1][j-1];
                neighbours[3] = M[i+1][j];
                neighbours[4] = M[i+1][j+1];
                neighbours[5] = M[i][j+1];
                neighbours[6] = M[i-1][j+1];
                neighbours[7] = M[i-1][j];

                Node res = M[i][j];
                int nAlive = 0;
                for(int k = 0;i<neighbours.length;k++){
                    if(neighbours[k]!=null){
                        nAlive++;
                    }
                }
                if (nAlive < 2 || nAlive > 3){
                    res = null;
                }else{
                    res = new Node();
                }
                Mr[i][j] =res;
            }
        }
    }

    void nodeToAuxiliaryMatrix(Node node){
        CM[1][1] = node.getSw().getSw();
        CM[1][2] = node.getSw().getSe();
        CM[1][3] = node.getSe().getSw();
        CM[1][4] = node.getSe().getSe();

        CM[2][1] = node.getSw().getNw();
        CM[2][2] = node.getSw().getNe();
        CM[2][3] = node.getSe().getNw();
        CM[2][4] = node.getNe().getNe();

        CM[3][1] = node.getNw().getSw();
        CM[3][2] = node.getNw().getSe();
        CM[3][3] = node.getNe().getSw();
        CM[3][4] = node.getNe().getSe();

        CM[4][1] = node.getNw().getNw();
        CM[4][2] = node.getNw().getNe();
        CM[4][3] = node.getNe().getNw();
        CM[4][4] = node.getNe().getNe();
    }

    Node auxiliaryMatrixToNode(){
        Node[][] M = CMr;

        Node sw = createNode(M[1][1], M[1][2], M[2][1], M[2][2]);
        Node se = createNode(M[1][3], M[1][4], M[2][3], M[2][4]);
        Node nw = createNode(M[3][1], M[3][2], M[4][1], M[4][2]);
        Node ne = createNode(M[3][3], M[3][4], M[4][3], M[4][4]);

        return createNode(sw, se, nw, ne);
    }

    Node stepNode(Node node){
        if(node.getDepth() == worldDepth){
            generation +=  Math.pow(2,worldDepth-2);
        }
        if(node.getArea()==0){
            return getCenterNode(node);
        }
        if(memoRes.contains(node)){
            return memoRes.get(memoRes.indexOf(node));
        }
        if(node.getDepth()==2){
            nodeToAuxiliaryMatrix(node);
            processAuxiliaryMatrix();
            Node result = getCenterNode(auxiliaryMatrixToNode());

            return null;
        }else{
            Node node11 = createNode(node.sw.sw, node.sw.se, node.sw.nw, node.sw.ne);
            Node node21 = createNode(node.sw.nw, node.sw.ne, node.nw.sw, node.nw.se);
            Node node31 = createNode(node.nw.sw, node.nw.se, node.nw.nw, node.nw.ne);

            Node node12 = createNode(node.sw.se, node.se.sw, node.sw.ne, node.se.nw);
            Node node22 = createNode(node.sw.ne, node.se.nw, node.nw.se, node.ne.sw);
            Node node32 = createNode(node.nw.se, node.ne.sw, node.nw.ne, node.ne.nw);

            Node node13 = createNode(node.se.sw, node.se.se, node.se.nw, node.se.ne);
            Node node23 = createNode(node.se.nw, node.se.ne, node.ne.sw, node.ne.se);
            Node node33 = createNode(node.ne.sw, node.ne.se, node.ne.nw, node.ne.ne);

            Node res11 = stepNode(node11);
            Node res12 = stepNode(node12);
            Node res13 = stepNode(node13);
            Node res21 = stepNode(node21);
            Node res22 = stepNode(node22);
            Node res23 = stepNode(node23);
            Node res31 = stepNode(node31);
            Node res32 = stepNode(node32);
            Node res33 = stepNode(node33);

            Node result = null;
            if(fastForward){
                Node sw = stepNode(createNode( res11, res12, res21, res22 ) );
                Node se = stepNode(createNode( res12, res13, res22, res23 ) );
                Node nw = stepNode(createNode( res21, res22, res31, res32 ) );
                Node ne = stepNode(createNode( res22, res23, res32, res33 ) );

                result = createNode(sw,se,nw,ne);

            }else{
                Node sw = getCenterNode( createNode( res11, res12, res21, res22 ) );
                Node se = getCenterNode( createNode( res12, res13, res22, res23 ) );
                Node nw = getCenterNode( createNode( res21, res22, res31, res32 ) );
                Node ne = getCenterNode( createNode( res22, res23, res32, res33 ) );

                result = createNode(sw,se,nw,ne);
            }
            memoRes.set(memoNodes.indexOf(node),result);
            return result;

        }
    }

    ArrayList<Node> getNodeList(Node node,ArrayList<Node> nodeList){
        if(nodeList==null){
            nodeList = new ArrayList<Node>();
        }
        if(node.getArea()>0){
            if(node.getDepth()==1){
                nodeList.add(node);
            }else{
                if(node.getSw().getArea()>0){
                    nodeList.set(nodeList.indexOf(node),node.getSw());
                }
                if(node.getSe().getArea()>0){
                    nodeList.set(nodeList.indexOf(node),node.getSe());
                }
                if(node.getNw().getArea()>0){
                    nodeList.set(nodeList.indexOf(node),node.getNw());
                }
                if(node.getNe().getArea()>0){
                    nodeList.set(nodeList.indexOf(node),node.getNe());
                }
            }
        }
        return nodeList;
    }

    Node cleanBorders(Node node){
        Node resNode = node;
        boolean keepGoing = true;
        while (keepGoing){
            Node centerNode = getCenterNode(resNode);
            if(resNode.getArea()==centerNode.getArea()){
                resNode  =centerNode;
            }else {
                keepGoing = false;
            }
        }
        return resNode;
    }

    public static void main(String[] args) {
        
    }


}
