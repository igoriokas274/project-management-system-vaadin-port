package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Country;
import org.igorl.pma.backend.entity.StockType;

import java.util.List;

public class StockTypeForm extends FormLayout {

    private StockType stockType;

    TextField stockName = new TextField("Stock name", "Stock name");
    TextField addressLine1 = new TextField("Address Line 1", "Address Line 1");
    TextField addressLine2 = new TextField("Address Line 2", "Address Line 2");
    TextField city = new TextField("City", "City");
    TextField zipCode = new TextField("Zip Code","Zip Code");
    ComboBox<Country> country = new ComboBox<>("Country");
    Checkbox closed = new Checkbox("Closed");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<StockType> binder = new BeanValidationBinder<>(StockType.class);

    public StockTypeForm(List<Country> countries) {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        stockName.setClearButtonVisible(true);
        addressLine1.setClearButtonVisible(true);
        addressLine2.setClearButtonVisible(true);
        city.setClearButtonVisible(true);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountryName);
        country.setPlaceholder("Select a country..");
        zipCode.setClearButtonVisible(true);

        fieldLayout.add(stockName, closed, addressLine1, addressLine2, city, zipCode, country);
        fieldLayout.setColspan(addressLine1, 3);
        fieldLayout.setColspan(addressLine2, 3);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, stockType)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(stockType);
            fireEvent(new SaveEvent(this, stockType));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
        binder.readBean(stockType);
    }


    public static abstract class StockTypeFormEvent extends ComponentEvent<StockTypeForm> {
        private StockType stockType;

        public StockType getStockType() {
            return stockType;
        }

        protected StockTypeFormEvent(StockTypeForm source, StockType stockType) {
            super(source, false);
            this.stockType = stockType;
        }
    }

    public static class SaveEvent extends StockTypeFormEvent {
        SaveEvent(StockTypeForm source, StockType stockType) {
            super(source, stockType);
        }
    }

    public static class DeleteEvent extends StockTypeFormEvent {
        DeleteEvent(StockTypeForm source, StockType stockType) {
            super(source, stockType);
        }
    }

    public static class CloseEvent extends StockTypeFormEvent {
        CloseEvent(StockTypeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




