package parser.syntaxtree;

public class VariableNode extends SyntaxNode {
    public void print() {
        System.out.print("x");
    }

    public double eval(double x) {
        return x;
    }
}
