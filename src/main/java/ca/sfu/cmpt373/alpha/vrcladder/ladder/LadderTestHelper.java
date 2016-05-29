package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserName;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Samus on 29/05/2016.
 */
public class LadderTestHelper {
    /**
     * Generates a team
     * @return null if generation failed
     */
    public Team generateTeam(){
        //get names file
        Scanner names;
        try{
            names = new Scanner(new BufferedReader(new FileReader("C:\\Users\\Samus\\IdeaProjects\\prj\\names.txt")));
        }catch (FileNotFoundException e){
            System.out.printf("%s", e.getMessage());
            e.printStackTrace();
            return null;
        }

        List<String> nameList = new ArrayList<>();
        do{
            nameList.add(names.next());
        }while(names.hasNext());

        //create two players
        Random randomInt = new Random();
        UserName playerName1 = new UserName(nameList.get(randomInt.nextInt(nameList.size())),null,
                nameList.get(randomInt.nextInt(nameList.size())));
        UserName playerName2 = new UserName(nameList.get(randomInt.nextInt(nameList.size())),null,
                nameList.get(randomInt.nextInt(nameList.size())));

        System.out.println(playerName1.getDisplayName());
        System.out.println(playerName2.getDisplayName());
        //create team

        Team team = null;
        return team;
    }
}
