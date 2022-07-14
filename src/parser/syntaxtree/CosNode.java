package parser.syntaxtree;

public class CosNode extends UnarySyntaxNode {

    public CosNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("cos(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return Math.cos(left.eval(x));
    }
}
