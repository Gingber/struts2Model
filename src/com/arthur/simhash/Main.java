package com.arthur.simhash;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Main {

	/**
	 * @author arthur503
	 * @param argv
	 * @throws IOException 
	 */
	public static void main(String[] argv) throws IOException{
		String str1 = "���̸����Ե׵Ļ���Ѹ�����̽�Ļ΢������ý��Ļ�ɵ��������ѵĻ ��";
		String str2 = "�����й���, �ҵ��й��ģ������ү�� ���۹����壬�������ϿΣ��㷢���ܷ�����";
		
		SimHash sim = new SimHash();
		//compare 6 and 8 for test.
		//sim.compareString(String.valueOf(6), String.valueOf(8));
		//System.out.println();
		
		StringReader reader1 = new StringReader(str1);
    	//IKSegmentation��һ��lucene�޹ص�ͨ�÷ִ���   
    	IKSegmenter seg1 = new IKSegmenter(reader1, true);
    	   
    	Lexeme lex1 = null, lex2 = null;
    	   
    	StringBuilder sb1 = new StringBuilder();
    	while ((lex1 = seg1.next()) != null) { //seg.next()��ȡ��һ�����嵥Ԫ
    		//System.out.print(lex1.getLexemeText() + " ");////ͨ�����嵥Ԫ����Ԫ������ȡ��Ԫ����
    		sb1.append(lex1.getLexemeText());
    		sb1.append(" ");
    	}
    	
    	//System.out.print("---------------------------------------\n");
    	
    	StringReader reader2 = new StringReader(str2);
    	//IKSegmentation��һ��lucene�޹ص�ͨ�÷ִ���   
    	IKSegmenter seg2 = new IKSegmenter(reader2, true);
    	
    	StringBuilder sb2 = new StringBuilder();
    	while ((lex2 = seg2.next()) != null) { //seg.next()��ȡ��һ�����嵥Ԫ
    		//System.out.print(lex2.getLexemeText() + " ");////ͨ�����嵥Ԫ����Ԫ������ȡ��Ԫ����
    		sb2.append(lex2.getLexemeText());
    		sb2.append(" ");
    	}
		
		//compare str1 and str2 for test.
		sim.compareString(str1, str2);
		
		
    	
	
	}
}
