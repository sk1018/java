package jp.ac.fukushima_u.gp;

public class SampleUtility {

	public static final int MORNING = 1;
	public static final int DAYTIME = 2;
	public static final int NIGHT = 3;

	public String getJapaneseHello(int time) {

		String hello = null;

		switch (time) {
		case MORNING:
			hello = "���͂悤";
			break;
		case DAYTIME:
			hello = "����ɂ���";
			break;
		case NIGHT:
			hello = "����΂��";
			break;
		default:
			throw new IllegalArgumentException("time: " + time);
		}

		return hello;
	}
}