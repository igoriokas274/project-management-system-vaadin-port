package org.igorl.pma.ui.views.projects;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class QuotationForm extends FormLayout {

    private Quotation quotation;
    private LocalDate currentDate = LocalDate.now();
    private Locale lithuanian = new Locale("lt");

    TextField quotationTitle = new TextField("Title", "Enter a title");
    Checkbox confirmed = new Checkbox("Confirmed");
    DatePicker quotationDate = new DatePicker(currentDate, lithuanian);
    ComboBox<Project> project = new ComboBox<>("Project");
    ComboBox<QuotationStatus> quotationStatus = new ComboBox<>("Status");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Quotation> binder = new BeanValidationBinder<>(Quotation.class);

    public QuotationForm(List<Project> projects, List<QuotationStatus> quotationStatuses) {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        quotationTitle.setClearButtonVisible(true);
        quotationDate.setLabel("Creation date");
        quotationDate.setClearButtonVisible(true);
        project.setItems(projects);
        project.setPlaceholder("Select a project..");
        project.setItemLabelGenerator(Project::getProjectName);
        quotationStatus.setItems(quotationStatuses);
        quotationStatus.setPlaceholder("Select a status..");
        quotationStatus.setItemLabelGenerator(QuotationStatus::getQuotationStatusName);

        confirmed.addValueChangeListener(event -> {
            if (event.getValue()) {
                quotationTitle.setReadOnly(true);
                quotationDate.setReadOnly(true);
                project.setReadOnly(true);
                quotationStatus.setReadOnly(true);
                confirmed.setEnabled(false);
                delete.setEnabled(false);
            } else {
                quotationTitle.setReadOnly(false);
                quotationDate.setReadOnly(false);
                project.setReadOnly(false);
                quotationStatus.setReadOnly(false);
                confirmed.setEnabled(true);
                delete.setEnabled(true);
            }
        });

        fieldLayout.add(quotationTitle, project, quotationDate, quotationStatus, confirmed);
        fieldLayout.setColspan(quotationTitle, 3);
        fieldLayout.setColspan(project, 3);

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, quotation)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(quotation);
            fireEvent(new SaveEvent(this, quotation));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
        binder.readBean(quotation);
    }

    public static abstract class QuotationFormEvent extends ComponentEvent<QuotationForm> {
        private final Quotation quotation;

        protected QuotationFormEvent(QuotationForm source, Quotation quotation) {
            super(source, false);
            this.quotation = quotation;
        }

        public Quotation getQuotation() {
            return quotation;
        }
    }

    public static class SaveEvent extends QuotationFormEvent {
        SaveEvent(QuotationForm source, Quotation quotation) {
            super(source, quotation);
        }
    }

    public static class DeleteEvent extends QuotationFormEvent {
        DeleteEvent(QuotationForm source, Quotation quotation) {
            super(source, quotation);
        }
    }

    public static class CloseEvent extends QuotationFormEvent {
        CloseEvent(QuotationForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
