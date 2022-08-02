package parser.syntaxtree;

public class SubNode extends BinarySyntaxNode {
    public SubNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" - ");
        right.print();
        System.out.print(")");
    }

    public double eval(double prev) {
        return left.eval(prev) - right.eval(prev);
    }
    
	public double[] evalAll(double[] values) {
		double[] l = left.evalAll(values);
		double[] r = right.evalAll(values);
		
		for(int i = 0; i < l.length; i++)
			l[i] -= r[i];
		
		return l;
	}
}
