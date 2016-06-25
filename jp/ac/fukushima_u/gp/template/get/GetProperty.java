package jp.ac.fukushima_u.gp.template.get;

import java.util.Properties;

public class GetProperty {

	public static void main(String[] args) {
	    Properties props = System.getProperties();
	    props.list(System.out);
	    System.out.println(System.getProperty("java.class.path"));
	}

}
