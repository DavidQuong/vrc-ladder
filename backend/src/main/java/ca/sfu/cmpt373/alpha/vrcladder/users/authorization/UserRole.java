package ca.sfu.cmpt373.alpha.vrcladder.users.authorization;

import java.util.EnumSet;

import static ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserAction.*;

public enum UserRole {
    PLAYER,
    MODERATOR,
    ADMINISTRATOR;

    private static final EnumSet<UserAction> PLAYER_ACTIONS = EnumSet.of(
        MODIFY_TEAM_PARTICIPATION,
        CREATE_TEAM,
        DELETE_TEAM,
        REPORT_GAME_SCORES
    );

    private static EnumSet<UserAction> MODERATOR_ACTIONS = EnumSet.of(
        GET_USER_INFORMATION,
        UPDATE_USER_INFORMATION,
        DELETE_USER,

        MODIFY_TEAM_PARTICIPATION,
        CREATE_TEAM,
        DELETE_TEAM,
        REPORT_GAME_SCORES,
        MODIFY_GAME_SCORES,
        MODIFY_RANKINGS,
        GENERATE_TOURNAMENT_SCHEDULE,
        GENERATE_NEW_RANKINGS
    );

    private static EnumSet<UserAction> ADMINISTRATOR_ACTIONS = EnumSet.of(
        GET_USER_INFORMATION,
        UPDATE_USER_INFORMATION,
        DELETE_USER,

        MODIFY_TEAM_PARTICIPATION,
        CREATE_TEAM,
        DELETE_TEAM,
        REPORT_GAME_SCORES,
        MODIFY_GAME_SCORES,
        MODIFY_RANKINGS,
        GENERATE_TOURNAMENT_SCHEDULE,
        GENERATE_NEW_RANKINGS,
        CREATE_MODERATOR,
        DELETE_MODERATOR
    );

    private EnumSet<UserAction> permittedActions;

    static {
        PLAYER.permittedActions = PLAYER_ACTIONS;
        MODERATOR.permittedActions = MODERATOR_ACTIONS;
        ADMINISTRATOR.permittedActions = ADMINISTRATOR_ACTIONS;
    }

    public boolean hasAuthorizationToPerform(UserAction action) {
        return permittedActions.contains(action);
    }

}
