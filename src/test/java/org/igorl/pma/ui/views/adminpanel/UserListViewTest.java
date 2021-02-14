package org.igorl.pma.ui.views.adminpanel;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.ui.views.adminpanel.UserForm;
import org.igorl.pma.ui.views.adminpanel.UserListView;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserListViewTest {

    @Autowired
    private UserListView userListView;

    @Test
    public void formShownWhenUserSelected() {
        Grid<UserAccount> grid = userListView.grid;
        UserAccount firstAccount = getFirstItem(grid);

        UserForm form = userListView.form;

        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstAccount);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstAccount.getUserName(), form.userName.getValue());
    }

    private UserAccount getFirstItem(Grid<UserAccount> grid) {
        return ((ListDataProvider<UserAccount>) grid.getDataProvider()).getItems().iterator().next();
    }
}
