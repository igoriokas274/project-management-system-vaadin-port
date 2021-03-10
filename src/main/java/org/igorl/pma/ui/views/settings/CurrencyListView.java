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
import org.igorl.pma.backend.entity.Currency;
import org.igorl.pma.backend.service.CurrencyServiceImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/currencies", layout = MainLayout.class)
@PageTitle("Currencies | PMA")
@CssImport("./styles/shared-styles.css")
public class CurrencyListView extends VerticalLayout {

    private CurrencyServiceImpl currencyService;
    private Grid<Currency> grid = new Grid<>(Currency.class);
    private TextField filterText = new TextField();
    private CurrencyForm form;

    public CurrencyListView(CurrencyServiceImpl theCurrencyService) {
        this.currencyService = theCurrencyService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new CurrencyForm();
        form.addListener(CurrencyForm.SaveEvent.class, this::saveCurrency);
        form.addListener(CurrencyForm.DeleteEvent.class, this::deleteCurrency);
        form.addListener(CurrencyForm.CloseEvent.class, e -> closeEditor());

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
        RouterLink payTermList = new RouterLink("Pay Terms", PayTermListView.class);
        RouterLink projectStatusList = new RouterLink("Project status", ProjectStatusListView.class);
        RouterLink projectTypeList = new RouterLink("Project type", ProjectTypeListView.class);
        RouterLink quotationStatusList = new RouterLink("Quotation status", QuotationStatusListView.class);
        routerLinks.add(countryList, currencyList, departmentList, payTermList, projectStatusList, projectTypeList,
                quotationStatusList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

        updateList();
    }

    private void saveCurrency(CurrencyForm.SaveEvent event) {
        currencyService.save(event.getCurrency());
        updateList();
        closeEditor();
    }

    private void deleteCurrency(CurrencyForm.DeleteEvent event) {
        currencyService.deleteById(event.getCurrency().getCurrencyId());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Currency...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCurrencyButton = new Button("Add currency");
        addCurrencyButton.addClickListener(click -> addCurrency());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCurrencyButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("currencyId", "currencyCode", "currencyName", "closed");
        grid.getColumns().forEach(CurrencyColumn -> CurrencyColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editCurrency(event.getValue()));
    }

    void addCurrency() {
        grid.asSingleSelect().clear();
        editCurrency(new Currency());
    }

    private void editCurrency(Currency currency) {
        if (currency == null) {
            closeEditor();
        } else {
            form.setCurrency(currency);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCurrency(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(currencyService.findAll(filterText.getValue()));
    }
}


