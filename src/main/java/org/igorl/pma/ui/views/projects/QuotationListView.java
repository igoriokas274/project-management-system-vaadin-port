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
import org.igorl.pma.backend.entity.Project;
import org.igorl.pma.backend.entity.Quotation;
import org.igorl.pma.backend.entity.QuotationStatus;
import org.igorl.pma.backend.service.ProjectServiceImpl;
import org.igorl.pma.backend.service.QuotationServiceImpl;
import org.igorl.pma.backend.service.QuotationStatusImpl;
import org.igorl.pma.ui.MainLayout;

@Route(value = "projects/quotations", layout = MainLayout.class)
@PageTitle("Quotations | PMA")
@CssImport("./styles/shared-styles.css")
public class QuotationListView extends VerticalLayout {

    public QuotationServiceImpl quotationService;

    public Grid<Quotation> grid = new Grid<>(Quotation.class);
    public TextField filterText = new TextField();
    public QuotationForm form;

    public QuotationListView(QuotationServiceImpl quotationService,
                             QuotationStatusImpl quotationStatus,
                             ProjectServiceImpl projectService) {

        this.quotationService = quotationService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new QuotationForm(projectService.findAll(), quotationStatus.findAll());
        form.addListener(QuotationForm.SaveEvent.class, this::saveQuotation);
        form.addListener(QuotationForm.DeleteEvent.class, this::deleteQuotation);
        form.addListener(QuotationForm.CloseEvent.class, e -> closeEditor());
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

        grid.removeClassName("project");
        grid.removeColumnByKey("quotationStatus");
        grid.setColumns("quotationId", "quotationDate", "quotationTitle");
        grid.addColumn(component -> {
            Project project = component.getProject();
            return project == null ? "-" : project.getProjectName();
        }).setHeader("Project").setSortable(true);
        grid.addColumn(status -> {
            QuotationStatus quotationStatus = status.getQuotationStatus();
            return quotationStatus == null ? "-" : quotationStatus.getQuotationStatusName();
        }).setHeader("Stage").setSortable(true);
        grid.addColumn(quotation -> quotation.isConfirmed() ? "Confirmed" : "Unconfirmed").setHeader("Status").setSortable(true);

        grid.getColumns().forEach(quotationColumn -> quotationColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editQuotation(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by title...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addQuotationButton = new Button("Add quotation");
        addQuotationButton.addClickListener(click -> addQuotation());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addQuotationButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveQuotation(QuotationForm.SaveEvent event) {
        quotationService.save(event.getQuotation());
        updateList();
        closeEditor();
    }

    private void deleteQuotation(QuotationForm.DeleteEvent event) {
        quotationService.deleteById(event.getQuotation().getQuotationId());
        updateList();
        closeEditor();
    }

    void addQuotation() {
        grid.asSingleSelect().clear();
        editQuotation(new Quotation());
    }

    public void editQuotation(Quotation quotation) {
        if (quotation == null) {
            closeEditor();
        } else {
            form.setQuotation(quotation);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setQuotation(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(quotationService.findAll(filterText.getValue()));
    }
}

