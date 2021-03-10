package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.ProjectStatus;

public class ProjectStatusForm extends FormLayout {

    private ProjectStatus projectStatus;

    TextField projectStatusName = new TextField("Project status");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<ProjectStatus> binder = new BeanValidationBinder<>(ProjectStatus.class);

    public ProjectStatusForm() {

        addClassName("form");
        binder.bindInstanceFields(this);

        add(projectStatusName, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, projectStatus)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(projectStatus);
            fireEvent(new SaveEvent(this, projectStatus));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
        binder.readBean(projectStatus);
    }


    public static abstract class ProjectStatusFormEvent extends ComponentEvent<ProjectStatusForm> {
        private ProjectStatus projectStatus;

        public ProjectStatus getProjectStatus() {
            return projectStatus;
        }

        protected ProjectStatusFormEvent(ProjectStatusForm source, ProjectStatus projectStatus) {
            super(source, false);
            this.projectStatus = projectStatus;
        }
    }

    public static class SaveEvent extends ProjectStatusFormEvent {
        SaveEvent(ProjectStatusForm source, ProjectStatus projectStatus) {
            super(source, projectStatus);
        }
    }

    public static class DeleteEvent extends ProjectStatusFormEvent {
        DeleteEvent(ProjectStatusForm source, ProjectStatus projectStatus) {
            super(source, projectStatus);
        }
    }

    public static class CloseEvent extends ProjectStatusFormEvent {
        CloseEvent(ProjectStatusForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




