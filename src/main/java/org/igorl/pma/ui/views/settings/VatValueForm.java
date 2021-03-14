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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.VATValue;

import java.util.Locale;

public class VatValueForm extends FormLayout {

    private VATValue value;
    private Locale lithuanian = new Locale("lt");

    TextField description = new TextField("Description", "Description");
    NumberField vatValue = new NumberField("VAT value", "VAT value");

    TextField lastModifiedBy = new TextField("Modified by:");
    DateTimePicker lastModifiedDate = new DateTimePicker("Modified at:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<VATValue> binder = new BeanValidationBinder<>(VATValue.class);

    public VatValueForm() {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        description.setClearButtonVisible(true);
        vatValue.setHasControls(true);
        vatValue.setStep(0.1d);
        vatValue.setMin(0);
        vatValue.setMax(100);


        fieldLayout.add(vatValue, description);
        fieldLayout.setColspan(vatValue, 1);
        fieldLayout.setColspan(description, 1);

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, value)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(value);
            fireEvent(new SaveEvent(this, value));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setVatValue(VATValue value) {
        this.value = value;
        binder.readBean(value);
    }


    public static abstract class VatValueFormEvent extends ComponentEvent<VatValueForm> {
        private VATValue vatValue;

        public VATValue getVatValue() {
            return vatValue;
        }

        protected VatValueFormEvent(VatValueForm source, VATValue vatValue) {
            super(source, false);
            this.vatValue = vatValue;
        }
    }

    public static class SaveEvent extends VatValueFormEvent {
        SaveEvent(VatValueForm source, VATValue vatValue) {
            super(source, vatValue);
        }
    }

    public static class DeleteEvent extends VatValueFormEvent {
        DeleteEvent(VatValueForm source, VATValue vatValue) {
            super(source, vatValue);
        }
    }

    public static class CloseEvent extends VatValueFormEvent {
        CloseEvent(VatValueForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




