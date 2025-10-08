import java.time.LocalDateTime;
import java.util.List;

public class InitData {

    public static List<Order> initOrders() {
        Order order1 = new Order(
                "1",
                LocalDateTime.now().minusDays(10),
                new Customer("1", "Ivan", "someemail@gmail.com", LocalDateTime.now().minusDays(30), 20, "Minsk"),
                List.of(
                        new OrderItem("The Witcher", 2, 20, Category.BOOKS),
                        new OrderItem("Quiet", 1, 15, Category.BOOKS),
                        new OrderItem("TV", 5, 100, Category.ELECTRONICS)
                ),
                OrderStatus.NEW
        );

        Order order2 = new Order(
                "2",
                LocalDateTime.now().minusDays(100),
                new Customer("2", "Emy", "someemail@gmail.com", LocalDateTime.now().minusDays(150), 25, "Moscow"),
                List.of(
                        new OrderItem("Shirt", 1, 50, Category.CLOTHING),
                        new OrderItem("Phone", 1, 500, Category.ELECTRONICS),
                        new OrderItem("Toothbrush", 2, 10, Category.HOME)
                ),
                OrderStatus.DELIVERED
        );

        Order order3 = new Order(
                "3",
                LocalDateTime.now().minusDays(30),
                new Customer("3", "Max", "someemail@gmail.com", LocalDateTime.now().minusDays(30), 26, "Moscow"),
                List.of(
                        new OrderItem("Cups", 15, 5, Category.HOME),
                        new OrderItem("Containers", 3, 10, Category.HOME),
                        new OrderItem("Toothbrush", 2, 10, Category.HOME)
                ),
                OrderStatus.DELIVERED
        );

        Customer customer = new Customer("4", "Kim", "someemail@gmail.com", LocalDateTime.now().minusDays(300), 26, "Moscow");

        Order order4 = new Order(
                "4",
                LocalDateTime.now().minusDays(35),
                customer,
                List.of(
                        new OrderItem("Homelander", 1, 1000, Category.TOYS)
                ),
                OrderStatus.DELIVERED
        );

        Order order5 = new Order(
                "5",
                LocalDateTime.now().minusDays(45),
                customer,
                List.of(
                        new OrderItem("Shirt", 5, 5, Category.CLOTHING)
                ),
                OrderStatus.DELIVERED
        );

        Order order6 = new Order(
                "6",
                LocalDateTime.now().minusDays(50),
                customer,
                List.of(
                        new OrderItem("TV", 1, 500, Category.ELECTRONICS)
                ),
                OrderStatus.DELIVERED
        );

        Order order7 = new Order(
                "7",
                LocalDateTime.now().minusDays(44),
                customer,
                List.of(
                        new OrderItem("Book", 1, 50, Category.BOOKS)
                ),
                OrderStatus.DELIVERED
        );

        Order order8 = new Order(
                "8",
                LocalDateTime.now().minusDays(111),
                customer,
                List.of(
                        new OrderItem("Phone", 1, 700, Category.ELECTRONICS)
                ),
                OrderStatus.DELIVERED
        );

        return List.of(order1, order2, order3, order4, order5, order6, order7, order8);
    }

}
