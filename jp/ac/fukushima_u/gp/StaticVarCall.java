package jp.ac.fukushima_u.gp;

class StaticVarCall{
	public static void main(String[] args) {
		StaticVar staticVar = new StaticVar();
		staticVar.addCount();
		
		System.out.println("count" + StaticVar.count);
	}
}