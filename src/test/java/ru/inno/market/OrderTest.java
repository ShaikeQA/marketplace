package ru.inno.market;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    static Catalog catalog = new Catalog();
    Order order;
    Client client;
    Item appleIphoneSE;

    @BeforeEach
    public void setUp() {
        //Создали клиента
        client = new Client(1, "Rinat");
    }

    //Основной сценарий покрывающий добавление одного товара
    @Test
    @DisplayName("Добавление iphoneSE в корзину с помощью метода addItem")
    @Description("Валидация кол-ва товаров, кол-во iphoneSE, итоговой стоимости заказа")
    public void verifyAddItem() {
        //Создали Item == appleIphoneSE
        appleIphoneSE = catalog.getItemById(1);
        //Создали новый заказ
        order = new Order(1, client);
        //Добавили iphoneSE в заказ
        order.addItem(appleIphoneSE);
        //Список товаров из корзины
        Map<Item, Integer> itemsInCard = order.getCart();
        // В заказе только 1 товар
        assertEquals(1, itemsInCard.size());
        //В заказе только 1 iphoneSE
        assertEquals(1, itemsInCard.get(appleIphoneSE));
        //Стоимость товаров равна стоимости iphoneSE
        assertEquals(order.getTotalPrice(), appleIphoneSE.getPrice());
    }

    //Усложнил первый тест, добавил на вход поочередно список всех товаров из каталога и их кол-во
    @DisplayName("Добавление товаров в корзину с помощью метода addItem")
    @ParameterizedTest(name = "Добавление {0}")
    @Description("Валидация кол-ва товаров, кол-во iphoneSE, итоговой стоимости заказа")
    @MethodSource("ru.inno.market.ArgumentsMethodsHelper#streamAllCatalog")
    public void verifyAddItemNew(Item item) {
        //Создали новый заказ
        order = new Order(1, client);
        //Добавили iphoneSE в заказ
        order.addItem(item);
        //Список товаров из корзины
        Map<Item, Integer> itemsInCard = order.getCart();
        // В заказе только 1 товар
        assertEquals(1, itemsInCard.size());
        //В заказе только 1 iphoneSE
        assertEquals(1, itemsInCard.get(item));
        //Стоимость товаров равна стоимости iphoneSE
        assertEquals(order.getTotalPrice(), item.getPrice());
    }

    @Test
    @DisplayName("Добавление всех items из catalog в order")
    @Description("Проверка кол-ва товаров в корзине")
    public void addAllItems() {
        order = new Order(1, client);
        List<Item> allItemsList = new ArrayList<>(catalog.getStorage().keySet());
        for (Item item : allItemsList) {
            order.addItem(item);
        }
        assertEquals(12, order.getCart().size());
    }
}

