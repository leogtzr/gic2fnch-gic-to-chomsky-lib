/**  Aplicación que transforma una gramática independiente del contexto en
 *  Forma normal de Chomsky
 *  @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com>
 */
package com.fnc.chomsky;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fnc.chomsky.util.Chomsky;
import com.fnc.chomsky.util.ChomskyNormalForm;

public class App {

    public static void main(final String ... args) throws IOException {

        final List<String> linesFromFile = getLinesFromFile("gic.txt");

        final Chomsky gic2fnch = new Chomsky(linesFromFile, "X");
        gic2fnch.generateChomsky();
        
        for (final String nf : gic2fnch.getNormalForms()) {
        }
        
        for (final String production : gic2fnch.getProductions()) {
        }
        
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        final ChomskyNormalForm chomskyNormalForm = new ChomskyNormalForm(linesFromFile, '@');
        chomskyNormalForm.create();
        
    }
    
    private static List<String> getLinesFromFile(final String filePath) throws IOException {
    	final List<String> linesFromFile = new ArrayList<>();
    	try (final BufferedReader br = new BufferedReader(new FileReader("gic.txt"))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
            	linesFromFile.add(line);
            }
        }
    	return linesFromFile;
    }
    
}
