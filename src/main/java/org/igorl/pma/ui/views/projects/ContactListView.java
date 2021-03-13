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
import org.igorl.pma.backend.entity.Contact;
import org.igorl.pma.backend.entity.Customer;
import org.igorl.pma.backend.entity.Supplier;
import org.igorl.pma.backend.service.ContactServiceImpl;
import org.igorl.pma.backend.service.CustomerServiceImpl;
import org.igorl.pma.backend.service.SupplierServiceImpl;
import org.igorl.pma.ui.MainLayout;

@Route(value = "projects/contacts", layout = MainLayout.class)
@PageTitle("Contacts | PMA")
@CssImport("./styles/shared-styles.css")
public class ContactListView extends VerticalLayout {

    public ContactServiceImpl contactService;

    public Grid<Contact> grid = new Grid<>(Contact.class);
    public TextField filterText = new TextField();
    public ContactForm form;

    public ContactListView(ContactServiceImpl contactService,
                           SupplierServiceImpl supplierService,
                           CustomerServiceImpl customerService) {

        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new ContactForm(customerService.findAll(), supplierService.findAll());
        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());
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
        RouterLink quotationList = new RouterLink("Quotations", QuotationListView.class);
        RouterLink customerList = new RouterLink("Customers", CustomerListView.class);
        RouterLink contactList = new RouterLink("Contacts", ContactListView.class);
        routerLinks.add(projectList, quotationList, customerList, contactList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon, pageName, routerLinks, content));

        updateList();
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.removeColumnByKey("firstName");
        grid.removeColumnByKey("lastName");
        grid.removeColumnByKey("title");
        grid.removeColumnByKey("contactPhone");
        grid.removeColumnByKey("contactEmail");
        grid.removeColumnByKey("closed");
        grid.setColumns("contactId");
        grid.addColumn(Contact::getFullName, "fullName").setHeader("Full name");
        grid.addColumn(Contact::getTitle, "title").setHeader("Title");
        grid.addColumn(Contact::getContactPhone, "contactPhone").setHeader("Contact phone");
        grid.addColumn(Contact::getContactEmail, "contactEmail").setHeader("Contact email");
        grid.addColumn(contact -> {
            Customer customer = contact.getCustomer();
            return customer == null ? "-" : customer.getCustomerName();
        }).setHeader("Customer");
        grid.addColumn(contact -> {
            Supplier supplier = contact.getSupplier();
            return supplier == null ? "-" : supplier.getSupplierName();
        }).setHeader("Supplier");
        grid.addColumn(Contact::isClosed, "closed").setHeader("Closed");

        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Contact...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveContact(ContactForm.SaveEvent event) {
        contactService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        contactService.deleteById(event.getContact().getContactId());
        updateList();
        closeEditor();
    }

    void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }
}

