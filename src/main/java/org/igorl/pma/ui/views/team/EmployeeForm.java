package org.igorl.pma.ui.views.team;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

import java.util.List;

public class EmployeeForm extends FormLayout {

    private Employee employee;

    TextField firstName = new TextField( "First name" );
    TextField middleName = new TextField( "Middle name" );
    TextField lastName = new TextField( "Last name" );
    TextField title = new TextField( "Title" );
    ComboBox<Department> department = new ComboBox<>("Department");
    TextField addressLine1 = new TextField( "Address Line 1" );
    TextField addressLine2 = new TextField( "Address Line 2" );
    TextField city = new TextField( "City" );
    TextField zipCode = new TextField( "Zip Code" );
    ComboBox<Country> country = new ComboBox<>("Countries");
    TextField officePhone = new TextField( "Office phone" );
    TextField mobilePhone = new TextField( "Mobile phone" );
    TextField homePhone = new TextField( "Home phone" );
    EmailField workEmail = new EmailField( "Work email" );
    EmailField personalEmail = new EmailField( "Personal email" );
    ComboBox<Gender> gender = new ComboBox<>( "Gender" );
    DatePicker dateOfBirth = new DatePicker("Date of Birth");
    DatePicker dateOfEmployment = new DatePicker("Date of Employment");
    TextField bankCode = new TextField( "Bank code" );
    TextField bankName = new TextField( "Bank name" );
    TextField bankAccount = new TextField( "Bank account" );
    Checkbox closed = new Checkbox( "Closed" );

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    public EmployeeForm(List<Country> countries, List<Department> departments){

        addClassName("employee-form");
        binder.bindInstanceFields(this);
        firstName.setPlaceholder( "First name" );
        firstName.setClearButtonVisible( true );
        middleName.setPlaceholder( "Middle name" );
        middleName.setClearButtonVisible( true );
        lastName.setPlaceholder( "Last name" );
        lastName.setClearButtonVisible( true );
        title.setPlaceholder( "Title" );
        title.setClearButtonVisible( true );
        department.setItems(departments);
        department.setItemLabelGenerator(Department::getDepartmentName);
        addressLine1.setPlaceholder( "Address Line 1" );
        addressLine2.setPlaceholder( "Address Line 2" );
        city.setPlaceholder( "City" );
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountryName);
        zipCode.setPlaceholder( "Zip code" );
        officePhone.setPlaceholder( "Office phone" );
        officePhone.setClearButtonVisible( true );
        mobilePhone.setPlaceholder( "mobile phone" );
        mobilePhone.setClearButtonVisible( true );
        homePhone.setPlaceholder( "Office phone" );
        homePhone.setClearButtonVisible( true );
        workEmail.setPlaceholder( "Work email" );
        workEmail.setClearButtonVisible( true );
        workEmail.setErrorMessage("Please enter a valid email address");
        personalEmail.setPlaceholder( "Personal email" );
        personalEmail.setClearButtonVisible( true );
        personalEmail.setErrorMessage("Please enter a valid email address");
        gender.setPlaceholder( "Set gender.." );
        gender.setItems(Gender.values());
        dateOfBirth.setPlaceholder( "Choose a date.." );
        dateOfBirth.setClearButtonVisible( true );
        dateOfEmployment.setPlaceholder( "Choose a date" );
        dateOfEmployment.setClearButtonVisible( true );
        bankCode.setPlaceholder( "Bank code" );
        bankCode.setClearButtonVisible( true );
        bankAccount.setPlaceholder( "Bank account" );
        bankAccount.setClearButtonVisible( true );
        bankName.setPlaceholder( "Bank name" );
        bankName.setClearButtonVisible( true );

        add(firstName,
                middleName,
                lastName,
                title,
                department,
                addressLine1,
                addressLine2,
                city,
                zipCode,
                country,
                officePhone,
                mobilePhone,
                homePhone,
                workEmail,
                personalEmail,
                gender,
                dateOfBirth,
                dateOfEmployment,
                bankCode,
                bankName,
                bankAccount,
                closed,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants( ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut( Key.ENTER);
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
