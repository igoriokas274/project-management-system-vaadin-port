package org.igorl.pma.ui.views.team;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Employee;

import java.util.List;

public class EmployeeForm extends FormLayout {

    private Employee employee;

    public TextField firstName = new TextField( "First name" );
    public TextField middleName = new TextField( "Middle name" );
    public TextField lastName = new TextField( "Last name" );
    public TextField title = new TextField( "Title" );
    public TextField addressLine1 = new TextField( "Address Line 1" );
    public TextField addressLine2 = new TextField( "Address Line 2" );
    public TextField city = new TextField( "City" );
    public TextField zipCode = new TextField( "Zip Code" );
    public NumberField officePhone = new NumberField( "Office phone" );
    public NumberField mobilePhone = new NumberField( "Mobile phone" );
    public NumberField homePhone = new NumberField( "Home phone" );
    public EmailField workEmail = new EmailField( "Work email" );
    public EmailField personalEmail = new EmailField( "Personal email" );
    //public ComboBox gender = new ComboBox( "Gender" );////TODO: fix this
    //public DatePicker dateOfBirth = new DatePicker( "Date of birth" );//TODO: fix this
    //public DatePicker dateOfEmployment = new DatePicker( "Date of employment" );//TODO: fix this
    public TextField bankCode = new TextField( "Bank code" );
    public TextField bankName = new TextField( "Bank name" );
    public TextField bankAccount = new TextField( "Bank account" );
    public Checkbox closed = new Checkbox( "Closed" );

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    public EmployeeForm(List<Employee> employees){

        addClassName("employee-form");
        //binder.bindInstanceFields(this);//TODO: why error?
        firstName.setPlaceholder( "First name" );
        firstName.setClearButtonVisible( true );
        middleName.setPlaceholder( "Middle name" );
        middleName.setClearButtonVisible( true );
        lastName.setPlaceholder( "Last name" );
        lastName.setClearButtonVisible( true );
        title.setPlaceholder( "Title" );
        title.setClearButtonVisible( true );
        addressLine1.setPlaceholder( "Address Line 1" );
        addressLine2.setPlaceholder( "Address Line 2" );
        city.setPlaceholder( "City" );
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
        //gender.setPlaceholder( "Set gender.." );//TODO: fix this
        //gender.setItems( gender );//TODO: fix this
        //dateOfBirth.setPlaceholder( "Choose a date.." );TODO: fix this
        //dateOfBirth.setClearButtonVisible( true );TODO: fix this
        //dateOfEmployment.setPlaceholder( "Choose a date" );//TODO: fix this
        //dateOfEmployment.setClearButtonVisible( true );//TODO: fix this
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
                addressLine1,
                addressLine2,
                city,
                zipCode,
                officePhone,
                mobilePhone,
                homePhone,
                workEmail,
                personalEmail,
                //gender,
                //dateOfBirth,//something wrong
                //dateOfEmployment,//something wrong
                bankCode,
                bankAccount,
                bankName,
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
