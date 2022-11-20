import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

public class FileProcessor implements Runnable{
    private final int complexity;
    ArrayList<Double> fMeasures;
    private final File file;
    private ArrayList<ArrayList<Integer>> tree;
    private ArrayList<Integer> eqs;

    public FileProcessor(int complexity , File file, ArrayList<ArrayList<Integer>> tree, ArrayList<Integer> eqs) {
        this.complexity = complexity;
        this.fMeasures = new ArrayList<>();
        this.file = file;
        this.tree = tree;
        this.eqs = eqs;
    }

    @Override
    public void run() {
        double idealThreshold = 0;
        ReadCSV readCSV = new ReadCSV();
        readCSV.read(file.getAbsolutePath());
        ArrayList<Double> thresholds = readCSV.getThresholds();
        ArrayList<Double> FMeasure = readCSV.getFMeasure();
        CreateTree createTree = new CreateTree();
        for (int i = 0; i < complexity; i++) {
            ArrayList<Double> selectedThresholds = createTree.selectThresholds(thresholds, tree.get(i));
            double node = createTree.createNode(selectedThresholds, eqs.get(i));
            thresholds.add(node);
        }
        idealThreshold = thresholds.get(thresholds.size() - 1);
        int index = (int) Math.floor(idealThreshold*255);
        try {
            FileWriter fileWriter = new FileWriter("src/resources/fMeasures.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(FMeasure.get(index) + "\n");
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processFileData(File dir, Executor executor) {
        File[] listOfFiles = dir.listFiles();
        assert listOfFiles != null;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                executor.execute(new FileProcessor(complexity, file, tree, eqs));
            }
        }
    }



}
