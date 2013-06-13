package Indexing;
import BPTree.BPTree;
import GUI.SimpleGUI;
import Query.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.SortedMap;

import minidbms.Table;

public class hashDatabase implements Serializable
{
      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	public HashMap<String,Table_obj_index> tables_hashed = new HashMap<String,Table_obj_index>();
	
	public hashDatabase() throws Exception{
		for(String key : SimpleGUI.d.tables.keySet()) {
			  //System.out.println(key);
			
			  tables_hashed.put(key.toUpperCase(), new Table_obj_index(key));
			  
		}
		
	/*	
	    //testing bpt
		
		BPTree<Integer,  BPTree<Integer, String[]>> temp=tables_hashed.get("BOOK").bptList[0];
		
	//	String keyStart=new String("1");
		
    	SortedMap<Integer,  BPTree<Integer, String[]>> subMap=temp.subMap(10, 100);
     	System.out.println("size: "+subMap.size());
		for(Integer key : subMap.keySet()) {
			BPTree<Integer, String[]> resultBpt=subMap.get(key);
			for(Integer keybpt : resultBpt.keySet()) {
				String[] tempStr=resultBpt.get(keybpt);
			    if(tempStr==null){
			    	System.out.println("null");
			    }
			    System.out.print(keybpt+" result: ");
		    	for(int i=0;i<tempStr.length;i++){
		    	   System.out.print(tempStr[i]+"  ");
		    	}
		    	System.out.println();
			}
		}
     	
		System.out.println("First Key: "+temp.firstKey());
		System.out.println("Last Key: "+temp.lastKey());
		System.out.println("size: "+temp.size());
		if(temp.containsKey(19999)){
			System.out.println("has 19999");
		}
	
	
	*/
		
		//tessting bpt
		/*
		BPTree<String,  BPTree<String, String[]>> temp=tables_hashed.get("BOOK").bptList[0];
		System.out.println("First Key: "+temp.firstKey());
		String key1=new String("Bible");
		BPTree<String, String[]> resultBpt=temp.get((Object)key1);
		for(String key : resultBpt.keySet()) {
			String[] tempStr=resultBpt.get(key);
		    if(tempStr==null){
		    	System.out.println("null");
		    }
		    System.out.println("result:");
	    	for(int i=0;i<tempStr.length;i++){
	    	   System.out.println(tempStr[i]);
	    	}
		}*/
		

		//tessting hashMap
	/*	HashMap<String, HashMap<String, String[]>> temp=tables_hashed.get("BOOK").hashList[1];
		
		String key1=new String("Bible");
		 HashMap<String, String[]> resultHash=temp.get(key1);
		
		for(String key : resultHash.keySet()) {
			String[] tempStr=resultHash.get(key);
		    if(tempStr==null){
		    	System.out.println("null");
		    }
		    System.out.println("result:");
	    	for(int i=0;i<tempStr.length;i++){
	    	   System.out.println(tempStr[i]);
	    	}
		}
		*/
		
		
	}
	

	
}
