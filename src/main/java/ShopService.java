import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductNotFoundException("Product with ID: " + productId + " could not be found!");
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepo.getOrders();
        return orders.stream()
                .filter(order -> order.status().equals(status))
                .toList();
    }

    public void updateOrder(String orderId, OrderStatus newStatus) {
        Order order = orderRepo.getOrderById(orderId);
        orderRepo.removeOrder(orderId);
        Order updatedOrder = order.withStatus(newStatus);
        orderRepo.addOrder(updatedOrder);
    }
}
