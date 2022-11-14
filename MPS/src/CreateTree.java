import java.util.*;
import java.io.*;


public class CreateTree {
    public ArrayList<Double> selectThresholds(ArrayList<Double> data) {
        int numOfThresholds = data.size();
        ArrayList<Double> thresholds = new ArrayList<>();
        Random rand = new Random();
//      coppy data to new array
        ArrayList<Double> dataCopy = new ArrayList<>();
        for (Double datum : data) {
            dataCopy.add(datum);
        }
        int num = rand.nextInt(numOfThresholds);
        while (num < 2) {
            num = rand.nextInt(numOfThresholds);
        }
        for (int i = 0; i < num; i++) {
            int index = rand.nextInt(0, dataCopy.size());
            thresholds.add(dataCopy.get(index));
            dataCopy.remove(index);
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
