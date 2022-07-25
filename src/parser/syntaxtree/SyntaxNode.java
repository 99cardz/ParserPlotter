package parser.syntaxtree;

public abstract class SyntaxNode {
    /**
     * Print the syntax tree to the console.
     */
    public abstract void print();

    /**
     * Evaluate the given syntax tree.
     * @param x - the value to evaluate
     * @return the result of the expression represented by the syntax tree.
     */
    public abstract double eval(double x);
    
    /**
     * Evaluate all given values on this syntax tree.
     * The array of values can be used to find and adjust limits that are passed.
     * @param values to evaluate
     * @return result
     */
    public abstract double[] evalAll(double[] values);
}
