package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.hibernate.exception.ConstraintViolationException;

import java.security.Key;
import java.util.List;

public class CsvUtil {
    //notes: changed "Not Found" userIds to userIds as with 0000, 0001, 0002... 0013
    String csvFile = "Last Name,First Name,Player #,Status,,Last Name,First Name,Player #,Status,Ranking\n" +
            "Truong,Cindy,9217,,&,Wong,Alex,5447,,1\n" +
            "Ferguson,Robbie,8331,,&,Shum,Jamie,9735,,2\n" +
            "Chong,Rosalynn,5864,,&,Fung,Stevie,9235,,3\n" +
            "Tay,Siu-Imm,2231,,&,Lau,Leslie,3877,,4\n" +
            "Ip,Victor,9468,,&,Ko,Joycelyn,9965,,5\n" +
            "Lee,Daisy,5565,,&,Wadsworth,Samuel,7792,,6\n" +
            "Wong,Clinton,5446,,&,Cheung,Daphne,6525,,7\n" +
            "Wong,Jason,9973,,&,Cheng,Kylie,7591,,8\n" +
            "Nguyen,Mike,9614,,&,wang,Erica,8696,,9\n" +
            "Fu,Hilda,8516,,&,Luk,Alex,7535,,10\n" +
            ",Vicky,0003,,&,,David,0010,,11\n" +
            "Ito,Akiko,5104,,&,Wong,Raymond,3904,,12\n" +
            "To,Cynthia,9161,,&,To,Aaron,9160,,13\n" +
            "Qiao,Ying,8174,,&,lui,Yorkie,10008,,14\n" +
            "Haebler,Catherine,7972,,&,Zhao,Jason,8389,,15\n" +
            "Lee,Aaron,10001,,&,Cheng,Katrina,10007,,16\n" +
            "li,Raymond,7040,,&,tsui,Teresa,10125,,17\n" +
            "King,Julie,6059,,&,Lai,Rock,9810,,18\n" +
            "Wong,Wilson,6496,,&,Tang,Bonnie,9454,,19\n" +
            "Jordan,Richard,5593,,&,Tao,Erica,6136,,20\n" +
            "Ng,Denys,8951,,&,Yeung,Jacinda (Oi Man),9515,,21\n" +
            "wou,Ian,9440,,&,wang,Ivy,9252,,22\n" +
            "Yu,Billy,6642,,&,Dompas,Florence,5805,,23\n" +
            "Hong,Siew Har,1053,,&,Prior,Julian,10190,,24\n" +
            "Lau,Pedro,7268,,&,Ferguson,Yuri,8637,,25\n" +
            "Dalin,Wayne,2642,,&,Valenta,Reesa,7961,,26\n" +
            "Chua,Zong,9236,,&,kish,Kathy,7964,,27\n" +
            "Yao,Duncan,5925,,&,Lo,Vanora,7041,,28\n" +
            "Tsai,David,3996,,&,So,Victoria,7975,,29\n" +
            "Chen,Yoyo,9052,,&,Liu,Louis,9023,,30\n" +
            "Chan,Andre,4301,,&,Cheng,Sally,9750,,31\n" +
            "Leung,Ben,9903,,&,Li,Kristal,10292,,32\n" +
            "Rockburn,Vicki,1641,,&,Lai,Keevin,5003,,33\n" +
            "Britten,Mike,1727,,&,Wai,Clara,1395,,34\n" +
            "Kiang,Yu-Hui,9517,,&,Ng,Chi-Ho,9518,,35\n" +
            ",Peter,0005,,&,Kidd,Abby,1609,,36\n" +
            "Lim,Jerome,1506,,&,Tham,Karen,7269,,37\n" +
            "Li,Antonio,10288,,&,,Catherine,0006,,38\n" +
            "Li,Antonio,10288,,&,Chen,Yoyo,9052,,39\n" +
            "Leung,Ben,9903,,&,Cheng,Katrina,10007,,40\n" +
            "Yun,Carol,9255,,&,Chan,Shurman,8403,,41\n" +
            "Sun,Karen,1375,,&,Au,Jason,9703,,42\n" +
            "Woolfries,Cameron,10276,,&,Hang,Michelle,9675,,43\n" +
            "Lee,Daisy,5565,,&,Britten,Mike,1727,,44\n" +
            "Choi,Nathan,4895,Not Current,&,Hang,Michelle,9675,,45\n" +
            "Yu,Kevin,9972,Not Current,&,Yuen,Katherine,6202,,46\n" +
            "Sullivan ,Joanne,1651,,&,Wong,Terry,2594,,47\n" +
            "Dryborough,Sanne,1249,,&,Price,Frank,2170,,48\n" +
            "wang,Ivy,9252,,&,,Scott,0014,,49\n" +
            "Kiang,Yu-Hui,9517,,&,fan,Chris,9037,Not Current,50\n" +
            "Tanweer,Farrukh,9991,,&,Yun,Carol,9255,,51\n" +
            "Lim,Lana,1600,,&,Lui,Yorkie,10008,,52\n" +
            "lim,Lana,1600,,&,chen,Albert,0002,,53\n" +
            "kish,Kathy,7964,,&,Wong,Terry,2594,,54\n" +
            ",Denis,0007,,&,Nirwal,Sushil,10201,,55\n" +
            "King,Julie,6059,,&,Liu,Adrian,9049,Not Current,56\n" +
            "lam,Helen,2904,,&,yip,Peter,5519,,57\n" +
            "Valenta,Reesa,7961,,&,,Chris,0008,,58\n" +
            "Truong,Cindy,9217,,&,Wong,Wilson,6496,,59\n" +
            "Tham ,Karen,7269,,&,Sumida,Reed,9558,,60\n" +
            "Li,Antonio,10288,,&,King,Julie,6059,,61\n" +
            "chan,Jessica,9692,,&,wong,Ben,0000,,62\n" +
            "ip,Victor,9468,,&,Yuen,Helen,5988,Not Current,63\n" +
            "Hong,Siew Har,1053,,&,Sumida,Reed,9558,,64\n" +
            "Hudson,Scott,5959,,&,Dryborough,Sanne,1249,,65\n" +
            "Sun,Karen,1375,,&,Wong,Terry,2594,,66\n" +
            "Choo,Linus,8002,Not Current,&,Chen,Sammie,8001,,67\n" +
            "Tham ,Karen,7269,,&,Price,Frank,2170,,68\n" +
            "Tsai,David,3996,,&,Lim,Lana,1600,,69\n" +
            "Guo,Shaolin,8191,,&,achtymichuk,Paul,5545,,70\n" +
            "Sullivan ,Joanne,1651,,&,Sumida,Reed,9558,,71\n" +
            "Chen,Andrew,8559,Not Current,&,Chang,Serene,8941,,72\n" +
            "Lai,Keevin,5003,,&,Holoboff,Marian,1680,,73\n" +
            "Wong,Terry,2594,,&,Qiao,Ying,8174,,74\n" +
            "Lee,Daisy,5565,,&,Lau,Pedro,7268,,75\n" +
            "wang,Ivy,9252,,&,Yip,Peter,5519,,76\n" +
            "Cheng,Simon,6461,,&,tay,Siu-Imm,2231,,77\n" +
            "Lai,Rock,9810,,&,,Catherine,0009,,78\n" +
            "Chua,Zong,9236,,&,Dompas,Florence,5805,,79\n" +
            "Zheng,Vicky,7480,,&,Nilsson,Erik,2498,,80\n" +
            "Tsai,David,3996,,&,Regan,Patsy,1270,,81\n" +
            "Hudson,Scott,5959,,&,Sullivan ,Joanne,1651,,82\n" +
            "wang,Ivy,9252,,&,Sumida,Reed,9558,,83\n" +
            "lim,Jerome,1506,,&,lim,Lana,1600,,84\n" +
            "Swayampakula,Mridula,9790,,&,Sumida,Reed,9558,,85\n" +
            ",Vicky,0003,,&,,Alex,0004,,86\n" +
            "Luk,Alex,7535,,&,Shum,Jamie,9735,,87\n" +
            "Lee,Jansen,9827,,&,Yeung,Jacinda (Oi Man),9515,,88\n" +
            ",Keilvin,0001,,&,Qiao,Ying,8174,,89\n" +
            "Lee,Jansen,9827,,&,Shum,Karina,9734,,90\n" +
            "Ferguson,Robbie,8331,,&,Ferguson,Yuri,8637,,91\n" +
            "Yuen,Katherine,6202,,&,zhao,Jason,8389,,92\n" +
            "wai,Clara,1395,,&,Price,Frank,2170,,93\n" +
            ",Dennis,0011,,&,,Carol,0012,,94\n" +
            "Tang,Bonnie,9454,,&,Lau,Pedro,7268,,95\n" +
            "king,Julie,6059,,&,,Unable to read,,,96\n" +
            "Walia,Anand,1448,,&,Sullivan,Joanne,1651,,97\n" +
            "Lim,Jerome,1506,,&,,Denise,0013,,98";

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    Key signatureKey = MacProvider.generateKey();
    SecurityManager securityManager = new SecurityManager(signatureAlgorithm, signatureKey);
    SessionManager sessionManager = new SessionManager(new ConfigurationManager());
    UserManager userManager = new UserManager(sessionManager);
    TeamManager teamManager = new TeamManager(sessionManager);
    int emailIndex = 0;

