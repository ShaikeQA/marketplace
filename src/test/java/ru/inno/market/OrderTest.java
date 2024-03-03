package ru.inno.market;

import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;
import ru.inno.market.model.PromoCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tags({@Tag("regress"), @Tag("qgOrder")})
public class OrderTest {
    Catalog catalog;
    Order order;
    Client client;
    Item appleIphoneSE;

    @BeforeEach

    public void setUp() {
        //Создали клиента
        client = new Client(1, "Rinat");
        catalog = new Catalog();
    }

    //Первый тест, который написал:)
    @Test
    @Tag("firstTest")
    @DisplayName("Добавление iphoneSE в корзину с помощью метода addItem")
    @Description("Валидация кол-ва товаров, кол-во iphoneSE, итоговой стоимости заказа")
    public void verifyAddItemFirstTest() {
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
    @Tags({@Tag("sOrder"), @Tag("smoke")})
    @DisplayName("Добавление товаров в корзину с помощью метода addItem")
    @ParameterizedTest(name = "Добавление {0}")
    @Description("Валидация кол-ва товаров, кол-во iphoneSE, итоговой стоимости заказа")
    @MethodSource("ru.inno.market.steps.ArgumentsMethodsHelper#streamAllCatalog")
    public void verifyAddItem(Item item) {
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
    @Tags({@Tag("sOrder"), @Tag("smoke")})
    @DisplayName("Добавление всех items из catalog в order")
    @Description("Проверка кол-ва товаров в корзине")
    public void addAllItemsToCard() {
        //Создаем заказ для клиента 1
        order = new Order(1, client);
        //Берем список товаров из каталога
        List<Item> allItemsList = new ArrayList<>(catalog.getStorage().keySet());
        //Добавляем все товары в заказ
        for (Item item : allItemsList) {
            order.addItem(item);
        }
        //Проверяем, что каждый товар из каталога был добавлен по 1 разу
        for (Item item : allItemsList) {
            assertEquals(order.getCart().get(item), 1);
        }
    }

    @Test
    @DisplayName("Создание заказа у каждого из двух клиентов с разными товарами")
    @Description("Валидация кол-ва товаров, кол-во позиций у товара, итоговой стоимости заказа, у каждого заказа и клиента")
    public void createTwoOrdersWhitDifferentClients() {
        //Создаем iphone
        appleIphoneSE = catalog.getItemById(1);
        //Создаем список товаров из каталога без iphone
        List<Item> items = new ArrayList<>(catalog.getStorage().keySet().stream().filter(f -> f.getId() != 1).toList());
        //Создаем 1 заказ
        order = new Order(1, client);
        //Создаем второго клиента
        Client anotherClient = new Client(2, "Alex");
        //Создаем заказ для второго клиента
        Order anotherOrder = new Order(2, anotherClient);
        //Добавляем iphone в первый заказ
        order.addItem(appleIphoneSE);
        //Берем item !=  iphoneSE
        Item itemForAnotherOrder = items.stream().findAny().orElseThrow();
        //Добавляем товар во второй заказ
        anotherOrder.addItem(itemForAnotherOrder);

        //Проверяем корзину для первого заказа
        Map<Item, Integer> itemsInCard = order.getCart();
        assertEquals(1, itemsInCard.size());
        assertEquals(1, itemsInCard.get(appleIphoneSE));
        assertEquals(order.getTotalPrice(), appleIphoneSE.getPrice());

        //Проверяем корзину для второго заказа
        Map<Item, Integer> itemsInAnotherCard = anotherOrder.getCart();
        assertEquals(1, itemsInAnotherCard.size());
        assertEquals(1, itemsInAnotherCard.get(itemForAnotherOrder));
        assertEquals(anotherOrder.getTotalPrice(), itemForAnotherOrder.getPrice());

    }

    @Test
    @DisplayName("Добавление несуществующего товара")
    @Description("Бередовый тест, написал ради изучения функционала assertThrows")
    public void nonexistentItemFromCatalog() {
        //Создаем заказ
        order = new Order(1, client);
        //Создаем список товаров из каталога
        List<Item> items = new ArrayList<>(catalog.getStorage().keySet());
        //Попытка сложить в заказ товар, которого нет в списке
        assertThrows(IndexOutOfBoundsException.class, () -> order.addItem(items.get(items.size() + 1)));
    }

    @Test
    @DisplayName("Применение скидки без проверки ее размера")
    public void isDiscountAppliedValid() {
        order = new Order(1, client);
        assertFalse(order.isDiscountApplied());

        List<Item> items = new ArrayList<>(catalog.getStorage().keySet());

        order.addItem(items.get(0));
        order.addItem(items.get(1));

        order.applyDiscount(PromoCodes.FIRST_ORDER.getDiscount());
        assertTrue(order.isDiscountApplied());
    }

    @Tags({@Tag("sOrder"), @Tag("smoke")})
    @ParameterizedTest(name = "Промокод {1}")
    @DisplayName("Применение промокодов, проверка итоговой стоимости корзины")
    @MethodSource("ru.inno.market.steps.ArgumentsMethodsHelper#streamPromoCodes")
    public void applyAllPromoCodes(Double promoCount, PromoCodes promoCodes) {
        assertEquals(promoCodes.getDiscount(), promoCount);
        order = new Order(1, client);
        List<Item> items = new ArrayList<>(catalog.getStorage().keySet());

        for (Item item : items) {
            order.addItem(item);
        }

        double totalPriceBeforeDiscount = order.getTotalPrice();
        order.applyDiscount(promoCount);

        assertEquals(totalPriceBeforeDiscount * (1 - promoCount), order.getTotalPrice());

    }

}

