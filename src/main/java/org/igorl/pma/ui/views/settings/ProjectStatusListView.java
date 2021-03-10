package org.igorl.pma.ui.views.settings;

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
import org.igorl.pma.backend.entity.ProjectStatus;
import org.igorl.pma.backend.service.ProjectStatusImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/projectstatus", layout = MainLayout.class)
@PageTitle("Project Status | PMA")
@CssImport("./styles/shared-styles.css")
public class ProjectStatusListView extends VerticalLayout {

    private ProjectStatusImpl projectStatus;
    private Grid<ProjectStatus> grid = new Grid<>(ProjectStatus.class);
    private TextField filterText = new TextField();
    private ProjectStatusForm form;

    public ProjectStatusListView(ProjectStatusImpl projectStatus) {
        this.projectStatus = projectStatus;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new ProjectStatusForm();
        form.addListener(ProjectStatusForm.SaveEvent.class, this::saveProjectStatus);
        form.addListener(ProjectStatusForm.DeleteEvent.class, this::deleteProjectStatus);
        form.addListener(ProjectStatusForm.CloseEvent.class, e -> closeEditor());

        closeEditor();

        Div div = new Div(grid, form);
        div.addClassName("content");
        div.setSizeFull();

        VerticalLayout content = new VerticalLayout(getToolbar(), div);

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

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

        updateList();
    }

    private void saveProjectStatus(ProjectStatusForm.SaveEvent event) {
        projectStatus.save(event.getProjectStatus());
        updateList();
        closeEditor();
    }

    private void deleteProjectStatus(ProjectStatusForm.DeleteEvent event) {
        projectStatus.deleteById(event.getProjectStatus().getProjectStatusId());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Project Status...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addProjectStatusButton = new Button("Add project status");
        addProjectStatusButton.addClickListener(click -> addProjectStatus());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProjectStatusButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("projectStatusId", "projectStatusName");
        grid.getColumns().forEach(projectStatusColumn -> projectStatusColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editProjectStatus(event.getValue()));
    }

    void addProjectStatus() {
        grid.asSingleSelect().clear();
        editProjectStatus(new ProjectStatus());
    }

    private void editProjectStatus(ProjectStatus projectStatus) {
        if (projectStatus == null) {
            closeEditor();
        } else {
            form.setProjectStatus(projectStatus);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProjectStatus(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(projectStatus.findAll(filterText.getValue()));
    }
}

