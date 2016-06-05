package ca.sfu.cmpt373.alpha.vrctextui;

import ca.sfu.cmpt373.alpha.vrcladder.Application;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchScheduler;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI_Driver {
    private static final int NULL_OPTION = -1;
    private static List<User> users;
    private static List<Team> teams;

    public static void main(String[] args) {
        final int ADD_PLAYER_OPTION = 1;
        final int DELETE_PLAYER_OPTION = 2;
        final int MAKE_TEAM_OPTION = 3;
        final int SCHEDULE_MATCHES_OPTION = 4;
        final int EXIT_OPTION = 5;

        Application application = new Application();
        users = new ArrayList<>();
        teams = new ArrayList<>();

        String[] options = {"Add new player", "Delete player", "Make team", "Schedule matches", "Exit"};
        Menu mainMenu = new Menu("Main Menu", options);
        int userChoice = NULL_OPTION;
        while (userChoice != EXIT_OPTION) {
            Decorator.clearScreen();
            users = application.getUserManager().getAllUsers();
            teams = application.getTeamManager().getAllTeams();
            displayLadderBoard();
            mainMenu.display();
            System.out.println("Choose an option between " + ADD_PLAYER_OPTION + " and " + EXIT_OPTION + ":");
            userChoice = mainMenu.getMenuChoice();
            switch (userChoice) {
                case ADD_PLAYER_OPTION:
                    Decorator.clearScreen();
                    showAddPlayerMenu(application);
                    break;
                case DELETE_PLAYER_OPTION:
                    Decorator.clearScreen();
                    showDeletePlayerMenu(application);
                    break;
                case MAKE_TEAM_OPTION:
                    Decorator.clearScreen();
                    showMakeTeamMenu(application);
                    break;
                case SCHEDULE_MATCHES_OPTION:
                    Decorator.clearScreen();
                    showScheduleMatchesMenu(application);
                case EXIT_OPTION:
                    break;
                default:
            }
            System.out.println();
        }
        application.shutDown();
    }

    private static void displayLadderBoard() {
        for (int i = 0; i < teams.size(); i++) {
            System.out.println("Team: " + (i + 1));
            System.out.println(teams.get(i).getFirstPlayer().getDisplayName());
            System.out.println(teams.get(i).getSecondPlayer().getDisplayName());
            System.out.println();
        }
    }

    public static void showAddPlayerMenu(Application application) {
        final int CONFIRM_OPTION = 1;
        final int RETURN_OPTION = 0;

        Decorator.printInBox("Add Player", '*');

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter middle name: ");
        String middleName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email address (format: example@domain.com): ");
        String emailAddress = scanner.nextLine();
        System.out.print("Enter phone number: ");
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
            application.getUserManager().createUser("" + (users.size() + 1), UserRole.PLAYER, firstName, middleName, lastName, emailAddress, phoneNumber);
        }
    }

    public static void showDeletePlayerMenu(Application application) {
        final int CONFIRM_OPTION = 1;
        final int RETURN_OPTION = 0;

        Decorator.clearScreen();
        Decorator.printInBox("Delete Player", '*');
        System.out.println();
        Decorator.printUnderlined("Existing Users:");
        listAllUsers();
        if (users.size() > 0) {
            System.out.println("\nType 0 to return to main menu, or choose a user by entering their list number:");
            // TODO: Error checking when user tries to choose a deleted UserId
            int userChoice = Menu.getNumberInRange(RETURN_OPTION, users.size());

            if (userChoice != RETURN_OPTION) {
                User userToBeDeleted = application.getUserManager().getUser("" + userChoice);

                System.out.println("\nDelete user with following details?");
                System.out.println("\tName: \"" + userToBeDeleted.getDisplayName() + "\"");
                System.out.println("\tEmail Address: \"" + userToBeDeleted.getEmailAddress() + "\"");
                System.out.println("\tPhone Number: \"" + userToBeDeleted.getPhoneNumber() + "\"");

                System.out.println("\nType " + RETURN_OPTION + " to return to main menu, or " + CONFIRM_OPTION + " to confirm:");

                if (Menu.getNumberInRange(RETURN_OPTION, CONFIRM_OPTION) == CONFIRM_OPTION) {
                    users.remove(userToBeDeleted);
                    application.getUserManager().deleteUser("" + userChoice);
                    // Note createdUsersCount should not be decremented here
                }
            }
        } else {
            System.out.print("\nPress Enter to return to main menu ... ");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
    }

    public static void showMakeTeamMenu(Application application) {
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
                    application.getTeamManager().createTeam(firstUser, secondUser);
                }
            }
        } else {
            System.out.print("\nPress Enter to return to main menu ... ");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
        System.out.println();
    }

    public static void showScheduleMatchesMenu(Application application) {
        List<Court> courts = MatchScheduler.scheduleMatches(6, MatchGroupGenerator.generateMatchGroupings(teams));
        for (Court court : courts) {
            //TODO: schedule matches display code
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

    private static void listAllUsersExceptOne(String ignoredUserId) {
        if (users.size() > 0) {
            for (User user : users) {
                if (user.getUserId() != ignoredUserId) {
                    System.out.println(user.getUserId() + ". " + user.getDisplayName());
                }
            }
        } else {
            System.out.println("-- No users are currently in the system. --");
        }
    }
}
