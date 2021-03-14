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
import org.igorl.pma.backend.entity.Department;
import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.entity.Item;
import org.igorl.pma.backend.entity.Supplier;
import org.igorl.pma.backend.service.*;
import org.igorl.pma.ui.MainLayout;

@Route(value = "stock/item", layout = MainLayout.class)
@PageTitle("Items | PMA")
@CssImport("./styles/shared-styles.css")
public class ItemListView extends VerticalLayout {

    public ItemServiceImpl itemService;

    public Grid<Item> grid = new Grid<>(Item.class);
    public TextField filterText = new TextField();
    public ItemForm form;

    public ItemListView(ItemServiceImpl itemService , StockTypeImpl stockType,
                        SupplierServiceImpl supplierService,
                        VATValueImpl vatValue) {

        this.itemService = itemService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new ItemForm(stockType.findAll(), supplierService.findAll(), vatValue.findAll());
        form.addListener(ItemForm.SaveEvent.class, this::saveItem);
        form.addListener(ItemForm.DeleteEvent.class, this::deleteItem);
        form.addListener(ItemForm.CloseEvent.class, e -> closeEditor());
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

        grid.removeColumnByKey("supplier");
        grid.removeColumnByKey("closed");
        grid.setColumns("itemId", "itemName", "itemType", "salesPrice", "purchasePrice");
        grid.addColumn(item -> {
            Supplier supplier = item.getSupplier();
            return supplier == null ? "-" : supplier.getSupplierName();
        }).setHeader("Supplier").setSortable(true);
        grid.addColumn(Item::isClosed, "closed").setHeader("Closed");

        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editItem(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Item name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addItemButton = new Button("Add item");
        addItemButton.addClickListener(click -> addItem());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addItemButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveItem(ItemForm.SaveEvent event) {
        itemService.save(event.getItem());
        updateList();
        closeEditor();
    }

    private void deleteItem(ItemForm.DeleteEvent event) {
        itemService.deleteById(event.getItem().getItemId());
        updateList();
        closeEditor();
    }

    void addItem() {
        grid.asSingleSelect().clear();
        editItem(new Item());
    }

    public void editItem(Item item) {
        if (item == null) {
            closeEditor();
        } else {
            form.setItem(item);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setItem(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(itemService.findAll(filterText.getValue()));
    }
}

