package jp.ac.fukushima_u.gp;

public class SampleUtility {

	public static final int MORNING = 1;
	public static final int DAYTIME = 2;
	public static final int NIGHT = 3;

	public String getJapaneseHello(int time) {

		String hello = null;

		switch (time) {
		case MORNING:
			hello = "‚¨‚Í‚æ‚¤";
			break;
		case DAYTIME:
			hello = "‚±‚ñ‚É‚¿‚í";
			break;
		case NIGHT:
			hello = "‚±‚ñ‚Î‚ñ‚í";
			break;
		default:
			throw new IllegalArgumentException("time: " + time);
		}

		return hello;
	}
}