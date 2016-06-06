//package ca.sfu.cmpt373.alpha.vrcladder.users;
//
//import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
//import ca.sfu.cmpt373.alpha.vrcladder.users.User;
//import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
//import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
//import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
//import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
//import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserName;
//import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
//
////import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Scanner;
//
///**
// * Created by Samus on 29/05/2016.
// */
//public class UserGenerator {
//
//    final int MAX_ID = 99999999;
//
//
//    final String[] AREA_CODE = new String[]{"604", "778"};
//    final int PHONE_DIGITS = 10;
//    final short EMAIL_STARTS_WITH = 0;
//
//    public UserGenerator() {
//
//    }
//
//    public User generateUser() {
//        //get names file
//        Scanner names;
//        try {
//            names = new Scanner(new BufferedReader(new FileReader("res\\names.txt")));
//        } catch (FileNotFoundException e) {
//            System.out.printf("%s", e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//
//        List<String> nameList = new ArrayList<>();
//        do {
//            nameList.add(names.next());
//        } while (names.hasNext());
//
//        //create player data
//        Random randomInt = new Random();
//        UserName username = new UserName(nameList.get(randomInt.nextInt(nameList.size())), null,
//                nameList.get(randomInt.nextInt(nameList.size())));
//        names.close();
//        UserId userID = new UserId(randomInt.nextInt(MAX_ID));
//
//        String newEmail = username.getFirstName().charAt(EMAIL_STARTS_WITH) + username.getLastName() + "@vrc.ca";
//        EmailAddress userEmail = new EmailAddress(newEmail);
//
//        //build a phone number
//        StringBuilder phone = new StringBuilder();
//        Random randomAreaCode = new Random();
//        int rn = randomAreaCode.nextInt(AREA_CODE.length);
//        phone.append(AREA_CODE[rn]);
//        phone.append("-" + randomAreaCode.nextInt(PHONE_DIGITS) + randomAreaCode.nextInt(PHONE_DIGITS)
//                + randomAreaCode.nextInt(PHONE_DIGITS));
//        phone.append("-" + randomAreaCode.nextInt(PHONE_DIGITS) + randomAreaCode.nextInt(PHONE_DIGITS)
//                + randomAreaCode.nextInt(PHONE_DIGITS) + randomAreaCode.nextInt(PHONE_DIGITS));
//        PhoneNumber userPhone = new PhoneNumber(phone.toString());
//
//        User user = new User(userID, UserRole.PLAYER, username, userEmail, userPhone);
//
//
//        return user;
//    }
//}
