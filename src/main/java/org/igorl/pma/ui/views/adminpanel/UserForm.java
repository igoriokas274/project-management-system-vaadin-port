package org.igorl.pma.ui.views.adminpanel;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.backend.enums.UserRoles;

import java.util.List;
import java.util.Locale;

public class UserForm extends FormLayout {

    private UserAccount userAccount;
    private Locale lithuanian = new Locale("lt");

    public TextField userName = new TextField("Username","Username");
    // TODO: Password should not be populated on edit
    PasswordField password = new PasswordField("Password","Password");
    Checkbox enabled = new Checkbox("Enabled");
    ComboBox<UserRoles> role = new ComboBox<>("Role");
    ComboBox<Employee> employee = new ComboBox<>("Employee");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<UserAccount> binder = new BeanValidationBinder<>(UserAccount.class);

    public UserForm(List<Employee> employees) {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);
        userName.setClearButtonVisible(true);
        password.setClearButtonVisible(true);
        employee.setPlaceholder("Select an employee..");
        employee.setItems(employees);
        employee.setItemLabelGenerator(Employee::getFullName);
        role.setPlaceholder("Select a role..");
        role.setItems(UserRoles.values());

        fieldLayout.add(userName, password, role, employee, enabled);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        HorizontalLayout createdOrChanged = new HorizontalLayout();
        lastModifiedBy.setReadOnly(true);
        lastModifiedDate.setReadOnly(true);
        lastModifiedDate.setLocale(lithuanian);
        createdOrChanged.add(lastModifiedBy, lastModifiedDate);

        add(fieldLayout, createButtonsLayout(), new Hr(), createdOrChanged);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, userAccount)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(userAccount);
            fireEvent(new SaveEvent(this, userAccount));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
        binder.readBean(userAccount);
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private UserAccount userAccount;

        protected UserFormEvent(UserForm source, UserAccount userAccount) {
            super(source, false);
            this.userAccount = userAccount;
        }

        public UserAccount getUserAccount() {
            return userAccount;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, UserAccount userAccount) {
            super(source, userAccount);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, UserAccount userAccount) {
            super(source, userAccount);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
