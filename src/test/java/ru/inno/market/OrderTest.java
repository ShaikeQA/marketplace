package ru.inno.market;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    Catalog catalog = new Catalog();
    Order order;
    Client client;
    Item appleIphoneSE;

    @BeforeEach
    public void setUp() {
        //Создали клиента
        client = new Client(1, "Rinat");
        //Создали Item == appleIphoneSE
        appleIphoneSE = catalog.getItemById(1);
    }

    @Test
    @DisplayName("Добавление iphoneSE в корзину с помощью метода addItem")
    @Description("Валидация кол-ва товаров, кол-во iphoneSE, ")
    public void verifyAddItem() {
        //Создали новый заказ
        order = new Order(1, client);
        //Добавили iphoneSE в заказ
        order.addItem(appleIphoneSE);
        //Список товаров из корзины
        Map<Item, Integer> itemsInCard = order.getCart();
        // В заказе только 1 товар
        assertEquals(itemsInCard.size(), 1);
        //В заказе только 1 iphoneSE
        assertEquals(itemsInCard.get(appleIphoneSE), 1);
        //Стоимость товаров равна стоимости iphoneSE
        assertEquals(order.getTotalPrice(),appleIphoneSE.getPrice());

    }
}
