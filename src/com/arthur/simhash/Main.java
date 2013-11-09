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
		String str1 = "二奶干了卧底的活，网友干了侦探的活，微博干了媒体的活，干爹干了男友的活！ 。";
		String str2 = "我是中国人, 我的中国心，草拟大爷， 美帝国主义，阿凡达上课，你发可能发生的";
		
		SimHash sim = new SimHash();
		//compare 6 and 8 for test.
		//sim.compareString(String.valueOf(6), String.valueOf(8));
		//System.out.println();
		
		StringReader reader1 = new StringReader(str1);
    	//IKSegmentation是一个lucene无关的通用分词器   
    	IKSegmenter seg1 = new IKSegmenter(reader1, true);
    	   
    	Lexeme lex1 = null, lex2 = null;
    	   
    	StringBuilder sb1 = new StringBuilder();
    	while ((lex1 = seg1.next()) != null) { //seg.next()获取下一个语义单元
    		//System.out.print(lex1.getLexemeText() + " ");////通过语义单元（词元），获取词元内容
    		sb1.append(lex1.getLexemeText());
    		sb1.append(" ");
    	}
    	
    	//System.out.print("---------------------------------------\n");
    	
    	StringReader reader2 = new StringReader(str2);
    	//IKSegmentation是一个lucene无关的通用分词器   
    	IKSegmenter seg2 = new IKSegmenter(reader2, true);
    	
    	StringBuilder sb2 = new StringBuilder();
    	while ((lex2 = seg2.next()) != null) { //seg.next()获取下一个语义单元
    		//System.out.print(lex2.getLexemeText() + " ");////通过语义单元（词元），获取词元内容
    		sb2.append(lex2.getLexemeText());
    		sb2.append(" ");
    	}
		
		//compare str1 and str2 for test.
		sim.compareString(str1, str2);
		
		
    	
	
	}
}
