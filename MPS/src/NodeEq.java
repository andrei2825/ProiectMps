import java.util.*;

public class NodeEq {
    public double pick(int id, ArrayList<Double> args) {
        return switch (id) {
            case 1 -> f1(args);
            case 2 -> f2(args);
            case 3 -> f3(args);
            case 4 -> f4(args);
//            case 5 -> f5(args);
//            case 6 -> f6(args);
//            case 7 -> f7(args);
//            case 8 -> f8(args);
            default -> 0;
        };
    }

    public double f1 (ArrayList<Double> args)  {
        double sum = 0;
        for (double arg : args) {
            sum += arg;
        }
        return sum / args.size();
    }
// calculate the median of the given array
    public double f2 (ArrayList<Double> args)  {
        double product = 1;
        for (double arg : args) {
            product *= arg;
        }
        return Math.pow(product, 1.0 / args.size());
    }

    public double f3 (ArrayList<Double> args) {
        Collections.sort(args);
        int middle = args.size() / 2;
        if (args.size() % 2 == 1) {
            return args.get(middle);
        } else {
            return (args.get(middle - 1) + args.get(middle)) / 2.0;
        }
    }
//range
//    public double f4 (ArrayList<Double> args) {
//        double max = Collections.max(args);
//        double min = Collections.min(args);
//        return (max + min)/2;
//    }

    public double f4 (ArrayList<Double> args) {
        double sum = 0;
//        harmonic mean
        for (double arg : args) {
            sum += 1/arg;
        }
        return args.size()/sum;
    }


}