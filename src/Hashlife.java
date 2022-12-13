import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Hashlife {
    private ArrayList<Object> memoNodes = new ArrayList<>();

    private HashMap<Object, Object> memoRes = new HashMap<>();
    private int generation = 0;
    private int worldDepth;
    private Object rootNode;
    private Object[][] CM = new Object[6][6];
    private Object[][] CMr = new Object[6][6];

    ServerNode[] CNList = {
            new ServerNode(0, 0, 0, 0),
            new ServerNode(0, 0, 0, 1),
            new ServerNode(0, 0, 1, 0),
            new ServerNode(0, 0, 1, 1),
            new ServerNode(0, 1, 0, 0),
            new ServerNode(0, 1, 0, 1),
            new ServerNode(0, 1, 1, 0),
            new ServerNode(0, 1, 1, 1),
            new ServerNode(1, 0, 0, 0),
            new ServerNode(1, 0, 0, 1),
            new ServerNode(1, 0, 1, 0),
            new ServerNode(1, 0, 1, 1),
            new ServerNode(1, 1, 0, 0),
            new ServerNode(1, 1, 0, 1),
            new ServerNode(1, 1, 1, 0),
            new ServerNode(1, 1, 1, 1)
    };

    public ServerNode pickCannonical(Object sw, Object se, Object nw, Object ne) {
        ServerNode candidat = null;

        if (sw == null) {
            sw = 0;
        }
        if (se == null) {
            se = 0;
        }
        if (nw == null) {
            nw = 0;
        }
        if (ne == null) {
            ne = 0;
        }


        for (int i = 0; i < CNList.length; i++) {

            if (sw == CNList[i].sw && se == CNList[i].se && nw == CNList[i].nw && ne == CNList[i].ne) {
                candidat = CNList[i];
            }
        }
        if (candidat == null) {
            System.exit(0);
        }
        return candidat;
    }

    Object createNode(Object sw, Object se, Object nw, Object ne) {
        ServerNode node;

        if (sw instanceof Integer) {
            node = this.pickCannonical(sw, se, nw, ne);
        } else {
            node = new ServerNode(sw, se, nw, ne, memoNodes.size());
        }

        if (memoNodes.contains(node)) {
            return memoNodes.get(memoNodes.indexOf(node));
        }
        memoNodes.add(node);

        return node;
    }

    Object generateCanonical0(int depth) {
        if (depth == 0) {
            return 0;
        }
        Object n1 = CNList[0];
        for (int i = 2; i < depth + 1; i++) {
            n1 = this.createNode(n1, n1, n1, n1);
        }


        return n1;
    }

    Object addBorder(Object n) {
        int depth = ((ServerNode) n).getDepth();

        Object nodeBorder = generateCanonical0(depth - 1);

        Object resSW = createNode(nodeBorder, nodeBorder, nodeBorder, ((ServerNode) ((ServerNode) n).getSw()));
        Object resSE = createNode(nodeBorder, nodeBorder, ((ServerNode) ((ServerNode) n).getSe()), nodeBorder);
        Object resNW = createNode(nodeBorder, ((ServerNode) ((ServerNode) n).getNw()), nodeBorder, nodeBorder);
        Object resNE = createNode(((ServerNode) (ServerNode) n).getNe(), nodeBorder, nodeBorder, nodeBorder);

        return createNode(resSW, resSE, resNW, resNE);
    }

    Object getCenterNode(Object node) {
        return createNode(((ServerNode) ((ServerNode) node).getSw()).getNe(), ((ServerNode) ((ServerNode) node).getSe()).getNw(), ((ServerNode) ((ServerNode) node).getNw()).getSe(), ((ServerNode) ((ServerNode) node).getNe()).getSw());
    }

    void processAuxiliaryMatrix() {
        Object[][] M = CM;
        Object[][] Mr = CMr;
        Object[] neighbours = new Object[9];

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                neighbours[0] = M[i - 1][j - 1];
                neighbours[1] = M[i][j - 1];
                neighbours[2] = M[i + 1][j - 1];
                neighbours[3] = M[i + 1][j];
                neighbours[4] = M[i + 1][j + 1];
                neighbours[5] = M[i][j + 1];
                neighbours[6] = M[i - 1][j + 1];
                neighbours[7] = M[i - 1][j];

                Object res = M[i][j];
                int nAlive = 0;
                for (int k = 0; k < neighbours.length; k++) {
                    if (neighbours[k] != null && neighbours[k] != (Object) 0) {
                        nAlive++;
                    }
                }
                if (nAlive < 2 || nAlive > 3) {
                    System.out.println(("JE MEURS"));
                    res = 0;
                } else {
                    if (nAlive == 3) {
                        System.out.println(("JE NAIS"));
                        res = 1;
                    }
                }
                Mr[i][j] = res;
            }
        }

    }

    /**
     *
     * @param node
     */
    void nodeToAuxiliaryMatrix(Object node) {
        CM[1][1] = ((ServerNode) ((ServerNode) node).getSw()).getSw();
        CM[1][2] = ((ServerNode) ((ServerNode) node).getSw()).getSe();
        CM[1][3] = ((ServerNode) ((ServerNode) node).getSe()).getSw();
        CM[1][4] = ((ServerNode) ((ServerNode) node).getSe()).getSe();

        CM[2][1] = ((ServerNode) ((ServerNode) node).getSw()).getNw();
        CM[2][2] = ((ServerNode) ((ServerNode) node).getSw()).getNe();
        CM[2][3] = ((ServerNode) ((ServerNode) node).getSe()).getNw();
        CM[2][4] = ((ServerNode) ((ServerNode) node).getSe()).getNe();

        CM[3][1] = ((ServerNode) ((ServerNode) node).getNw()).getSw();
        CM[3][2] = ((ServerNode) ((ServerNode) node).getNw()).getSe();
        CM[3][3] = ((ServerNode) ((ServerNode) node).getNe()).getSw();
        CM[3][4] = ((ServerNode) ((ServerNode) node).getNe()).getSe();

        CM[4][1] = ((ServerNode) ((ServerNode) node).getNw()).getNw();
        CM[4][2] = ((ServerNode) ((ServerNode) node).getNw()).getNe();
        CM[4][3] = ((ServerNode) ((ServerNode) node).getNe()).getNw();
        CM[4][4] = ((ServerNode) ((ServerNode) node).getNe()).getNe();
    }

    Object auxiliaryMatrixToNode() {
        Object[][] M = CMr;

        Object sw = createNode(M[1][1], M[1][2], M[2][1], M[2][2]);
        Object se = createNode(M[1][3], M[1][4], M[2][3], M[2][4]);
        Object nw = createNode(M[3][1], M[3][2], M[4][1], M[4][2]);
        Object ne = createNode(M[3][3], M[3][4], M[4][3], M[4][4]);

        return createNode(sw, se, nw, ne);
    }

    /**
     * Fonction Principale du HashLife
     * Prend un noeud et retourne le résultat comme centre (depth - 1)
     *
     *
     * @param node
     * @return Object
     */
    Object stepNode(Object node) {
        Object result;
        if (((ServerNode) node).getDepth() == worldDepth) {
            generation += Math.pow(2, worldDepth - 2);
        }

        if (((ServerNode) node).getArea() == 0) {
            return getCenterNode(node);
        }
        if (memoRes.containsKey(node)) {
            return memoRes.get(node);
        }

        if (((ServerNode) node).getDepth() == 2) {

            nodeToAuxiliaryMatrix(node);
            processAuxiliaryMatrix();
            result = getCenterNode(auxiliaryMatrixToNode());


        } else {

            Object node11 = createNode(((ServerNode) (((ServerNode) node).getSw())).getSw(), ((ServerNode) (((ServerNode) node).getSw())).getSe(), ((ServerNode) (((ServerNode) node).getSw())).getNw(), ((ServerNode) (((ServerNode) node).getSw())).getNe());
            Object node21 = createNode(((ServerNode) (((ServerNode) node).getSw())).getNw(), ((ServerNode) (((ServerNode) node).getSw())).getNe(), ((ServerNode) (((ServerNode) node).getNw())).getSw(), ((ServerNode) (((ServerNode) node).getNw())).getSe());
            Object node31 = createNode(((ServerNode) (((ServerNode) node).getNw())).getSw(), ((ServerNode) (((ServerNode) node).getNw())).getSe(), ((ServerNode) (((ServerNode) node).getNw())).getNw(), ((ServerNode) (((ServerNode) node).getNw())).getNe());
            Object node12 = createNode(((ServerNode) (((ServerNode) node).getSw())).getSe(), ((ServerNode) (((ServerNode) node).getSe())).getSw(), ((ServerNode) (((ServerNode) node).getSw())).getNe(), ((ServerNode) (((ServerNode) node).getSe())).getNw());
            Object node22 = createNode(((ServerNode) (((ServerNode) node).getSw())).getNe(), ((ServerNode) (((ServerNode) node).getSe())).getNw(), ((ServerNode) (((ServerNode) node).getNw())).getSe(), ((ServerNode) (((ServerNode) node).getNe())).getSw());
            Object node32 = createNode(((ServerNode) (((ServerNode) node).getNw())).getSe(), ((ServerNode) (((ServerNode) node).getNe())).getSw(), ((ServerNode) (((ServerNode) node).getNw())).getNe(), ((ServerNode) (((ServerNode) node).getNe())).getNw());
            Object node13 = createNode(((ServerNode) (((ServerNode) node).getSe())).getSw(), ((ServerNode) (((ServerNode) node).getSe())).getSe(), ((ServerNode) (((ServerNode) node).getSe())).getNw(), ((ServerNode) (((ServerNode) node).getSe())).getNe());
            Object node23 = createNode(((ServerNode) (((ServerNode) node).getSe())).getNw(), ((ServerNode) (((ServerNode) node).getSe())).getNe(), ((ServerNode) (((ServerNode) node).getNe())).getSw(), ((ServerNode) (((ServerNode) node).getNe())).getSe());
            Object node33 = createNode(((ServerNode) (((ServerNode) node).getNe())).getSw(), ((ServerNode) (((ServerNode) node).getNe())).getSe(), ((ServerNode) (((ServerNode) node).getNe())).getNw(), ((ServerNode) (((ServerNode) node).getNe())).getNe());

            Object res11 = stepNode(node11);
            Object res12 = stepNode(node12);
            Object res13 = stepNode(node13);
            Object res21 = stepNode(node21);
            Object res22 = stepNode(node22);
            Object res23 = stepNode(node23);
            Object res31 = stepNode(node31);
            Object res32 = stepNode(node32);
            Object res33 = stepNode(node33);

            Object sw = getCenterNode(createNode(res11, res12, res21, res22));
            Object se = getCenterNode(createNode(res12, res13, res22, res23));
            Object nw = getCenterNode(createNode(res21, res22, res31, res32));
            Object ne = getCenterNode(createNode(res22, res23, res32, res33));

            result = createNode(sw, se, nw, ne);
        }

        memoRes.put(node, result);

        return result;
    }

    Object openFile(String filename) {

        Object node = null;

        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            // Créer l'objet BufferedReader
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();


            Map<Integer, Object> nodes = new HashMap<Integer, Object>();
            int idx = 1;

            nodes.put(0, generateCanonical0(3));

            String line;

            int numLine = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    if (line.charAt(0) != '#' && line.charAt(0) != '[') {

                        if (line.charAt(0) == '.' || line.charAt(0) == '*' || line.charAt(0) == '$') {
                            int[][] M = lineToMatrix(line);
                            node = matrixToNode(M);


                        } else {

                            Object v[] = new Object[line.split(" ").length];
                            int[] v2 = new int[line.split(" ").length];
                            for (int x = 0; x < line.split(" ").length; x++) {

                                v2[x] = Integer.valueOf(line.split(" ")[x]);

                                if (v2[x] > 0) {

                                    v[x] = 1;

                                }
                            }

                            if (v2[0] == 1) {

                                node = pickCannonical(v[3], v[4], v[1], v[2]);

                            } else {
                                Object sw = nodes.get(v2[3]);
                                Object se = nodes.get(v2[4]);
                                Object nw = nodes.get(v2[1]);
                                Object ne = nodes.get(v2[2]);
                                if (v2[3] == 0) {

                                    sw = generateCanonical0(v2[0] - 1);
                                }
                                if (v2[4] == 0) {
                                    se = generateCanonical0(v2[0] - 1);
                                }

                                if (v2[1] == 0) {
                                    nw = generateCanonical0(v2[0] - 1);
                                }

                                if (v2[2] == 0) {
                                    ne = generateCanonical0(v2[0] - 1);
                                }


                                node = createNode(sw, se, nw, ne);

                            }
                        }
                        nodes.put(idx, node);
                        idx += 1;
                    }
                }


                numLine++;

            }
            System.out.println("NB NOEUDS FINAUX : " + nodes.size());
            br.close();


            Object res1 = addBorder(addBorder(nodes.get(idx - 1)));

            worldDepth = ((ServerNode) res1).getDepth();

            return res1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Hashlife() {}

    public void init(String fileName) {
        this.rootNode = this.openFile(fileName);
    }

    public void createHashLife() {
        this.worldDepth = ((ServerNode) rootNode).getDepth();
    }

    public void run() {
        System.out.println("COURS");
        rootNode = this.addBorder(this.stepNode(rootNode));
    }

    int[][] lineToMatrix(String line) {
        int[][] M = new int[8][8];
        String[] pixelLines = line.split(Pattern.quote("$"));
        int j = 0;
        for (int l = 0; l < pixelLines.length; l++) {
            String pixelline = pixelLines[l];
            int i = 0;
            for (int k = 0; k < pixelline.length(); k++) {
                char pixel = pixelline.charAt(k);
                if (pixel == '*') {
                    M[j][i] = 1;
                } else {
                    M[j][i] = 0;
                }
                i += 1;
            }

            j++;
        }
        return M;

    }

    Object matrixToNode(int[][] M) {
        Object[] subNodes = new Object[4 * 4];
        int count = 0;


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                int sw = M[M.length - 1 - 2 * i][2 * j];
                int se = M[M.length - 1 - 2 * i][2 * j + 1];
                int nw = M[M.length - 1 - 2 * i - 1][2 * j];
                int ne = M[M.length - 1 - 2 * i - 1][2 * j + 1];

                subNodes[count] = pickCannonical(sw, se, nw, ne);

                if (subNodes[count] == (Object) 0) {
                    System.err.println("ERREUR DE CREATION");
                }
                count++;
            }
        }

        Object sw2 = createNode(subNodes[0], subNodes[1], subNodes[4], subNodes[5]);
        Object se2 = createNode(subNodes[2], subNodes[3], subNodes[6], subNodes[7]);
        Object nw2 = createNode(subNodes[8], subNodes[9], subNodes[12], subNodes[13]);
        Object ne2 = createNode(subNodes[10], subNodes[11], subNodes[14], subNodes[15]);

        return createNode(sw2, se2, nw2, ne2);
    }

    public ArrayList<Object> getMemoNodes() {
        return memoNodes;
    }

    public HashMap<Object, Object> getMemoRes() {
        return memoRes;
    }

    public int getGeneration() {
        return generation;
    }

    public int getWorldDepth() {
        return worldDepth;
    }

    public void setRootNode(Object rootNode) {
        this.rootNode = rootNode;
    }
    
    public Object getRootNode() {
        return rootNode;
    }

    public Object[][] getCM() {
        return CM;
    }

    public Object[][] getCMr() {
        return CMr;
    }

    public ServerNode[] getCNList() {
        return CNList;
    }
}
