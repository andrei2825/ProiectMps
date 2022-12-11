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
        BufferedReader br;
        String line;
        String previousLine;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        ReadCSV readCSV;
        String dir;
        File folder;
        Random r;
        int complexity;
        RandomTree randomTree;
        int processors;
        long startTime;
        ExecutorService executor;
        FileProcessor fileProcessor;
        long endTime;
        ArrayList<Double> fMeasures;
        double mean;
        int i;
        LinkedHashMap<Integer, Double> data;
        while (oldMean < 95) {
            System.out.println("Sample count: " + sampleCount);
            try {
                br = new BufferedReader(new FileReader(treeModel.getPath()));
                line = br.readLine();
                previousLine = "";
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
                fileWriter = new FileWriter("src/resources/fMeasures.txt", false);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            readCSV = new ReadCSV();
            dir = "src/resources/global_data";
            folder = new File(dir);
            r = new Random();
            complexity = r.nextInt(5, 51);
            System.out.println(complexity);
            randomTree = new RandomTree(complexity);
            randomTree.createTree();



            processors = Runtime.getRuntime().availableProcessors();

            startTime = System.nanoTime();
            executor = Executors.newFixedThreadPool(processors);
            fileProcessor = new FileProcessor(complexity, folder, randomTree.getTree(), randomTree.getEqs());
            fileProcessor.processFileData(folder, executor);
            executor.shutdown();
            endTime = System.nanoTime();
            System.out.println("Time: " + (endTime - startTime) / 1000000000.0);
            fMeasures = readCSV.getFMeasure();
            try {
                br = new BufferedReader(new FileReader("src/resources/fMeasures.txt"));
                line = br.readLine();
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
            mean = 0;
            for (Double fMeasure : fMeasures) {
                mean += fMeasure;
            }
            mean = mean/fMeasures.size();
            System.out.println("Mean: " + mean);
            System.out.println("Old Mean: " + oldMean);

            if (mean >= oldMean) {
//        Clear the file of treeModel
                try {
                    fileWriter = new FileWriter(treeModel.getPath(), false);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write("");
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileWriter = new FileWriter(treeModel.getPath(), true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write("Thresholds: " + randomTree.getTree().get(0) + "\n");
                    for (i = 1; i < randomTree.getTree().size(); i++) {
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
        data = new LinkedHashMap<>();
        for (i = 0; i < complexities.size(); i++) {
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