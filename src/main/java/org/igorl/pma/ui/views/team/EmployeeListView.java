package org.igorl.pma.ui.views.team;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.service.EmployeeServiceImpl;
import org.igorl.pma.ui.MainLayout;
import org.springframework.stereotype.Component;

@Component
@Route(value = "team/employee", layout = MainLayout.class)
@PageTitle("Employees | PMA")
@CssImport("./styles/shared-styles.css")
public class EmployeeListView extends VerticalLayout {

    public EmployeeForm form;
    public Grid<Employee> grid = new Grid<>( Employee.class );
    public TextField filterText = new TextField( );

    public EmployeeServiceImpl employeeService;

    public EmployeeListView(EmployeeServiceImpl theEmployeeService){
        this.employeeService = theEmployeeService;
        addClassName( "list-view" );
        setSizeFull();


        form = new EmployeeForm (theEmployeeService.findAll( ));
        form.addListener( EmployeeForm.SaveEvent.class, this::saveEmployee );
        form.addListener( EmployeeForm.DeleteEvent.class, this::deleteEmployee );
        form.addListener( EmployeeForm.CloseEvent.class, e -> closeEditor() );
        closeEditor();

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
    }

    private void saveEmployee(EmployeeForm.SaveEvent event) {
        employeeService.save(event.getEmployee() );
        updateList();
        closeEditor();
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        employeeService.deleteById(event.getEmployee( ).getEmployeeId( ) );
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Employee...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode( ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addEmployeeButton = new Button("Add Employee");
        addEmployeeButton.addClickListener(click -> addEmployee());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addEmployeeButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("employee-grid");
        grid.setSizeFull();
        grid.addThemeVariants( GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("employeeId", "firstName", "lastName","title", "dateOfEmployment", "closed");
        grid.getColumns().forEach(employeeColumn -> employeeColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editEmployee(event.getValue()));
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
        grid.setItems(employeeService.findAll( ));
    }
}

