package fr.univtln.projet.planning.entity.person;

import java.util.function.Function;

public class EmailCreate implements Function<User, String> {

    @Override
    public String apply(User user) {
        String firstName = user.getFirstName().toLowerCase();
        String lastName = user.getLastName().toLowerCase();
        if (user instanceof Student) {
            // forme of mail for student: firstName-lastName@etud.univ-tln.fr
            return firstName + '-' + lastName + "@etud.univ-tln.fr";
        }
        else {
            // forme to precise for the other users (professor, Admin, StaffDRI)
            // firstName.lastName@univ-tln.fr
            return firstName + '.' + lastName + "@univ-tln.fr";
        }
    }
}
