import java.util.*;

public class Register {

    private ArrayList<Dog> dogsInRegister = new ArrayList<>();
    private ArrayList<User> usersInRegister = new ArrayList<>();
    private ArrayList<Auction> auctionsInRegister = new ArrayList<>();

    private Scanner input = new Scanner(System.in);

    private void initialize() {

        System.out.println("Welcome!");
        System.out.println("\nThe following commands exist: \n* register new dog\n* increase age\n* list dogs\n* remove dog\n* register new user\n* list users\n* remove user\n* start auction\n* list auctions\n* list bids\n* make bid\n* close auction\n* exit");
    }

    private void runCommandLoop() {

        boolean done;
        do {
            String command = readCommand();
            done = handleCommand(command);
        } while (!done);
    }

    private String readCommand() {

        System.out.print("Command> ");
        String command = input.nextLine();
        return command;
    }

    private boolean handleCommand(String command) {

        switch (command.toLowerCase()) {
            case "register new dog":
                registerNewDog();
                break;
            case "increase age":
                increaseAge();
                break;
            case "list dogs":
                listDogs();
                break;
            case "remove dog":
                removeDog();
                break;
            case "register new user":
                registerNewUser();
                break;
            case "list users":
                listUsers();
                break;
            case "remove user":
                removeUser();
                break;
            case "start auction":
                startAuction();
                break;
            case "list auctions":
                listAuctions();
                break;
            case "list bids":
                listBids();
                break;
            case "make bid":
                makeBid();
                break;
            case "close auction":
                closeAuction();
                break;
            case "exit":
                return true;
            default:
                System.out.println("Error: unknown command \"" + command + "\"");
                System.out.println("\nThe following commands exist: \n* register new dog\n* increase age\n* list dogs\n* remove dog\n* register new user\n* list users\n* remove user\n* start auction\n* list auctions\n* list bids\n* make bid\n* close auction\n* exit");
        }
        return false;

    }

