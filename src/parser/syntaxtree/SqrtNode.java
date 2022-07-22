package parser.syntaxtree;

public class SqrtNode extends UnarySyntaxNode {

    public SqrtNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("sqrt(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x, double stride) {
    	// if the child node evaluates the next calculated pixel to be above zero
    	// and this pixel to be below or equal to zero
    	// the sqrt function just passed its limit
    	double value = left.eval(x, stride);
    	if (left.eval(x + stride, stride) > 0 && value <= 0)
    		return 0;
        return Math.sqrt(value);
    }
}
