import java.util.ArrayList;
import java.util.StringJoiner;

public class User {

    private String name;

    private ArrayList<Dog> ownedDogs = new ArrayList<>();

    public User (String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean addDog (Dog dog){

        if (dog == null || ownedDogs.contains(dog)){
            return false; }

        else { ownedDogs.add(dog);
            dog.setOwner(this);
            return true;}
    }

    public void removeDog(Dog dog){
        ownedDogs.remove(dog);
    }

    public ArrayList<Dog> getOwnedDogs(){
        return new ArrayList<Dog>(ownedDogs);
    }

    public String getDogsAsString(){
        StringJoiner joiner = new StringJoiner(", ");
        for (Dog item : ownedDogs) {
            joiner.add(item.getName().toString());
        }
        return joiner.toString();
    }

    @Override
    public String toString(){
        return String.format("%s", name);
    }

}
