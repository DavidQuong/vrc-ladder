package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;

public class Application {

    private DatabaseManager dbManager;

    public Application() {
        dbManager = new DatabaseManager();
    }

    public void shutDown() {
        dbManager.shutDown();
    }

}
