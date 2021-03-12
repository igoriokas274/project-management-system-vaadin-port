package org.igorl.pma.ui.views.stock;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.igorl.pma.ui.MainLayout;

@Route(value = "stock", layout = MainLayout.class)
@PageTitle("Stock Panel | PMA")
@CssImport("./styles/shared-styles.css")
public class StockPanelView extends VerticalLayout {

    public StockPanelView() {

        addClassName("list-view");
        setSizeFull();

        Icon icon = VaadinIcon.STOCK.create();
        String pageName = "Stock Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink itemList = new RouterLink("Items", ItemListView.class);
        RouterLink supplierList = new RouterLink("Suppliers", SupplierListView.class);
        routerLinks.add(itemList, supplierList); // Here you can add RouterLinks
        Label content = new Label("This section is under construction");

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

    }

}
