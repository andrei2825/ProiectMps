import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class FileProcessorLocal implements Runnable{
    private final int complexity;
    private final File file;
    private ArrayList<ArrayList<Integer>> tree;
    private ArrayList<Integer> eqs;

    public FileProcessorLocal(int complexity , File file, ArrayList<ArrayList<Integer>> tree, ArrayList<Integer> eqs) {
        this.complexity = complexity;
        this.file = file;
        this.tree = tree;
        this.eqs = eqs;
    }

    @Override
    public void run() {
        double idealThreshold = 0;
        ReadCSV readCSV = new ReadCSV();
        readCSV.read(file.getAbsolutePath());
        ArrayList<ArrayList<Double>> thresholds = readCSV.getLocalThresholds();
        ArrayList<Double> groundTruth = readCSV.getGroundTruth();
        ArrayList<Double> optimalResults = readCSV.getOptimalResults();
        int results = 0;
        double total = 0;
        int pixelCount = groundTruth.size();
        CreateTree createTree = new CreateTree();
        int i;
        int j;
        ArrayList<Double> selectedThresholds;
        double node;
        int check = 0;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        for (j = 0; j < pixelCount; j++) {
            for (i = 0; i < complexity; i++) {
                selectedThresholds = createTree.selectThresholds(thresholds.get(j), tree.get(i));
                node = createTree.createNode(selectedThresholds, eqs.get(i));
                thresholds.get(j).add(node);
            }
            idealThreshold = thresholds.get(j).get(thresholds.get(j).size() - 1);
            if (groundTruth.get(j) >= idealThreshold) {
                check = 0;
            } else {
                check = 1;
            }
            if (check == optimalResults.get(j)) {
                results++;
            }
        }
        total = (double) results / pixelCount;
        total = total * 100;
        try {
            fileWriter = new FileWriter("src/resources/fMeasures.txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(total + "\n");
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processLocalFileData(File dir, Executor executor) {
        File[] listOfFiles = dir.listFiles();
        assert listOfFiles != null;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                executor.execute(new FileProcessorLocal(complexity, file, tree, eqs));
            }
        }
    }



}
