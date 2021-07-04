package de.ixsen.accsaber.business;

public class APUtils {

    private APUtils() {
    }

    public static double calculateApByAcc(double accuracy, double complexity) {
        return (Math.pow(1.0000004d, ((float) Math.pow(accuracy * 100d, 3.5d))) - 1d) * (5d + (complexity / 10d));
    }
}
