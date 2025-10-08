import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;
}