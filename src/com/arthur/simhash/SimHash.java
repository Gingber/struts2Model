package com.arthur.simhash;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class SimHash {
	
	private static int HASH_LENGTH = 256;
	
	public interface CityHash extends Library {
	     /**
	      * ��ǰ·��������Ŀ�£�������bin���Ŀ¼�¡�
	      */
	     CityHash INSTANCE = (CityHash)Native.loadLibrary("CityHash", CityHash.class);
	     public int CityHash64(byte str[], int strlen);
	}
	
	/**
	 * In this method, use int(32 bits) to store hashcode.
	 * use 1 as all words weight for simple reason.
	 * use Hamming distance as hashcode distance.
	 * 
	 * @author arthur503
	 */
	public SimHash(){
		
	}
	
	public int compareString(String str1, String str2) throws IOException{
		//System.out.println("SimHash compare string of: \""+str1+"\" AND \""+str2+"\"");
		int hash1 = getHashCode(str1);
		int hash2 = getHashCode(str2);
		
		int distance = getDistance(hash1, hash2);
		System.out.println("SimHash string distance of: \""+str1+"\" AND \""+str2+"\" is:"+distance);
		return distance;
	}

	/**
	 * Use hamming distance in this method.
	 * Can change to other distance like Euclid distance or p-distance, etc.
	 * @param hash1
	 * @param hash2
	 * @return
	 */
	public static int getDistance(int hash1, int hash2) {
		// TODO Auto-generated method stub
		
		/*BigInteger x = Integer.toBinaryString(hash1).xor(Integer.toBinaryString(hash2));   
        int tot = 0;        
        //ͳ��x�ж�����λ��Ϊ1�ĸ���   
        //�������룬һ������������ȥ1����ô��������Ǹ�1�������Ǹ�1�����������ȫ�����ˣ��԰ɣ�Ȼ��n&(n-1)���൱�ڰѺ����������0��   
        //���ǿ�n�������ٴ������Ĳ�����OK�ˡ�         
         while (x.signum() != 0) {   
            tot += 1;   
            x = x.and(x.subtract(new BigInteger("1")));   
        } 
        long endTime = System.currentTimeMillis(); //��ȡ����ʱ��  
        System.out.println("hammingDistance�� "+ (endTime-startTime) +"ms");
        return tot;*/
		
		
		int distance = 0;
		int i = 0;
		for(i=0;i<HASH_LENGTH;i++){
			int bit1 = hash1 & (1 << i);
			int bit2 = hash2 & (1 << i);
			if(bit1 != bit2){
				distance ++;
				if(distance > 3) 
					break;
			}
		}
//		System.out.println("Distance of hash1 and hash2 is:"+distance);
		if (i < HASH_LENGTH)
			return 4;
		else
			return distance;
	}
	
	public static final String CleaningDate(String title) {  
		
		// �޳��ַ����������ַ������������֡���ĸ�ͺ��ֵ�
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]";
		Pattern   p  =  Pattern.compile(regEx);   
		Matcher   m  =  p.matcher(title); 
		
		int counter = 0;
        char[] ch = m.replaceAll("").trim().toCharArray();
        StringBuilder sb  =  new StringBuilder();
        for (int i = 0; i < ch.length; i++) { 
            char c = ch[i];  
            if (c >= 0x4e00 && c <= 0x9fa5) {  
            	++counter;
            	sb.append(c);
            }  
        }  
        return sb.toString();  
    }  

	public int getHashCode(String str) throws IOException {
		// TODO Auto-generated method stub
		String clndata = CleaningDate(str);
		StringReader reader = new StringReader(clndata);
    	//IKSegmentation��һ��lucene�޹ص�ͨ�÷ִ���   
    	IKSegmenter seg = new IKSegmenter(reader, true); 
    	Lexeme lex = null;
    	   
    	StringBuilder sb = new StringBuilder();
    	while ((lex = seg.next()) != null) { //seg.next()��ȡ��һ�����嵥Ԫ
    		//System.out.print(lex.getLexemeText() + " ");////ͨ�����嵥Ԫ����Ԫ������ȡ��Ԫ����
    		sb.append(lex.getLexemeText());
    		sb.append(" ");
    	}
		
		int result = 0;

		//Step One: Expand.
		String[] array = sb.toString().split(" ");
		//System.out.println("array length:"+array.length+". array0 is:"+array[0]);
		int hash = 0;
		int[] hashBits = new int[HASH_LENGTH];
		for(int i=0;i<array.length;i++){
			hash = array[i].hashCode();
			//hash = CityHash.INSTANCE.CityHash64(array[i].getBytes(), array[i].length());
			//System.out.println("String \""+array[i]+"\""+" hashcode is:"+Integer.toBinaryString(hash));
			for(int j=HASH_LENGTH-1;j>=0;j--){
				int bit = (hash >> j) & 1;
				//System.out.println("shift j is:"+j+" bit is:"+bit);
				
				//Different keyword may have different weight. add or minus their weight here.
				//For simple reason, all weight are assigned as 1 in this method.
				if(bit == 1){
					hashBits[HASH_LENGTH-1-j]++; 
				}else{
					hashBits[HASH_LENGTH-1-j]--;
				}			
				
			}
			
			//print hashbits for debug.
/*			System.out.println("hashbits is:");
			for(int k=0;k<HASH_LENGTH;k++){
				System.out.println("k="+k+" "+hashBits[k]);
			}*/
		}
		
		//Step Two: Shrink.
		for(int i=0;i<HASH_LENGTH;i++){
			int bit = hashBits[i] > 0 ? 1 : 0;
			if(bit == 1){
				result |= 1 << (HASH_LENGTH-1-i);
			}
		}
		/*System.out.println("String \""+str+ "\" hashcode is:"+result
				+". Binary format is: "+Integer.toBinaryString(result));*/
		return result;
	}
	
	

}
