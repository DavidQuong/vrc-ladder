package ca.sfu.cmpt373.alpha.vrctextui;

import ca.sfu.cmpt373.alpha.vrcladder.Application;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchScheduler;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TextUserInterfaceDriver {
    private static final int NULL_OPTION = -1;
    private static List<User> users;
    private static List<Team> teams;

    public static void main(String[] args) {
        final int ADD_PLAYER_OPTION = 1;
        final int SHOW_PLAYERS_OPTION = 2;
        final int MAKE_TEAM_OPTION = 3;
        final int SET_ATTENDANCE_OPTION = 4;
        final int SCHEDULE_MATCHES_OPTION = 5;
        final int EXIT_OPTION = 6;

        Application application = new Application();
        users = new ArrayList<>();
        teams = new ArrayList<>();

        String[] options = {"Add new player", "Show existing players", "Make team", "Set attendance for teams", "Schedule matches", "Exit"};
        Menu mainMenu = new Menu("Main Menu", options);
        int userChoice = NULL_OPTION;
        while (userChoice != EXIT_OPTION) {
            Decorator.clearScreen();
            users = application.getUserManager().getAllUsers();
            teams = application.getTeamManager().getAllTeams();
            Decorator.printInBox("Weekly Leaderboard", '*');
            displayLadderBoard();
            System.out.println();
            mainMenu.display();
            System.out.println("Choose an option between " + ADD_PLAYER_OPTION + " and " + EXIT_OPTION + ":");
            userChoice = mainMenu.getMenuChoice();
            switch (userChoice) {
                case ADD_PLAYER_OPTION:
                    Decorator.clearScreen();
                    showAddPlayerMenu(application);
                    break;
                case SHOW_PLAYERS_OPTION:
                    Decorator.clearScreen();
                    showPlayersMenu(application);
                    break;
                case MAKE_TEAM_OPTION:
                    Decorator.clearScreen();
                    showMakeTeamMenu(application);
                    break;
                case SET_ATTENDANCE_OPTION:
                    Decorator.clearScreen();
                    setAttendanceStatusMenu(application);
                    break;
                case SCHEDULE_MATCHES_OPTION:
                    Decorator.clearScreen();
                    showScheduleMatchesMenu();
                    waitForEnterKey();
                    Decorator.clearScreen();
                case EXIT_OPTION:
                    break;
                default:
            }
            System.out.println();
        }
        application.shutDown();
    }

    private static void showPlayersMenu(Application application) {
        Decorator.clearScreen();
        Decorator.printInBox("Existing Players", '*');
        System.out.println();
        // TODO: Only list users that don't have a team, not all users
        // TODO: Handle case when there are no users available to join a team
        listAllUsers();
        waitForEnterKey();
        System.out.println();
    }

    private static void displayLadderBoard() {
        if (teams.size() > 0) {
            for (int i = 0; i < teams.size(); i++) {
                System.out.print((i + 1) + ". ");
                System.out.print(teams.get(i).getFirstPlayer().getDisplayName());
                System.out.print(" & " + teams.get(i).getSecondPlayer().getDisplayName());

                PlayTime attendanceStatus = teams.get(i).getAttendanceCard().getPreferredPlayTime();
                System.out.print(" (Attendance Status is \"" + attendanceStatus.getDisplayTime() + "\")");

                System.out.println();
            }
        } else {
            System.out.println("-- No teams are currently in the system. --");
        }
    }

    private static void showAddPlayerMenu(Application application) {
        final int CONFIRM_OPTION = 1;
        final int RETURN_OPTION = 0;

        Decorator.printInBox("Add Player", '*');

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter first name*: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter middle name: ");
            String middleName = scanner.nextLine();
            System.out.print("Enter last name*: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter email address (format: example@domain.com)*: ");
            String emailAddress = scanner.nextLine();
            System.out.print("Enter phone number*: ");
            String phoneNumber = scanner.nextLine();

            // TODO: Handle possible input errors here like blank fields, IllegalFormatException etc with a while loop

            System.out.println("\nCreate user with following details?");
            if (middleName.isEmpty()) {
                System.out.println("\tName: \"" + firstName + " " + lastName + "\"");
            } else {
                System.out.println("\tName: \"" + firstName + " " + middleName + " " + lastName + "\"");
            }
            System.out.println("\tEmail Address: \"" + emailAddress + "\"");
            System.out.println("\tPhone Number: \"" + phoneNumber + "\"");

            System.out.println("\nType " + RETURN_OPTION + " to return to main menu, or " + CONFIRM_OPTION + " to confirm:");

            if (Menu.getNumberInRange(RETURN_OPTION, CONFIRM_OPTION) == CONFIRM_OPTION) {
                application.getUserManager().create("" + (users.size() + 1), UserRole.PLAYER, firstName, middleName, lastName, emailAddress, phoneNumber);
            }
        } catch (Exception exception) {
            System.out.print("\nInput error: missing required fields or illegal format.");
            waitForEnterKey();
        }
    }

    private static void setAttendanceStatusMenu(Application application) {
        final int TIME_SLOT_B_OPTION = 3;
        final int TIME_SLOT_A_OPTION = 2;
        final int NOT_ATTENDING_OPTION = 1;
        final int RETURN_OPTION = 0;

        Decorator.clearScreen();
        Decorator.printInBox("Set Attendance for Teams", '*');
        System.out.println();
        Decorator.printUnderlined("Existing Teams:");
        displayLadderBoard();

        if (teams.size() > 0) {
            System.out.println("\nType 0 to return to main menu, or choose a user by entering their list number:");
            // TODO: Error checking when user tries to choose a deleted UserId
            int teamChoice = Menu.getNumberInRange(RETURN_OPTION, teams.size());
            System.out.println();

            if (teamChoice != RETURN_OPTION) {

                Team team = teams.get(teamChoice - 1);
                String[] options = {"Not Attending", "8:00PM", "9:30PM"};
                Menu attendanceStatusMenu = new Menu("Attendance Choices", options);
                attendanceStatusMenu.display();
                int attendanceStatusChoice = attendanceStatusMenu.getMenuChoice();
                PlayTime chosenPlayTime = null;
                switch (attendanceStatusChoice) {
                    case NOT_ATTENDING_OPTION:
                        chosenPlayTime = PlayTime.NONE;
                        break;
                    case TIME_SLOT_A_OPTION:
                        chosenPlayTime = PlayTime.TIME_SLOT_A;
                        break;
                    case TIME_SLOT_B_OPTION:
                        chosenPlayTime = PlayTime.TIME_SLOT_B;
                        break;
                    default:
                        break;
                }
                application.getTeamManager().updateAttendance(team.getId(), chosenPlayTime);
            }
        } else {
            waitForEnterKey();
        }
    }

    private static void waitForEnterKey() {
        System.out.print("\nPress Enter to return to main menu ... ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private static void showMakeTeamMenu(Application application) {
        final int CONFIRM_OPTION = 1;
        final int RETURN_OPTION = 0;

        Decorator.clearScreen();
        Decorator.printInBox("Make Team", '*');
        System.out.println();
        Decorator.printUnderlined("Existing Users:");
        // TODO: Only list users that don't have a team, not all users
        // TODO: Handle case when there are no users available to join a team
        listAllUsers();

        if (users.size() > 0) {
            System.out.println("\nType 0 to return to main menu, or choose a user by entering their list number:");
            // TODO: Error checking when user tries to choose a deleted UserId
            int firstPlayerChoice = Menu.getNumberInRange(0, users.size());
            if (firstPlayerChoice != RETURN_OPTION) {
                Decorator.printUnderlined("\nSelect a partner:");
                User firstUser = users.get(firstPlayerChoice - 1);
                users.remove(firstUser);
                listAllUsers();

                System.out.println("\nChoose a user by entering their list number:");
                // TODO: Error checking when user tries to choose a deleted UserId
                int secondPlayerChoice = NULL_OPTION;
                while (secondPlayerChoice == NULL_OPTION || secondPlayerChoice == firstPlayerChoice) {
                    secondPlayerChoice = Menu.getNumberInRange(1, Integer.MAX_VALUE);
                }

                User secondUser = users.get(secondPlayerChoice - 1);

                System.out.println("\nCreate team with the following players?");
                System.out.println("\tName: \"" + firstUser.getDisplayName() + "\"");
                System.out.println("\tName: \"" + secondUser.getDisplayName() + "\"");

                System.out.println("\nType " + RETURN_OPTION + " to return to main menu, or " + CONFIRM_OPTION + " to confirm:");

                if (Menu.getNumberInRange(RETURN_OPTION, CONFIRM_OPTION) == CONFIRM_OPTION) {
                    application.getTeamManager().create(firstUser, secondUser);
                }
            }
        } else {
            waitForEnterKey();
        }
        System.out.println();
    }

    private static void showScheduleMatchesMenu() {
        try {
            final int LINE_LENGTH = 30;
            List<Court> courts = MatchScheduler.scheduleMatches(6, MatchGroupGenerator.generateMatchGroupings(teams));
            for (int i = 0; i < courts.size(); i++) {
                System.out.println("Court " + (i + 1));
                Map<PlayTime, MatchGroup> scheduledMatches = courts.get(i).getScheduledMatches();
                if (scheduledMatches.size() > 0) {
                    for (PlayTime playTime : scheduledMatches.keySet()) {
                        System.out.println("\t(Playing at " + playTime.getDisplayTime() + ")");
                        MatchGroup matchGroup = scheduledMatches.get(playTime);
                        for (Team team : matchGroup.getTeams()) {
                            System.out.println("\tTeam " + team.getFirstPlayer().getDisplayName() + " & " + team.getSecondPlayer().getDisplayName());
                        }
                        Decorator.printLine(LINE_LENGTH, '~');
                    }
                } else {
                    System.out.println("-- No groups are scheduled at this court at any time --");
                }
                System.out.println();
            }
        } catch (MatchMakingException e) {
            System.out.println(e.getMessage());
            waitForEnterKey();
        }
    }

    private static void listAllUsers() {
        if (users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i).getDisplayName());
            }
        } else {
            System.out.println("-- No users are currently in the system. --");
        }
    }
}
