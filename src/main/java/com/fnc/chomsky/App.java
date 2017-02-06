/**  Aplicación que transforma una gramática independiente del contexto en
 *  Forma normal de Chomsky
 *  @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com>
 */
package com.fnc.chomsky;

import com.fnc.chomsky.bean.Chomsky;
import com.fnc.chomsky.util.ChomskyGenerator;

public class App {

    public static void main(final String ... args) {
        final ChomskyGenerator generator = new ChomskyGenerator(args[0], "X");
        final Chomsky chomsky = generator.generate();

        for (final String nf : chomsky.getNormalForms()) {
            System.out.println(nf);
        }

        for (final String production : chomsky.getProductions()) {
            System.out.println(production);
        }
    }
    
}