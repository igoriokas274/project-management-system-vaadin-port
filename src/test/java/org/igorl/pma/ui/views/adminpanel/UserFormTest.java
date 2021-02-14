package org.igorl.pma.ui.views.adminpanel;

import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.backend.enums.UserRoles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserFormTest {

    private List<Employee> employees;
    private UserAccount user1;

    @Before
    public void setupData() {
        employees = new ArrayList<>();

        user1 = new UserAccount();
        user1.setUserName("john");
        user1.setPassword("john1234");
        user1.setRole(UserRoles.ADMIN);
        user1.setEnabled(true);
    }

    @Test
    public void formFieldsPopulated() {
        UserForm userForm = new UserForm(employees);
        userForm.setUserAccount(user1);
        Assert.assertEquals("john", userForm.userName.getValue());
        Assert.assertEquals("john1234", userForm.password.getValue());
        Assert.assertEquals(UserRoles.ADMIN, userForm.role.getValue());
        Assert.assertEquals(true, userForm.enabled.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        UserForm userForm = new UserForm(employees);
        UserAccount userAccount = new UserAccount();
        userForm.setUserAccount(userAccount);

        userForm.userName.setValue("john");
        userForm.password.setValue("john1234");
        userForm.role.setValue(UserRoles.ADMIN);
        userForm.enabled.setEnabled(true);

        AtomicReference<UserAccount> savedUserRef = new AtomicReference<>(null);
        userForm.addListener(UserForm.SaveEvent.class, e -> {
            savedUserRef.set(e.getUserAccount());
        });
        userForm.save.click();
        UserAccount savedUser = savedUserRef.get();

        Assert.assertEquals("john", savedUser.getUserName());
        Assert.assertEquals("john1234", savedUser.getPassword());
        Assert.assertEquals(UserRoles.ADMIN, savedUser.getRole());
        Assert.assertTrue(savedUser.isEnabled());
    }
}
