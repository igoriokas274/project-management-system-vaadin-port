package org.igorl.pma.ui.views.settings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.ui.MainLayout;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings Panel | PMA")
@CssImport("./styles/shared-styles.css")
public class SettingsPanelView extends VerticalLayout {

    public SettingsPanelView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.COG_O.create();
        String pageName = "Settings Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink countryList = new RouterLink("Countries", CountryListView.class);
        RouterLink currencyList = new RouterLink("Currencies", CurrencyListView.class);
        RouterLink departmentList = new RouterLink("Departments", DepartmentListView.class);
        RouterLink payTermList = new RouterLink("Pay Terms", PayTermListView.class);
        RouterLink projectStatusList = new RouterLink("Project status", ProjectStatusListView.class);
        RouterLink projectTypeList = new RouterLink("Project type", ProjectTypeListView.class);
        RouterLink quotationStatusList = new RouterLink("Quotation status", QuotationStatusListView.class);
        routerLinks.add(countryList, currencyList, departmentList, payTermList, projectStatusList, projectTypeList,
                quotationStatusList); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));
    }

}
