package ru.inno.market.steps;

import org.junit.jupiter.params.provider.Arguments;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Item;
import ru.inno.market.model.PromoCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArgumentsMethodsHelper {
    private static final Catalog catalog = new Catalog();

    public static Stream<Arguments> streamAllCatalog() {
        List<Item> catalogItems = new ArrayList<>(catalog.getStorage().keySet());
        return toArguments.of(catalogItems);
    }


    public static Stream<Arguments> streamPromoCodes() {
        List<Double> promoCodesList = new ArrayList<>(Stream.of(PromoCodes.values()).map(PromoCodes::getDiscount).toList());
        List<PromoCodes> promoNames = new ArrayList<>(Stream.of(PromoCodes.values()).toList());
        return toArguments.of(promoCodesList, promoNames);
    }


    static class toArguments {
        private static <T, K> Stream<Arguments> of(List<T> firstArguments, List<K> secondArguments) {
            List<Arguments> listOfArguments = new ArrayList<>();
            for (int i = 0; i < firstArguments.size(); i++) {
                listOfArguments.add(Arguments.of(firstArguments.get(i), secondArguments.get(i)));
            }
            return listOfArguments.stream();
        }

        private static <T> Stream<Arguments> of(List<T> firstArguments) {
            List<Arguments> listOfArguments = new ArrayList<>();
            for (T firstArgument : firstArguments) {
                listOfArguments.add(Arguments.of(firstArgument));
            }
            return listOfArguments.stream();
        }
    }


}
