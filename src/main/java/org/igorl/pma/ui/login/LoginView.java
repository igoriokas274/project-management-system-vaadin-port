package org.igorl.pma.ui.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | PMA")
public class LoginView extends FlexLayout implements AfterNavigationObserver {

    private final LoginOverlay login;

    public LoginView() {
        login = new LoginOverlay();
        login.setTitle("PMA");
        login.setDescription("Project Management App");
        login.setForgotPasswordButtonVisible(false);
        login.setAction("login");

        add(login);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        login.setOpened(true);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        login.setError(
                event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
