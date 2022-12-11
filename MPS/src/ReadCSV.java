import java.io.*;
import java.util.*;

public class ReadCSV {
    private ArrayList<Double> thresholds;
    private ArrayList<Double> FMeasure;
    public double idealThreshold;

    public ReadCSV() {
        this.thresholds = new ArrayList<>();
        this.FMeasure = new ArrayList<>();
    }

    public void read(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            String[] values;
            int i;
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
}
