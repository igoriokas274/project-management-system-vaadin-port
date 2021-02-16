package org.igorl.pma.ui.views.settings;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.igorl.pma.backend.entity.Country;

import java.util.List;

public class CountryForm extends FormLayout {

    TextField countryCode = new TextField("Country code" );
    TextField countryName = new TextField("Country name" );
    Checkbox closed =  new Checkbox( "Closed" );

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Country> binder = new BeanValidationBinder<>( Country.class );
    private Object CountryForm;

    public CountryForm(List<Country> all){

        addClassName( "country-form" );
        binder.bindInstanceFields( this );
        countryCode.setLabel( "Country code" );
        countryCode.setPlaceholder( "Enter country code.." );
        countryName.setLabel( "Country name" );
        countryName.setPlaceholder( "Enter country name.." );

        add(countryCode, countryName, closed);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants( ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut( Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent( new CountryForm.DeleteEvent(this, countryCode, countryName) ));
        close.addClickListener(event -> fireEvent(new CountryForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean( (Country) CountryForm );
            fireEvent(new CountryForm.SaveEvent(this, (TextField) CountryForm ));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setCountryCode(Country country){
        binder.readBean( (Country) CountryForm );
    }

    public void setCountryName(Country country){
        binder.readBean( (Country) CountryForm );
    }

    public static abstract class CountryFormEvent extends ComponentEvent<CountryForm> {
        public Country getCountry;
        private TextField country;

        protected CountryFormEvent(CountryForm source, TextField country) {
            super( source, false );
            this.country = country;
        }
    }

        public class SaveEvent extends CountryForm.CountryFormEvent {
            SaveEvent(CountryForm source, TextField country) {
                super( source, country );
            }
        }

        public class DeleteEvent extends CountryForm.CountryFormEvent {
            DeleteEvent(CountryForm source, TextField countryCode, TextField country) {
                super( source, country );
            }
        }

        public class CloseEvent extends CountryForm.CountryFormEvent {
            CloseEvent(CountryForm source) {
                super( source, null );
            }
        }

        public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
            return getEventBus( ).addListener( eventType, listener );
        }

}
