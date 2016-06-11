-- For TeamManagerTest
INSERT INTO USER (ID, USER_ROLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL_ADDRESS, PHONE_NUMBER) VALUES ('59152', 'PLAYER', 'David', '', 'Lee', 'davidlee@vrc.ca', '6045554444');
INSERT INTO USER (ID, USER_ROLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL_ADDRESS, PHONE_NUMBER) VALUES ('59153', 'PLAYER', 'John', '', 'Kim', 'jkim@vrc.ca', '6044445555');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('8250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('35abe4c1-1e5b-4e6a-ab5c-9fffa5c89bf8', '59152', '59153', '8250c624-a8de-4078-9b6c-701666541ed3');

INSERT INTO USER (ID, USER_ROLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL_ADDRESS, PHONE_NUMBER) VALUES ('59154', 'PLAYER', 'Max', '', 'Schwartz', 'maxschawrtz@vrc.ca', '6047781234');
INSERT INTO USER (ID, USER_ROLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL_ADDRESS, PHONE_NUMBER) VALUES ('59155', 'PLAYER', 'Bruce', '', 'Lai', 'blai@vrc.ca', '6040659999');

-- For MatchGroupManagerTest
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('1250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('2250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('3250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('4250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('5250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('6250c624-a8de-4078-9b6c-701666541ed3', 'NONE');
INSERT INTO ATTENDANCE_CARD (ID, PLAY_TIME) VALUES ('7250c624-a8de-4078-9b6c-701666541ed3', 'NONE');

INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('1', '59152', '59153', '1250c624-a8de-4078-9b6c-701666541ed3');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('2', '59152', '59153', '2250c624-a8de-4078-9b6c-701666541ed3');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('3', '59152', '59153', '3250c624-a8de-4078-9b6c-701666541ed3');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('4', '59152', '59153', '4250c624-a8de-4078-9b6c-701666541ed3');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('5', '59152', '59153', '5250c624-a8de-4078-9b6c-701666541ed3');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('6', '59152', '59153', '6250c624-a8de-4078-9b6c-701666541ed3');
INSERT INTO TEAM (ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, ATTENDANCE_CARD_ID) VALUES ('7', '59152', '59153', '7250c624-a8de-4078-9b6c-701666541ed3');

Insert INTO MATCH_GROUP(ID) VALUES ('1')
INSERT INTO MATCH_GROUP_TEAM(MatchGroup_ID, teams_ID) VALUES ('1', '1')
INSERT INTO MATCH_GROUP_TEAM(MatchGroup_ID, teams_ID) VALUES ('1', '2')
INSERT INTO MATCH_GROUP_TEAM(MatchGroup_ID, teams_ID) VALUES ('1', '3')

-- INSERT INTO MATCH_GROUP_TEAM(MA)