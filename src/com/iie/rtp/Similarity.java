/**
 * 
 */
package com.iie.rtp;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

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
	public Vector<Content> SimilarityCluster(Vector<Content> content) {
		
		long startTime1 = System.currentTimeMillis();   //获取开始时间

		int chnum = 0; // 汉字个数统计
		Map<String, Integer> bagmap = new HashMap<String, Integer>();
		for (int i = 0, size = content.size(); i < size; i++) {
			String title = content.get(i).getTitle();
			for (int j = 0, len = title.length(); j < len; j++) {
				char ch = title.charAt(j);
				if (isChinese(ch)) {
					if (!bagmap.containsKey(ch) && !stopChar.contains(ch+"")) {
						bagmap.put(String.valueOf(ch), chnum);
						chnum++;
					}
				}
			}
		}

		System.out.println("字典大小：" + bagmap.size());

		int num = 1;
		// <<<<<<<<<<<<<<<<遍历所有文档，将样本向量化并取模-----------------------
		int[] flagLen = new int[content.size()];// 标识样本长度是否满足条件(len > 10),如果满足
		for (int i = 0, size = content.size(); i < size; i++) {
			/*String doc1 = content.get(i).getTitle();// 第i条记录
			int len1 = StrLength(doc1);*/
			Map<Integer, Integer> nowContent = new HashMap<Integer, Integer>();// 当前文档
			
			// <<<<<<<<<<<<<<<<-------------new idea(2013-09-13)----------------------------------
			String doc1 = content.get(i).getTitle().replaceAll("[^\u4E00-\u9FA5]", "");// 第i条记录
			int len1 = doc1.length();
			// <<<<<<<<<<<<<<<<-------------new idea(2013-09-13)----------------------------------

			if (doc1 != null && len1 > 10) {
				for (int j = 0, len = doc1.length(); j < len; j++) {
					/*char ch = doc1.charAt(j);
					if (isChinese(ch)) {
						if (nowContent.containsKey(ch)) {
							int index = bagmap.get(ch + "");
							int num01 = nowContent.get(ch) + 1;
							nowContent.put(index, num01);
						} else if(!stopChar.contains(ch+""))
							nowContent.put(bagmap.get(ch + ""), 1);
					}*/
					
					// <<<<<<<<<<<<<<<<-------------new idea(2013-09-13)--------------------------
					char ch = doc1.charAt(j);
					if (nowContent.containsKey(ch)) {
						int index = bagmap.get(ch + "");
						int num01 = nowContent.get(ch) + 1;
						nowContent.put(index, num01);
					} else if(!stopChar.contains(ch+""))
						nowContent.put(bagmap.get(ch + ""), 1);
					// <<<<<<<<<<<<<<<<-------------new idea(2013-09-13)--------------------------
				
				}				
				mapVector.put(i, nowContent);
				modular.put(i, getmodular(nowContent));

				flagLen[i] = 1;
				Vector vecInsert = new Vector();
				if (i > 0)// 从第1条记录起与第0条记录进行相似度计算
				{
					double d = getSimilarity02(mapVector.get(0), mapVector
							.get(i), 0, i);

					if (d > 0.90) {
						if (!vecInsert.contains(0)) {
							vecInsert.add(0);
						}
						if (!vecInsert.contains(i)) {
							vecInsert.add(i);
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
						content.get(0).setNum(num);
					}
				}

			}
			
		}
		num++; // 分类编号自增

		// --------------------遍历所有文档，将样本向量化并取模>>>>>>>>>>>>>>>>>>>

		// <<<<<<<<<<<<<<<<遍历所有文档，两两比较并计算余弦相似度-----------------------
		for (int i = 1, iSize = content.size(); i < iSize; i++) {
			if (i == content.size() - 1 && content.get(i).getNum() == 0) { // 已标记到最后项
				content.get(i).setNum(num);
			} else if (content.get(i).getNum() != 0) { // 此项已被编号，跳出被层循环，进入下一层循环
				continue;
			} else {
				Vector vecInsert = new Vector();

				if (flagLen[i] > 0) {
					// 遍历Vector中的元素
					for (int j = i + 1, jSize = content.size(); j < jSize; j++) {
						if (content.get(j).getNum() != 0) { // 此项已被编号，跳出被层循环，进入下一层循环
							continue;
						} else {
							if (flagLen[j] > 0) {
								double d = 0;//相似度
								if(mapVector.get(i).size() < mapVector.get(j).size())
									d = getSimilarity02(mapVector.get(i),
										mapVector.get(j), i, j);
								else
									d = getSimilarity02(mapVector.get(j),
											mapVector.get(i), j, i);

								if (d > 0.90) {
									if (!vecInsert.contains(i)) {
										vecInsert.add(i);
									}
									if (!vecInsert.contains(j)) {
										vecInsert.add(j);
									}
								}
							}
						}
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

	public static double getmodular(Map<Integer, Integer> nowContent) {
		double modular = 0;
		Set<Entry<Integer, Integer>> set = nowContent.entrySet();
		Iterator<Entry<Integer, Integer>> itor = set.iterator();
		while (itor.hasNext()) {
			Entry<Integer, Integer> entry = itor.next();
			modular += Math.pow(entry.getValue(), 2);

		}

		return Math.sqrt(modular);
	}

	public static double getSimilarity02(Map<Integer, Integer> nowContent01,
			Map<Integer, Integer> nowContent02, int index01, int index02) {
		double similarity = 0;
		Iterator iter = nowContent01.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key01 = entry.getKey();
			if (nowContent02.containsKey(key01)) {
				similarity += nowContent01.get(key01) * nowContent02.get(key01);
			}
		}
		similarity /= (modular.get(index01) * modular.get(index02));

		return similarity;
	}


	public static boolean isChinese(char ch) {
		// 判读是否是汉字
		return (ch >= 0x4E00 && ch <= 0x9FA5);

	}

	/**
	 * 根据输入的Unicode字符，获取它的GB2312编码或者ASCII编码，
	 * 
	 * @param ch
	 *            输入的GB2312中文字符或者ASCII字符(128个)
	 * 
	 * @return ch 在GB2312中的位置，-1表示该字符不认识
	 */
	public static short getGB2312Id(char ch) {
		try {
			byte[] buffer = Character.toString(ch).getBytes("GB2312");
			if (buffer.length != 2) {
				// 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
				return -1;
			}

			int b0 = (int) (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
			int b1 = (int) (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
			return (short) (b0 * 94 + b1);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return -1;
	}

}
