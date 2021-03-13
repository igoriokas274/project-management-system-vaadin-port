package org.igorl.pma.ui.views.settings;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.backend.entity.Country;
import org.igorl.pma.backend.service.CountryServiceImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/countries", layout = MainLayout.class)
@PageTitle("Countries | PMA")
@CssImport("./styles/shared-styles.css")
public class CountryListView extends VerticalLayout {

    private CountryServiceImpl countryService;
    private Grid<Country> grid = new Grid<>(Country.class);
    private TextField filterText = new TextField();
    private CountryForm form;

    public CountryListView(CountryServiceImpl theCountryService) {
        this.countryService = theCountryService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new CountryForm();
        form.addListener(CountryForm.SaveEvent.class, this::saveCountry);
        form.addListener(CountryForm.DeleteEvent.class, this::deleteCountry);
        form.addListener(CountryForm.CloseEvent.class, e -> closeEditor());

        closeEditor();

        Div div = new Div(grid, form);
        div.addClassName("content");
        div.setSizeFull();

        VerticalLayout content = new VerticalLayout(getToolbar(), div);

        Icon icon = VaadinIcon.COG_O.create();
        String pageName = "Settings Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink countryList = new RouterLink("Countries", CountryListView.class);
        RouterLink currencyList = new RouterLink("Currencies", CurrencyListView.class);
        RouterLink departmentList = new RouterLink("Departments", DepartmentListView.class);
        RouterLink payTermList = new RouterLink("Payment Terms", PayTermListView.class);
        RouterLink projectStatusList = new RouterLink("Project Statuses", ProjectStatusListView.class);
        RouterLink projectTypeList = new RouterLink("Project Types", ProjectTypeListView.class);
        RouterLink quotationStatusList = new RouterLink("Quotation Statuses", QuotationStatusListView.class);
        RouterLink stockTypeList = new RouterLink("Stock Types", StockTypeListView.class);
        RouterLink vatValueList = new RouterLink("VAT values", VatValueListView.class);
        routerLinks.add(countryList, currencyList, departmentList, payTermList, projectStatusList, projectTypeList,
                quotationStatusList, stockTypeList, vatValueList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon, pageName, routerLinks, content));

        updateList();
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("countryId", "countryCode", "countryName", "closed");
        grid.getColumns().forEach(CountryColumn -> CountryColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editCountry(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Country...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCountryButton = new Button("Add country");
        addCountryButton.addClickListener(click -> addCountry());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCountryButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveCountry(CountryForm.SaveEvent event) {
        countryService.save(event.getCountry());
        updateList();
        closeEditor();
    }

    private void deleteCountry(CountryForm.DeleteEvent event) {
        countryService.deleteById(event.getCountry().getCountryId());
        updateList();
        closeEditor();
    }

    void addCountry() {
        grid.asSingleSelect().clear();
        editCountry(new Country());
    }

    private void editCountry(Country country) {
        if (country == null) {
            closeEditor();
        } else {
            form.setCountry(country);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCountry(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(countryService.findAll(filterText.getValue()));
    }
}


