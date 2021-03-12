package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.PayTerm;

public class PayTermForm extends FormLayout {

    private PayTerm payTerm;

    // TODO: Check compatibility with BigDecimalFiled math actions [check PayTerm entity]
    IntegerField term = new IntegerField("Pay term", "Pay term");
    TextField description = new TextField("Description", "Description");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<PayTerm> binder = new BeanValidationBinder<>(PayTerm.class);

    public PayTermForm() {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);
        term.setHasControls(true);
        term.setMin(0);
        description.setClearButtonVisible(true);

        fieldLayout.add(term, description);
        fieldLayout.setColspan(term, 1);
        fieldLayout.setColspan(description, 1);

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, payTerm)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(payTerm);
            fireEvent(new SaveEvent(this, payTerm));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setPayTerm(PayTerm payTerm) {
        this.payTerm = payTerm;
        binder.readBean(payTerm);
    }


    public static abstract class PayTermFormEvent extends ComponentEvent<PayTermForm> {
        private PayTerm payTerm;

        public PayTerm getPayTerm() {
            return payTerm;
        }

        protected PayTermFormEvent(PayTermForm source, PayTerm payTerm) {
            super(source, false);
            this.payTerm = payTerm;
        }
    }

    public static class SaveEvent extends PayTermFormEvent {
        SaveEvent(PayTermForm source, PayTerm payTerm) {
            super(source, payTerm);
        }
    }

    public static class DeleteEvent extends PayTermFormEvent {
        DeleteEvent(PayTermForm source, PayTerm payTerm) {
            super(source, payTerm);
        }
    }

    public static class CloseEvent extends PayTermFormEvent {
        CloseEvent(PayTermForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}




