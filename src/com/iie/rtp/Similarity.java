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
	public Vector<Content> SimilarityCluster(Vector<Content> content) throws IOException {
		
		long startTime1 = System.currentTimeMillis();   //��ȡ��ʼʱ��

		Comparator fgpcomp = new fgpComparatorImplementation();
		Collections.sort(content, fgpcomp);
		

		int num = 1;
		// <<<<<<<<<<<<<<<<���������ĵ��������Ƚϲ������������ƶ�-----------------------
		for (int i = 0, iSize = content.size(); i < iSize; i++) {
			if (i == content.size() - 1 && content.get(i).getNum() == 0) { // �ѱ�ǵ������
				content.get(i).setNum(num);
			} else if (content.get(i).getNum() != 0) { // �����ѱ���ţ���������ѭ����������һ��ѭ��
				continue;
			} else {
				Vector vecInsert = new Vector();
	    		// ����Vector�е�Ԫ��
				for (int j = i + 1, jSize = content.size(); j < jSize; j++) {
					if (content.get(j).getNum() != 0) { // �����ѱ���ţ���������ѭ����������һ��ѭ��
						continue;
					} else {
			    		int d = SimHash.getDistance(content.get(i).getFingerprint(), content.get(j).getFingerprint());
			    		
			    		//System.out.println(i + " -> " + j + "\t" + d);
						if (d <= 3) { //���ƶ�����
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

