public class Bid {

    private User user;
    private int amount;

    public Bid (User user, int bidAmount){
        this.user = user;
        this.amount = bidAmount;
    }

    public User getUser(){
        return user;
    }

    public int getAmount(){
        return amount;
    }

    @Override
    public String toString(){
        return user + " " + amount + " kr";
    }

}
