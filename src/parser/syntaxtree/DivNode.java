package parser.syntaxtree;

public class DivNode extends BinarySyntaxNode {
    public DivNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" / ");
        right.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return left.eval(x) / right.eval(x);
    }
}