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
import org.igorl.pma.backend.entity.QuotationStatus;
import org.igorl.pma.backend.service.QuotationStatusImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/quotationstatus", layout = MainLayout.class)
@PageTitle("Quotation Statuses | PMA")
@CssImport("./styles/shared-styles.css")
public class QuotationStatusListView extends VerticalLayout {

    private QuotationStatusImpl quotationStatus;
    private Grid<QuotationStatus> grid = new Grid<>(QuotationStatus.class);
    private TextField filterText = new TextField();
    private QuotationStatusForm form;

    public QuotationStatusListView(QuotationStatusImpl quotationStatus) {
        this.quotationStatus = quotationStatus;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new QuotationStatusForm();
        form.addListener(QuotationStatusForm.SaveEvent.class, this::saveQuotationStatus);
        form.addListener(QuotationStatusForm.DeleteEvent.class, this::deleteQuotationStatus);
        form.addListener(QuotationStatusForm.CloseEvent.class, e -> closeEditor());

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

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

        updateList();
    }

    private void saveQuotationStatus(QuotationStatusForm.SaveEvent event) {
        quotationStatus.save(event.getQuotationStatus());
        updateList();
        closeEditor();
    }

    private void deleteQuotationStatus(QuotationStatusForm.DeleteEvent event) {
        quotationStatus.deleteById(event.getQuotationStatus().getQuotationStatusId());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Quotation Status...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addQuotationStatusButton = new Button("Add quotation status");
        addQuotationStatusButton.addClickListener(click -> addQuotationStatus());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addQuotationStatusButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("quotationStatusId", "quotationStatusName");
        grid.getColumns().forEach(quotationStatusColumn -> quotationStatusColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editQuotationStatus(event.getValue()));
    }

    void addQuotationStatus() {
        grid.asSingleSelect().clear();
        editQuotationStatus(new QuotationStatus());
    }

    private void editQuotationStatus(QuotationStatus quotationStatus) {
        if (quotationStatus == null) {
            closeEditor();
        } else {
            form.setQuotationStatus(quotationStatus);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setQuotationStatus(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(quotationStatus.findAll(filterText.getValue()));
    }
}

