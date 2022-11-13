import java.io.*;
import java.util.*;

public class ReadCSV {
    private ArrayList<Double> thresholds;
    private ArrayList<Double> FMeasure;

    public ReadCSV() {
        this.thresholds = new ArrayList<>();
        this.FMeasure = new ArrayList<>();
    }

    public void read(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                String[] values = line.split(",");
                if (values.length == 16) {
                    for (int i = 0; i < values.length; i++) {
                        thresholds.add(Double.parseDouble(values[i]));
                    }
                } else if (values.length == 256) {
                    for (int i = 0; i < values.length; i++) {
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

}
