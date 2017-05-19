Leo Gutiérrez R. leogutierrezramirez@gmail.com

Transform a GIC into a Chomsky normal form.

Input: 

	S->ab|aaB

```java
ChomskyGenerator generator = new ChomskyGenerator("S->ab|aaB", "X");
Chomsky chomsky = generator.generate();

for (final String nf : chomsky.normalForms()) {
    System.out.println(nf);
}

for (final String production : chomsky.productions()) {
    System.out.println(production);
}
```

Output:

    S -> X1X2|X1X3
    X1->a
    X2->b
    X3->X1B
