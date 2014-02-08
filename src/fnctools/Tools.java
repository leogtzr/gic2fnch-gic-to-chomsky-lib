package fnctools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

/**
 * 
 * Tools.java es una clase que contiene métodos auxiliares para el algoritmo de generación de
 * Forma Normal de Chomsky contenido en la clase Gic2FnCH.java
 * <P>Nota: Tener en cuenta que tanto Tools como Gic2FnCH deben de estar en el mismo paquete.
 * @author Leonardo Gutiérrez Ramírez | leogutierrezramirez@gmail.com <a href="mailto:leogutierrezramirez@gmail.com">Leonardo Gutiérrez Ramírez</a>
 *  @version 1.0
 */
public class Tools {
    
    /**
     * isProduction(s) es un método que devuelve un booleano si la cadena S
     * se encuentra en la forma S->XXXX
     * 
     * @param s String
     * @return Boolean
     */
    public static boolean isProduction(String s) {
        return Pattern.matches(".*->.*", s);
    }    
    
    /**
     * 
     * getNumberCases() devuelve el número de casos de una producción
     * S--> a|aaB|Bab|abBc, tiene 4 casos, a, aaB, Bab|abBc
     * 
     * @param s String 
     * @return byte
     */
    public static byte getNumberCases(String s) {
        return (byte)new StringTokenizer(s, "|").countTokens();
    }
    
    /**
     * 
     * getNameProduction() devuelve el nombre de la producción
     * Teniendo S-> a|Bab, devuelve S que es el nombre de la producción
     * 
     * @param s String
     * @return String
     */
    public static String getNameProduction(String s) {
        Pattern pattern = Pattern.compile("(.*)->");
        Matcher matcher = pattern.matcher(s);
        
        if(matcher.find())
            return matcher.group(1);
        else
            return null;
    }
    
    /**
     * 
     * getCases() devuelve los casos de una producción, por ejemplo:
     * Teniendo S->a|aaB|Bab|abBc devuelve 'a|aaB|Bab|abBc', solo los casos
     * 
     * @param s String
     * @return String
     */
    public static String getCases(String s) {
        
        Pattern pattern = Pattern.compile("->(.*)");
        Matcher matcher = pattern.matcher(s);
        
        if(matcher.find())
            return matcher.group(1);
        else
            return null;
	
    }
    
    /**
     * 
     * isChomsky() devuelve un true o false si un caso no está en la forma normal de Chomsky, 
     * por ejemplo, teniendo esto, el método devolvería:
     * a - true
     * AA - true
     * aB - false
     * bb - false
     * 
     * @param s String
     * @return String
     */
    public static boolean isChomsky(String s) {
        return Pattern.matches("[a-z]|[A-Z]{2}", s);
    }
    
    /**
     * 
     * isTerminal() devuelve un true o false si el símbolo es terminal o no terminal, por ejemplo,
     * teniendo lo siguiente, el método devolvería:
     * A - true
     * a - false
     * 
     * @param s String
     * @return String
     */
    public static boolean isTerminal(String s) {
        return Pattern.matches("^[A-Z]$", s);
    }

    // Devuelve el texto en una cadena delimitado por {}
    /**
     * 
     * getStringBtrn() devuelve el texto entre {}
     * 
     * @param s String
     * @return String
     */
    public static String getStringBtwn(String s) {
        Pattern pattern = Pattern.compile("\\{(.*)\\}");
        Matcher matcher = pattern.matcher(s);
        
        if(matcher.find())
            return matcher.group(1);
        else
            return null;
    }
    
    public static boolean isTerminalLower(String s) {
        return Pattern.matches("^[a-z]$", s);
    }
    
}
