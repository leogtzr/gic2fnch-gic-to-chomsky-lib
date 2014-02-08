/*  Aplicación que transforma una gramática independiente del contexto en
 *  Forma normal de Chomsky
 *  @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com>
 */

import fnctools.Gic2FnCH;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com>
 */
public class Main {

    public static void main(String ... args) {

        ArrayList<String> linesFromFile = new ArrayList<String>();

        try {

            BufferedReader br = new BufferedReader(new FileReader("gic.txt"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                linesFromFile.add(strLine);
            }
            br.close();

        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }

        Gic2FnCH gic2fnch = new Gic2FnCH();

        for (byte i = 0; i < linesFromFile.size(); i++) {
            System.out.println("GIC [ " + linesFromFile.get(i) + " ]");
            gic2fnch.generate(linesFromFile.get(i));
            System.out.println("FNCH: " + gic2fnch.getFNCH());
            
            System.out.println("Producciones: ");
            for (String s : gic2fnch.getResultProductions()) {
                System.out.println(s);
            }
        }
    }
}
