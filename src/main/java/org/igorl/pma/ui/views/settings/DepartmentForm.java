package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Department;

import java.util.List;

public class DepartmentForm  extends FormLayout {

    TextField departmentName = new TextField("Department name");
    Checkbox closed = new Checkbox("Closed");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Department> binder = new BeanValidationBinder<>( Department.class);
    private Object DepartmentForm;

    public DepartmentForm(List<Department> all){
        
        addClassName( "department-form" );
        binder.bindInstanceFields( this );
        departmentName.setLabel( "Department name" );
        departmentName.setPlaceholder( "Enter department name.." );
        
        add( departmentName,closed );
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants( ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut( Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent( new DeleteEvent(this, departmentName) ));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean( (Department) DepartmentForm );
            fireEvent(new DepartmentForm.SaveEvent(this, (TextField) DepartmentForm ));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setDepartmentName(Department department) {
        binder.readBean( (Department) DepartmentForm );
    }
    

    public static abstract class DepartmentFormEvent extends ComponentEvent<DepartmentForm> {
        public Department getDepartment;
        private TextField department;

        protected DepartmentFormEvent(DepartmentForm source, TextField department) {
            super( source, false );
            this.department = department;
        }


    }

    public class SaveEvent extends DepartmentForm.DepartmentFormEvent {
        SaveEvent(DepartmentForm source, TextField department) {
            super(source, department);
        }
    }

    public class DeleteEvent extends DepartmentForm.DepartmentFormEvent {
        DeleteEvent(DepartmentForm source, TextField department) {
            super(source, department);
        }
    }

    public class CloseEvent extends DepartmentForm.DepartmentFormEvent {
        CloseEvent(DepartmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}



