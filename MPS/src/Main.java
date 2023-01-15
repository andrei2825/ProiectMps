import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        double averageTimeGlobal = 0;
        int timeCount = 0;
        double averageTimeLocal = 0;
        double avgResult;
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
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> means = new ArrayList<>();

        System.out.println("Pick 1 for the global thresholding algorithm or 2 for the local thresholding algorithm.");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
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
                    complexity = 10;
                    randomTree = new RandomTree(complexity, 15);
                    randomTree.createTree();


                    processors = Runtime.getRuntime().availableProcessors();

                    startTime = System.nanoTime();
                    executor = Executors.newFixedThreadPool(processors);
                    fileProcessor = new FileProcessor(complexity, folder, randomTree.getTree(), randomTree.getEqs());
                    fileProcessor.processFileData(folder, executor);
                    executor.shutdown();
                    if (executor.awaitTermination(1, TimeUnit.DAYS))
                        endTime = System.nanoTime();
                    else
                        return;
                    averageTimeGlobal += (endTime - startTime) / 1000000000.0;
                    timeCount++;
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
                    mean = mean / fMeasures.size();
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
                            for (i = 0; i < randomTree.getTree().size(); i++) {
                                bufferedWriter.write("Node " + i + ": " + randomTree.getTree().get(i) + "\n");
                                bufferedWriter.write("Equation: " + randomTree.getEqs().get(i - 1) + "\n");
                            }
                            bufferedWriter.write(mean + "");
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("The new tree is better");
                        System.out.println("New tree model saved");
                    } else {
                        System.out.println("The old tree is better");
                    }
                    means.add(mean);
                    sampleCount += 1;
                    if (sampleCount == sampleSize) {
                        System.out.println("Sample size reached");
                        break;
                    }
                }
                avgResult = means.stream().mapToDouble(val -> val).average().orElse(0.0);
                System.out.println("Average result: " + avgResult);
                averageTimeGlobal = averageTimeGlobal / timeCount;
                System.out.println("Average time: " + averageTimeGlobal);
            }
            case 2 -> {
                File treeModel2 = new File("src/resources/treeModel2.txt");
                int maxIterations = 100;
                while (oldResult < 90) {
                    System.out.println("Sample count: " + sampleCount);
                    sampleCount += 1;
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
                    complexity = 10;
                    System.out.println(complexity);
                    randomTree = new RandomTree(complexity, 9);
                    randomTree.createTree();

                    processors = Runtime.getRuntime().availableProcessors();
                    startTime = System.nanoTime();
                    executor = Executors.newFixedThreadPool(processors);
                    fileProcessorLocal = new FileProcessorLocal(complexity, folder, randomTree.getTree(), randomTree.getEqs());
                    fileProcessorLocal.processLocalFileData(folder, executor);
                    executor.shutdown();
                    if (executor.awaitTermination(1, TimeUnit.DAYS))
                        endTime = System.nanoTime();
                    else
                        return;
                    averageTimeLocal += (endTime - startTime) / 1000000000.0;
                    timeCount++;
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
                    newResult = newResult / size;
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
                            for (i = 0; i < randomTree.getTree().size(); i++) {
                                bufferedWriter.write("Node " + i + ": " + randomTree.getTree().get(i) + "\n");
                                bufferedWriter.write("Equation: " + randomTree.getEqs().get(i - 1) + "\n");
                            }
                            bufferedWriter.write(newResult + "");
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("The new tree is better");
                        System.out.println("New tree model saved");
                    } else {
                        System.out.println("The old tree is better");
                    }
                    means.add(newResult);
                    sampleCount += 1;
                    if (sampleCount == sampleSize) {
                        System.out.println("Sample size reached");
                        break;
                    }
                    maxIterations -= 1;
                    if (maxIterations == 0) {
                        System.out.println("Max iterations reached");
                        break;
                    }
                }
                avgResult = means.stream().mapToDouble(val -> val).average().orElse(0.0);
                System.out.println("Average result: " + avgResult);
                averageTimeLocal = averageTimeLocal / timeCount;
                System.out.println("Average time: " + averageTimeLocal);
            }
            default -> System.out.println("Invalid option");
            // Invalid option was chosen
        }
    }
}