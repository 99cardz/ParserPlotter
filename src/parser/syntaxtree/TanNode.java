package parser.syntaxtree;

public class TanNode extends UnarySyntaxNode {

    public TanNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("tan(");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev, double x, double next) {
    	double prevY = Math.tan(left.eval(prev));
    	double y = Math.tan(left.eval(x));
    	double nextY = Math.tan(left.eval(next));
    	return prevY > y ? Double.NEGATIVE_INFINITY : nextY < y ? Double.POSITIVE_INFINITY: y;
    }
    
    public double eval(double x) {
        return Math.tan(left.eval(x));
    }
}
