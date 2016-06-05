package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import org.junit.Test;

public class EmailAddressTest {
    private String[] emailList = {"user1@example.com",
                                  "user2@example1.com",
                                  "user_example-1@sub.example.ca"};

    @Test
    public void testEmailAddress(){
        // TODO: either assert or some extra for failure tests.
        for(String email : emailList){
            EmailAddress emailObject = new EmailAddress();
            emailObject.verifyFormat(email);
        }
    }
}
