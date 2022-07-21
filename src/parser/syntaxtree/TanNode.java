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
//    	System.out.println(prevY + " " + y + " " + nextY);
    	return prevY > y ? Double.NEGATIVE_INFINITY : nextY < y ? Double.POSITIVE_INFINITY: y;
    }
}
