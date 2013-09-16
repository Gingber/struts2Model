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

// TreeMap�������
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

	static Map<Integer, Double> modular = new HashMap<Integer, Double>();// ��һ���ĵ�
	static Map<Integer, Map<Integer, Integer>> mapVector = new HashMap<Integer, Map<Integer, Integer>>();
	static String stopChar = "��,��,��,��,�� ,�� ,��,��,��,��,��,��,Ҳ,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,�� ,��,��,��,��,��," +
			"��,��,ʹ,��,ȴ,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��," +
			"��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,��,ð,ô,ÿ,��,ĳ,��,��,��,��,��,��,��,��,����Ŷ,Ż," +
			"��,ƾ,��,��,��,��,��,ɶ,˭,˳,��,��,��,��,��,��,��,��,��,ͬ,��,��,��,Ϊ,ι,��,��,��,��,�� ,��,��,ѽ,��,��,Ҫ,��,��,��,��,Ӵ,��,��," +
			"��,��,��,��,��,��,��,զ,��,��,��,֨ ,֮,��,��,��,��,��,��,��,��,��,��,��,��,��";
	

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
		
		long startTime1 = System.currentTimeMillis();   //��ȡ��ʼʱ��

		int chnum = 0; // ���ָ���ͳ��
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

		System.out.println("�ֵ��С��" + bagmap.size());

		int num = 1;
		// <<<<<<<<<<<<<<<<���������ĵ�����������������ȡģ-----------------------
		int[] flagLen = new int[content.size()];// ��ʶ���������Ƿ���������(len > 10),�������
		for (int i = 0, size = content.size(); i < size; i++) {
			/*String doc1 = content.get(i).getTitle();// ��i����¼
			int len1 = StrLength(doc1);*/
			Map<Integer, Integer> nowContent = new HashMap<Integer, Integer>();// ��ǰ�ĵ�
			
			// <<<<<<<<<<<<<<<<-------------new idea(2013-09-13)----------------------------------
			String doc1 = content.get(i).getTitle().replaceAll("[^\u4E00-\u9FA5]", "");// ��i����¼
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
				if (i > 0)// �ӵ�1����¼�����0����¼�������ƶȼ���
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
					if (vecInsert != null && vecInsert.size() > 0) { // �����ظ���
						for (int j = vecInsert.size() - 1; j >= 0; j--) {
							int index = Integer.parseInt(vecInsert.get(j)
									.toString());
							// System.out.println(content.get(index).getMessageId()
							// + "\t" + content.get(index).getTitle());
							content.get(index).setNum(num);
						}
					} else { // ���ظ����һ��
						content.get(0).setNum(num);
					}
				}

			}
			
		}
		num++; // ����������

		// --------------------���������ĵ�����������������ȡģ>>>>>>>>>>>>>>>>>>>

		// <<<<<<<<<<<<<<<<���������ĵ��������Ƚϲ������������ƶ�-----------------------
		for (int i = 1, iSize = content.size(); i < iSize; i++) {
			if (i == content.size() - 1 && content.get(i).getNum() == 0) { // �ѱ�ǵ������
				content.get(i).setNum(num);
			} else if (content.get(i).getNum() != 0) { // �����ѱ���ţ���������ѭ����������һ��ѭ��
				continue;
			} else {
				Vector vecInsert = new Vector();

				if (flagLen[i] > 0) {
					// ����Vector�е�Ԫ��
					for (int j = i + 1, jSize = content.size(); j < jSize; j++) {
						if (content.get(j).getNum() != 0) { // �����ѱ���ţ���������ѭ����������һ��ѭ��
							continue;
						} else {
							if (flagLen[j] > 0) {
								double d = 0;//���ƶ�
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

				if (vecInsert != null && vecInsert.size() > 0) { // �����ظ���
					for (int j = vecInsert.size() - 1; j >= 0; j--) {
						int index = Integer.parseInt(vecInsert.get(j)
								.toString());
						// System.out.println(content.get(index).getMessageId()
						// + "\t" + content.get(index).getTitle());
						content.get(index).setNum(num);
					}
				} else { // ���ظ����һ��
					content.get(i).setNum(num);
				}
			}
			num++; // ����������
		}
		// --------------------���������ĵ��������Ƚϲ������������ƶ�>>>>>>>>>>>>>>>>>>>
		
		long endTime1 = System.currentTimeMillis();
		System.out.println("�����������ƶȼ���ʱ�䣺 "+ (endTime1-startTime1) +"ms");
		
		long startTime2 = System.currentTimeMillis();   //��ȡ��ʼʱ��
		// ���շ�����num��������
		Comparator comp = new ComparatorImpl();
		Collections.sort(content, comp);
		
		long endTime2 = System.currentTimeMillis();
		System.out.println("�����������ʱ�䣺 "+ (endTime2-startTime2) +"ms");

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
		// �ж��Ƿ��Ǻ���
		return (ch >= 0x4E00 && ch <= 0x9FA5);

	}

	/**
	 * ���������Unicode�ַ�����ȡ����GB2312�������ASCII���룬
	 * 
	 * @param ch
	 *            �����GB2312�����ַ�����ASCII�ַ�(128��)
	 * 
	 * @return ch ��GB2312�е�λ�ã�-1��ʾ���ַ�����ʶ
	 */
	public static short getGB2312Id(char ch) {
		try {
			byte[] buffer = Character.toString(ch).getBytes("GB2312");
			if (buffer.length != 2) {
				// ���������bufferӦ���������ֽڣ�����˵��ch������GB2312���룬�ʷ���'?'����ʱ˵������ʶ���ַ�
				return -1;
			}

			int b0 = (int) (buffer[0] & 0x0FF) - 161; // �����A1��ʼ����˼�ȥ0xA1=161
			int b1 = (int) (buffer[1] & 0x0FF) - 161; // ��һ���ַ������һ���ַ�û�к��֣����ÿ����ֻ��16*6-2=94������
			return (short) (b0 * 94 + b1);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return -1;
	}

}
