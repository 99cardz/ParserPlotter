package parser.syntaxtree;

public class AbsNode extends UnarySyntaxNode {

    public AbsNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("abs(");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev, double stride) {
        return Math.abs(left.eval(prev, stride));
    }
}