package org.igorl.pma.ui.views.adminpanel;

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
import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.backend.service.EmployeeServiceImpl;
import org.igorl.pma.backend.service.UserAccountServiceImpl;
import org.igorl.pma.ui.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Route(value = "admin/users", layout = MainLayout.class)
@PageTitle("Users | PMA")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/true-false.css", themeFor = "vaadin-grid")
public class UserListView extends VerticalLayout {

    public UserForm form;
    public Grid<UserAccount> grid = new Grid<>(UserAccount.class);
    public TextField filterText = new TextField();

    public UserAccountServiceImpl userAccountService;

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

        Div div = new Div(grid, form);
        div.addClassName("content");
        div.setSizeFull();

        VerticalLayout content = new VerticalLayout(getToolbar(), div);

        Icon icon = VaadinIcon.SHIELD.create();
        String pageName = "Admin Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink userList = new RouterLink("Users", UserListView.class);
        routerLinks.add(userList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

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
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.removeColumnByKey("employee");
        grid.setColumns("userId", "userName", "password", "role");
        grid.addColumn(userAccount -> {
            Employee employee = userAccount.getEmployee();
            return employee == null ? "-" : employee.getFullName();
        }).setHeader("Assigned to...").setSortable(true);
        grid.addColumn(user -> user.isEnabled() ? "Enabled" : "Disabled").setHeader("Status").setSortable(true);
        grid.setClassNameGenerator(user -> user.isEnabled() ? "true" : "false");
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