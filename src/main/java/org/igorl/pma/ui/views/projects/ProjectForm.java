package org.igorl.pma.ui.views.projects;

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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.*;

import java.util.List;
import java.util.Locale;

public class ProjectForm extends FormLayout {

    private Project project;

    TextField projectName = new TextField("Project name", "Project name");
    DatePicker projectStartDate = new DatePicker("Start Date");
    DatePicker projectEndDate = new DatePicker("End Date");
    TextArea projectMemo1 = new TextArea("Memo 1", "Memo 1");
    TextArea projectMemo2 = new TextArea("Memo 2", "Memo 2");
    Checkbox closed = new Checkbox("Closed");
    ComboBox<ProjectStatus> projectStatus = new ComboBox<>("Status");
    ComboBox<ProjectType> projectType = new ComboBox<>("Type");
    ComboBox<Customer> customer = new ComboBox<>("Customer");
    ComboBox<Employee> manager = new ComboBox<>("Manager");
    ComboBox<PayTerm> payTerm = new ComboBox<>("Payment Terms");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Project> binder = new BeanValidationBinder<>(Project.class);

    public ProjectForm(List<ProjectStatus> projectStatuses, List<ProjectType> projectTypes,
                       List<Customer> customers, List<Employee> employees, List<PayTerm> payTerms) {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        projectName.setClearButtonVisible(true);
        projectStartDate.setPlaceholder("Select a date..");
        projectStartDate.setClearButtonVisible(true);
        projectStartDate.setLocale(new Locale("lt"));
        projectEndDate.setPlaceholder("Select a date..");
        projectEndDate.setClearButtonVisible(true);
        projectEndDate.setLocale(new Locale("lt"));
        projectMemo1.setClearButtonVisible(true);
        projectMemo2.setClearButtonVisible(true);
        projectStatus.setItems(projectStatuses);
        projectStatus.setPlaceholder("Select a status..");
        projectStatus.setItemLabelGenerator(ProjectStatus::getProjectStatusName);
        projectType.setItems(projectTypes);
        projectType.setPlaceholder("Select a type..");
        projectType.setItemLabelGenerator(ProjectType::getProjectTypeName);
        customer.setItems(customers);
        customer.setPlaceholder("Select a customer..");
        customer.setItemLabelGenerator(Customer::getCustomerName);
        manager.setItems(employees);
        manager.setPlaceholder("Select a manager..");
        manager.setItemLabelGenerator(Employee::getFullName);
        payTerm.setItems(payTerms);
        payTerm.setPlaceholder("Select a payment term..");
        payTerm.setItemLabelGenerator(PayTerm::getDescription);

        fieldLayout.add(projectName, projectMemo1, projectMemo2, customer, manager, projectStartDate, projectEndDate,
                projectStatus, projectType, payTerm, closed);
        fieldLayout.setColspan(projectName, 3);
        fieldLayout.setColspan(projectMemo1, 3);
        fieldLayout.setColspan(projectMemo2, 3);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, project)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(project);
            fireEvent(new SaveEvent(this, project));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setProject(Project project) {
        this.project = project;
        binder.readBean(project);
    }

    public static abstract class ProjectFormEvent extends ComponentEvent<ProjectForm> {
        private final Project project;

        protected ProjectFormEvent(ProjectForm source, Project project) {
            super(source, false);
            this.project = project;
        }

        public Project getProject() {
            return project;
        }
    }

    public static class SaveEvent extends ProjectFormEvent {
        SaveEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }

    public static class DeleteEvent extends ProjectFormEvent {
        DeleteEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }

    public static class CloseEvent extends ProjectFormEvent {
        CloseEvent(ProjectForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
