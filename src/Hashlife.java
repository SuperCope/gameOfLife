import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Hashlife {
    ArrayList<Object> memoNodes = new ArrayList<Object>();

    HashMap<Object, Object> memoRes = new HashMap<Object, Object>();
    int generation = 0;
    int worldDepth = 0;
    boolean fastForward = false;
    Object[][] CM = new Object[6][6];
    Object[][] CMr = new Object[6][6];

    Node[] CNList = {
            new Node(0, 0, 0, 0),
            new Node(0, 0, 0, 1),
            new Node(0, 0, 1, 0),
            new Node(0, 0, 1, 1),
            new Node(0, 1, 0, 0),
            new Node(0, 1, 0, 1),
            new Node(0, 1, 1, 0),
            new Node(0, 1, 1, 1),
            new Node(1, 0, 0, 0),
            new Node(1, 0, 0, 1),
            new Node(1, 0, 1, 0),
            new Node(1, 0, 1, 1),
            new Node(1, 1, 0, 0),
            new Node(1, 1, 0, 1),
            new Node(1, 1, 1, 0),
            new Node(1, 1, 1, 1)
    };

    public Node pickCannonical(Object sw, Object se, Object nw, Object ne) {
        Node candidat = null;

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
        Node node = null;

        if (sw instanceof Integer) {
            node = this.pickCannonical(sw, se, nw, ne);
        } else {

            node = new Node(sw, se, nw, ne, memoNodes.size());


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
        int depth = ((Node) n).getDepth();

        Object nodeBorder = generateCanonical0(depth - 1);

        Object resSW = createNode(nodeBorder, nodeBorder, nodeBorder, ((Node) ((Node) n).getSw()));
        Object resSE = createNode(nodeBorder, nodeBorder, ((Node) ((Node) n).getSe()), nodeBorder);
        Object resNW = createNode(nodeBorder, ((Node) ((Node) n).getNw()), nodeBorder, nodeBorder);
        Object resNE = createNode(((Node) (Node) n).getNe(), nodeBorder, nodeBorder, nodeBorder);

        return createNode(resSW, resSE, resNW, resNE);
    }

    Object getCenterNode(Object node) {

        return createNode(((Node) ((Node) node).getSw()).getNe(), ((Node) ((Node) node).getSe()).getNw(), ((Node) ((Node) node).getNw()).getSe(), ((Node) ((Node) node).getNe()).getSw());
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
                    res = 0;
                } else {
                    if (nAlive == 3) {
                        res = 1;
                    }
                }
                Mr[i][j] = res;
            }
        }

    }

    void nodeToAuxiliaryMatrix(Object node) {
        CM[1][1] = ((Node) ((Node) node).getSw()).getSw();
        CM[1][2] = ((Node) ((Node) node).getSw()).getSe();
        CM[1][3] = ((Node) ((Node) node).getSe()).getSw();
        CM[1][4] = ((Node) ((Node) node).getSe()).getSe();

        CM[2][1] = ((Node) ((Node) node).getSw()).getNw();
        CM[2][2] = ((Node) ((Node) node).getSw()).getNe();
        CM[2][3] = ((Node) ((Node) node).getSe()).getNw();
        CM[2][4] = ((Node) ((Node) node).getSe()).getNe();

        CM[3][1] = ((Node) ((Node) node).getNw()).getSw();
        CM[3][2] = ((Node) ((Node) node).getNw()).getSe();
        CM[3][3] = ((Node) ((Node) node).getNe()).getSw();
        CM[3][4] = ((Node) ((Node) node).getNe()).getSe();

        CM[4][1] = ((Node) ((Node) node).getNw()).getNw();
        CM[4][2] = ((Node) ((Node) node).getNw()).getNe();
        CM[4][3] = ((Node) ((Node) node).getNe()).getNw();
        CM[4][4] = ((Node) ((Node) node).getNe()).getNe();
    }

    Object auxiliaryMatrixToNode() {
        Object[][] M = CMr;

        Object sw = createNode(M[1][1], M[1][2], M[2][1], M[2][2]);
        Object se = createNode(M[1][3], M[1][4], M[2][3], M[2][4]);
        Object nw = createNode(M[3][1], M[3][2], M[4][1], M[4][2]);
        Object ne = createNode(M[3][3], M[3][4], M[4][3], M[4][4]);

        return createNode(sw, se, nw, ne);
    }

    Object stepNode(Object node) {
        Object result = 0;
        if (((Node) node).getDepth() == worldDepth) {
            generation += Math.pow(2, worldDepth - 2);
        }
        if (((Node) node).getArea() == 0) {
            return getCenterNode(node);
        }
        if (memoRes.containsKey(node)) {
            return memoRes.get(node);
        }

        if (((Node) node).getDepth() == 2) {
            nodeToAuxiliaryMatrix(node);
            processAuxiliaryMatrix();
            result = getCenterNode(auxiliaryMatrixToNode());


        } else {
            Object node11 = createNode(((Node) (((Node) node).getSw())).getSw(), ((Node) (((Node) node).getSw())).getSe(), ((Node) (((Node) node).getSw())).getNw(), ((Node) (((Node) node).getSw())).getNe());
            Object node21 = createNode(((Node) (((Node) node).getSw())).getNw(), ((Node) (((Node) node).getSw())).getNe(), ((Node) (((Node) node).getNw())).getSw(), ((Node) (((Node) node).getNw())).getSe());
            Object node31 = createNode(((Node) (((Node) node).getNw())).getSw(), ((Node) (((Node) node).getNw())).getSe(), ((Node) (((Node) node).getNw())).getNw(), ((Node) (((Node) node).getNw())).getNe());
            Object node12 = createNode(((Node) (((Node) node).getSw())).getSe(), ((Node) (((Node) node).getSe())).getSw(), ((Node) (((Node) node).getSw())).getNe(), ((Node) (((Node) node).getSe())).getNw());
            Object node22 = createNode(((Node) (((Node) node).getSw())).getNe(), ((Node) (((Node) node).getSe())).getNw(), ((Node) (((Node) node).getNw())).getSe(), ((Node) (((Node) node).getNe())).getSw());
            Object node32 = createNode(((Node) (((Node) node).getNw())).getSe(), ((Node) (((Node) node).getNe())).getSw(), ((Node) (((Node) node).getNw())).getNe(), ((Node) (((Node) node).getNe())).getNw());
            Object node13 = createNode(((Node) (((Node) node).getSe())).getSw(), ((Node) (((Node) node).getSe())).getSe(), ((Node) (((Node) node).getSe())).getNw(), ((Node) (((Node) node).getSe())).getNe());
            Object node23 = createNode(((Node) (((Node) node).getSe())).getNw(), ((Node) (((Node) node).getSe())).getNe(), ((Node) (((Node) node).getNe())).getSw(), ((Node) (((Node) node).getNe())).getSe());
            Object node33 = createNode(((Node) (((Node) node).getNe())).getSw(), ((Node) (((Node) node).getNe())).getSe(), ((Node) (((Node) node).getNe())).getNw(), ((Node) (((Node) node).getNe())).getNe());

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


            worldDepth = ((Node) res1).getDepth();


            return res1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Hashlife() {

        Object rootNode = this.openFile("dataFiles/sample.mc");
        this.worldDepth = ((Node) rootNode).getDepth();

        for (int i = 0; i < 200; i++) {

            rootNode = this.addBorder(this.stepNode(rootNode));
            System.out.println("J'OBTIENS " + this.memoNodes.size() + "," + i);
            System.out.println("TAILLE DE MEMORES : " + this.memoRes.size());
            if (i == 4) {
                System.exit(0);
            }
        }
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


}