    public void setAttending() {
        List<Team> teams = teamManager.getAll();
        int numTeams = 9;
        for (int i = 0; i < numTeams; i++) {
            teamManager.updateAttendancePlaytime(teams.get(i).getId(), PlayTime.TIME_SLOT_A);
        }
    }

    public void insertData() {
        String[] rows = csvFile.split("\n");
        //skip the first row because it just contains headers with column names
        for (int i = 1; i < rows.length; i++) {
            String row = rows[i];
            String[] columnValues = row.split(",");

            String firstPlayerLastName = columnValues[0];
            String firstPlayerFirstName = columnValues[1];
            String firstPlayerId = columnValues[2];

            String statusPlayer1 = columnValues[3];

            String secondPlayerLastName = columnValues[5];
            String secondPlayerFirstName = columnValues[6];
            String secondPlayerId = columnValues[7];

            String statusPlayer2 = columnValues[8];

            String position = columnValues[9];

            if (
//                    !isEmpty(firstPlayerLastName) &&
                    !isEmpty(firstPlayerFirstName) &&
                    !isEmpty(firstPlayerId) &&
                    isEmpty(statusPlayer1) && //note that this is supposed to be empty!
//                    !isEmpty(secondPlayerLastName) &
                    !isEmpty(secondPlayerFirstName) &&
                    !isEmpty(secondPlayerId) &&
                    isEmpty(statusPlayer2) && //also should be empty
                    !isEmpty(position)) {
                User user1 = addPlayer(userManager, firstPlayerFirstName, firstPlayerLastName, firstPlayerId);
                User user2 = addPlayer(userManager, secondPlayerFirstName, secondPlayerLastName, secondPlayerId);
                teamManager.create(user1, user2);
            }
        }
    }

    private boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    private User addPlayer(UserManager userManager, String firstName, String lastName, String id) {
        User user;
        try {
            user = userManager.create(new UserBuilder()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUserId(id)
                    .setMiddleName("")
                    .setUserRole(UserRole.PLAYER)
                    .setEmailAddress("test" + emailIndex + "@test.com")
                    .setPhoneNumber("1111111111")
                    .setPassword(securityManager.hashPassword("123"))
                    .buildUser());
        } catch (ConstraintViolationException e) {
            return userManager.getById(new UserId(id));
        }
        emailIndex++;
        return user;
    }
}

