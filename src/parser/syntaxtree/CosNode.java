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

    public double eval(double x, double stride) {
    	double prevY = Math.cos(left.eval(x - stride, stride));
    	double y = Math.cos(left.eval(x, stride));
    	double nextY = Math.cos(left.eval(x + stride, stride));
    	// round to zero if we pass it or will pass it
    	if ((prevY < 0 && y >= 0) || (prevY > 0 && y <= 0))
    		return 0;
    	if ((nextY < 0 && y >= 0) || (nextY > 0 && y <= 0))
    		return 0;
        return y;
    }
}
