package org.igorl.pma.ui.views.teams;

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
import org.igorl.pma.backend.service.CountryServiceImpl;
import org.igorl.pma.backend.service.DepartmentServiceImpl;
import org.igorl.pma.backend.service.EmployeeServiceImpl;
import org.igorl.pma.ui.MainLayout;

@Route(value = "teams/employee", layout = MainLayout.class)
@PageTitle("Employees | PMA")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/true-false.css", themeFor = "vaadin-grid")
public class EmployeeListView extends VerticalLayout {

    public EmployeeServiceImpl employeeService;

    public Grid<Employee> grid = new Grid<>(Employee.class);
    public TextField filterText = new TextField();
    public EmployeeForm form;

    public EmployeeListView(EmployeeServiceImpl theEmployeeService,
                            CountryServiceImpl theCountryService,
                            DepartmentServiceImpl theDepartmentService) {

        employeeService = theEmployeeService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new EmployeeForm(theCountryService.findAll(), theDepartmentService.findAll());
        form.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
        form.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmployee);
        form.addListener(EmployeeForm.CloseEvent.class, e -> closeEditor());
        closeEditor();

        Div div = new Div(grid, form);
        div.addClassName("content");
        div.setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.add(getToolbar(), div);

        Icon icon = VaadinIcon.CLUSTER.create();
        String pageName = "Team Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink employeeList = new RouterLink("Employees", EmployeeListView.class);
        routerLinks.add(employeeList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

        updateList();
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.removeColumnByKey("department");
        grid.removeColumnByKey("firstName");
        grid.removeColumnByKey("lastName");
        grid.removeColumnByKey("title");
        grid.removeColumnByKey("mobilePhone");
        grid.removeColumnByKey("workEmail");
        grid.setColumns("employeeId");
        grid.addColumn(Employee::getFullName, "fullName").setHeader("Full name");
        grid.addColumn(Employee::getTitle, "title").setHeader("Title");
        grid.addColumn(employee -> {
            Department department = employee.getDepartment();
            return department == null ? "-" : department.getDepartmentName();
        }).setHeader("Department").setSortable(true);
        grid.addColumn(Employee::getMobilePhone, "mobilePhone").setHeader("Mobile phone");
        grid.addColumn(Employee::getWorkEmail, "workEmail").setHeader("Work email");
        grid.addColumn(employee -> employee.isClosed() ? "Closed" : "Active").setHeader("Status").setSortable(true);
        grid.setClassNameGenerator(employee -> employee.isClosed() ? "false" : "true");
        grid.getColumns().forEach(employeeColumn -> employeeColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editEmployee(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Employee...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addEmployeeButton = new Button("Add Employee");
        addEmployeeButton.addClickListener(click -> addEmployee());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addEmployeeButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveEmployee(EmployeeForm.SaveEvent event) {
        employeeService.save(event.getEmployee());
        updateList();
        closeEditor();
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        employeeService.delete(event.getEmployee());
        updateList();
        closeEditor();
    }

    void addEmployee() {
        grid.asSingleSelect().clear();
        editEmployee(new Employee());
    }

    public void editEmployee(Employee employee) {
        if (employee == null) {
            closeEditor();
        } else {
            form.setEmployee(employee);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setEmployee(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(employeeService.findAll(filterText.getValue()));
    }
}

