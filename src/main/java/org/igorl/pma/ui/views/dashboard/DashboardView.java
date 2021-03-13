package org.igorl.pma.ui.views.dashboard;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.igorl.pma.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | PMA")
@CssImport("./styles/shared-styles.css")
public class DashboardView extends VerticalLayout {

    public DashboardView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.GRID_BIG_O.create();
        String pageName = "Dashboard";
        VerticalLayout routerLinks = new VerticalLayout();
        routerLinks.add(); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

    }

}
