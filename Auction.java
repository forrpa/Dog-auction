import java.util.*;
import java.util.StringJoiner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Auction {

    private Dog dog;
    private int auctionNo;
    private static int counter;

    private Bid[] topBids = new Bid[3];
    private List<Bid> bidsInAuction = new CopyOnWriteArrayList<Bid>();;

    public Auction(Dog dog){
        this.dog = dog;
        this.auctionNo = ++counter;
    }

    public void removeBid(Bid bid){
        for (int i = 0; i<bidsInAuction.size(); i++) {
            if (bidsInAuction.get(i) != null && bid.getUser().equals(bidsInAuction.get(i).getUser())) {
                bidsInAuction.remove(bidsInAuction.get(i));
            }
        }

        for (int i = 0; i < topBids.length; i++) {
            if (topBids[i] != null && bid.getUser().equals(topBids[i].getUser())) {
                if (topBids[i] == topBids [0]){
                    topBids[i] = topBids[i+1];
                    topBids[i+1] = topBids[i+2];
                } else if(topBids[i] == topBids[1]){
                    topBids[i] = topBids[2];
                } else {
                    topBids[i] = null;
                }
            }
        }
    }

    public List<Bid> getBidList(){
        if (bidsInAuction != null) {
            return bidsInAuction;
        } else{
            return null;
        }
    }

    public void addBid(Bid bid) {

        for (int i = 0; i < topBids.length; i++) {
            if (topBids[i] !=null && bid.getUser().equals(topBids[i].getUser())) {
                if (topBids[i] == topBids[0]) {
                    topBids[0] = bid;
                } else if (topBids[i] == topBids[1]){
                    topBids[1] = topBids[0];
                    topBids[0] = bid;
                } else {
                    System.arraycopy(topBids, 0, topBids, 1, topBids.length-1);
                    topBids[0] = bid;
                }
            }
        }

        for (int i = 0; i < bidsInAuction.size(); i++){
            if(bidsInAuction.get(i) != null && bid.getUser().equals(bidsInAuction.get(i).getUser())){
                bidsInAuction.set(i, bid);
                return;
            }
        }

        if (topBids[0] == null) {
            topBids[0] = bid;
            bidsInAuction.add(bid);
        } else {
            System.arraycopy(topBids, 0, topBids, 1, topBids.length-1);
            topBids[0] = bid;
            bidsInAuction.add(bid);
        }
    }

    public String getBidsAsString() {

        StringJoiner joiner = new StringJoiner(", ");
        for (Bid bid : topBids) {
            if (bid != null) {
                joiner.add(bid.toString());
            }
        }
        return "[" + joiner.toString() + "]";
    }

    public Bid getWinningBid(){

        if (topBids == null){
            return null;
        } else{
            return topBids[0];
        }
    }

    private void insertionSort (List<Bid> array) {
        int i, j;

        for(i=1; i < array.size(); i++){
            Bid tmp = array.get(i);
            j = i;
            while ((j>0) && (array.get(j-1).getAmount() < tmp.getAmount())){
                array.set(j, array.get(j-1));
                j--;
            }
            array.set(j, tmp);
        }
    }

    public void printBidsInList() {
        insertionSort(bidsInAuction);

        for (int i = 0; i<bidsInAuction.size(); i++){
            if(bidsInAuction.get(i) != null){
                System.out.println(bidsInAuction.get(i));
            }
        }
    }

    public int getHighestBid(){
        if (topBids[0] != null) {
            return topBids[0].getAmount();
        } else {
            return 0;
        }
    }


    public int getAuctionNo(){
        return auctionNo;
    }

    public Dog getDog(){
        return dog;
    }

    @Override
    public String toString(){
        return String.format("Auction #%d: %s.", auctionNo, dog.getName());
    }

}
