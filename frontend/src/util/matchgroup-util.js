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

export const findUserMatchGroupFromAllUserTeams =
(allMatchGroups, allTeams, allUserTeams) => {
  for (let i = 0;  i < allUserTeams.length; i++) {
    const userMatchGroup = findUserMatchGroup(
      allMatchGroups,
      allTeams,
      allUserTeams[i].teamId);
    if (userMatchGroup) {
      return userMatchGroup;
    }
  }
  return undefined;
};
