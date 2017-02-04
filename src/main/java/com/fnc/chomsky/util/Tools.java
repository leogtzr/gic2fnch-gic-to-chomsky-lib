package com.fnc.chomsky.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Leo Gutiérrez
 *
 */
public class Tools {
	
	private Tools() {}
    
    public static boolean isProduction(final String s) {
        return Pattern.matches(".*->.*", s);
    }    
    
    public static String getProductionName(final String s) {
        final Pattern pattern = Pattern.compile("(.*)->");
        final Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    public static String getCases(final String s) {
        final Pattern pattern = Pattern.compile("->(.*)");
        final Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    public static boolean isChomsky(final String s) {
        return Pattern.matches("[a-z]|[A-Z]{2}", s);
    }
    
    public static boolean isTerminal(final String s) {
        return Pattern.matches("^[A-Z]$", s);
    }

    public static String getStringBtwn(final String s) {
        final Pattern pattern = Pattern.compile("\\{(.*)\\}");
        final Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    public static boolean isLowerTerminal(final String s) {
        return Pattern.matches("^[a-z]$", s);
    }
    
}
