package ca.sfu.cmpt373.alpha.vrctextui;

import ca.sfu.cmpt373.alpha.vrcladder.Application;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.Application;

import java.util.Scanner;

public class UI_Driver {
    private static final int NULL_OPTION = -1;
    private static long numUsers;

    public static void main(String[] args) {
        final int ADD_PLAYER_OPTION = 1;
        final int MAKE_TEAM_OPTION = 2;
        final int EXIT_OPTION = 3;
        String[] options = {"Add new player", "Make team", "Exit"};

        Application application = new Application();
        numUsers = 0;

        Menu mainMenu = new Menu("Main Menu", options);
        int userChoice = NULL_OPTION;
        while (userChoice != EXIT_OPTION) {
            Decorator.clearScreen();
            mainMenu.display();
            userChoice = mainMenu.getMenuChoice();
            switch (userChoice) {
                case ADD_PLAYER_OPTION:
                    Decorator.clearScreen();
                    showAddPlayerMenu(application);
                    for (int i = 1; i < numUsers; i++) {
                        User user = application.getUserManager().getUser(Integer.toString(i));
                        System.out.println(user.getFirstName());
                        System.out.println(user.getEmailAddress());
                        System.out.println(user.getPhoneNumber());
                    }
                    break;
                case MAKE_TEAM_OPTION:
                    Decorator.clearScreen();
//                    showMakeTeamMenu(application.getUserManager());
                    break;
                case EXIT_OPTION:
                    break;
                default:
            }
            System.out.println();
        }
        application.shutDown();
    }

//    public static void showMakeTeamMenu(UserManager userManager) {
//        final int ADD_PLAYER_OPTION = 1;
//        final int MAKE_TEAM_OPTION = 2;
//        final int EXIT_OPTION = 3;
//        String[] options = {"Add new player", "Make team", "Exit"};
//
//        Menu makeTeamMenu = new Menu("Make new team", options);
//        int userChoice = NULL_OPTION;
//        while (userChoice != EXIT_OPTION) {
//            Decorator.clearScreen();
//            makeTeamMenu.display();
//            userChoice = makeTeamMenu.getMenuChoice();
//            switch (userChoice) {
//                case ADD_PLAYER_OPTION:
//                    Decorator.clearScreen();
//                    showAddPlayerMenu(userManager);
//                    break;
//                case MAKE_TEAM_OPTION:
//                    Decorator.clearScreen();
//                    showMakeTeamMenu(userManager);
//                    break;
//                case EXIT_OPTION:
//                    return;
//                default:
//            }
//            System.out.println();
//        }
//    }

    public static void showAddPlayerMenu(Application application) {
        final int CONFIRM_OPTION = 1;
        final int RETURN_OPTION = 0;

        Decorator.printInBox("Add Player", '*');

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter middle name: ");
        String middleName = scanner.nextLine();
        System.out.println("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter email address (id@domain.com): ");
        String emailAddress = scanner.nextLine();
        System.out.println("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.println("Create user with following details?");
        System.out.println("\tName: \"" + firstName + " " + middleName + " " + lastName + "\"");
        System.out.println("\tEmail Address: \"" + emailAddress + "\"");
        System.out.println("\tPhone Number: \"" + phoneNumber + "\"");

        System.out.println("(Type " + CONFIRM_OPTION + " to confirm and " + RETURN_OPTION + " to return to main menu)");

        switch (Menu.getNumberInRange(RETURN_OPTION, CONFIRM_OPTION)) {
            case CONFIRM_OPTION:
                numUsers++;
                application.getUserManager().createUser(Long.toString(numUsers), UserRole.PLAYER, firstName, middleName, lastName, emailAddress, phoneNumber);
                return;
            case RETURN_OPTION:
                break;
            default:
        }
    }
}
