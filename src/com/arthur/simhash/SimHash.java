/**
 * 
 */
package com.arthur.simhash;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * @author Gingber
 *
 */
public class SimHash {
	
	/**
	 * 
	 */
	public SimHash() {
		// TODO Auto-generated constructor stub
	}

	private String tokens;   
    private BigInteger intSimHash;   
    public String strSimHash;   
    private int hashbits = 64;   
    public SimHash(String tokens) throws Exception {   
        this.tokens = tokens;   
        this.intSimHash = this.SimHash2();   
    }   
  
    public SimHash(String tokens, int hashbits) throws Exception {   
        this.tokens = tokens;   
        this.hashbits = hashbits;   
        this.intSimHash = this.SimHash2();   
    }   
    public BigInteger SimHash2() throws Exception {   
        int[] v = new int[this.hashbits];   
        //StringTokenizer stringTokens = new StringTokenizer(this.tokens); 
        IKAnalyzer analyzer = new IKAnalyzer();
        //使用智能分词
        analyzer.setUseSmart(true);
        ArrayList<String> List = printAnalysisResult(analyzer, this.tokens);;
        for (int k = 0, size = List.size(); k < size; k++) {    
            BigInteger t = this.hash(List.get(k));   
            for (int i = 0; i < this.hashbits; i++) {   
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);   
                 if (t.and(bitmask).signum() != 0) {   
                    v[i] += 1;   
                } else {   
                    v[i] -= 1;   
                }   
            }   
        }   
        BigInteger fingerprint = new BigInteger("0");   
        StringBuffer simHashBuffer = new StringBuffer();   
        for (int i = 0; i < this.hashbits; i++) {   
            if (v[i] >= 0) {   
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));   
                simHashBuffer.append("1");   
            }else{   
                simHashBuffer.append("0");   
            }   
        }   
        this.strSimHash = simHashBuffer.toString();   
        //System.out.println(this.strSimHash + " length " + this.strSimHash.length());   
        return fingerprint;   
    }   
    private BigInteger hash(String source) {   
        if (source == null || source.length() == 0) {   
            return new BigInteger("0");   
        } else {   
            char[] sourceArray = source.toCharArray();   
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);   
            BigInteger m = new BigInteger("1000003");   
            BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(   
                    new BigInteger("1"));   
            for (char item : sourceArray) {   
                BigInteger temp = BigInteger.valueOf((long) item);   
                x = x.multiply(m).xor(temp).and(mask);   
            }   
            x = x.xor(new BigInteger(String.valueOf(source.length())));   
            if (x.equals(new BigInteger("-1"))) {   
                x = new BigInteger("-2");   
            }   
            return x;   
        }   
    }   
    public int hammingDistance(SimHash other) {
    	//long startTime = System.currentTimeMillis();   //获取开始时间
        BigInteger x = this.intSimHash.xor(other.intSimHash);   
        int tot = 0;        
        //统计x中二进制位数为1的个数   
        //我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，对吧，然后，n&(n-1)就相当于把后面的数字清0，   
        //我们看n能做多少次这样的操作就OK了。         
         while (x.signum() != 0) {   
            tot += 1;   
            x = x.and(x.subtract(new BigInteger("1")));   
        } 
       /* long endTime = System.currentTimeMillis(); //获取结束时间  
        System.out.println("hammingDistance： "+ (endTime-startTime) +"ms");*/
        return tot;   
    }       
    public int getDistance(String str1, String str2) {  
    	long startTime = System.currentTimeMillis();   //获取开始时间
        int distance;    
        if (str1.length() != str2.length()) {    
            distance = -1;    
        } else {    
            distance = 0;    
            for (int i = 0; i < str1.length(); i++) {    
                if (str1.charAt(i) != str2.charAt(i)) {    
                    distance++;    
                }    
            }    
        } 
        long endTime = System.currentTimeMillis(); //获取结束时间  
		System.out.println("getDistance： "+ (endTime-startTime) +"ms");
        return distance;    
    }     
    public List subByDistance(SimHash SimHash2, int distance){   
        int numEach = this.hashbits/(distance+1);   
        List characters = new ArrayList();          
        StringBuffer buffer = new StringBuffer();   
        int k = 0;   
        for( int i = 0; i < this.intSimHash.bitLength(); i++){   
            boolean sr = SimHash2.intSimHash.testBit(i);   
              
            if(sr){   
                buffer.append("1");   
            }      
            else{   
                buffer.append("0");   
            }   
              
            if( (i+1)%numEach == 0 ){   
                BigInteger eachValue = new BigInteger(buffer.toString(),2);   
                System.out.println("----" +eachValue );   
                buffer.delete(0, buffer.length());   
                characters.add(eachValue);   
            }   
        }   
        return characters;   
    }    
    public static void main(String[] args) throws Exception {   
    	String s = "新华网太原11月8日电　记者从山西省公安厅获悉，2013年11月6日7时40分至8时许，太原市迎泽大街迎泽桥东发生一起爆炸案件。案件发生后，中央领导同志作出一系列重要批示指示，要求迅速侦破案件，严惩危害公共安全的暴力犯罪。公安部派出一名副部长带队的专家组赶赴现场，指导破案。山西省、太原市两级公安机关迅速成立专案组，在兄弟省市公安机关的支持下，全力开展侦破。8日凌晨2时，该案成功告破，犯罪嫌疑人丰志均在太原被抓捕归案。" +
    			"警方初步查明，犯罪嫌疑人丰志均，男，41岁，太原市杏花岭区人，曾因盗窃被判处有期徒刑9年。警方从其住处缴获了私自制造的爆炸装置，发现了大量犯罪证据，并查获了其作案时驾驶的车辆。犯罪嫌疑人丰志均对犯罪事实供认不讳。目前，相关审讯和调查取证等工作正在紧张进行。" +
    			"另据中新网11月8日电 据新华社微博“新华视点”消息，8日凌晨2时，山西太原“11•06”爆炸案件告破，犯罪嫌疑人丰志均在太原被抓捕归案。丰志均，男";   
        SimHash hash1 = new SimHash(s, 64);   
        //System.out.println(hash1.intSimHash + "  " + hash1.intSimHash.bitLength());         
        //hash1.subByDistance(hash1, 3);     
        s = "近年来，上海坚决贯彻国家各项房地产调控政策措施，采取差别化住房信贷税收、住房限购、增加土地供应等综合措施，抑制投资投机性需求，调控取得积极成效，市场运行总体平稳。2011年、2012年本市新建商品住房价格指数全年涨幅分别为2%和0。今年以来，住房价格波动较为明显，近期随着交易量上升，房价上涨压力加大。对近期房价持续上涨，上海市委、市政府高度重视，近日市政府常务会议进行了专题研究，会议明确本市将进一步按照“以居住为主、以市民消费为主、以普通商品住房为主”的原则，不断完善房地产市场体系和住房保障体系，有效抑制房地产市场价格过快上涨，会议明确了进一步严格执行国家房地产市场调控政策的相关措施。";   
        SimHash hash2 = new SimHash(s, 64);   
        //System.out.println(hash2.intSimHash+ "  " + hash2.intSimHash.bitCount());   
       // hash1.subByDistance(hash2, 3);   
        s = "进一步完善“四位一体”、租售并举的住房保障体系。对符合条件的廉租住房申请家庭实行“应保尽保”、“愿配尽配”；加快共有产权保障住房申请供应，扩大受益家庭规模；推动公共租赁住房建设筹措和分配供应，尽力满足保障对象租赁需求；继续做好征收安置住房搭桥供应，确保旧区改造家庭用房需求。参照物价、收入等因素，研究明年放宽廉租住房和共有产权保障住房准入标准，努力扩大住房保障受益面。";   
        SimHash hash3 = new SimHash(s, 64);   
        //System.out.println(hash3.intSimHash+ "  " + hash3.intSimHash.bitCount());   
        //hash1.subByDistance(hash3, 3);   
        System.out.println("============================");   
        int dis = hash1.getDistance(hash1.strSimHash,hash2.strSimHash);         
        System.out.println(hash1.hammingDistance(hash2) + " "+ dis);        
        int dis2 = hash1.getDistance(hash1.strSimHash,hash3.strSimHash);   
        System.out.println(hash1.hammingDistance(hash3) + " " + dis2);   
    }
    
    /**
     * 打印出给定分词器的分词结果
     * @param analyzer 分词器
     * @param keyWord 关键词
     * @throws Exception
    */
    private static ArrayList<String> printAnalysisResult(Analyzer analyzer, String keyWord) throws Exception {
    	ArrayList<String> List = new ArrayList();
    	//System.out.println("["+keyWord+"]分词效果如下");  
    	TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(keyWord));
    	tokenStream.addAttribute(CharTermAttribute.class);
    	while (tokenStream.incrementToken()) {  
    		CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
    		List.add(charTermAttribute.toString());
    		//System.out.println(charTermAttribute.toString());  
        }
    	
    	return List;
    }
}
