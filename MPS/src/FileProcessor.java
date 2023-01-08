import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class FileProcessor implements Runnable{
    private final int complexity;
    ArrayList<Double> fMeasures;
    private final File file;
    private ArrayList<ArrayList<Integer>> tree;
    private ArrayList<Integer> eqs;

    /**
     * Instantiates a file processor
     * @param complexity
     * @param file
     * @param tree
     * @param eqs
     */
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
        int i;
        ArrayList<Double> selectedThresholds;
        double node;
        int index;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        for (i = 0; i < complexity; i++) {
            selectedThresholds = createTree.selectThresholds(thresholds, tree.get(i));
            node = createTree.createNode(selectedThresholds, eqs.get(i));
            thresholds.add(node);
        }
        idealThreshold = thresholds.get(thresholds.size() - 1);
        index = (int) Math.floor(idealThreshold*255);
        try {
            fileWriter = new FileWriter("src/resources/fMeasures.txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(FMeasure.get(index) + "\n");
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes a list of files from a directory
     * @param dir
     * @param executor
     */
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
