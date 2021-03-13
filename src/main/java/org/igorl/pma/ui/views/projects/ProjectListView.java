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
import org.igorl.pma.backend.entity.*;
import org.igorl.pma.backend.service.*;
import org.igorl.pma.ui.MainLayout;

@Route(value = "projects/projects", layout = MainLayout.class)
@PageTitle("Projects | PMA")
@CssImport("./styles/shared-styles.css")
public class ProjectListView extends VerticalLayout {

    public ProjectServiceImpl projectService;

    public Grid<Project> grid = new Grid<>(Project.class);
    public TextField filterText = new TextField();
    public ProjectForm form;

    public ProjectListView(ProjectServiceImpl projectService,
                           ProjectStatusImpl projectStatus,
                           ProjectTypeImpl projectType,
                           CustomerServiceImpl customerService,
                           EmployeeServiceImpl employeeService,
                           PayTermServiceImpl payTermService) {

        this.projectService = projectService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        form = new ProjectForm(projectStatus.findAll(), projectType.findAll(),
                customerService.findAll(), employeeService.findAll(), payTermService.findAll());
        form.addListener(ProjectForm.SaveEvent.class, this::saveProject);
        form.addListener(ProjectForm.DeleteEvent.class, this::deleteProject);
        form.addListener(ProjectForm.CloseEvent.class, e -> closeEditor());
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
        grid.removeColumnByKey("manager");
        grid.removeColumnByKey("projectStartDate");
        grid.removeColumnByKey("projectEndDate");
        grid.removeColumnByKey("projectStatus");
        grid.removeColumnByKey("closed");
        grid.setColumns("projectId", "projectName");
        grid.addColumn(manager -> {
            Employee employee = manager.getManager();
            return employee == null ? "-" : employee.getFullName();
        }).setHeader("Manager");
        grid.addColumn(Project::getProjectStartDate, "projectStartDate").setHeader("Start date");
        grid.addColumn(Project::getProjectEndDate, "projectEndDate").setHeader("End date");
        grid.addColumn(status -> {
            ProjectStatus projectStatus = status.getProjectStatus();
            return projectStatus == null ? "-" : projectStatus.getProjectStatusName();
        }).setHeader("Status");
        grid.addColumn(Project::isClosed, "closed").setHeader("Closed");

        grid.getColumns().forEach(projectColumn -> projectColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Project name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addProjectButton = new Button("Add project");
        addProjectButton.addClickListener(click -> addProject());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProjectButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void saveProject(ProjectForm.SaveEvent event) {
        projectService.save(event.getProject());
        updateList();
        closeEditor();
    }

    private void deleteProject(ProjectForm.DeleteEvent event) {
        projectService.deleteById(event.getProject().getProjectId());
        updateList();
        closeEditor();
    }

    void addProject() {
        grid.asSingleSelect().clear();
        editContact(new Project());
    }

    public void editContact(Project project) {
        if (project == null) {
            closeEditor();
        } else {
            form.setProject(project);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProject(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(projectService.findAll(filterText.getValue()));
    }
}

