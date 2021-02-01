package org.igorl.pma.ui.view.adminpanel;

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
import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.backend.service.EmployeeServiceImpl;
import org.igorl.pma.backend.service.UserAccountServiceImpl;
import org.igorl.pma.ui.MainLayout;

@Route(value = "adminpanel/users", layout = MainLayout.class)
@PageTitle("Users | PMA")
@CssImport("./styles/shared-styles.css")
public class UserListView extends VerticalLayout {

    private UserAccountServiceImpl userAccountService;
    private Grid<UserAccount> grid = new Grid<>(UserAccount.class);
    private TextField filterText = new TextField();
    private UserForm form;

    public UserListView(UserAccountServiceImpl theUserAccountService, EmployeeServiceImpl theEmployeeService) {

        this.userAccountService = theUserAccountService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new UserForm(theEmployeeService.findAll());
        form.addListener(UserForm.SaveEvent.class, this::saveUser);
        form.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());
        closeEditor();

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
    }

    private void saveUser(UserForm.SaveEvent event) {
        userAccountService.save(event.getUserAccount());
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userAccountService.delete(event.getUserAccount());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Username...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addUserButton = new Button("Add User");
        addUserButton.addClickListener(click -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.removeColumnByKey("employee");
        grid.setColumns("userId", "userName", "password", "role", "enabled");
        grid.addColumn(userAccount -> {
            Employee employee = userAccount.getEmployee();
            return employee == null ? "-" : employee.getFirstName() + " " + employee.getLastName();
        }).setHeader("Assigned to...");
        grid.getColumns().forEach(userAccountColumn -> userAccountColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
    }

    void addUser() {
        grid.asSingleSelect().clear();
        editUser(new UserAccount());
    }

    public void editUser(UserAccount userAccount) {
        if (userAccount == null) {
            closeEditor();
        } else {
            form.setUserAccount(userAccount);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUserAccount(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(userAccountService.findAll(filterText.getValue()));
    }
}