package org.igorl.pma.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.ui.views.adminpanel.AdminPanelView;
import org.igorl.pma.ui.views.dashboard.DashboardView;
import org.igorl.pma.ui.views.helpdesk.HelpPanelView;
import org.igorl.pma.ui.views.projects.ProjectPanelView;
import org.igorl.pma.ui.views.settings.CountryListView;
import org.igorl.pma.ui.views.settings.DepartmentListView;
import org.igorl.pma.ui.views.settings.SettingsPanelView;
import org.igorl.pma.ui.views.stock.StockPanelView;
import org.igorl.pma.ui.views.teams.TeamsPanelView;

@CssImport(value = "./styles/shared-styles.css", themeFor = "vaadin-app-layout")
public class MainLayout extends AppLayout {

    public MainLayout() {

        createDrawer();
    }

    private void createDrawer() {
        // DrawerToggle toggle = new DrawerToggle();
        Icon logo = VaadinIcon.VAADIN_H.create();
        logo.setSize("24px");
        logo.setColor("green");

        RouterLink dashboard = new RouterLink(null, DashboardView.class);
        dashboard.add(new Icon(VaadinIcon.GRID_BIG_O));

        RouterLink projects = new RouterLink(null, ProjectPanelView.class);
        projects.add(new Icon(VaadinIcon.COMPILE));

        RouterLink teams = new RouterLink(null, TeamsPanelView.class);
        teams.add(new Icon(VaadinIcon.CLUSTER));

        RouterLink stock = new RouterLink(null, StockPanelView.class);
        stock.add(new Icon(VaadinIcon.STOCK));

        RouterLink help = new RouterLink(null, HelpPanelView.class);
        help.add(new Icon(VaadinIcon.QUESTION_CIRCLE_O));

        RouterLink admin = new RouterLink(null, AdminPanelView.class);
        admin.add(new Icon(VaadinIcon.SHIELD));

        RouterLink settings = new RouterLink(null, SettingsPanelView.class);
        settings.add(new Icon(VaadinIcon.COG_O));

        dashboard.setHighlightCondition(HighlightConditions.sameLocation());

        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        MenuItem profile = menuBar.addItem(new Icon(VaadinIcon.USER));

        profile.getSubMenu().addItem(new RouterLink("View Profile", CountryListView.class));
        profile.getSubMenu().addItem(new RouterLink("Change Password", DepartmentListView.class));
        profile.getSubMenu().addItem(new Hr());
        profile.getSubMenu().addItem(new Anchor("logout", "Logout"));

        VerticalLayout layout = new VerticalLayout(logo, dashboard, projects, teams, stock, help, admin, settings, menuBar);

        layout.expand(stock);
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setHeight("100%");

        addToDrawer(layout);
    }

    public SplitLayout createSplitLayout(Icon pageIcon, String pageName, VerticalLayout routerLinks, Component content) {

        HorizontalLayout layout = new HorizontalLayout();
        pageIcon.setSize("18px");
        H3 primaryPanelName = new H3(pageName);
        layout.add(pageIcon, primaryPanelName);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        H5 view = new H5("View");
        Label greeting = new Label("Welcome back <Current User>!"); // TODO: bind with current session user

        SplitLayout linkLayout = new SplitLayout();

        linkLayout.addToPrimary(layout, new Hr(), greeting, new Hr(), view, routerLinks);
        linkLayout.addToSecondary(content);

        linkLayout.setPrimaryStyle("maxWidth", "20rem");
        linkLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);

        linkLayout.setSizeFull();

        return linkLayout;
    }
}
