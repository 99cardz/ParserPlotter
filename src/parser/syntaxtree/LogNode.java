package parser.syntaxtree;

public class LogNode extends UnarySyntaxNode {

    public LogNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("log(");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev, double x, double next) {
        return Math.log(left.eval(prev, x, next));
    }
}