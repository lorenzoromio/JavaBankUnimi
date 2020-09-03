package javabankunimi.bank;

import javax.naming.InvalidNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    private String stringToCheck;


    public RegexChecker(String stringToCheck, String regex) throws IllegalArgumentException {
        if (stringToCheck == null) throw new IllegalArgumentException("String is null, does not match regex");

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(stringToCheck);

        if (!m.matches()) throw new IllegalArgumentException("String does not match regex");
    }

    public static Double checkValidAmount(String amount) throws IllegalArgumentException {

        String amountRegex = "^\\$?[0-9]+(\\.([0-9]{1,2}))?$";
        try {
            new RegexChecker(amount.replace(",", "."), amountRegex);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Importo non valido");
        }

        return Double.parseDouble(amount);
    }

    public static void checkValidName(String name) throws InvalidNameException {
        //language=RegExp
        String nameRegex = "^[A-Za-z]+((s)?(('|-|.)?([A-Za-z])+))*$"; //nomi singoli, doppi nomi con spazio, apostrofi
        try {
            new RegexChecker(name, nameRegex);
        } catch (IllegalArgumentException e) {
            throw new InvalidNameException();
        }
    }

    public static void checkValidPassword(String password) throws IllegalArgumentException {
        // RegexChecker to check valid password.

        String pswRegex = "^(?=.*[0-9])"                     //un numero
                + "(?=.*[a-z])"                      //una lettere minuscola
                + "(?=.*[A-Z])"                      //una lettere maiuscola
                + "(?=.*[!£$%&/()=?^*§°çé\"])"       //un caratteri speciale
                + "(?=\\S+$).{8,20}$";               //lunghezza tra 8 e 20

        try {
            new RegexChecker(password, pswRegex);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("La password deve contenere un carattere minuscolo, uno maiuscolo, " +
                    "\nun numero, un carattere speciale e deve essere lunga almeno 8 caratteri");
        }
    }

}


