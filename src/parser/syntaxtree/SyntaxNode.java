package parser.syntaxtree;

public abstract class SyntaxNode {
    /**
     * Print the syntax tree to the console.
     */
    public abstract void print();

    /**
     * Evaluate the given syntax tree.
     * @param x - The value of x.
     * @return the result of the expression represented by the syntax tree.
     */
    public abstract double eval(double x);
}
