package parser.syntaxtree;

public class NegateNode extends UnarySyntaxNode {

    public NegateNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("(-");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev, double x, double next) {
        return -left.eval(prev, x, next);
    }
}
