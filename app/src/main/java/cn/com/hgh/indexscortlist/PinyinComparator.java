package cn.com.hgh.indexscortlist;

import java.util.Comparator;

import com.lianbi.mezone.b.bean.SalesMan;

public class PinyinComparator implements Comparator<SalesMan> {

	public int compare(SalesMan o1, SalesMan o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
