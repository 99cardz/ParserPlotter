package parser.syntaxtree;

public class NumberNode extends SyntaxNode {
    double value;

    public NumberNode(double value) {
        this.value = value;
    }

    public void print() {
        System.out.print(value);
    }

    public double eval(double x) {
        return value;
    }
}
