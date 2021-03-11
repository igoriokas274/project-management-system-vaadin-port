package org.igorl.pma.ui.views.teams;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Country;
import org.igorl.pma.backend.entity.Department;
import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.enums.Gender;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmployeeForm extends FormLayout {

    private Employee employee;

    TextField firstName = new TextField("First name", "First name");
    TextField middleName = new TextField("Middle name", "Middle name");
    TextField lastName = new TextField("Last name", "Last name");
    TextField title = new TextField("Title", "Title");
    ComboBox<Department> department = new ComboBox<>("Department");
    TextField addressLine1 = new TextField("Address Line 1", "Address Line 1");
    TextField addressLine2 = new TextField("Address Line 2", "Address Line 2");
    TextField city = new TextField("City", "City");
    TextField zipCode = new TextField("Zip Code", "Zip Code");
    ComboBox<Country> country = new ComboBox<>("Country");
    TextField officePhone = new TextField("Office phone", "Office phone");
    TextField mobilePhone = new TextField("Mobile phone", "Mobile phone");
    TextField homePhone = new TextField("Home phone", "Home phone");
    EmailField workEmail = new EmailField("Work email", "Work email");
    EmailField personalEmail = new EmailField("Personal email", "Personal email");
    ComboBox<Gender> gender = new ComboBox<>("Gender");
    DatePicker dateOfBirth = new DatePicker("Date of Birth");
    DatePicker dateOfEmployment = new DatePicker("Date of Employment");
    TextField bankCode = new TextField("Bank code", "Bank code");
    TextField bankName = new TextField("Bank name", "Bank name");
    TextField bankAccount = new TextField("Bank account","Bank account");
    Checkbox closed = new Checkbox("Closed");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    public EmployeeForm(List<Country> countries, List<Department> departments) {

        addClassName("form");
        binder.bindInstanceFields(this);

        Tab main = new Tab("Main");
        Tab personal = new Tab("Personal");

        FormLayout mainInfoForm = new FormLayout();
        FormLayout personalInfoForm = new FormLayout();

        firstName.setClearButtonVisible(true);
        middleName.setClearButtonVisible(true);
        lastName.setClearButtonVisible(true);
        title.setClearButtonVisible(true);
        department.setItems(departments);
        department.setPlaceholder("Select a department..");
        department.setItemLabelGenerator(Department::getDepartmentName);
        addressLine1.setClearButtonVisible(true);
        addressLine2.setClearButtonVisible(true);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountryName);
        country.setPlaceholder("Select a country..");
        officePhone.setClearButtonVisible(true);
        mobilePhone.setClearButtonVisible(true);
        homePhone.setClearButtonVisible(true);
        workEmail.setClearButtonVisible(true);
        workEmail.setErrorMessage("Please enter a valid email address");
        personalEmail.setClearButtonVisible(true);
        personalEmail.setErrorMessage("Please enter a valid email address");
        gender.setPlaceholder("Select a gender..");
        gender.setItems(Gender.values());
        dateOfBirth.setPlaceholder("Select a date..");
        dateOfBirth.setClearButtonVisible(true);
        dateOfBirth.setMax(LocalDate.now());
        dateOfBirth.setLocale(new Locale("lt"));
        dateOfEmployment.setPlaceholder("Select a date..");
        dateOfEmployment.setClearButtonVisible(true);
        dateOfEmployment.setLocale(new Locale("lt"));
        bankCode.setClearButtonVisible(true);
        bankAccount.setClearButtonVisible(true);
        bankName.setClearButtonVisible(true);

        mainInfoForm.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));
        personalInfoForm.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));
        personalInfoForm.setVisible(false);

        mainInfoForm.add(firstName, middleName, lastName, title, department, mobilePhone, officePhone, workEmail,
                dateOfEmployment, closed);

        personalInfoForm.add(addressLine1, addressLine2, city, zipCode, country, homePhone, personalEmail, dateOfBirth,
                bankName, bankCode, bankAccount, gender);
        personalInfoForm.setColspan(addressLine1, 3);
        personalInfoForm.setColspan(addressLine2, 3);
        personalInfoForm.setColspan(bankCode, 1);
        personalInfoForm.setColspan(bankAccount, 2);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(main, mainInfoForm);
        tabsToPages.put(personal, personalInfoForm);

        Tabs employeeTabs = new Tabs(main, personal);
        Div forms = new Div(mainInfoForm, personalInfoForm);

        employeeTabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(employeeTabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(employeeTabs, forms, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employee)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(employee);
            fireEvent(new SaveEvent(this, employee));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        binder.readBean(employee);
    }

    public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
        private final Employee employee;

        protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
            super(source, false);
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }
    }

    public static class SaveEvent extends EmployeeFormEvent {
        SaveEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class DeleteEvent extends EmployeeFormEvent {
        DeleteEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class CloseEvent extends EmployeeFormEvent {
        CloseEvent(EmployeeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
