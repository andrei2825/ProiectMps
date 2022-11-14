import java.util.*;
import java.io.*;


public class CreateTree {
    private final int numOfThresholds;

    public CreateTree(ArrayList<Double> data) {
        this.numOfThresholds = data.size();
    }


    public ArrayList<Double> selectThresholds(ArrayList<Double> data) {
        ArrayList<Double> thresholds = new ArrayList<>();
        Random rand = new Random();
        int num = rand.nextInt(numOfThresholds);
        while (num < 2) {
            num = rand.nextInt(numOfThresholds);
        }
        for (int i = 0; i < num; i++) {
            int index = rand.nextInt(numOfThresholds);
            if (!thresholds.contains(data.get(index))) {
                thresholds.add(data.get(index));
            }
            else {
                i--;
            }
        }
        return thresholds;
    }

    public double createNode(ArrayList<Double> thresholds) {
        double node= 0;
        Random rand = new Random();
        int num = rand.nextInt(1,7);
        NodeEq nodeEq = new NodeEq();
        node = nodeEq.pick(num, thresholds);
        return node;
    }


}
