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
import org.igorl.pma.backend.entity.PayTerm;
import org.igorl.pma.backend.service.PayTermServiceImpl;
import org.igorl.pma.ui.MainLayout;


@Route(value = "settings/payterms", layout = MainLayout.class)
@PageTitle("Payment Terms | PMA")
@CssImport("./styles/shared-styles.css")
public class PayTermListView extends VerticalLayout {

    private PayTermServiceImpl payTermService;
    private Grid<PayTerm> grid = new Grid<>(PayTerm.class);
    private TextField filterText = new TextField();
    private PayTermForm form;

    public PayTermListView(PayTermServiceImpl thePayTermService) {
        this.payTermService = thePayTermService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new PayTermForm();
        form.addListener(PayTermForm.SaveEvent.class, this::savePayForm);
        form.addListener(PayTermForm.DeleteEvent.class, this::deletePayForm);
        form.addListener(PayTermForm.CloseEvent.class, e -> closeEditor());

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
        RouterLink payTermList = new RouterLink("Payment Terms", PayTermListView.class);
        RouterLink projectStatusList = new RouterLink("Project Statuses", ProjectStatusListView.class);
        RouterLink projectTypeList = new RouterLink("Project Types", ProjectTypeListView.class);
        RouterLink quotationStatusList = new RouterLink("Quotation Statuses", QuotationStatusListView.class);
        RouterLink stockTypeList = new RouterLink("Stock Types", StockTypeListView.class);
        RouterLink vatValueList = new RouterLink("VAT values", VatValueListView.class);
        routerLinks.add(countryList, currencyList, departmentList, payTermList, projectStatusList, projectTypeList,
                quotationStatusList, stockTypeList, vatValueList); // Here you can add RouterLinks

        add(new MainLayout().createSplitLayout(icon,pageName, routerLinks, content));

        updateList();
    }

    private void savePayForm(PayTermForm.SaveEvent event) {
        payTermService.save(event.getPayTerm());
        updateList();
        closeEditor();
    }

    private void deletePayForm(PayTermForm.DeleteEvent event) {
        payTermService.deleteById(event.getPayTerm().getTermId());
        updateList();
        closeEditor();
    }

    public HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by Term...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addPayTermButton = new Button("Add payment term");
        addPayTermButton.addClickListener(click -> addPayTerm());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addPayTermButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("termId", "term", "description");
        grid.getColumns().forEach(payTermColumn -> payTermColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editPayTerm(event.getValue()));
    }

    void addPayTerm() {
        grid.asSingleSelect().clear();
        editPayTerm(new PayTerm());
    }

    private void editPayTerm(PayTerm payTerm) {
        if (payTerm == null) {
            closeEditor();
        } else {
            form.setPayTerm(payTerm);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPayTerm(null);
        form.setVisible(false); // Change to false if edit panel closing needed
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(payTermService.findAll(filterText.getValue()));
    }
}

