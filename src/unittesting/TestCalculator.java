package unittesting;

import org.testng.Assert;

public class TestCalculator {
    public static void main(String[] args) {
        Calculator cal = new Calculator();
        int expectedAdditionResult = cal.addition(10,5);
        int actualAdditionResult = 15;
        Assert.assertEquals(expectedAdditionResult,actualAdditionResult);

        int expectedSubtractionResult = cal.subtraction(10,5);
        int actualSubtractionResult = 5;
        Assert.assertEquals(expectedSubtractionResult,actualSubtractionResult);
    }
}
