package org.igorl.pma.backend.enums;

public enum ItemType {
    SERVICE ("Service"),
    STOCK_ITEM ("Stock Item"),
    RENTAL ("Rental");

    private String displayItemTypes;

    ItemType(String displayItemTypes) {
        this.displayItemTypes = displayItemTypes;
    }

    @Override
    public String toString() {
        return displayItemTypes;
    }
}
