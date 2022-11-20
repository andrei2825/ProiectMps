import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class FileProcessor implements Runnable{
    private final int complexity;
    ArrayList<Double> fMeasures;
    private final File file;

    public FileProcessor(int complexity , File file) {
        this.complexity = complexity;
        this.fMeasures = new ArrayList<>();
        this.file = file;
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
            ArrayList<Double> selectedThresholds = createTree.selectThresholds(thresholds);
            double node = createTree.createNode(selectedThresholds);
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
        int fileIndex = 0;
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println("File " + fileIndex);
                fileIndex++;
                executor.execute(new FileProcessor(complexity, file));
            }
        }
    }



}
