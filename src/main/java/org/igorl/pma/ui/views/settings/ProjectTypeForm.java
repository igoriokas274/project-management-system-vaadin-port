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
import org.igorl.pma.backend.entity.ProjectType;

public class ProjectTypeForm extends FormLayout {

    private ProjectType projectType;

    TextField projectTypeName = new TextField("Project type", "Project type");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<ProjectType> binder = new BeanValidationBinder<>(ProjectType.class);

    public ProjectTypeForm() {

        addClassName("form");
        binder.bindInstanceFields(this);

        add(projectTypeName, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, projectType)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(projectType);
            fireEvent(new SaveEvent(this, projectType));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
        binder.readBean(projectType);
    }


    public static abstract class ProjectTypeFormEvent extends ComponentEvent<ProjectTypeForm> {
        private ProjectType projectType;

        public ProjectType getProjectType() {
            return projectType;
        }

        protected ProjectTypeFormEvent(ProjectTypeForm source, ProjectType projectType) {
            super(source, false);
            this.projectType = projectType;
        }
    }

    public static class SaveEvent extends ProjectTypeFormEvent {
        SaveEvent(ProjectTypeForm source, ProjectType projectType) {
            super(source, projectType);
        }
    }

    public static class DeleteEvent extends ProjectTypeFormEvent {
        DeleteEvent(ProjectTypeForm source, ProjectType projectType) {
            super(source, projectType);
        }
    }

    public static class CloseEvent extends ProjectTypeFormEvent {
        CloseEvent(ProjectTypeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




