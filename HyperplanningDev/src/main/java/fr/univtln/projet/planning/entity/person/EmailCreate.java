package fr.univtln.projet.planning.entity.person;

import java.util.Random;
import java.util.function.Function;

public class EmailCreate implements Function<User, String> {
    private final static Random random = new Random();
    
    @Override
    public String apply(User user) {
        String firstName = user.getFirstName().toLowerCase();
        String lastName = user.getLastName().toLowerCase();
        if (user instanceof Student) {
            // forme of mail for student: firstName-lastName000@etud.univ-tln.fr, 000 - three random digits
            int randomNumber = 100 + random.nextInt(900);
            return firstName + '-' + lastName + randomNumber + "@etud.univ-tln.fr";
        }
        else {
            // forme to precise for the other users (professor, Admin, StaffDRI)
            // firstName.lastName0@univ-tln.fr, 0 random digit
            int randomDigit = 1 + random.nextInt(9);
            return firstName + '.' + lastName + randomDigit + "@univ-tln.fr";
        }
    }
}
