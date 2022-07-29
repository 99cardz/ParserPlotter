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

    public double eval(double x) {
        return Math.cos(left.eval(x));
    }

	public double[] evalAll(double[] values) {
		
		double[] check = left.evalAll(values);
		double[] result = new double[values.length];
		
		for (int i = 0; i < values.length; i++)
			check[i] = Math.cos(check[i]);
		
		result[0] = check[0];
		int len = values.length-1;
		result[len] = check[len];
		for (int i = 1; i < len; i++) {
			// round to zero if we pass it or will pass it
			result[i] = 
					check[i-1] < 0 && check[i] >= 0 || check[i-1] >= 0 && check[i] < 0
					|| check[i+1] < 0 && check[i] >= 0 || check[i+1] >= 0 && check[i] < 0
					? 0 : check[i];
		}
		
		return result;
	}
}
