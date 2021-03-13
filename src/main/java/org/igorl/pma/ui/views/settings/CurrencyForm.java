package org.igorl.pma.ui.views.settings;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Currency;

public class CurrencyForm extends FormLayout {

    private Currency currency;

    TextField currencyCode = new TextField("Currency code", "Currency code");
    TextField currencyName = new TextField("Currency name", "Currency name");
    Checkbox closed = new Checkbox("Closed");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Currency> binder = new BeanValidationBinder<>(Currency.class);

    public CurrencyForm() {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);
        currencyCode.setClearButtonVisible(true);
        currencyCode.setMaxLength(3);
        binder.forField(currencyCode)
                .withValidator(new RegexpValidator("Not a valid currency code", "[A-Z]{3}"))
                .bind(Currency::getCurrencyCode, Currency::setCurrencyCode);
        currencyName.setClearButtonVisible(true);

        fieldLayout.add(currencyCode, closed);
        fieldLayout.add(currencyName, 3);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        Paragraph help = new Paragraph(new Anchor("https://www.iban.com/currency-codes", "ISO 4217 code format"));

        add(fieldLayout, createButtonsLayout(), help);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, currency)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(currency);
            fireEvent(new SaveEvent(this, currency));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
        binder.readBean(currency);
    }


    public static abstract class CurrencyFormEvent extends ComponentEvent<CurrencyForm> {
        private Currency currency;

        public Currency getCurrency() {
            return currency;
        }

        protected CurrencyFormEvent(CurrencyForm source, Currency currency) {
            super(source, false);
            this.currency = currency;
        }
    }

    public static class SaveEvent extends CurrencyFormEvent {
        SaveEvent(CurrencyForm source, Currency currency) {
            super(source, currency);
        }
    }

    public static class DeleteEvent extends CurrencyFormEvent {
        DeleteEvent(CurrencyForm source, Currency currency) {
            super(source, currency);
        }
    }

    public static class CloseEvent extends CurrencyFormEvent {
        CloseEvent(CurrencyForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
