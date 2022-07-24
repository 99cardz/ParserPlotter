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
    	double value = left.eval(x, stride);
    	double y = Math.tan(value);
    	// if the previous calculated value is bigger than the current,
    	// the tan crossed its limit.
    	// Similarly if the next calculated value will be less the the current,
    	// then the tan will cross its limit.
    	// This only applies if the all values of which
    	// the tan is calculated are ascending or descending
    	double prevValue = left.eval(x - stride, stride);
    	double nextValue = left.eval(x + stride, stride);
    	// ascending
    	if (prevValue < value && value < nextValue) {
    		double prevY = Math.tan(prevValue);
    		if (prevY > y) return Double.NEGATIVE_INFINITY;
    		double nextY = Math.tan(nextValue);
    		if (y > nextY) return Double.POSITIVE_INFINITY;
    	}
    	// descending
    	if (prevValue > value && value > nextValue) {
    		double prevY = Math.tan(prevValue);
    		if (prevY < y) return Double.POSITIVE_INFINITY;
    		double nextY = Math.tan(nextValue);
    		if (y < nextY) return Double.NEGATIVE_INFINITY;
    	}
    	return y;
    }
}
