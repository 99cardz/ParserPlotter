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

    public double eval(double prev, double x, double next) {
    	double denomPrevY = right.eval(prev);
    	double denomY = right.eval(x);
    	double denomNextY = right.eval(next);
    	if (denomPrevY <= 0 && denomY >= 0 || denomNextY <= 0 && denomY >= 0)
    		return Double.POSITIVE_INFINITY;
    	if (denomY <= 0 && denomNextY >= 0 || denomY <= 0 && denomPrevY >= 0)
    		return Double.NEGATIVE_INFINITY;
        return left.eval(prev, x, next) / right.eval(prev, x, next);
    }
    public double eval(double x) {
    	return left.eval(x) / right.eval(x);
    }
}
