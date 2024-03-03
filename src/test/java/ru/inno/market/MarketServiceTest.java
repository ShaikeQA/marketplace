package ru.inno.market;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.PromoCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tags({@Tag("regress"), @Tag("qgMarketService")})
public class MarketServiceTest {
    Client client;
    MarketService service;
    Catalog catalog;
    List<Item> items;


    @BeforeEach
    public void setUp() {
        catalog = new Catalog();
        items = new ArrayList<>(catalog.getStorage().keySet());
        client = new Client(1, "Rinat");
        service = new MarketService();
    }

    @Test
    @Tags({@Tag("smoke"), @Tag("sMarketService")})
    @DisplayName("Успешное создание заказа у клиента без товаров")
    public void createOrderWithOutItems() {
        int orderId = service.createOrderFor(client);

        assertEquals(0, service.getOrderInfo(orderId).getTotalPrice());
        assertEquals(new HashMap<>(), service.getOrderInfo(orderId).getCart());
        assertEquals(client, service.getOrderInfo(orderId).getClient());
        assertEquals(0, service.getOrderInfo(orderId).getTotalPrice());
        assertEquals(0, service.getOrderInfo(orderId).getId());
    }

    @Test
    @Tags({@Tag("smoke"), @Tag("sMarketService")})
    @DisplayName("Добавление одного товара в заказ")
    public void createOrderWithOneItem() {
        int orderId = service.createOrderFor(client);
        Item itemToAdd = items.stream().findAny().orElseThrow();
        service.addItemToOrder(itemToAdd, orderId);

        assertEquals(itemToAdd.getPrice(), service.getOrderInfo(orderId).getTotalPrice());
        assertEquals(itemToAdd, service.getOrderInfo(orderId).getCart().keySet().stream().findFirst().orElseThrow());
        assertEquals(client, service.getOrderInfo(orderId).getClient());
        assertEquals(itemToAdd.getPrice(), service.getOrderInfo(orderId).getTotalPrice());
        assertEquals(0, service.getOrderInfo(orderId).getId());
    }

    @Test
    @DisplayName("Добавление всех товаров в заказ")
    public void createOrderWithAllItems() {
        int orderId = service.createOrderFor(client);
        double expectedTotalPrice = 0;
        for (Item item : items) {
            service.addItemToOrder(item, orderId);
            expectedTotalPrice = expectedTotalPrice + item.getPrice();
        }

        assertEquals(expectedTotalPrice, service.getOrderInfo(orderId).getTotalPrice());
        assertEquals(client, service.getOrderInfo(orderId).getClient());
        assertEquals(0, service.getOrderInfo(orderId).getId());
    }

    @Tags({@Tag("smoke"), @Tag("sMarketService")})
    @DisplayName("Приминение прокодов к заказу со всеми товарами")
    @ParameterizedTest(name = "Промокод {1}")
    @MethodSource("steps.ArgumentsMethodsHelper#streamPromoCodes")
    public void discountApply(Double promoRatio, PromoCodes code) {
        int orderId = service.createOrderFor(client);
        double expectedTotalPrice = 0;
        for (Item item : items) {
            service.addItemToOrder(item, orderId);
            expectedTotalPrice = expectedTotalPrice + item.getPrice();
        }
        assertEquals(expectedTotalPrice * (1 - promoRatio), service.applyDiscountForOrder(orderId, code));

    }


}
