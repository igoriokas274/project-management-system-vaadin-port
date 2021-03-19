package org.igorl.pma.ui.views.stock;

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
import org.igorl.pma.backend.entity.Supplier;
import org.igorl.pma.backend.service.CountryServiceImpl;
import org.igorl.pma.backend.service.CurrencyServiceImpl;
import org.igorl.pma.backend.service.PayTermServiceImpl;
import org.igorl.pma.backend.service.SupplierServiceImpl;
import org.igorl.pma.ui.MainLayout;

@Route(value = "stock/supplier", layout = MainLayout.class)
@PageTitle("Suppliers | PMA")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/true-false.css", themeFor = "vaadin-grid")
public class SupplierListView extends VerticalLayout {

    public SupplierServiceImpl supplierService;

    public Grid<Supplier> grid = new Grid<>(Supplier.class);
    public TextField filterText = new TextField();
    public org.igorl.pma.ui.views.stock.SupplierForm form;

    public SupplierListView(SupplierServiceImpl supplierService ,CountryServiceImpl countryService,
                            PayTermServiceImpl payTermService,
                            CurrencyServiceImpl currencyService) {

        this.supplierService = supplierService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new org.igorl.pma.ui.views.stock.SupplierForm(countryService.findAll(), payTermService.findAll(), currencyService.findAll());
        form.addListener(org.igorl.pma.ui.views.stock.SupplierForm.SaveEvent.class, this::saveSupplier);
        form.addListener(org.igorl.pma.ui.views.stock.SupplierForm.DeleteEvent.class, this::deleteSupplier);
        form.addListener(org.igorl.pma.ui.views.stock.SupplierForm.CloseEvent.class, e -> closeEditor());
        closeEditor();

        Div div = new Div(grid, form);
        div.addClassName("content");
        div.setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.add(getToolbar(), div);

        Icon icon = VaadinIcon.STOCK.create();
        String pageName = "Stock Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink itemList = new RouterLink("Items", ItemListView.class);
        RouterLink supplierList = new RouterLink("Suppliers", SupplierListView.class);
        routerLinks.add(itemList, supplierList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon, pageName, routerLinks, content));

        updateList();
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("supplierId", "supplierName");
        grid.getColumns().forEach(supplierColumn -> supplierColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editSupplier(event.getValue()));
        grid.addColumn(supplier -> supplier.isClosed() ? "Closed" : "Active").setHeader("Status").setSortable(true);
        grid.setClassNameGenerator(supplier -> supplier.isClosed() ? "false" : "true");
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Supplier...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addSupplierButton = new Button("Add supplier");
        addSupplierButton.addClickListener(click -> addSupplier());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSupplierButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveSupplier(SupplierForm.SaveEvent event) {
        supplierService.save(event.getSupplier());
        updateList();
        closeEditor();
    }

    private void deleteSupplier(SupplierForm.DeleteEvent event) {
        supplierService.deleteById(event.getSupplier().getSupplierId());
        updateList();
        closeEditor();
    }

    void addSupplier() {
        grid.asSingleSelect().clear();
        editSupplier(new Supplier());
    }

    public void editSupplier(Supplier supplier) {
        if (supplier == null) {
            closeEditor();
        } else {
            form.setSupplier(supplier);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setSupplier(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(supplierService.findAll(filterText.getValue()));
    }
}

