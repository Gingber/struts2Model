/**
 * 
 */
package com.iie.rtp;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import com.arthur.simhash.SimHash;

/**
 * @author Gingber
 * 
 */

// TreeMap排序操作
class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;

	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}

public class Similarity {

	static Map<Integer, Double> modular = new HashMap<Integer, Double>();// 第一个文档
	static Map<Integer, Map<Integer, Integer>> mapVector = new HashMap<Integer, Map<Integer, Integer>>();
	static String stopChar = "啊,阿,哎,唉,俺 ,按 ,吧,本,比,彼,边,别,也,了,仍,朝,趁,乘,冲,除,此,打,待,但,当,到,等,第,多,嘎 ,该,赶,个,各,跟," +
			"从,以,使,则,却,又,及,对,就,并,很,或,把,是,的,得,地,着,给,而,被,让,在,还,比,等,当,与,于,但,故,管,归,过,哈,呵,和,何,嘿,哼,乎,哗,或," +
			"及,即,几,己,既,将,较,叫,借,尽,经,就,据,靠,咳,可,啦,来,离,哩,连,了,临,另,论,嘛,吗,冒,么,每,们,某,拿,哪,那,乃,呢,能,你,您,宁，哦,呕," +
			"呸,凭,其,起,且,任,如,啥,谁,顺,虽,随,所,他,它,她,倘,腾,替,同,哇,往,望,为,喂,我,呜,嘻,吓,像 ,向,嘘,呀,焉,沿,要,依,矣,以,因,哟,用,由," +
			"有,又,于,哉,咱,则,怎,咋,照,者,这,吱 ,之,至,自,纵,兮,呃,呗,咚,咦,喏,啐,嗬,嗯,嗳";
	

	public int StrLength(String title) {
		int length = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(title);
		while (m.find()) {
			length++;
		}
		return length;
	}

	@SuppressWarnings("unchecked")
	public Vector<Content> SimilarityCluster(Vector<Content> content) throws IOException {
		
		long startTime1 = System.currentTimeMillis();   //获取开始时间

		Comparator fgpcomp = new fgpComparatorImplementation();
		Collections.sort(content, fgpcomp);
		

		int num = 1;
		// <<<<<<<<<<<<<<<<遍历所有文档，两两比较并计算余弦相似度-----------------------
		for (int i = 0, iSize = content.size(); i < iSize; i++) {
			if (i == content.size() - 1 && content.get(i).getNum() == 0) { // 已标记到最后项
				content.get(i).setNum(num);
			} else if (content.get(i).getNum() != 0) { // 此项已被编号，跳出被层循环，进入下一层循环
				continue;
			} else {
				Vector vecInsert = new Vector();
	    		// 遍历Vector中的元素
				for (int j = i + 1, jSize = content.size(); j < jSize; j++) {
					if (content.get(j).getNum() != 0) { // 此项已被编号，跳出被层循环，进入下一层循环
						continue;
					} else {
			    		int d = SimHash.getDistance(content.get(i).getFingerprint(), content.get(j).getFingerprint());
			    		
			    		//System.out.println(i + " -> " + j + "\t" + d);
						if (d <= 3) { //相似度设置
							if (!vecInsert.contains(i)) {
								vecInsert.add(i);
							}
							if (!vecInsert.contains(j)) {
								vecInsert.add(j);
							}
							
							/*System.out.println(content.get(i).getTitle() + "\n");
							System.out.println(content.get(j).getTitle());
							System.out.println("*****************************************\n\n");*/
						}
						/*else {
							continue;
						}*/
					}
				}

				if (vecInsert != null && vecInsert.size() > 0) { // 存在重复项
					for (int j = vecInsert.size() - 1; j >= 0; j--) {
						int index = Integer.parseInt(vecInsert.get(j)
								.toString());
						// System.out.println(content.get(index).getMessageId()
						// + "\t" + content.get(index).getTitle());
						content.get(index).setNum(num);
					}
				} else { // 无重复项，仅一个
					content.get(i).setNum(num);
				}
			}
			num++; // 分类编号自增
		}
		// --------------------遍历所有文档，两两比较并计算余弦相似度>>>>>>>>>>>>>>>>>>>
		
		long endTime1 = System.currentTimeMillis();
		System.out.println("数据两两相似度计算时间： "+ (endTime1-startTime1) +"ms");
		
		long startTime2 = System.currentTimeMillis();   //获取开始时间
		// 按照分类编号num进行排序
		Comparator comp = new ComparatorImpl();
		Collections.sort(content, comp);
		
		long endTime2 = System.currentTimeMillis();
		System.out.println("数据排序计算时间： "+ (endTime2-startTime2) +"ms");

		return content;
	}
}

class fgpComparatorImplementation implements Comparator<Content> {
	public int compare(Content s1,Content s2) {
		int num1 = s1.getFingerprint();
		int num2 = s2.getFingerprint();
		if(num1 > num2) {
			return 1;
		}else if(num1 < num2){
			return -1;
		}else{
			return 0;
		}
	}
}

