package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

// TODO - Potentially add more fields
public class Team {

    private IdType id;
    private User firstPlayer;
    private User secondPlayer;

    public Team(IdType id, User firstPlayer, User secondPlayer) {
        verifyPlayers(firstPlayer, secondPlayer);
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public IdType getId() {
        return id;
    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    // TODO - Replace RuntimeException with more specific (possibly custom) type of error.
    private void verifyPlayers(User firstPlayer, User secondPlayer) throws RuntimeException {
        if (firstPlayer.getUserRole() != UserRole.PLAYER || secondPlayer.getUserRole() != UserRole.PLAYER) {
            throw new RuntimeException();
        }
    }

    @Override
    public String toString(){
        return  "[" + id + ",\n"
                + firstPlayer.toString() + ",\n"
                + secondPlayer.toString() + "]\n";

    }

}
