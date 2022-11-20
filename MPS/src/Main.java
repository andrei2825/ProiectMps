import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("src/resources/fMeasures.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReadCSV readCSV = new ReadCSV();
        String dir = "src/resources/global_data";
        File folder = new File(dir);
        int complexity = 20;

        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        FileProcessor fileProcessor = new FileProcessor(complexity, folder);
        fileProcessor.processFileData(folder, executor);
        executor.shutdown();
        ArrayList<Double> fMeasures = readCSV.getFMeasure();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/resources/fMeasures.txt"));
            String line = br.readLine();
            while (line != null) {
                fMeasures.add(Double.parseDouble(line));
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + "src/resources/fMeasures.txt");
        } catch (IOException e) {
            System.out.println("Error reading file: " + "src/resources/fMeasures.txt");
        }
        double mean = 0;
        for (Double fMeasure : fMeasures) {
            mean += fMeasure;
        }
        mean = mean/fMeasures.size();
        System.out.println("Mean: " + mean);
    }
}