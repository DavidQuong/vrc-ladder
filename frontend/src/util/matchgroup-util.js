import map from 'lodash/fp/map';

export const getMatchGroupTeams = (matchGroup, allTeams) => {
  const teamIds = matchGroup.teamId4 ? [
    matchGroup.teamId1,
    matchGroup.teamId2,
    matchGroup.teamId3,
    matchGroup.teamId4] : [
      matchGroup.teamId1,
      matchGroup.teamId2,
      matchGroup.teamId3];
  return map(
    (teamId) => allTeams.find((team) => (team.teamId === teamId)),
    teamIds);
};

export const findUserMatchGroup = (matchGroups, allTeams, teamId) => {
  const userMatchGroup = matchGroups.find((matchGroup) => {
    const matchGroupTeams = getMatchGroupTeams(matchGroup, allTeams);
    const userTeam = matchGroupTeams.find((team) => {
      return team.teamId === teamId;
    });
    return userTeam ? true : false;
  });
  return userMatchGroup;
};

// TODO replace this with a check to team timeslot
export const findAttendingUserTeam = (matchGroups, allTeams, userTeams) => {
  let userTeam;
  for (let i = 0; i < userTeams.length; i++) {
    userTeam = userTeams[i];
    const userMatchGroup = findUserMatchGroup(
      matchGroups,
      allTeams,
      userTeam.teamId);
    if (userMatchGroup) {
      return userTeam;
    }
  }
  return null;
};
