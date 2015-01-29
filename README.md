Leo Gutiérrez R. leogutierrezramirez@gmail.com

Transform a GIC into a Chomsky normal form

Input: 

	S->ab|aaB

```java
Chomsky gic2fnch = new Chomsky(linesFromFile, "X");
gic2fnch.generateChomsky();

for(String nf : gic2fnch.getNormalForms()) {
System.out.println(nf);
}

for(String production : gic2fnch.getProductions()) {
System.out.println(production);
}
```

Output:

	FNCH: S -> X1X2|X1X3
	Producciones: 
	X1->a
	X2->b
	X3->X1B
