package javabankunimi.bank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {

    public RegexChecker(String string, String regex) throws IllegalArgumentException {
        if (string == null) throw new IllegalArgumentException();

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);

        if (!m.matches()) throw new IllegalArgumentException();
    }
}