package ru.inno.market;

import org.junit.jupiter.params.provider.Arguments;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArgumentsMethodsHelper {
    private static final Catalog catalog = new Catalog();

    public static Stream<Arguments> streamAllCatalog() {
        List<Item> catalogItems = new ArrayList<>(catalog.getStorage().keySet());
        List<Arguments> listOfAgrumets = new ArrayList<>();
        for (Item catalogItem : catalogItems) {
            listOfAgrumets.add(Arguments.of(catalogItem, catalog.getStorage().get(catalogItem)));
        }
        return listOfAgrumets.stream();
    }




}
