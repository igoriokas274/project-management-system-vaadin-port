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
import org.igorl.pma.backend.entity.Country;

public class CountryForm extends FormLayout {

    private Country country;

    TextField countryCode = new TextField("Country code", "Country code");
    TextField countryName = new TextField("Country name", "Country name");
    Checkbox closed = new Checkbox("Closed");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Country> binder = new BeanValidationBinder<>(Country.class);

    public CountryForm() {

        FormLayout fieldLayout = new FormLayout();

        addClassName("form");
        binder.bindInstanceFields(this);
        countryCode.setClearButtonVisible(true);
        countryCode.setMaxLength(2);
        binder.forField(countryCode)
                .withValidator(new RegexpValidator("Not a valid country code", "[A-Z]{2}"))
                .bind(Country::getCountryCode, Country::setCountryCode);
        countryName.setClearButtonVisible(true);

        fieldLayout.add(countryCode, closed);
        fieldLayout.add(countryName, 3);

        fieldLayout.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        Paragraph help = new Paragraph(new Anchor("https://www.iban.com/country-codes", "ISO 3166 ALPHA-2 code format"));

        add(fieldLayout, createButtonsLayout(), help);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, country)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(country);
            fireEvent(new SaveEvent(this, country));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setCountry(Country country) {
        this.country = country;
        binder.readBean(country);
    }


    public static abstract class CountryFormEvent extends ComponentEvent<CountryForm> {
        private Country country;

        public Country getCountry() {
            return country;
        }

        ;

        protected CountryFormEvent(CountryForm source, Country country) {
            super(source, false);
            this.country = country;
        }
    }

    public static class SaveEvent extends CountryFormEvent {
        SaveEvent(CountryForm source, Country country) {
            super(source, country);
        }
    }

    public static class DeleteEvent extends CountryFormEvent {
        DeleteEvent(CountryForm source, Country country) {
            super(source, country);
        }
    }

    public static class CloseEvent extends CountryFormEvent {
        CloseEvent(CountryForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
