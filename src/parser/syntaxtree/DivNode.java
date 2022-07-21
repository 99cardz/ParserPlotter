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

    public double eval(double x, double stride) {
    	double denomY = right.eval(x, stride);
    	if (Double.isInfinite(denomY))
    		return 0;
    	
    	double denomPrevY = right.eval(x - stride, stride);
    	double denomNextY = right.eval(x + stride, stride);
    	if (denomPrevY <= 0 && denomY >= 0 || denomNextY <= 0 && denomY >= 0)
    		return Double.POSITIVE_INFINITY;
    	if (denomY <= 0 && denomNextY >= 0 || denomY <= 0 && denomPrevY >= 0)
    		return Double.NEGATIVE_INFINITY;
    	
        return left.eval(x, stride) / denomY;
    }
}
