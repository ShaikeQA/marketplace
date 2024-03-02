package ru.inno.market;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;

public class MarketServiceTest {
    private MarketService service;
    private Catalog catalog;

    @BeforeEach
    public void setUp() {
        service = new MarketService();
        catalog = new Catalog();
    }

    @Test
    public static void fistTest() {


    }


}
