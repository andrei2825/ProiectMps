import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ReadCSV readCSV = new ReadCSV();
//        open directory
        String dir = "src/resources/Test";
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        int complexity = 20;
        double idealThreshold = 0;
        ArrayList<Double> fMeasures = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                readCSV.read(file.getAbsolutePath());
                ArrayList<Double> thresholds = readCSV.getThresholds();
                ArrayList<Double> FMeasure = readCSV.getFMeasure();
                CreateTree createTree = new CreateTree(thresholds);
                for (int i = 0; i < complexity; i++) {
                    ArrayList<Double> selectedThresholds = createTree.selectThresholds(thresholds);
                    double node = createTree.createNode(selectedThresholds);
                    System.out.println("Complexity: " + i + " " + file.getName() + " " + node);
                    thresholds.add(node);

                }
                idealThreshold = thresholds.get(thresholds.size() - 1);
                int index = (int) Math.floor(idealThreshold*255);
                fMeasures.add(FMeasure.get(index));
            }
        }
        double mean = 0;
        for (int i = 0; i < fMeasures.size(); i++) {
            mean += fMeasures.get(i);
        }
        mean = mean/fMeasures.size();
        System.out.println("Mean: " + mean);
    }
}