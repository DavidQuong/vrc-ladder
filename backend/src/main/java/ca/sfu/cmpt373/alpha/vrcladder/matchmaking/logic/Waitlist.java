package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

public class Waitlist<T> {

    private T content;

    public Waitlist(T content){
        this.content = content;
    }

    public T get(){
        return this.content;
    }

}
