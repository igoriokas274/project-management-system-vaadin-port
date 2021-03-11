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
import org.igorl.pma.backend.entity.VATValue;
import org.igorl.pma.backend.service.VATValueImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/vatvalues", layout = MainLayout.class)
@PageTitle("VAT Values | PMA")
@CssImport("./styles/shared-styles.css")
public class VatValueListView extends VerticalLayout {

    private VATValueImpl vatValue;
    private Grid<VATValue> grid = new Grid<>(VATValue.class);
    private TextField filterText = new TextField();
    private VatValueForm form;

    public VatValueListView(VATValueImpl vatValue) {
        this.vatValue = vatValue;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new VatValueForm();
        form.addListener(VatValueForm.SaveEvent.class, this::saveVatValue);
        form.addListener(VatValueForm.DeleteEvent.class, this::deleteVatValue);
        form.addListener(VatValueForm.CloseEvent.class, e -> closeEditor());

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
        grid.setColumns("vatId", "description", "vatValue");
        grid.getColumns().forEach(stockTypeColumn -> stockTypeColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editVatValue(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by VAT value...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addVATValueButton = new Button("Add VAT value");
        addVATValueButton.addClickListener(click -> addVatValue());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addVATValueButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveVatValue(VatValueForm.SaveEvent event) {
        vatValue.save(event.getVatValue());
        updateList();
        closeEditor();
    }

    private void deleteVatValue(VatValueForm.DeleteEvent event) {
        vatValue.deleteById(event.getVatValue().getVatId());
        updateList();
        closeEditor();
    }

    void addVatValue() {
        grid.asSingleSelect().clear();
        editVatValue(new VATValue());
    }

    private void editVatValue(VATValue vatValue) {
        if (vatValue == null) {
            closeEditor();
        } else {
            form.setVatValue(vatValue);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setVatValue(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(vatValue.findAll(filterText.getValue()));
    }
}

