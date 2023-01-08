import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int i;
        double mean;
        long endTime;
        double oldResult = 0;
        double newResult;
        int complexity;
        int processors;
        long startTime;
        double oldMean = 0;
        int sampleCount = 0;
        int sampleSize = 100;
        Random r;
        String dir;
        String line;
        File folder;
        ReadCSV readCSV;
        BufferedReader br;
        String previousLine;
        FileWriter fileWriter;
        RandomTree randomTree;
        ExecutorService executor;
        FileProcessor fileProcessor;
        FileProcessorLocal fileProcessorLocal;
        ArrayList<Double> fMeasures;
        BufferedWriter bufferedWriter;
        LinkedHashMap<Integer, Double> data;
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> means = new ArrayList<>();
        ArrayList<Integer> complexities = new ArrayList<>();

        System.out.println("Pick 1 for the global thresholding algorithm or 2 for the local thresholding algorithm.");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                File treeModel = new File("src/resources/treeModel.txt");
                while (oldMean < 97) {
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
                    randomTree = new RandomTree(complexity, 15);
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
                break;




            case 2:
                File treeModel2 = new File("src/resources/treeModel2.txt");
                while (oldResult < 90) {
                    try {
                        br = new BufferedReader(new FileReader(treeModel2.getPath()));
                        line = br.readLine();
                        previousLine = "";
                        while (line != null) {
                            previousLine = line;
                            line = br.readLine();
                        }
                        oldResult = Double.parseDouble(previousLine);
                        br.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found: " + treeModel2.getName());
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + treeModel2.getName());
                    }
                    try {
                        fileWriter = new FileWriter("src/resources/fMeasures.txt", false);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write("");
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dir = "src/resources/local_data";
                    folder = new File(dir);
                    r = new Random();
                    complexity = r.nextInt(5, 10);
                    System.out.println(complexity);
                    randomTree = new RandomTree(complexity, 9);
                    randomTree.createTree();

                    processors = Runtime.getRuntime().availableProcessors();
                    startTime = System.nanoTime();
                    executor = Executors.newFixedThreadPool(processors);
                    fileProcessorLocal = new FileProcessorLocal(complexity, folder, randomTree.getTree(), randomTree.getEqs());
                    fileProcessorLocal.processLocalFileData(folder, executor);
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.DAYS);
                    endTime = System.nanoTime();
                    System.out.println("Time: " + (endTime - startTime) / 1000000000.0);
                    ArrayList<Double> successRates = new ArrayList<>();
                    try {
                        br = new BufferedReader(new FileReader("src/resources/fMeasures.txt"));
                        line = br.readLine();
                        while (line != null) {
                            successRates.add(Double.parseDouble(line));
                            line = br.readLine();
                        }
                        br.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found: " + "src/resources/fMeasures.txt");
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + "src/resources/fMeasures.txt");
                    }
                    newResult = 0;
                    int size = successRates.size();
                    for (Double successRate : successRates) {
                        if (Double.isNaN(successRate)) {
                            newResult += 0;
                            size -= 1;
                        } else {
                            newResult += successRate;
                        }
                    }
                    newResult = newResult/size;
                    System.out.println("New Success Rate: " + newResult);

                    if (newResult >= oldResult) {
//        Clear the file of treeModel
                        try {
                            fileWriter = new FileWriter(treeModel2.getPath(), false);
                            bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write("");
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fileWriter = new FileWriter(treeModel2.getPath(), true);
                            bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write("Thresholds: " + randomTree.getTree().get(0) + "\n");
                            for (i = 1; i < randomTree.getTree().size(); i++) {
                                bufferedWriter.write("Node "+ i + ": " + randomTree.getTree().get(i) + "\n");
                                bufferedWriter.write("Equation: " + randomTree.getEqs().get(i-1) + "\n");
                            }
                            bufferedWriter.write( newResult + "");
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
                    means.add(newResult);
                    sampleCount += 1;
                    if (sampleCount == sampleSize) {
                        System.out.println("Sample size reached");
                        break;
                    }
                }

                break;
            default:
                // Invalid option was chosen
                break;
        }
    }
}