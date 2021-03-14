package org.igorl.pma.ui.views.stock;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SupplierForm extends FormLayout {

    private Supplier supplier;
    private Locale lithuanian = new Locale("lt");

    TextField supplierName = new TextField("Supplier name", "Supplier name");
    TextField supplierRegistrationNumber = new TextField("Registration code", "Registration code");
    TextField supplierVATNumber = new TextField("VAT code", "VAT code");
    TextField addressLine1 = new TextField("Address Line 1", "Address Line 1");
    TextField addressLine2 = new TextField("Address Line 2", "Address Line 2");
    TextField city = new TextField("City", "City");
    TextField zipCode = new TextField("Zip Code", "Zip Code");
    TextField contactPhone = new TextField("Phone", "Phone");
    EmailField contactEmail = new EmailField("Email", "Email");
    TextField swift = new TextField("SWIFT", "SWIFT");
    TextField bankCode = new TextField("Bank code", "Bank code");
    TextField bankName = new TextField("Bank name", "Bank name");
    TextField bankAccount = new TextField("Bank account","Bank account");
    Checkbox closed = new Checkbox("Closed");
    ComboBox<Country> country = new ComboBox<>("Country");
    ComboBox<PayTerm> payTerm = new ComboBox<>("Payment terms");
    ComboBox<Currency> currency = new ComboBox<>("Currency");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Supplier> binder = new BeanValidationBinder<>(Supplier.class);

    public SupplierForm(List<Country> countries, List<PayTerm> payTerms, List<Currency> currencies) {

        addClassName("form");
        binder.bindInstanceFields(this);

        Tab main = new Tab("Main");
        Tab additional = new Tab("Additional");

        FormLayout mainInfoForm = new FormLayout();
        FormLayout addInfoForm = new FormLayout();

        supplierName.setClearButtonVisible(true);
        supplierRegistrationNumber.setClearButtonVisible(true);
        supplierVATNumber.setClearButtonVisible(true);
        payTerm.setItems(payTerms);
        payTerm.setPlaceholder("Select a Payment Term..");
        payTerm.setItemLabelGenerator(PayTerm::getDescription);
        currency.setItems(currencies);
        currency.setPlaceholder("Select a Currency..");
        currency.setItemLabelGenerator(Currency::getCurrencyName);
        addressLine1.setClearButtonVisible(true);
        addressLine2.setClearButtonVisible(true);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountryName);
        country.setPlaceholder("Select a country..");
        contactPhone.setClearButtonVisible(true);
        contactEmail.setClearButtonVisible(true);
        contactEmail.setErrorMessage("Please enter a valid email address");
        swift.setClearButtonVisible(true);
        bankCode.setClearButtonVisible(true);
        bankAccount.setClearButtonVisible(true);
        bankName.setClearButtonVisible(true);

        mainInfoForm.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
        addInfoForm.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
        addInfoForm.setVisible(false);

        mainInfoForm.add(supplierName, supplierRegistrationNumber, supplierVATNumber, payTerm, currency,
                bankName, bankCode, bankAccount, swift, closed);
        mainInfoForm.setColspan(supplierName, 3);
        mainInfoForm.setColspan(bankCode, 1);
        mainInfoForm.setColspan(bankAccount, 2);

        addInfoForm.add(addressLine1, addressLine2, city, zipCode, country, contactPhone, contactEmail);
        addInfoForm.setColspan(addressLine1, 3);
        addInfoForm.setColspan(addressLine2, 3);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(main, mainInfoForm);
        tabsToPages.put(additional, addInfoForm);

        Tabs supplierTabs = new Tabs(main, additional);
        Div forms = new Div(mainInfoForm, addInfoForm);

        supplierTabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(supplierTabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        HorizontalLayout createdOrChanged = new HorizontalLayout();
        lastModifiedBy.setReadOnly(true);
        lastModifiedDate.setReadOnly(true);
        lastModifiedDate.setLocale(lithuanian);
        createdOrChanged.add(lastModifiedBy, lastModifiedDate);

        add(supplierTabs, forms, createButtonsLayout(), new Hr(), createdOrChanged);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, supplier)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(supplier);
            fireEvent(new SaveEvent(this, supplier));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        binder.readBean(supplier);
    }

    public static abstract class SupplierFormEvent extends ComponentEvent<SupplierForm> {
        private final Supplier supplier;

        protected SupplierFormEvent(SupplierForm source, Supplier supplier) {
            super(source, false);
            this.supplier = supplier;
        }

        public Supplier getSupplier() {
            return supplier;
        }
    }

    public static class SaveEvent extends SupplierFormEvent {
        SaveEvent(SupplierForm source, Supplier supplier) {
            super(source, supplier);
        }
    }

    public static class DeleteEvent extends SupplierFormEvent {
        DeleteEvent(SupplierForm source, Supplier supplier) {
            super(source, supplier);
        }
    }

    public static class CloseEvent extends SupplierFormEvent {
        CloseEvent(SupplierForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
