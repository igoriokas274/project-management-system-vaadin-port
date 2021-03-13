package org.igorl.pma.ui.views.teams;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.ui.MainLayout;

@Route(value = "teams", layout = MainLayout.class)
@PageTitle("Team Panel | PMA")
@CssImport("./styles/shared-styles.css")
public class TeamsPanelView extends VerticalLayout {

    public TeamsPanelView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.CLUSTER.create();
        String pageName = "Team Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink employeeList = new RouterLink("Employees", EmployeeListView.class);
        routerLinks.add(employeeList); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));
    }

}
