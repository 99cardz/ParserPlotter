package parser.syntaxtree;

public class PowNode extends BinarySyntaxNode {
    public PowNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("^");
        right.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return Math.pow(left.eval(x), right.eval(x));
    }
}
