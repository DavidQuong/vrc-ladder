package ca.sfu.cmpt373.alpha.vrcladder.users.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;

import java.util.UUID;

/**
 * Created by Amaterasu on 2016-05-27.
 */
public class Team {

    User player1;
    User player2;
    UUID teamID;
    public Team(UUID teamID){
        this.teamID = teamID;
    }

    public UUID getTeamID(){return teamID;}
}
