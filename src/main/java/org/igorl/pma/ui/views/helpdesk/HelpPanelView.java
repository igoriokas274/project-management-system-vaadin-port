package org.igorl.pma.ui.views.helpdesk;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.igorl.pma.ui.MainLayout;

@Route(value = "help", layout = MainLayout.class)
@PageTitle("Help Desk | PMA")
@CssImport("./styles/shared-styles.css")
public class HelpPanelView extends VerticalLayout {

    public HelpPanelView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.QUESTION_CIRCLE_O.create();
        String pageName = "Help Desk";
        VerticalLayout routerLinks = new VerticalLayout();
        routerLinks.add(); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));
    }

}
