package org.igorl.pma.ui.views.projects;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.igorl.pma.ui.MainLayout;

@Route(value = "projects", layout = MainLayout.class)
@PageTitle("Project Panel | PMA")
@CssImport("./styles/shared-styles.css")
public class ProjectPanelView extends VerticalLayout {

    public ProjectPanelView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.COMPILE.create();
        String pageName = "Project Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        routerLinks.add(); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));
    }

}
