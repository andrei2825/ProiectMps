import java.util.*;

public class NodeEq {
    public double pick(int id, ArrayList<Double> args) {
        return switch (id) {
            case 1 -> f1(args);
            case 2 -> f2(args);
            case 3 -> f3(args);
            case 4 -> f4(args);
            case 5 -> f5(args);
            case 6 -> f6(args);
            case 7 -> f7(args);
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

    public double f2 (ArrayList<Double> args)  {
        double product = 1;
        for (double arg : args) {
            product *= arg;
        }
        return Math.pow(product, 1.0 / args.size());
    }

    public double f3 (ArrayList<Double> args) {
        double sum = 0;
        int num = 0;
        for (double arg : args) {
            if (arg <= 0.5) {
                sum += arg;
                num++;
            }
        }
        if (num == 0) {
            return 0;
        }
        return sum / num;
    }

    public double f4 (ArrayList<Double> args) {
        double sum = 0;
        int num = 0;
        for (double arg : args) {
            if (arg >= 0.5) {
                sum += arg;
                num++;
            }
        }
        if (num == 0) {
            return 0;
        }
        return sum / num;
    }

    public double f5 (ArrayList<Double> args) {
        double sum = 0;
        int num = 0;
        int pos = 0;
        for (double arg : args) {
            if (pos == 0) {
                sum += arg;
                num++;
                pos = 1;
            } else {
                pos = 0;
            }
        }
        if (num == 0) {
            return 0;
        }
        return sum / num;
    }

    public double f6 (ArrayList<Double> args) {
        double sum = 0;
        int num = 0;
        int pos = 0;
        for (double arg : args) {
            if (pos == 1) {
                sum += arg;
                num++;
                pos = 0;
            } else {
                pos = 1;
            }
        }
        if (num == 0) {
            return 0;
        }
        return sum / num;
    }

    public double f7 (ArrayList<Double> args) {
        double res = 0;
        for (int i = 0; i < args.size()-1; i++) {
            if (args.get(i) > args.get(i+1)) {
                res += args.get(i) - args.get(i+1);
            } else {
                res += args.get(i+1) - args.get(i);
            }
        }
        if (res > 1) {
            res = res - 1;
        }
        return res;
    }

}
