package de.ixsen.accsaber.business;

public class APUtils {
    public static double calculateApByAcc(double accuracy, double techyness) {
        return (Math.pow(1.0000004d, ((float) Math.pow(accuracy * 100d, 3.5d))) - 1d) * (5d + (techyness / 10d));
    }
}
