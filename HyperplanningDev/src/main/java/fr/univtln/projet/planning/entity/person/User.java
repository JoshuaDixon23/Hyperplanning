package fr.univtln.projet.planning.entity.person;

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
        String formattedFname = capitalize(fname);
        String formattedLname = lname.toUpperCase();

        // Call of real constructor of subclass
        T user = constructor.apply(formattedFname, formattedLname);

        user.emailUniv = functionUnivMail.apply(user);

        return user;
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
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
}
