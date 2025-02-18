import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectProductNotFoundException() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void getOrdersByStatus_ReturnsOrdersWithSpecifiedStatus_WhenOrdersExist() {
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        shopService.addOrder(productsIds);

        List<Order> orders = shopService.getOrdersByStatus(OrderStatus.PROCESSING);

        assertEquals(1, orders.size());
        assertEquals(OrderStatus.PROCESSING, orders.get(0).status());
    }

    @Test
    void updateOrder() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        Order order = shopService.addOrder(productsIds);

        // WHEN
        shopService.updateOrder(order.id(), OrderStatus.COMPLETED);

        // THEN
        List<Order> processingOrders = shopService.getOrdersByStatus(OrderStatus.PROCESSING);
        assertEquals(0, processingOrders.size());
        List<Order> completedOrders = shopService.getOrdersByStatus(OrderStatus.COMPLETED);
        assertEquals(1, completedOrders.size());
    }
}
