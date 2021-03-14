package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.QuotationStatus;

import java.util.Locale;

public class QuotationStatusForm extends FormLayout {

    private QuotationStatus quotationStatus;
    private Locale lithuanian = new Locale("lt");

    TextField quotationStatusName = new TextField("Quotation status", "Quotation status");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<QuotationStatus> binder = new BeanValidationBinder<>(QuotationStatus.class);

    public QuotationStatusForm() {

        addClassName("form");
        binder.bindInstanceFields(this);

        HorizontalLayout createdOrChanged = new HorizontalLayout();
        lastModifiedBy.setReadOnly(true);
        lastModifiedDate.setReadOnly(true);
        lastModifiedDate.setLocale(lithuanian);
        createdOrChanged.add(lastModifiedBy, lastModifiedDate);

        add(quotationStatusName, createButtonsLayout(), new Hr(), createdOrChanged);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, quotationStatus)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(quotationStatus);
            fireEvent(new SaveEvent(this, quotationStatus));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setQuotationStatus(QuotationStatus quotationStatus) {
        this.quotationStatus = quotationStatus;
        binder.readBean(quotationStatus);
    }


    public static abstract class QuotationStatusFormEvent extends ComponentEvent<QuotationStatusForm> {
        private QuotationStatus quotationStatus;

        public QuotationStatus getQuotationStatus() {
            return quotationStatus;
        }

        protected QuotationStatusFormEvent(QuotationStatusForm source, QuotationStatus quotationStatus) {
            super(source, false);
            this.quotationStatus = quotationStatus;
        }
    }

    public static class SaveEvent extends QuotationStatusFormEvent {
        SaveEvent(QuotationStatusForm source, QuotationStatus quotationStatus) {
            super(source, quotationStatus);
        }
    }

    public static class DeleteEvent extends QuotationStatusFormEvent {
        DeleteEvent(QuotationStatusForm source, QuotationStatus quotationStatus) {
            super(source, quotationStatus);
        }
    }

    public static class CloseEvent extends QuotationStatusFormEvent {
        CloseEvent(QuotationStatusForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




