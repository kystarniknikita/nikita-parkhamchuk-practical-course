import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class AnalyticTest {

    @Test
    @DisplayName("Test unique cities")
    void testUniqueCities() {
        Set<String> cities = Analytic.uniqueCities();
        Set<String> expected = Set.of("Minsk", "Moscow");

        Assertions.assertEquals(expected, cities);
    }

    @Test
    @DisplayName("Test total income for all completed orders")
    void testTotalIncomeOfOrders() {
        double expected = 2970.0;

        Assertions.assertEquals(expected, Analytic.totalIncomeForAllOrders());
    }

    @Test
    @DisplayName("The most popular product by sales")
    void testMostPopularProduct() {
        String expected = "Cups";

        Assertions.assertEquals(expected, Analytic.mostPopularProduct());
    }

    @Test
    @DisplayName("Test average check for successfully delivered orders")
    void testAverageCheckForOrders() {
        double expected = 424.2857142857143;

        Assertions.assertEquals(expected, Analytic.averageCheckForDeliveredOrders());
    }

    @Test
    @DisplayName("Test customers with at least 5 orders")
    void testCustomersWithFiveOrders() {
        String expected = "Kim";

        List<Customer> customers = Analytic.customersWithAtLeastFiveOrders();
        Assertions.assertEquals(1, customers.size());
        Assertions.assertEquals(expected, customers.getFirst().getName());
    }
}
