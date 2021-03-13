package org.igorl.pma.ui.views.adminpanel;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.ui.MainLayout;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin Panel | PMA")
@CssImport("./styles/shared-styles.css")
public class AdminPanelView extends VerticalLayout {

    public AdminPanelView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.SHIELD.create();
        String pageName = "Admin Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink userList = new RouterLink("Users", UserListView.class);
        routerLinks.add(userList); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));
    }

}
