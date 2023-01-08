import java.util.*;
public class CreateTree {
    /**
     * Selects the specific distinct thresholds
     * @param data
     * @param indexes
     * @return
     */
    public ArrayList<Double> selectThresholds(ArrayList<Double> data, ArrayList<Integer> indexes) {
        int numOfThresholds = data.size();
        ArrayList<Double> thresholds = new ArrayList<>();
        double threshold;
        for (int index : indexes) {
            threshold = data.get(index);
            if (!thresholds.contains(threshold)) {
                thresholds.add(threshold);
            }
        }
        return thresholds;
    }

    /**
     * Creates a node
     * @param thresholds list of thresholds
     * @param eq
     * @return
     */
    public double createNode(ArrayList<Double> thresholds, int eq) {
        double node= 0;
        NodeEq nodeEq = new NodeEq();
        node = nodeEq.pick(eq, thresholds);
        return node;
    }
}
