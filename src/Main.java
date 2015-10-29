/**  Aplicación que transforma una gramática independiente del contexto en
 *  Forma normal de Chomsky
 *  @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com>
 */

import fnctools.Chomsky;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

public class Main {

    public static void main(String ... args) {

        ArrayList<String> linesFromFile = new ArrayList<>();

        try {
            try (final BufferedReader br = new BufferedReader(new FileReader("gic.txt"))) {
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    linesFromFile.add(strLine);
                }
            }
        } catch (final IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }

        final Chomsky gic2fnch = new Chomsky(linesFromFile, "X");
        gic2fnch.generateChomsky();
        
        for(String nf : gic2fnch.getNormalForms()) {
            System.out.println(nf);
        }
        
        for(String production : gic2fnch.getProductions()) {
            System.out.println(production);
        }
        
    }
}
