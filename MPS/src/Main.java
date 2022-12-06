import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
public class Main {
    public static void main(String[] args) {
        File treeModel = new File("src/resources/treeModel.txt");
        double oldMean = 0;
        int sampleSize = 100;
        int sampleCount = 0;
        ArrayList<Integer> complexities = new ArrayList<>();
        ArrayList<Double> means = new ArrayList<>();
        while (oldMean < 95) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(treeModel.getPath()));
                String line = br.readLine();
                String previousLine = "";
                while (line != null) {
                    previousLine = line;
                    line = br.readLine();
                }
                oldMean = Double.parseDouble(previousLine);
                br.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + treeModel.getName());
            } catch (IOException e) {
                System.out.println("Error reading file: " + treeModel.getName());
            }
//      Clear the file of fMeasures
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
            Random r = new Random();
            int complexity = r.nextInt(5, 51);
            System.out.println(complexity);
            RandomTree randomTree = new RandomTree(complexity);
            randomTree.createTree();

            int processors = Runtime.getRuntime().availableProcessors();

            long startTime = System.nanoTime();
            ExecutorService executor = Executors.newFixedThreadPool(processors);
            FileProcessor fileProcessor = new FileProcessor(complexity, folder, randomTree.getTree(), randomTree.getEqs());
            fileProcessor.processFileData(folder, executor);
            executor.shutdown();
            long endTime = System.nanoTime();
            System.out.println("Time: " + (endTime - startTime) / 1000000000.0);
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
            System.out.println("Old Mean: " + oldMean);

            if (mean >= oldMean) {
//        Clear the file of treeModel
                try {
                    FileWriter fileWriter = new FileWriter(treeModel.getPath(), false);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write("");
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileWriter fileWriter = new FileWriter(treeModel.getPath(), true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write("Thresholds: " + randomTree.getTree().get(0) + "\n");
                    for (int i = 1; i < randomTree.getTree().size(); i++) {
                        bufferedWriter.write("Node "+ i + ": " + randomTree.getTree().get(i) + "\n");
                        bufferedWriter.write("Equation: " + randomTree.getEqs().get(i-1) + "\n");
                    }
                    bufferedWriter.write( mean + "");
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("The new tree is better");
                System.out.println("New tree model saved");
            } else {
                System.out.println("The old tree is better");
            }
            complexities.add(complexity);
            means.add(mean);
            sampleCount += 1;
            if (sampleCount == sampleSize) {
                System.out.println("Sample size reached");
                break;
            }
        }
//        create hashmap of complexity and mean
        LinkedHashMap<Integer, Double> data = new LinkedHashMap<>();
        for (int i = 0; i < complexities.size(); i++) {
            if (data.containsKey(complexities.get(i))) {
                if (means.get(i) > data.get(complexities.get(i))) {
                    data.put(complexities.get(i), means.get(i));
                } else {
                    continue;
                }
            } else {
                data.put(complexities.get(i), means.get(i));
            }
        }
        JFrame frame = new JFrame("Complexity vs Mean");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Plot(data));
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }


}