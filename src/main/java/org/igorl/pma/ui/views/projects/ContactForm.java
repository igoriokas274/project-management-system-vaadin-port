package org.igorl.pma.ui.views.projects;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.*;
import org.igorl.pma.backend.enums.Gender;

import java.util.List;

public class ContactForm extends FormLayout {

    private Contact contact;

    TextField firstName = new TextField("First name", "First name");
    TextField middleName = new TextField("Middle name", "Middle name");
    TextField lastName = new TextField("Last name", "Last name");
    TextField title = new TextField("Title", "Title");
    TextField contactPhone = new TextField("Contact phone", "Contact phone");
    EmailField contactEmail = new EmailField("Contact email", "Contact email");
    ComboBox<Gender> gender = new ComboBox<>("Gender");
    Checkbox closed = new Checkbox("Closed");
    ComboBox<Customer> customer = new ComboBox<>("Customer");
    ComboBox<Supplier> supplier = new ComboBox<>("Supplier");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    public ContactForm(List<Customer> customers, List<Supplier> suppliers) {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        firstName.setClearButtonVisible(true);
        middleName.setClearButtonVisible(true);
        lastName.setClearButtonVisible(true);
        title.setClearButtonVisible(true);
        contactPhone.setClearButtonVisible(true);
        contactEmail.setClearButtonVisible(true);
        contactEmail.setErrorMessage("Please enter a valid email address");
        gender.setPlaceholder("Select a gender..");
        gender.setItems(Gender.values());
        customer.setItems(customers);
        customer.setPlaceholder("Select a customer..");
        customer.setItemLabelGenerator(Customer::getCustomerName);
        supplier.setItems(suppliers);
        supplier.setItemLabelGenerator(Supplier::getSupplierName);
        supplier.setPlaceholder("Select a supplier..");

        fieldLayout.add(firstName, middleName, lastName, title, contactPhone, contactEmail, gender, customer,
                supplier, close);

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(contact);
            fireEvent(new SaveEvent(this, contact));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        binder.readBean(contact);
    }

    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private final Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
