package Indexing;

import java.io.FileWriter;
import Query.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import BPTree.BPTree;
import java.util.HashMap;

import minidbms.ColumnDef;
import minidbms.Database;
import minidbms.Table;

import GUI.SimpleGUI;

public class Table_obj_index implements Serializable
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;			
	/**
	 * 
	 */
	String tname; //table name
	Table rawTable;
	
	public  HashMap<String,HashMap<String,String[]>>[] hashList=null;
	public  BPTree<Integer, BPTree<Integer, String[]>>[] bptList=null;
	//public  BPTree<String, BPTree<String, String[]>>[] bptList_str=null;
	
	//PrintWriter out = new PrintWriter(new FileWriter("ContainKey.txt"));
	
	//a table of all different hash type of eaach attribute.
	
	int num_Attri=0;
	
	
	public Table_obj_index(String tableName) throws Exception{
		tname=tableName;
		rawTable=getTableFromOld(SimpleGUI.d);
		num_Attri= getNumAttr();
		hashList=new HashMap[num_Attri];
		bptList=new BPTree[num_Attri];
	//	bptList_str=new BPTree[num_Attri];
		setBPtree();
		setHashMap();
		
		
	}
	
	public void setBPtree() throws Exception{
		int PriIndex=-1;
		for(int i=0;i<rawTable.cdata.length;i++){
			if(rawTable.cdata[i].cpk==true){
				PriIndex=i;
				
			}
		}
		
		for(int i=0;i<rawTable.cdata.length;i++){
			if(rawTable.cdata[i].ctype.equalsIgnoreCase("int")){
				if(PriIndex!=-1&&rawTable.argument_type_in_schema(rawTable.cdata[PriIndex].cname).equalsIgnoreCase("int")){
					setBpt_of_specific_attr(i,PriIndex);
				}else{
					setBpt_of_specific_attr(i,-1);
				}
				
				
			}
			//setBpt_of_specific_attr_str(i,PriIndex);
			
		}
		
		
	}
	
	public void setHashMap(){
		
		int PriIndex=-1;
		for(int i=0;i<rawTable.cdata.length;i++){
			if(rawTable.cdata[i].cpk==true){
				PriIndex=i;
			}
		}
		for(int i=0;i<rawTable.cdata.length;i++){
			setHashMap_of_specific_attr(i,PriIndex);
		}
		
		
	}
	
	public Table getTableFromOld(Database d){
		if(d.tables.get(tname)!=null){
			
		  return (Table)d.tables.get(tname);
		}
		else
		  return null;
	}
	
	public int getNumAttr(){
		return getTableFromOld(SimpleGUI.d).cdata.length;
	}
  
	public void setBpt_of_specific_attr( int index,int PriIndex) throws IOException{
		
	
		BPTree<Integer, BPTree<Integer, String[]>> bpt=new BPTree<Integer, BPTree<Integer, String[]>>();
	
		bpt.clear();
		
	//	int i=0;
	
		
		
		for(Integer key : rawTable.rdata.keySet()) {
			  String [] temp=rawTable.rdata.get(key);
			//  System.out.println(temp[index]);
			  Integer hashKey;
			  if(temp[index]==null){
				  hashKey=0;
			  }else{
				  hashKey=Integer.parseInt(temp[index]);
			  }
	
			  if(bpt.containsKey(hashKey)){
			//	  out.println(temp[index]+" contain key");
				  BPTree<Integer, String[]> oldBpt= bpt.get(hashKey);
				  if(PriIndex==-1){
					  oldBpt.put(oldBpt.size(),  temp);
				  }else{
					  oldBpt.put(Integer.parseInt(temp[PriIndex]),  temp);
				  }
				  
				  bpt.put(hashKey,oldBpt);
			  }else{
				//  out.println(temp[index]+" Not contain key");
				  BPTree<Integer, String[]> newBpt=new BPTree<Integer, String[]>();
				  if(PriIndex==-1){
					  newBpt.put(newBpt.size(),  temp);
				  }else{
					  newBpt.put(Integer.parseInt(temp[PriIndex]),  temp);
					 
				  }
				  
				  bpt.put(hashKey,newBpt);
				  
				  //System.out.println(temp[secondaryIndex]);
			  }
			  
		//	  i++;
			  
			  
		}
		
		//out.close();
		//System.out.println(i);
	//	System.out.println("Obj: "+bpt.toString());
		bptList[index]=bpt;
	
		//System.out.println("here");
		
	}
	
	/*public void setBpt_of_specific_attr_str( int index,int PriIndex) throws IOException{
		
		BPTree<String, BPTree<String, String[]>> bpt=new BPTree<String, BPTree<String, String[]>>();
	
		bpt.clear();
		
	//	int i=0;
	
		
		
		for(Integer key : rawTable.rdata.keySet()) {
			  String [] temp=rawTable.rdata.get(key);
			//  System.out.println(temp[index]);
			  String hashKey;
			  if(temp[index]==null){
				  hashKey="null";
			  }else{
				  hashKey=temp[index];
			  }
			  
			  if(bpt.containsKey(hashKey)){
			//	  out.println(temp[index]+" contain key");
				  BPTree<String, String[]> oldBpt= bpt.get(hashKey);
				  if(PriIndex==-1){
					  Integer tempInt=new Integer(oldBpt.size());
					  oldBpt.put(tempInt.toString(),  temp);
				  }else{
					  oldBpt.put(temp[PriIndex],  temp);
				  }
		
				  bpt.put(hashKey,oldBpt);
			  }else{
				//  out.println(temp[index]+" Not contain key");
				  BPTree<String, String[]> newBpt=new BPTree<String, String[]>();
				  if(PriIndex==-1){
					  Integer tempInt=new Integer(newBpt.size());
					  newBpt.put(tempInt.toString(),  temp);
				  }else{
					  newBpt.put(temp[PriIndex],  temp);
					 
				  }
			
				  bpt.put(hashKey,newBpt);
				  
				  //System.out.println(temp[secondaryIndex]);
			  }
			  
		//	  i++;
			  
			  
		}
		
		//out.close();
		//System.out.println(i);
	//	System.out.println("Obj: "+bpt.toString());
		bptList_str[index]=bpt;
	
		//System.out.println("here");
		
	}*/
	
	
	public void setHashMap_of_specific_attr( int index,int PriIndex){
	
		HashMap<String, HashMap<String, String[]>> hash=new HashMap<String, HashMap<String, String[]>>();
	
		hash.clear();
		
		for(Integer key : rawTable.rdata.keySet()) {
			  String [] temp=rawTable.rdata.get(key);
			//  System.out.println(temp[index]);
			  String hashKey;
			  if(temp[index]==null){
				  hashKey="null";
			  }else{
				  hashKey=temp[index];
			  }
			  if(hash.containsKey(hashKey)){
				  HashMap<String, String[]> oldHash= hash.get(hashKey);
				  if(PriIndex==-1){
					  Integer tempInt=new Integer(oldHash.size());
					  oldHash.put(tempInt.toString(),  temp);
				  }else{
					  oldHash.put(temp[PriIndex],  temp);
				  }
				
				  hash.put(hashKey,oldHash);
			  }else{
				  HashMap<String, String[]> newHash=new  HashMap<String, String[]>();
				  if(PriIndex==-1){
					  Integer tempInt=new Integer(newHash.size());
					  newHash.put(tempInt.toString(),  temp);
				  }else{
					  newHash.put(temp[PriIndex],  temp);
					 
				  }
				 
				  hash.put(hashKey,newHash);
			  }
			  
			  
			  
		}
		//System.out.println("Obj: "+hash.toString());
		hashList[index]=hash;
	
		//System.out.println("here");
		
	}
	
	public int getIndexOfAtrri(String attr){
		//System.out.println(rawTable.tname);
		for(int i=0;i<rawTable.cdata.length;i++){
			if(attr.equalsIgnoreCase(rawTable.cdata[i].cname))
				return i;
		}
		return -1;
	}
	
	public String getTypeOfAtrri_index(int index){
		return rawTable.cdata[index].ctype;
	}
    

}