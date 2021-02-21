package org.igorl.pma.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.ui.views.dashboard.DashboardView;
import org.igorl.pma.ui.views.settings.CountryListView;
import org.igorl.pma.ui.views.settings.DepartmentListView;
import org.igorl.pma.ui.views.team.EmployeeListView;
import org.igorl.pma.ui.views.adminpanel.UserListView;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {

        createDrawer();
    }

    private void createDrawer() {
        // DrawerToggle toggle = new DrawerToggle();
        Icon logo = VaadinIcon.VAADIN_H.create();
        logo.setSize("36px");
        logo.setColor("green");

        RouterLink dashboard = new RouterLink("Dashboard", DashboardView.class);
        RouterLink userList = new RouterLink("User List", UserListView.class);
        RouterLink employee = new RouterLink("Employees", EmployeeListView.class);
        RouterLink department = new RouterLink("Departments", DepartmentListView.class);
        RouterLink country = new RouterLink("Countries", CountryListView.class);
        dashboard.setHighlightCondition(HighlightConditions.sameLocation());
        Anchor logout = new Anchor("logout", "Log Out");

        VerticalLayout layout = new VerticalLayout(logo, dashboard, employee, department, country, userList, logout);
        layout.expand(country);
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);
        layout.setHeight("100%");

        addToDrawer(layout);
    }
}
