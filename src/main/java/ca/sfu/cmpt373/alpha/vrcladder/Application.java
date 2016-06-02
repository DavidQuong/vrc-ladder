package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserFactory;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;

public class Application {

    private SessionManager sessionManager;
    private UserManager userManager;

    public Application() {
        sessionManager = new SessionManager();
        userManager = new UserManager(sessionManager);
    }

    public void shutDown() {
        sessionManager.shutDown();
    }

}
