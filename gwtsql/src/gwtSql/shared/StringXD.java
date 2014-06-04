package gwtSql.shared;

import java.io.Serializable;

// String in X dimensiuni
public class StringXD implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1506108147693217503L;

	// id	
	public String ShowValue="";
	// valoare afisata
	public String KeyValue="";
	// valoare aditionala 1
	public String X1Value="";
	// una numerica
	public Double X1DValue=0d;

	public StringXD(){
	}

	public StringXD(String s, String k){
		this.ShowValue = s;
		this.KeyValue = k;
	}

	public StringXD(String s, String k, String x1, Double d1){
		this.ShowValue = s;
		this.KeyValue = k;
		this.X1Value = x1;
		this.X1DValue = d1;
	}

}
