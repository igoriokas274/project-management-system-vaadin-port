package org.igorl.pma.ui.views.stock;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.*;
import org.igorl.pma.backend.enums.ItemType;

import java.util.List;

public class ItemForm extends FormLayout {

    private Item item;

    TextField itemName = new TextField("Item name", "Item name");
    TextArea itemDescription = new TextArea("Description", "Description");
    ComboBox<ItemType> itemType = new ComboBox<>("Type");
    TextField unit = new TextField("Unit of measurement", "Unit of measurement");
    NumberField minStockLevel = new NumberField("Minimum level", "Minimum stock level");
    BigDecimalField salesPrice = new BigDecimalField("Sales price", "Sales price");
    BigDecimalField purchasePrice = new BigDecimalField("Purchase price", "Purchase price");
    Checkbox closed = new Checkbox("Closed");
    ComboBox<StockType> stockType = new ComboBox<>("Stock");
    ComboBox<Supplier> supplier = new ComboBox<>("Supplier");
    ComboBox<VATValue> vatValue = new ComboBox<>("VAT value");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Item> binder = new BeanValidationBinder<>(Item.class);

    public ItemForm(List<StockType> stockTypes, List<Supplier> suppliers, List<VATValue> vatValues) {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        itemName.setClearButtonVisible(true);
        itemDescription.setClearButtonVisible(true);
        itemType.setPlaceholder("Select a type..");
        itemType.setItems(ItemType.values());
        unit.setClearButtonVisible(true);
        minStockLevel.setHasControls(true);
        minStockLevel.setMin(0);
        salesPrice.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        salesPrice.setPrefixComponent(new Icon(VaadinIcon.EURO));
        purchasePrice.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        purchasePrice.setPrefixComponent(new Icon(VaadinIcon.EURO));
        stockType.setItems(stockTypes);
        stockType.setPlaceholder("Select a stock..");
        stockType.setItemLabelGenerator(StockType::getStockName);
        supplier.setItems(suppliers);
        supplier.setPlaceholder("Select a supplier..");
        supplier.setItemLabelGenerator(Supplier::getSupplierName);
        vatValue.setItems(vatValues);
        vatValue.setItemLabelGenerator(VATValue::getDescription);
        vatValue.setPlaceholder("Select a VAT value..");

        fieldLayout.add(itemName, itemDescription, stockType, supplier,
                unit, purchasePrice, salesPrice, minStockLevel, vatValue, itemType, closed);
        fieldLayout.setColspan(itemName, 3);
        fieldLayout.setColspan(itemDescription, 3);
        fieldLayout.setColspan(itemType, 1);
        fieldLayout.setColspan(unit, 1);
        fieldLayout.setColspan(minStockLevel, 1);
        fieldLayout.setColspan(salesPrice, 1);
        fieldLayout.setColspan(purchasePrice, 1);
        fieldLayout.setColspan(stockType, 2);
        fieldLayout.setColspan(supplier, 2);
        fieldLayout.setColspan(vatValue, 1);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        add(fieldLayout, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, item)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(item);
            fireEvent(new SaveEvent(this, item));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setItem(Item item) {
        this.item = item;
        binder.readBean(item);
    }

    public static abstract class ItemFormEvent extends ComponentEvent<ItemForm> {
        private final Item item;

        protected ItemFormEvent(ItemForm source, Item item) {
            super(source, false);
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }

    public static class SaveEvent extends ItemFormEvent {
        SaveEvent(ItemForm source, Item item) {
            super(source, item);
        }
    }

    public static class DeleteEvent extends ItemFormEvent {
        DeleteEvent(ItemForm source, Item item) {
            super(source, item);
        }
    }

    public static class CloseEvent extends ItemFormEvent {
        CloseEvent(ItemForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
