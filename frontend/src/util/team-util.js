export const findTeamById = (teamId, allTeams) => (
  allTeams.find((team) => team.teamId === teamId)
);

export const findAttendingUserTeam = (userTeams) => (
  userTeams.find((team) => team.playTime !== 'NONE')
);
