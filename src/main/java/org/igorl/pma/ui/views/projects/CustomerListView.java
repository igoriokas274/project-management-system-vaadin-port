package org.igorl.pma.ui.views.projects;

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
import org.igorl.pma.backend.entity.Customer;
import org.igorl.pma.backend.service.CountryServiceImpl;
import org.igorl.pma.backend.service.CurrencyServiceImpl;
import org.igorl.pma.backend.service.CustomerServiceImpl;
import org.igorl.pma.ui.MainLayout;

@Route(value = "projects/customer", layout = MainLayout.class)
@PageTitle("Customers | PMA")
@CssImport("./styles/shared-styles.css")
public class CustomerListView extends VerticalLayout {

    public CustomerServiceImpl customerService;

    public Grid<Customer> grid = new Grid<>(Customer.class);
    public TextField filterText = new TextField();
    public CustomerForm form;

    public CustomerListView(CustomerServiceImpl customerService , CountryServiceImpl countryService,
                            CurrencyServiceImpl currencyService) {

        this.customerService = customerService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new CustomerForm(countryService.findAll(), currencyService.findAll());
        form.addListener(CustomerForm.SaveEvent.class, this::saveCustomer);
        form.addListener(CustomerForm.DeleteEvent.class, this::deleteCustomer);
        form.addListener(CustomerForm.CloseEvent.class, e -> closeEditor());
        closeEditor();

        Div div = new Div(grid, form);
        div.addClassName("content");
        div.setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.add(getToolbar(), div);

        Icon icon = VaadinIcon.COMPILE.create();
        String pageName = "Project Panel";
        VerticalLayout routerLinks = new VerticalLayout();
        RouterLink projectList = new RouterLink("Projects", ProjectListView.class);
        RouterLink customerList = new RouterLink("Customers", CustomerListView.class);
        RouterLink contactList = new RouterLink("Contacts", ContactListView.class);
        routerLinks.add(projectList, customerList, contactList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon, pageName, routerLinks, content));

        updateList();
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("customerId", "customerName", "closed");
        grid.getColumns().forEach(supplierColumn -> supplierColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editCustomer(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Customer...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCustomerButton = new Button("Add customer");
        addCustomerButton.addClickListener(click -> addCustomer());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveCustomer(CustomerForm.SaveEvent event) {
        customerService.save(event.getCustomer());
        updateList();
        closeEditor();
    }

    private void deleteCustomer(CustomerForm.DeleteEvent event) {
        customerService.deleteById(event.getCustomer().getCustomerId());
        updateList();
        closeEditor();
    }

    void addCustomer() {
        grid.asSingleSelect().clear();
        editCustomer(new Customer());
    }

    public void editCustomer(Customer customer) {
        if (customer == null) {
            closeEditor();
        } else {
            form.setCustomer(customer);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCustomer(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(customerService.findAll(filterText.getValue()));
    }
}

