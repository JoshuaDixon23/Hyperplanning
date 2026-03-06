package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.TextTransformation;

import java.util.Objects;
import java.util.function.BiFunction;

public abstract class User{
    private final String firstName;
    private final String lastName;
    protected String emailUniv;
    // private ? phoneNumber;
    protected static final EmailCreate functionUnivMail =  new EmailCreate();

    protected User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected static <T extends User> T UserFactory(String fname, String lname, BiFunction<String, String, T> constructor) {
        // verification of entries to do !!!

        // format : Firstname LASTNAME
        String formattedFname = TextTransformation.capitalize(fname);
        String formattedLname = lname.toUpperCase();

        // Call of real constructor of subclass
        T user = constructor.apply(formattedFname, formattedLname);

        user.emailUniv = functionUnivMail.apply(user);

        return user;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmailUniv(String emailUniv) {
        this.emailUniv = emailUniv;
    }

    public String getEmailUniv() {
        return emailUniv;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailUniv='" + emailUniv + '\'' +
                '}';
    }



    //equals and hashCode


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(emailUniv, user.emailUniv);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(emailUniv);
    }
}