    private void registerNewDog() {

        System.out.print("Name> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);

        System.out.print("Breed> ");
        String breedInput = input.nextLine();
        String breed = isNameEmpty(breedInput);

        System.out.print("Age> ");
        int age = input.nextInt();
        System.out.print("Weight> ");
        int weight = input.nextInt();

        Dog dog = new Dog(convertName(name), convertName(breed), age, weight);
        dogsInRegister.add(dog);
        System.out.println(convertName(name) + " has been added to the register");
        input.nextLine();

    }

    private void increaseAge() {


        System.out.print("Enter the name of the dog> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);

        Dog dog = getDogByName(convertName(name).trim());

        if (dog == null) {
            System.out.println("Error: no such dog");
            return;
        } else {
            dog.increaseAge(1);
            System.out.println(convertName(name).trim() + " is now one year older");
        }
    }

    private void listDogs() {

        if (dogsInRegister.isEmpty()) {
            System.out.println("Error: no dogs in register");
        } else {
            System.out.print("Smallest tail length to display> ");
            double tailLength = input.nextDouble();
            input.nextLine();

            System.out.println("The following dogs has such a large tail:");

            Collections.sort(dogsInRegister, Dog.getTailComparator());

            for (Dog dog : dogsInRegister) {

                if (dog.getTailLength() >= tailLength) {

                    if (dog.getOwner() == null) {
                        System.out.println("* " + dog);
                    } else {
                        System.out.println("* " + dog + " owned by: " + dog.getOwner());
                    }
                }
            }
        }
    }

    private void removeDog() {

        System.out.print("Enter the name of the dog> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);
        Dog dog = getDogByName(convertName(name).trim());

        if (getDogByName(convertName(name).trim()) == null) {
            System.out.println("Error: no such dog");
        } else {
            dogsInRegister.remove(dog);

            if (dog.getOwner() != null) {
                User user = dog.getOwner();
                user.removeDog(dog);
            }

            if (isDogInAuction(dog)) {
                Auction auction = getAuctionByDog(dog);
                auctionsInRegister.remove(auction);
            }

            System.out.println(convertName(name).trim() + " has been removed from the register.");
        }
    }

    private void registerNewUser() {

        System.out.print("Name> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);

        User user = new User(convertName(name).trim());
        usersInRegister.add(user);

        System.out.println(convertName(name).trim() + " added to the register");
    }

    private void listUsers() {

        if (usersInRegister.isEmpty()) {
            System.out.println("No users in list");
        } else {
            for (User user : usersInRegister) {
                String comma = String.join(",", user.getDogsAsString());
                System.out.println(user + " [" + comma + "]");
            }
        }
    }

    private void removeUser() {

        System.out.print("Enter the name of the user> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);

        User user = getUserByName(convertName(name).trim());

        if (user == null) {
            System.out.println("Error: no such user");
            return;
        } else {
            usersInRegister.remove(user);

            Iterator<Dog> dogIterator = user.getOwnedDogs().iterator();

            while (dogIterator.hasNext()) {
                Dog dog = dogIterator.next();
                dogsInRegister.remove(dog);
            }

            for (Auction auction : auctionsInRegister) {
                for (Bid bid : auction.getBidList()) {
                    if (bid.getUser().equals(user)) {
                        auction.removeBid(bid);
                    }
                }
            }

            System.out.println(convertName(name).trim() + " has been removed from the register.");
        }
    }

    private void startAuction() {

        System.out.print("Enter the name of the dog> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);
        Dog dog = getDogByName(convertName(name).trim());

        if (dog == null) {
            System.out.println("Error: no such dog");
            return;
        }

        if (dog.getOwner() != null) {
            System.out.println("Error: this dog already has an owner");
            return;
        }

        if (!isDogInAuction(dog)) {
            Auction auction = new Auction(dog);
            auctionsInRegister.add(auction);
            System.out.println(convertName(name).trim() + " has been put up for auction in auction #" + auction.getAuctionNo());
        } else {
            System.out.println("Error: this dog is already up for auction");
        }
    }

    private void listAuctions() {

        if (auctionsInRegister.isEmpty()) {
            System.out.println("Error: no auctions in progress");
        } else {
            for (Auction auction : auctionsInRegister) {
                System.out.println(auction + " Top bids: " + auction.getBidsAsString());
            }
        }
    }

    private void listBids() {

        System.out.print("Enter the name of the dog> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);
        Dog dog = getDogByName(convertName(name).trim());

        if (dog == null) {
            System.out.println("Error: no such dog");
            return;
        }

        Auction auction = getAuctionByDog(dog);
        if (getAuctionByDog(dog) == null) {
            System.out.println("Error: this dog is not up for auction");

        } else if (auction.getBidList() == null) {
            System.out.println("No bids registered yet for this auction");

        } else {
            System.out.println("Here are the bids for this auction");
            auction.printBidsInList();

        }
    }

    private void makeBid() {

        System.out.print("Enter the name of the user> ");
        String userInput = input.nextLine();
        String userName = isNameEmpty(userInput);
        User user = getUserByName(convertName(userName).trim());

        if (user == null) {
            System.out.println("Error: no such user");
            return;
        }

        System.out.print("Enter the name of the dog> ");
        String dogInput = input.nextLine();
        String dogName = isNameEmpty(dogInput);
        Dog dog = getDogByName(convertName(dogName).trim());

        if (dog == null) {
            System.out.println("Error: no such dog");
            return;
        }

        if (getAuctionByDog(dog) == null) {
            System.out.println("Error: this dog is not up for auction");
            return;
        }

        Auction auction = getAuctionByDog(dog);

        if (auction.getWinningBid() == null) {
            System.out.print("Amount to bid (min 1)> ");
            int amount = input.nextInt();
            input.nextLine();

            if (amount <= 0) {
                do {
                    System.out.println("Error: too low bid! ");
                    System.out.print("Amount to bid (min 1)> ");
                    amount = input.nextInt();
                    input.nextLine();

                } while (amount <= 0);
            }
            Bid bid = new Bid(user, amount);
            auction.addBid(bid);
            System.out.println("Bid made");
        } else {
            System.out.print("Amount to bid (min " + (auction.getHighestBid() + 1) + ")> ");
            int amount = input.nextInt();
            input.nextLine();

            if (amount <= auction.getHighestBid()) {
                do {
                    System.out.println("Error: too low bid! ");
                    System.out.print("Amount to bid (min " + (auction.getHighestBid() + 1) + ")> ");
                    amount = input.nextInt();
                    input.nextLine();

                } while (amount <= auction.getHighestBid());
            }

            Bid bid = new Bid(user, amount);

            auction.addBid(bid);
            System.out.println("Bid made");
        }
    }

    private void closeAuction() {

        System.out.print("Enter the name of the dog> ");
        String nameInput = input.nextLine();
        String name = isNameEmpty(nameInput);

        Dog dog = getDogByName(convertName(name).trim());

        if (dog == null) {
            System.out.println("Error: no such dog");
            return;
        }

        if (getAuctionByDog(dog) == null) {
            System.out.println("Error: this dog is not up for auction");
            return;
        }

        Auction auction = getAuctionByDog(dog);
        Bid bid = auction.getWinningBid();

        if (bid == null) {
            System.out.println("The auction is closed. No bids where made for " + dog.getName());
            auctionsInRegister.remove(auction);
        } else {
            System.out.println("The auction is closed. The winning bid was " + auction.getHighestBid() + " kr and made by " + bid.getUser());
            auctionsInRegister.remove(auction);
            bid.getUser().addDog(dog);
            dog.setOwner(bid.getUser());
        }

    }

    private Dog getDogByName(String name) {
        for (Dog dog : dogsInRegister) {
            if (dog.getName().equals(name)) {
                return dog;
            }
        }
        return null;
    }

    private String isNameEmpty(String str) {
        if ((str.isEmpty() || Character.isWhitespace(str.charAt(0)))) {
            do {
                System.out.println("Error: the name can't be empty");
                System.out.print("Name> ");
                str = input.nextLine().trim();
            } while (str.isEmpty() || Character.isWhitespace(str.charAt(0)));
            return str.trim();
        } else {
            return str.trim();
        }
    }

    private String convertName(String name) {

        String input = name.toLowerCase().trim();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }

    private boolean isDogInAuction(Dog dog) {
        for (Auction auction : auctionsInRegister) {
            if (auction.getDog() == dog) {
                return true;
            }
        }
        return false;
    }

    private Auction getAuctionByDog(Dog dog) {
        for (Auction auction : auctionsInRegister) {
            if (auction.getDog().equals(dog)) {
                return auction;
            }
        }
        return null;
    }

    private User getUserByName(String name) {
        for (User user : usersInRegister) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public void run() {
        initialize();
        runCommandLoop();
        closeDown();
    }

    private void closeDown() {
        System.out.println("Goodbye!");
    }

    public static void main(String[] args) {
        new Register().run();
    }
}