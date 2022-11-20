import java.util.*;
import java.io.*;


public class CreateTree {
    public ArrayList<Double> selectThresholds(ArrayList<Double> data, ArrayList<Integer> indexes) {
        int numOfThresholds = data.size();
        ArrayList<Double> thresholds = new ArrayList<>();
        for (int index : indexes) {
            double threshold = data.get(index);
            if (!thresholds.contains(threshold)) {
                thresholds.add(threshold);
            }
        }
        return thresholds;
    }

    public double createNode(ArrayList<Double> thresholds, int eq) {
        double node= 0;
        NodeEq nodeEq = new NodeEq();
        node = nodeEq.pick(eq, thresholds);
        return node;
    }


}
