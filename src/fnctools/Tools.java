package fnctools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

public class Tools {
    
    public static boolean isProduction(String s) {
        return Pattern.matches(".*->.*", s);
    }    
    
    public static byte getNumberCases(String s) {
        return (byte)new StringTokenizer(s, "|").countTokens();
    }
    
    public static String getProductionName(String s) {
        Pattern pattern = Pattern.compile("(.*)->");
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    public static String getCases(String s) {
        Pattern pattern = Pattern.compile("->(.*)");
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    public static boolean isChomsky(String s) {
        return Pattern.matches("[a-z]|[A-Z]{2}", s);
    }
    
    public static boolean isTerminal(String s) {
        return Pattern.matches("^[A-Z]$", s);
    }

    public static String getStringBtwn(String s) {
        Pattern pattern = Pattern.compile("\\{(.*)\\}");
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    public static boolean isLowerTerminal(String s) {
        return Pattern.matches("^[a-z]$", s);
    }
    
}
