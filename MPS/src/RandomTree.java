import java.util.*;

public class RandomTree {
    private final int numOfNodes;
    private ArrayList<ArrayList<Integer>> tree;
    private ArrayList<Integer> eqs;
    private int numOfPoints;

    public RandomTree(int numOfNodes, int numOfPoints) {
        this.numOfNodes = numOfNodes;
        this.tree = new ArrayList<>();
        this.eqs = new ArrayList<>();
        this.numOfPoints = numOfPoints;
    }

    public void createTree() {
        ArrayList<Integer> thresholds = new ArrayList<>();
        Random rand = new Random();
        int numOfThresholds = rand.nextInt(2, numOfPoints+1);
        int i;
        int j;
        int num;
        ArrayList<Integer> nodes;
        for (i = 0; i < numOfThresholds; i++) {
            num = rand.nextInt(0, numOfPoints);
            while (thresholds.contains(num)) {
                num = rand.nextInt(0, numOfPoints);
            }
            thresholds.add(num);
        }
        tree.add(thresholds);
        for (i = 0; i < numOfNodes; i++) {
            num = rand.nextInt(1,5);
            eqs.add(num);
            nodes = new ArrayList<>();
            numOfThresholds = rand.nextInt(2, numOfPoints);
            for (j = 0; j < numOfThresholds; j++) {
                num = rand.nextInt(0, numOfPoints);
                while (nodes.contains(num)) {
                    num = rand.nextInt(0, numOfPoints);
                }
                nodes.add(num);
            }
            tree.add(nodes);
            numOfPoints++;
        }
    }

    public ArrayList<ArrayList<Integer>> getTree() {
        return tree;
    }

    public ArrayList<Integer> getEqs() {
        return eqs;
    }
}
