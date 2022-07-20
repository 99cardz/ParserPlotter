package parser.syntaxtree;

public class MultNode extends BinarySyntaxNode {
    public MultNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" * ");
        right.print();
        System.out.print(")");
    }

    public double eval(double prev, double x, double next) {
        return left.eval(prev, x, next) * right.eval(prev, x, next);
    }
}
