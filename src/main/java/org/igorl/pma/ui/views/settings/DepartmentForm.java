package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Department;

import java.text.Normalizer;
import java.util.Locale;

public class DepartmentForm extends FormLayout {

    private Department department;
    private Locale lithuanian = new Locale("lt");

    TextField departmentName = new TextField("Department name", "Department name");
    Checkbox closed = new Checkbox("Closed");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Department> binder = new BeanValidationBinder<>(Department.class);

    public DepartmentForm() {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);
        fieldLayout.add(departmentName, closed);

        fieldLayout.setColspan(departmentName, 1);

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, department)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(department);
            fireEvent(new SaveEvent(this, department));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setDepartment(Department department) {
        this.department = department;
        binder.readBean(department);
    }


    public static abstract class DepartmentFormEvent extends ComponentEvent<DepartmentForm> {
        private Department department;

        public Department getDepartment() {
            return department;
        }

        protected DepartmentFormEvent(DepartmentForm source, Department department) {
            super(source, false);
            this.department = department;
        }
    }

    public static class SaveEvent extends DepartmentFormEvent {
        SaveEvent(DepartmentForm source, Department department) {
            super(source, department);
        }
    }

    public static class DeleteEvent extends DepartmentFormEvent {
        DeleteEvent(DepartmentForm source, Department department) {
            super(source, department);
        }
    }

    public static class CloseEvent extends DepartmentFormEvent {
        CloseEvent(DepartmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




