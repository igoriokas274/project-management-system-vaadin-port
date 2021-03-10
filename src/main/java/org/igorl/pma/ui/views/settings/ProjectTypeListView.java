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
import org.igorl.pma.backend.entity.ProjectType;
import org.igorl.pma.backend.service.ProjectTypeImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/projecttype", layout = MainLayout.class)
@PageTitle("Project Type | PMA")
@CssImport("./styles/shared-styles.css")
public class ProjectTypeListView extends VerticalLayout {

    private ProjectTypeImpl projectType;
    private Grid<ProjectType> grid = new Grid<>(ProjectType.class);
    private TextField filterText = new TextField();
    private ProjectTypeForm form;

    public ProjectTypeListView(ProjectTypeImpl projectType) {
        this.projectType = projectType;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new ProjectTypeForm();
        form.addListener(ProjectTypeForm.SaveEvent.class, this::saveProjectType);
        form.addListener(ProjectTypeForm.DeleteEvent.class, this::deleteProjectType);
        form.addListener(ProjectTypeForm.CloseEvent.class, e -> closeEditor());

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

    private void saveProjectType(ProjectTypeForm.SaveEvent event) {
        projectType.save(event.getProjectType());
        updateList();
        closeEditor();
    }

    private void deleteProjectType(ProjectTypeForm.DeleteEvent event) {
        projectType.deleteById(event.getProjectType().getProjectTypeId());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Project Type...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addProjectTypeButton = new Button("Add project type");
        addProjectTypeButton.addClickListener(click -> addProjectType());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProjectTypeButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("projectTypeId", "projectTypeName");
        grid.getColumns().forEach(projectTypeColumn -> projectTypeColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editProjectType(event.getValue()));
    }

    void addProjectType() {
        grid.asSingleSelect().clear();
        editProjectType(new ProjectType());
    }

    private void editProjectType(ProjectType projectType) {
        if (projectType == null) {
            closeEditor();
        } else {
            form.setProjectType(projectType);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProjectType(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(projectType.findAll(filterText.getValue()));
    }
}

