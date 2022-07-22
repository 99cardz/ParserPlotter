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

    public double eval(double x, double stride) {
    	// if the previous child node evals to below or equal to zero,
    	// the log just passed its limit
    	if (left.eval(x - stride, stride) <= 0)
    		return Double.NEGATIVE_INFINITY;
    	
        return Math.log(left.eval(x, stride));
    }
}