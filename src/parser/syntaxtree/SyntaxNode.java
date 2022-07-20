package parser.syntaxtree;

public abstract class SyntaxNode {
    /**
     * Print the syntax tree to the console.
     */
    public abstract void print();

    /**
     * Evaluate the given syntax tree.
     * @param prev - the previous 
     * @param x - The value of x.
     * @param next - the next value to evaluate
     * @return the result of the expression represented by the syntax tree.
     */
    public abstract double eval(double prev, double x, double next);
    
    public abstract double eval(double x);
}
