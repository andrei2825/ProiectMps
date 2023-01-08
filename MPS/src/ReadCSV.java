import java.io.*;
import java.util.*;

public class ReadCSV {
    private ArrayList<Double> thresholds;
    private ArrayList<Double> FMeasure;
    private ArrayList<Double> groundTruth;
    private ArrayList<Double> optimalResults;
    private ArrayList<ArrayList<Double>> localThresholds;
    public double idealThreshold;
    public double idealLocalThreshold;

    public ReadCSV() {
        this.thresholds = new ArrayList<>();
        this.FMeasure = new ArrayList<>();
        this.groundTruth = new ArrayList<>();
        this.optimalResults = new ArrayList<>();
        this.localThresholds = new ArrayList<>();
    }

    public void read(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            String[] values;
            int i;
            int index = 0;
            while (line != null) {
                values = line.split(",");
                if (values.length == 16) {
                    idealThreshold = Double.parseDouble(values[0]);
                    for (i = 1; i < values.length; i++) {
                        thresholds.add(Double.parseDouble(values[i]));
                    }
                } else if (values.length == 256) {
                    for (i = 0; i < values.length; i++) {
                        FMeasure.add(Double.parseDouble(values[i]));
                    }
                } else if (values.length == 12) {
                    groundTruth.add(Double.parseDouble(values[0]));
                    optimalResults.add(Double.parseDouble(values[1]));
                    idealLocalThreshold = Double.parseDouble(values[2]);
                    localThresholds.add(new ArrayList<>());
                    for (i = 3; i < values.length; i++) {
                        localThresholds.get(index).add(Double.parseDouble(values[i]));
                    }
                    index++;
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }
    }

    public ArrayList<Double> getThresholds() {
        return thresholds;
    }

    public ArrayList<Double> getFMeasure() {
        return FMeasure;
    }

    public ArrayList<Double> getGroundTruth() {
        return groundTruth;
    }

    public ArrayList<Double> getOptimalResults() {
        return optimalResults;
    }

    public ArrayList<ArrayList<Double>> getLocalThresholds() {
        return localThresholds;
    }
}
