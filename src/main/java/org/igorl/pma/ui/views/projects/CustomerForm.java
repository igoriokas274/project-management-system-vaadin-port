package org.igorl.pma.ui.views.projects;

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

public class CustomerForm extends FormLayout {

    private Customer customer;
    private Locale lithuanian = new Locale("lt");

    TextField customerName = new TextField("Customer name", "Customer name");
    TextField customerRegistrationNumber = new TextField("Registration code", "Registration code");
    TextField customerVATNumber = new TextField("VAT code", "VAT code");
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
    ComboBox<Currency> currency = new ComboBox<>("Currency");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    public CustomerForm(List<Country> countries, List<Currency> currencies) {

        addClassName("form");
        binder.bindInstanceFields(this);

        Tab main = new Tab("Main");
        Tab additional = new Tab("Additional");

        FormLayout mainInfoForm = new FormLayout();
        FormLayout addInfoForm = new FormLayout();

        customerName.setClearButtonVisible(true);
        customerRegistrationNumber.setClearButtonVisible(true);
        customerVATNumber.setClearButtonVisible(true);
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

        mainInfoForm.setColspan(customerName, 3);
        mainInfoForm.setColspan(bankAccount, 3);
        addInfoForm.setColspan(addressLine1, 3);
        addInfoForm.setColspan(addressLine2, 3);

        mainInfoForm.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
        addInfoForm.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
        addInfoForm.setVisible(false);

        mainInfoForm.add(customerName, customerRegistrationNumber, customerVATNumber, currency,
                bankName, bankCode, bankAccount, swift, closed);
        mainInfoForm.setColspan(bankCode, 1);
        mainInfoForm.setColspan(bankAccount, 2);

        addInfoForm.add(addressLine1, addressLine2, city, zipCode, country, contactPhone, contactEmail);
        addInfoForm.setColspan(addressLine1, 3);
        addInfoForm.setColspan(addressLine2, 3);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(main, mainInfoForm);
        tabsToPages.put(additional, addInfoForm);

        Tabs customerTabs = new Tabs(main, additional);
        Div forms = new Div(mainInfoForm, addInfoForm);

        customerTabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(customerTabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        HorizontalLayout createdOrChanged = new HorizontalLayout();
        lastModifiedBy.setReadOnly(true);
        lastModifiedDate.setReadOnly(true);
        lastModifiedDate.setLocale(lithuanian);
        createdOrChanged.add(lastModifiedBy, lastModifiedDate);

        add(customerTabs, forms, createButtonsLayout(), new Hr(), createdOrChanged);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, customer)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(customer);
            fireEvent(new SaveEvent(this, customer));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        binder.readBean(customer);
    }

    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {
        private final Customer customer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        SaveEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class DeleteEvent extends CustomerFormEvent {
        DeleteEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class CloseEvent extends CustomerFormEvent {
        CloseEvent(CustomerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
