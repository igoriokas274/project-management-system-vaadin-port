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
import org.igorl.pma.backend.entity.Department;
import org.igorl.pma.backend.service.DepartmentServiceImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/departments", layout = MainLayout.class)
@PageTitle("Departments | PMA")
@CssImport("./styles/shared-styles.css")
public class DepartmentListView extends VerticalLayout {

    private DepartmentServiceImpl departmentService;
    private Grid<Department> grid = new Grid<>(Department.class);
    private TextField filterText = new TextField();
    private DepartmentForm form;

    public DepartmentListView(DepartmentServiceImpl theDepartmentService) {
        this.departmentService = theDepartmentService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new DepartmentForm();
        form.addListener(DepartmentForm.SaveEvent.class, this::saveDepartment);
        form.addListener(DepartmentForm.DeleteEvent.class, this::deleteDepartment);
        form.addListener(DepartmentForm.CloseEvent.class, e -> closeEditor());

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

    private void saveDepartment(DepartmentForm.SaveEvent event) {
        departmentService.save(event.getDepartment());
        updateList();
        closeEditor();
    }

    private void deleteDepartment(DepartmentForm.DeleteEvent event) {
        departmentService.deleteById(event.getDepartment().getDepartmentId());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Department...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addDepartmentButton = new Button("Add department");
        addDepartmentButton.addClickListener(click -> addDepartment());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addDepartmentButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("departmentId", "departmentName", "closed");
        grid.getColumns().forEach(departmentColumn -> departmentColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editDepartment(event.getValue()));
    }

    void addDepartment() {
        grid.asSingleSelect().clear();
        editDepartment(new Department());
    }

    private void editDepartment(Department department) {
        if (department == null) {
            closeEditor();
        } else {
            form.setDepartment(department);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setDepartment(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(departmentService.findAll(filterText.getValue()));
    }
}

