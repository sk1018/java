package jp.ac.fukushima_u.gp;

class MethodCall{
	public static void main(String[] args) {
		SkyLine skyLine = new SkyLine();
		
		String name = skyLine.getName();
		String maker = skyLine.getMaker();
		
		System.out.println(maker + " " + name);
	}
}