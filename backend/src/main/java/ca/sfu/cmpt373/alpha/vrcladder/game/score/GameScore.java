package ca.sfu.cmpt373.alpha.vrcladder.game.score;


public class GameScore {

    private boolean score;
    private boolean scoreStatus;

    public GameScore(){
        this.scoreStatus = false;
    }

    public void setGameScore(boolean score){
        this.score = score;
        this.scoreStatus = true;
    }

    public boolean getScore(){
        return this.score;
    }

    public boolean isScoreEntered(){
        return this.scoreStatus;
    }

}
