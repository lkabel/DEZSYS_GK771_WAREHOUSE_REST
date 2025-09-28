package rest.warehouse;

import rest.model.Product;
import rest.model.WarehouseData;

public class WarehouseSimulation {

    private double getRandomDouble(int inMinimum, int inMaximum) {

        double number = (Math.random() * ((inMaximum - inMinimum) + 1)) + inMinimum;
        double rounded = Math.round(number * 100.0) / 100.0;
        return rounded;

    }

    private int getRandomInt(int inMinimum, int inMaximum) {

        double number = (Math.random() * ((inMaximum - inMinimum) + 1)) + inMinimum;
        Long rounded = Math.round(number);
        return rounded.intValue();

    }

    public WarehouseData getData(String inID) {

        WarehouseData data = new WarehouseData();
        data.setWarehouseID("001");
        data.setWarehouseName("Linz Bahnhof");
        data.setWarehouseAddress("Bahnhofsstrasse 27/9");
        data.setWarehousePostalCode("Linz");
        data.setWarehouseCity("Linz");
        data.setWarehouseCountry("Austria");

        Product products[] = new Product[4];

        products[0] = new Product();
        products[0].setProductID("00-443175");
        products[0].setProductName("Bio Orangensaft Sonne");
        products[0].setProductCategory("Getraenk");
        products[0].setProductQuantity(2500);
        products[0].setProductUnit("Packung 1L");

        products[1] = new Product();
        products[1].setProductID("00-871895");
        products[1].setProductName("Bio Apfelsaft Gold");
        products[1].setProductCategory("Getraenk");
        products[1].setProductQuantity(3420);
        products[1].setProductUnit("Packung 1L");
        products[2] = new Product();
        products[2].setProductID("01-926885");
        products[2].setProductName("Ariel Waschmittel Color");
        products[2].setProductCategory("Waschmittel");
        products[2].setProductQuantity(478);
        products[2].setProductUnit("Packung 3KG");

        products[3] = new Product();
        products[3].setProductID("00-316253");
        products[3].setProductName("Persil Discs Color");
        products[3].setProductCategory("Waschmittel");
        products[3].setProductQuantity(1430);
        products[3].setProductUnit("Packung 700G");

        data.setProductData(products);

        return data;
    }
}
