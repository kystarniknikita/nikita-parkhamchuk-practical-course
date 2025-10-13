import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Analytic {

    private static final List<Order> orders = InitData.initOrders();

    public static Set<String> uniqueCities() {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .collect(Collectors.toSet());
    }

    public static double totalIncomeForAllOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public static String mostPopularProduct() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName,
                        Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    public static double averageCheckForDeliveredOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .average()
                .orElse(0.0);
    }

    public static List<Customer> customersWithAtLeastFiveOrders() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet().stream()
                .filter(customerLongEntry -> customerLongEntry.getValue() >= 5)
                .map(Map.Entry::getKey)
                .toList();
    }
}
