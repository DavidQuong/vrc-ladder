package ca.sfu.cmpt373.alpha.vrcladder.users.authorization;

public enum UserAction {
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
}
