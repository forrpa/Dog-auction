import java.util.Comparator;

public class Dog{

    private String name;
    private String breed;
    private int age;
    private int weight;

    private User owner;

    public Dog (String name, String breed, int age, int weight){
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public User getOwner(){
        return owner;
    }

    public void setOwner(User owner){
        this.owner = owner;
        owner.addDog(this);
    }

    public void increaseAge (int newAge){
        if (newAge > 0) {
            age += newAge;
        }
    }

    public double getTailLength (){
        if (breed.toLowerCase().equals("tax") || breed.toLowerCase().equals("dachshund")) {
            return 3.7;
        } else {
            return age * weight / 10.0;
        }
    }

    private static Comparator<Dog> tailComparator = new Comparator<Dog>() {
        @Override
        public int compare(Dog dogOne, Dog dogTwo) {
            double doubleOne = dogOne.getTailLength();
            double doubleTwo = dogTwo.getTailLength();

            if (doubleOne == doubleTwo){
                String dogNameOne = dogOne.getName().toUpperCase();
                String dogNameTwo = dogTwo.getName().toUpperCase();
                return dogNameOne.compareTo(dogNameTwo);
            }
            return Double.valueOf(doubleOne).compareTo(doubleTwo);
        }
    };

    public static Comparator<Dog> getTailComparator(){
        return tailComparator;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d years, %d kilo, %1.2f tail)", name, breed, age, weight, getTailLength());
    }
}
