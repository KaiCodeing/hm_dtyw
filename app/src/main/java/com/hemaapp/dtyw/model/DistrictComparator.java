package com.hemaapp.dtyw.model;

import java.util.Comparator;
import java.util.Locale;

public class DistrictComparator implements Comparator<Brands> {

	@Override
	public int compare(Brands o1, Brands o2) {
		if (o1.getCharindex().toUpperCase(Locale.getDefault()).equals("@") || o2.getCharindex().toUpperCase(Locale.getDefault()).equals("#")) {
			return -1;
		} else if (o1.getCharindex().toUpperCase(Locale.getDefault()).equals("#")
				|| o2.getCharindex().toUpperCase(Locale.getDefault()).equals("@")) {
			return 1;
		} else {
			return o1.getCharindex().toUpperCase(Locale.getDefault()).compareTo(o2.getCharindex().toUpperCase(Locale.getDefault()));
		}
	}

}
