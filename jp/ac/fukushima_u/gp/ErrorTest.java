package jp.ac.fukushima_u.gp;

class ErrorTest{
	void errorMethod() {
		
		System.out.println("ERROR TEST START");
		
		try {
			int[] i = new int[1];
			i[0] = 0;
			i[1] = 1;
			System.out.println("test");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ttt");
			System.out.println("error type\n" + e);
		} catch (ArithmeticException e) {
			System.out.println("");
		} finally {
			System.out.println("");
		}
		
		System.out.println("ERROR TEST END");
		
	}
	
}