package ca.sfu.cmpt373.alpha.vrcrest.payloads;

import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcrest.interfaces.Validateable;

@Deprecated
public class NewTeamPayload implements Validateable {
    private UserId userId1;
    private UserId userId2;

    public UserId getUserId1() {
        return userId1;
    }

    public UserId getUserId2() {
        return userId2;
    }

    public boolean isValid() {
        return userId1 != null && userId2 != null;
    }
}
