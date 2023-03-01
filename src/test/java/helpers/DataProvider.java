package helpers;

public class DataProvider {
    @org.testng.annotations.DataProvider(name = "validIds")
    public Object[][] provideValidIds() {
        return new Object[][]{{"1"}, {"50"}, {"9999"}};
    }

    @org.testng.annotations.DataProvider(name = "nonIntegerIds")
    public Object[][] provideNonIntegerIds() {
        return new Object[][]{{"string"}, {"12345678909876323423"}, {"!*"}};
    }

    @org.testng.annotations.DataProvider(name = "lessOrEqualThanZeroIds")
    public Object[][] provideLessOrEqualThanZeroIds() {
        return new Object[][]{{"0"}, {"-1"}, {"-100"}};
    }

    @org.testng.annotations.DataProvider(name = "moreOrEqualThanTenThousandIds")
    public Object[][] provideMoreOrEqualThanTenThousandIds() {
        return new Object[][]{{"10000"}, {"10001"}, {"15000"}};
    }

    @org.testng.annotations.DataProvider(name = "validPrices")
    public Object[][] provideValidPrices() {
        return new Object[][]{{0.01}, {100}, {9999.99}};
    }

    @org.testng.annotations.DataProvider(name = "nonDoublePrices")
    public Object[][] provideNonDoublePrices() {
        return new Object[][]{{"string"}, {"  "}, {"!*"}, {"0.01.1"}};
    }

    @org.testng.annotations.DataProvider(name = "lessOrEqualThanZeroPrices")
    public Object[][] provideLessOrEqualThanZeroPrices() {
        return new Object[][]{{0.00}, {-0.01}, {-100}};
    }

    @org.testng.annotations.DataProvider(name = "moreOrEqualThanTenThousandPrices")
    public Object[][] provideMoreOrEqualThanTenThousandPrices() {
        return new Object[][]{{10000.00}, {10000.01}, {15000}};
    }

    @org.testng.annotations.DataProvider(name = "validQuantities")
    public Object[][] provideValidQuantities() {
        return new Object[][]{{1}, {100}, {9999}};
    }

    @org.testng.annotations.DataProvider(name = "nonLongQuantities")
    public Object[][] provideNonLongQuantities() {
        return new Object[][]{{"string"}, {"0.0"}, {"!*"}, {"  "}};
    }

    @org.testng.annotations.DataProvider(name = "lessOrEqualThanZeroQuantities")
    public Object[][] provideLessOrEqualThanZeroQuantities() {
        return new Object[][]{{0}, {-1}, {-100}};
    }

    @org.testng.annotations.DataProvider(name = "moreOrEqualThanTenThousandQuantities")
    public Object[][] provideMoreOrEqualThanTenThousandQuantities() {
        return new Object[][]{{10000}, {10001}, {15000}};
    }

    @org.testng.annotations.DataProvider(name = "validSides")
    public Object[][] provideValidSides() {
        return new Object[][]{{"Buy"}, {"Sell"}};
    }

    @org.testng.annotations.DataProvider(name = "nonValidSides")
    public Object[][] provideNonValidSides() {
        return new Object[][]{{"buy"}, {"sell"}, {"0"}, {"1"}, {"test"}};
    }
}
