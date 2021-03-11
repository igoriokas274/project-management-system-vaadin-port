package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.VATValue;

public class VatValueForm extends FormLayout {

    private VATValue value;

    TextField description = new TextField("Description");
    BigDecimalField vatValue = new BigDecimalField("VAT value");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<VATValue> binder = new BeanValidationBinder<>(VATValue.class);

    public VatValueForm() {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);

        description.setPlaceholder("Description");
        description.setClearButtonVisible(true);
        vatValue.setPlaceholder("VAT value");
        vatValue.setClearButtonVisible(true);

        fieldLayout.add(description, vatValue);
        fieldLayout.setColspan(description, 1);
        fieldLayout.setColspan(vatValue, 1);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("25em", 2),
                new ResponsiveStep("30em", 3));

        add(fieldLayout, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);

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




