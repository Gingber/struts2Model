/**
 * 
 */
package com.iie.rtp;

import java.util.Vector;

/**
 * @author Gingber
 *
 */
public class SpecTopic {

	/**
	 * 
	 */
	public SpecTopic() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Vector<Content> ChooseTopic(Vector<Content> content) {
		
		Vector<Content> SpecContent = new Vector<Content>();
		
		for (int i = 0, size = content.size(); i < size; i++) {
			String title = content.get(i).getTitle();
			if (title.contains("±¡°¸") | title.contains("±¡ÎõÀ´")) {
				SpecContent.add((Content)(content.get(i)));				
			}
			
		}
		//System.out.println("SpecContent = " + SpecContent.size());
		
		return SpecContent;
		
	}
	

}
