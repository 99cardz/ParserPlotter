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

    public double eval(double x, double stride) {
    	double prevY = Math.tan(left.eval(x - stride, stride));
    	double y = Math.tan(left.eval(x, stride));
    	double nextY = Math.tan(left.eval(x + stride, stride));
    	// if the previous calculated value is biggen than the current,
    	// the tan crossed its limit.
    	// similary if the next calculated value will be less the the current,
    	// then the tan will cross its limit
    	return prevY > y ? Double.NEGATIVE_INFINITY : nextY < y ? Double.POSITIVE_INFINITY: y;
    }
}
