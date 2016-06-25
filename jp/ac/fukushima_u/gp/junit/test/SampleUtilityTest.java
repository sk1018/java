package jp.ac.fukushima_u.gp.junit.test;

import static org.junit.Assert.*;
import jp.ac.fukushima_u.gp.SampleUtility;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SampleUtilityTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass()");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass()");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("setUp()");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown()");
	}

	@Test
	public void testGetJapaneseHelloMorning() {
		System.out.println("testGetJapaneseHelloMorning()");
		SampleUtility util = new SampleUtility();
		assertEquals("‚¨‚Í‚æ‚¤", util.getJapaneseHello(SampleUtility.MORNING));
	}

	@Test
	public void testGetJapaneseHelloDayTime() {
		System.out.println("testGetJapaneseHelloDayTime()");
		SampleUtility util = new SampleUtility();
		assertEquals("‚±‚ñ‚É‚¿‚Í", util.getJapaneseHello(SampleUtility.DAYTIME));
	}

	@Test
	public void testGetJapaneseHelloNigth() {
		System.out.println("testGetJapaneseHelloNigth()");
		SampleUtility util = new SampleUtility();
		assertEquals("‚±‚ñ‚Î‚ñ‚Í", util.getJapaneseHello(SampleUtility.NIGHT));
	}
}