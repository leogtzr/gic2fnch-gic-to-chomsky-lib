/**  Aplicación que transforma una gramática independiente del contexto en
 *  Forma normal de Chomsky
 *  @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com>
 */
package com.fnc.chomsky;

import java.util.Arrays;
import java.util.List;

import com.fnc.chomsky.util.ChomskyGenerator;

public class App {

    public static void main(final String ... args) {
        final ChomskyGenerator generator = new ChomskyGenerator(Arrays.<String>asList(args), "X");
        final List<ChomskyGenerator.Chomsky> chomskys = generator.generate();
        System.out.println(chomskys);
    }
    
}