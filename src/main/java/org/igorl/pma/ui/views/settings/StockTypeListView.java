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
import org.igorl.pma.backend.entity.StockType;
import org.igorl.pma.backend.service.CountryServiceImpl;
import org.igorl.pma.backend.service.StockTypeImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/stocktype", layout = MainLayout.class)
@PageTitle("Stock Types | PMA")
@CssImport("./styles/shared-styles.css")
public class StockTypeListView extends VerticalLayout {

    private StockTypeImpl stockType;
    private Grid<StockType> grid = new Grid<>(StockType.class);
    private TextField filterText = new TextField();
    private StockTypeForm form;


    public StockTypeListView(StockTypeImpl stockType, CountryServiceImpl countryService) {
        this.stockType = stockType;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new StockTypeForm(countryService.findAll());
        form.addListener(StockTypeForm.SaveEvent.class, this::saveStockType);
        form.addListener(StockTypeForm.DeleteEvent.class, this::deleteStockType);
        form.addListener(StockTypeForm.CloseEvent.class, e -> closeEditor());

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
        grid.removeColumnByKey("closed");
        grid.removeColumnByKey("addressLine1");
        grid.removeColumnByKey("addressLine2");
        grid.removeColumnByKey("city");
        grid.removeColumnByKey("zipCode");
        grid.setColumns("stockId", "stockName");
        grid.addColumn(StockType::getFullAddress, "fullAddress").setHeader("Full address");
        grid.addColumn(StockType::isClosed, "closed").setHeader("Closed");
        grid.getColumns().forEach(stockTypeColumn -> stockTypeColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editStockType(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Stock Type...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addStockTypeButton = new Button("Add stock type");
        addStockTypeButton.addClickListener(click -> addStockType());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStockTypeButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveStockType(StockTypeForm.SaveEvent event) {
        stockType.save(event.getStockType());
        updateList();
        closeEditor();
    }

    private void deleteStockType(StockTypeForm.DeleteEvent event) {
        stockType.deleteById(event.getStockType().getStockId());
        updateList();
        closeEditor();
    }

    void addStockType() {
        grid.asSingleSelect().clear();
        editStockType(new StockType());
    }

    private void editStockType(StockType stockType) {
        if (stockType == null) {
            closeEditor();
        } else {
            form.setStockType(stockType);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setStockType(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(stockType.findAll(filterText.getValue()));
    }
}

