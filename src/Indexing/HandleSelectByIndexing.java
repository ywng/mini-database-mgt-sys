package Indexing;

import java.util.HashMap;
import java.util.SortedMap;

import minidbms.Table;

import Query.*;
import BPTree.BPTree;
import GUI.*;

public class HandleSelectByIndexing {
	
	public static Result select(selectItem[] selectList, fromItem[] fromList, whereItem where) throws Exception{
		
		Result result = null;
		
		//Case by Case handling 
		
		//One table case
		if (fromList.length==1){
			if(where.con[1]==null){//one condition case
			//	System.out.println("in 1T1C");
				result=select1T1C(selectList,fromList,where);
			}else{//2 condition case
				///System.out.println("in 1T2C");
				result=select1T2C(selectList,fromList,where);
			}
			
		}else{
			if(where.con[1]==null){//one condition case
			//	System.out.println("in 2T1C");
				result=select2T1C(selectList,fromList,where);
			}else{//2 condition case
				//System.out.println("in 2T2C");
				try {
					result=select2T2C(selectList,fromList,where);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return result;
		
	}
	
	
	
	public static Result select1T1C(selectItem[] selectList, fromItem[] fromList, whereItem where){
	
		
		//get the single table
		Table_obj_index table=SimpleGUI.d_hash.tables_hashed.get(fromList[0].main.toUpperCase() );
		Result result;
		if(fromList[0].sub==null){
			result=new Result(selectList,table.rawTable.cdata,null,table.tname,null,null,null);
		}else{
			result=new Result(selectList,table.rawTable.cdata,null,table.tname,null,fromList[0].sub,null);
		}
		
		
		String attr_processed;
		if(where.con[0].LOperand.sub==null){
			attr_processed=where.con[0].LOperand.main;
		}else{
			attr_processed=where.con[0].LOperand.sub;
		}
		//System.out.print(attr_processed);
		int index_attr=table.getIndexOfAtrri(attr_processed);
		//System.out.print(attr_processed+" "+index_attr);
		String attr_type=table.getTypeOfAtrri_index(index_attr);
		
		
		String key;
		if(attr_type.equalsIgnoreCase("int")){
			key=where.con[0].ROperand.main;
		}else{
			key=where.con[0].ROperand.main.substring(1, where.con[0].ROperand.main.length()-1);
		}
		
		if(where.con[0].logicOp.equalsIgnoreCase("=")||where.con[0].logicOp.equalsIgnoreCase("<>") ){//doing equaling
			HashMap<String, HashMap<String, String[]>> index_selected=table.hashList[index_attr];
			if(where.con[0].logicOp.equalsIgnoreCase("=")){
		
				
				HashMap<String, String[]> re=index_selected.get(key);
				int i=0;
				if(re!=null){
					for(String key_re:re.keySet()){
						result.resultTable1.put(i, re.get(key_re));
						i++;	
					}
				}else
					result.resultTable1=null;
			}else{
				HashMap<String, HashMap<String, String[]>> re= index_selected;
				
			
				int i=0;
				for(String keyRe:re.keySet()){
					if(keyRe.equals(key)==false){
						HashMap<String, String[]> reTemp=re.get(keyRe);
						for(String key2:reTemp.keySet()){
							
							result.resultTable1.put(i, reTemp.get(key2));
							i++;	
					
						}
					}
			
				
					
				}
					
				
				
			}
			

		
	
			
		}else{
		
			if(attr_type.equalsIgnoreCase("int")){
				BPTree<Integer, BPTree<Integer, String[]>> index_selected=table.bptList[index_attr];
				SortedMap<Integer,  BPTree<Integer, String[]>> subMap = null;
				if(where.con[0].logicOp.equalsIgnoreCase(">")){
					
					subMap=index_selected.tailMap(Integer.parseInt(where.con[0].ROperand.main));
				
				}else if(where.con[0].logicOp.equalsIgnoreCase("<")){//inclusive and exculsive problem
					subMap=index_selected.headMap(Integer.parseInt(where.con[0].ROperand.main) );
				}
				
				int i=0;
				for(Integer keyRe : subMap.keySet()) {
					BPTree<Integer, String[]> resultBpt=subMap.get(keyRe);
					for(Integer keybpt : resultBpt.keySet()) {
						String[] tempStr=resultBpt.get(keybpt);
					   // if(tempStr==null){
					   // 	System.out.println("null");
					  //  }
					    if(where.con[0].logicOp.equalsIgnoreCase(">")){
					    	if(keyRe!=Integer.parseInt(where.con[0].ROperand.main))
					    		result.resultTable1.put(i,tempStr);
					    }else if(where.con[0].logicOp.equalsIgnoreCase("<")){
					    		result.resultTable1.put(i,tempStr);
					    }
					    
					    i++;
					    /*System.out.print(keybpt+" result: ");
				    	for(int i=0;i<tempStr.length;i++){
				    	   System.out.print(tempStr[i]+"  ");
				    	}
				    	System.out.println();*/
					}
				}
				
			}else{
				//no String >  or  < !!!
				
			}
			
			
			
		}
	
		
		
		return result;
	}
	
	public static Result select1T2C(selectItem[] selectList, fromItem[] fromList, whereItem where){
		
		conditionItem first;
		conditionItem second;
		if(SimpleGUI.indexingMode==0){
			first=where.con[1];
			second=where.con[0];
		}else{
			first=where.con[0];
			second=where.con[1];
		}
		
		//get the single table
		Table_obj_index table=SimpleGUI.d_hash.tables_hashed.get(fromList[0].main.toUpperCase() );
		Result result;
		if(fromList[0].sub==null){
			result=new Result(selectList,table.rawTable.cdata,null,table.tname,null,null,null);
		}else{
			result=new Result(selectList,table.rawTable.cdata,null,table.tname,null,fromList[0].sub,null);
		}
		
		String attr_processed_first;
		String attr_processed_second;
		if(first.LOperand.sub==null){
			attr_processed_first=first.LOperand.main;
		}else{
			attr_processed_first=first.LOperand.sub;
		}
		if(second.LOperand.sub==null){
			attr_processed_second=second.LOperand.main;
		}else{
			attr_processed_second=second.LOperand.sub;
		}
	
		int index_attr_first=table.getIndexOfAtrri(attr_processed_first);
		String attr_type_first=table.getTypeOfAtrri_index(index_attr_first);
		int index_attr_second=table.getIndexOfAtrri(attr_processed_second);
		String attr_type_second=table.getTypeOfAtrri_index(index_attr_second);
		
		
		String key_first;
		if(attr_type_first.equalsIgnoreCase("int")){
			key_first=first.ROperand.main;
		}else{
			key_first=first.ROperand.main.substring(1, first.ROperand.main.length()-1);
		}
		String key_second;
		if(attr_type_second.equalsIgnoreCase("int")){
			key_second=second.ROperand.main;
		}else{
			key_second=second.ROperand.main.substring(1, second.ROperand.main.length()-1);
		}
		
		String OpBetweenCon=where.logicOp;
		//doing the testing criteria:
		//doing the first criteria
		if(first.logicOp.equalsIgnoreCase("=")||first.logicOp.equalsIgnoreCase("<>") ){//doing equaling
			HashMap<String, HashMap<String, String[]>> index_selected=table.hashList[index_attr_first];
			if(first.logicOp.equalsIgnoreCase("=")){
		
				HashMap<String, String[]> firstConRe=index_selected.get(key_first);
				
			
				if(OpBetweenCon.equalsIgnoreCase("and")){//and case
					if(firstConRe!=null){
						int i=0;
						for(String key_re:firstConRe.keySet()){
							String temp[]=firstConRe.get(key_re);
							if(second.logicOp.equalsIgnoreCase("=")){
								if(temp[index_attr_second].equals(key_second)){
									result.resultTable1.put(i, temp);
								}
							}else if(second.logicOp.equalsIgnoreCase("<>")){
								if(temp[index_attr_second].equals(key_second)==false){
									result.resultTable1.put(i, temp);
								}
								
							}else if(second.logicOp.equalsIgnoreCase("<")){
								if(Integer.parseInt(temp[index_attr_second])<Integer.parseInt(key_second)){
									result.resultTable1.put(i, temp);
								}
							}else{
								if(Integer.parseInt(temp[index_attr_second])>Integer.parseInt(key_second)){
									result.resultTable1.put(i, temp);
								}
							}
	
							//result.resultTable1.put(i, re.get(key_re));
							i++;	
						}
					}else{
						result.resultTable1=null;
					}
				}else{//or case
					int i=0;
					for(String keyL1:index_selected.keySet()){
						  firstConRe=index_selected.get(keyL1);
						  if(keyL1.equals(key_first)){
							  for(String key_re:firstConRe.keySet()){
								  result.resultTable1.put(i, firstConRe.get(key_re));
								  i++;
							  } 
						  }else{

							for(String key_re:firstConRe.keySet()){
								String temp[]=firstConRe.get(key_re);
								if(second.logicOp.equalsIgnoreCase("=")){
									if(temp[index_attr_second].equals(key_second)){
										result.resultTable1.put(i, temp);
									}
								}else if(second.logicOp.equalsIgnoreCase("<>")){
									if(temp[index_attr_second].equals(key_second)==false){
										result.resultTable1.put(i, temp);
									}
									
								}else if(second.logicOp.equalsIgnoreCase("<")){
									if(Integer.parseInt(temp[index_attr_second])<Integer.parseInt(key_second)){
										result.resultTable1.put(i, temp);
									}
								}else{
									if(Integer.parseInt(temp[index_attr_second])>Integer.parseInt(key_second)){
										result.resultTable1.put(i, temp);
									}
								}
								 i++;
								//result.resultTable1.put(i, re.get(key_re));
									
							} 
						  }
						 
						
					}
					
				}
					
				
					
				
		
					
			}else{//<>  case
			
				HashMap<String, String[]> firstConRe;
				
				
				if(OpBetweenCon.equalsIgnoreCase("and")){//and case
					int i=0;
					for(String keyL1:index_selected.keySet()){
						  firstConRe=index_selected.get(keyL1);
						  if(keyL1.equals(key_first)==false){
							  for(String key_re:firstConRe.keySet()){
									String temp[]=firstConRe.get(key_re);
									if(second.logicOp.equalsIgnoreCase("=")){
										if(temp[index_attr_second].equals(key_second)){
											result.resultTable1.put(i, temp);
										}
									}else if(second.logicOp.equalsIgnoreCase("<>")){
										if(temp[index_attr_second].equals(key_second)==false){
											result.resultTable1.put(i, temp);
										}
										
									}else if(second.logicOp.equalsIgnoreCase("<")){
										if(Integer.parseInt(temp[index_attr_second])<Integer.parseInt(key_second)){
											result.resultTable1.put(i, temp);
										}
									}else{
										if(Integer.parseInt(temp[index_attr_second])>Integer.parseInt(key_second)){
											result.resultTable1.put(i, temp);
										}
									}
									 i++;									
								} 
						  }
						 
						
					}
				}else{//or case
					int i=0;
					for(String keyL1:index_selected.keySet()){
						  firstConRe=index_selected.get(keyL1);
						  if(keyL1.equals(key_first)==false){
							  for(String key_re:firstConRe.keySet()){
								  result.resultTable1.put(i, firstConRe.get(key_re));
								  i++;
							  } 
						  }else{

							for(String key_re:firstConRe.keySet()){
								String temp[]=firstConRe.get(key_re);
								if(second.logicOp.equalsIgnoreCase("=")){
									if(temp[index_attr_second].equals(key_second)){
										result.resultTable1.put(i, temp);
									}
								}else if(second.logicOp.equalsIgnoreCase("<>")){
									if(temp[index_attr_second].equals(key_second)==false){
										result.resultTable1.put(i, temp);
									}
									
								}else if(second.logicOp.equalsIgnoreCase("<")){
									if(Integer.parseInt(temp[index_attr_second])<Integer.parseInt(key_second)){
										result.resultTable1.put(i, temp);
									}
								}else{
									if(Integer.parseInt(temp[index_attr_second])>Integer.parseInt(key_second)){
										result.resultTable1.put(i, temp);
									}
								}
								 i++;
								//result.resultTable1.put(i, re.get(key_re));
									
							} 
						  }
						 
						
					}
					
				}
					
				
				//-------------------<> case end
				
			}
			

	//------------------ "=" "<>" case end
	
			
		}else{//------------- ">" "<" case starts
		
			if(attr_type_first.equalsIgnoreCase("int")){//only support int < || >
				BPTree<Integer, BPTree<Integer, String[]>> index_selected=table.bptList[index_attr_first];
				SortedMap<Integer,  BPTree<Integer, String[]>> subMap = null;
				SortedMap<Integer,  BPTree<Integer, String[]>> subMap_complement = null;
				if(first.logicOp.equalsIgnoreCase(">")){
					
					subMap=index_selected.tailMap(Integer.parseInt(key_first));
					subMap_complement=index_selected.headMap(Integer.parseInt(key_first));//miss the equal case
				
				}else if(first.logicOp.equalsIgnoreCase("<")){//inclusive and exculsive problem
					subMap=index_selected.headMap(Integer.parseInt(key_first) );
					subMap_complement=index_selected.tailMap(Integer.parseInt(key_first));
				}
				
				int i=0;
				if(subMap!=null){
					for(Integer keyRe : subMap.keySet()) {
						BPTree<Integer, String[]> resultBpt=subMap.get(keyRe);
						for(Integer keybpt : resultBpt.keySet()) {
							String[] tempStr=resultBpt.get(keybpt);
						    if(first.logicOp.equalsIgnoreCase(">")){
						    	if(keyRe==Integer.parseInt(key_first)){
						    		tempStr=null;//dump result 
						    	}
						    		
						    }else if(first.logicOp.equalsIgnoreCase("<")){
						    		//do nth ; need the result
						    }
						    
						  
						    //doing second criteria
						    if(OpBetweenCon.equalsIgnoreCase("and")&& tempStr!=null){//and case
								
								if(second.logicOp.equalsIgnoreCase("=")){
									if(tempStr[index_attr_second].equals(key_second)){
										result.resultTable1.put(i, tempStr);
										i++;
									}
								}else if(second.logicOp.equalsIgnoreCase("<>")){
									if(tempStr[index_attr_second].equals(key_second)==false){
										result.resultTable1.put(i, tempStr);
										i++;
									}
									
								}else if(second.logicOp.equalsIgnoreCase("<")){
									if(Integer.parseInt(tempStr[index_attr_second])<Integer.parseInt(key_second)){
										result.resultTable1.put(i, tempStr);
										i++;
									}
								}else{
									if(Integer.parseInt(tempStr[index_attr_second])>Integer.parseInt(key_second)){
										result.resultTable1.put(i, tempStr);
										i++;
									}
								}
				
							
							}else if(tempStr!=null){//or case
								//first citeria match must needed.
								result.resultTable1.put(i, tempStr);
								i++;
							}
						    
						    

						}
					}
				}
				
				
				if(OpBetweenCon.equalsIgnoreCase("or")&& subMap_complement!=null){//put back or complement
					
					for(Integer keyRe : subMap_complement.keySet()) {
						BPTree<Integer, String[]> resultBpt=subMap_complement.get(keyRe);
						for(Integer keybpt : resultBpt.keySet()) {
							String[] tempStr=resultBpt.get(keybpt);

				
							if(second.logicOp.equalsIgnoreCase("=")){
								if(tempStr[index_attr_second].equals(key_second)){
									result.resultTable1.put(result.resultTable1.size(), tempStr);
									
								}
							}else if(second.logicOp.equalsIgnoreCase("<>")){
								if(tempStr[index_attr_second].equals(key_second)==false){
									result.resultTable1.put(result.resultTable1.size(), tempStr);
									
								}
								
							}else if(second.logicOp.equalsIgnoreCase("<")){
								if(Integer.parseInt(tempStr[index_attr_second])<Integer.parseInt(key_second)){
									result.resultTable1.put(result.resultTable1.size(), tempStr);
									
								}
							}else{
								if(Integer.parseInt(tempStr[index_attr_second])>Integer.parseInt(key_second)){
									result.resultTable1.put(result.resultTable1.size(), tempStr);
								
								}
							}
				
							
							
						}
						
						
					}
					
				
					//add back the miss case of subMap_complement
					if(first.logicOp.equalsIgnoreCase(">")){
						BPTree<Integer, String[]> complementMiss=subMap.get(Integer.parseInt(key_first));
						if(complementMiss!=null){
							for(Integer keybpt : complementMiss.keySet()) {
								String[] tempStr=complementMiss.get(keybpt);

								 
								if(second.logicOp.equalsIgnoreCase("=")){
									if(tempStr[index_attr_second].equals(key_second)){
										result.resultTable1.put(result.resultTable1.size(), tempStr);
										
									}
								}else if(second.logicOp.equalsIgnoreCase("<>")){
									if(tempStr[index_attr_second].equals(key_second)==false){
										result.resultTable1.put(result.resultTable1.size(), tempStr);
										
									}
									
								}else if(second.logicOp.equalsIgnoreCase("<")){
									if(Integer.parseInt(tempStr[index_attr_second])<Integer.parseInt(key_second)){
										result.resultTable1.put(result.resultTable1.size(), tempStr);
										
									}
								}else{
									if(Integer.parseInt(tempStr[index_attr_second])>Integer.parseInt(key_second)){
										result.resultTable1.put(result.resultTable1.size(), tempStr);
									
									}
								}
								
							}
						}
						
					}
				
					 
				}
				
		
			}
			
		}
		//------------------------
		
		
		
		
		
		
		
		return result;
	}
	
	public static Result select2T1C(selectItem[] selectList, fromItem[] fromList, whereItem where) throws Exception{
		//get the two tables
		Table_obj_index table1=SimpleGUI.d_hash.tables_hashed.get(fromList[0].main.toUpperCase() );
		Table_obj_index table2=SimpleGUI.d_hash.tables_hashed.get(fromList[1].main.toUpperCase() );
		Result result;
		String alias1;
		String alias2;
		if(fromList[0].sub==null){
			alias1=null;
		}else{
			alias1=fromList[0].sub;
		}
		if(fromList[1].sub==null){
			alias2=null;
		}else{
			alias2=fromList[1].sub;
		}
		result=new Result(selectList, 
				table1.rawTable.cdata,
				table2.rawTable.cdata,
				table1.tname,
				table2.tname,
				alias1,
				alias2);
		
		String attr_processedL;
		if(where.con[0].LOperand.sub==null){
			attr_processedL=where.con[0].LOperand.main;
		}else{
			attr_processedL=where.con[0].LOperand.sub;
		}
	
		Table_obj_index TableL;
		Table_obj_index TableR;
		if(where.con[0].LOperand.sub==null){
			if(SimpleGUI.d.tables.get(fromList[0].main.toUpperCase()).argument_in_schema(attr_processedL)){
				
				 TableL=table1;
				 TableR=table2;
			}

			else{
				TableR=table1;
				TableL=table2;
			}
				
				
		}else{
			if(where.con[0].LOperand.main.equalsIgnoreCase(fromList[0].main)||where.con[0].LOperand.main.equalsIgnoreCase(fromList[0].sub)){
				 TableL=table1;
				 TableR=table2;
				
			}else{
				TableR=table1;
				TableL=table2;
			}
				
		}
		
		int index_attrL=TableL.getIndexOfAtrri(attr_processedL);
		String attr_typeL=TableL.getTypeOfAtrri_index(index_attrL);


//-------------------processing 
		if(where.join[0]!=null){//-----------Join case
			String attr_processedR;
			if(where.con[0].ROperand.sub==null){
				attr_processedR=where.con[0].ROperand.main;
			}else{
				attr_processedR=where.con[0].ROperand.sub;
			}
			
			int index_attrR=TableR.getIndexOfAtrri(attr_processedR);
			
			HashMap<String, HashMap<String, String[]>> index_selectedL=TableL.hashList[index_attrL];
			HashMap<String, HashMap<String, String[]>> index_selectedR=TableR.hashList[index_attrR];
			
			int i=0;
			for(String keyL:index_selectedL.keySet()){
				HashMap<String, String[]> match=index_selectedR.get(keyL);
				if(match!=null){
					HashMap<String, String[]> Lmap=index_selectedL.get(keyL);
					for(String LMatch:Lmap.keySet()){
						String LStr[]=Lmap.get(LMatch);
						for(String RMatch:match.keySet()){
							String RStr[]=match.get(RMatch);
							String[] ComL;
							String[] ComR;
							if(TableL==table1){
								ComL=LStr;
								ComR=RStr;

							}else{
								ComL=RStr;
								ComR=LStr;
							}
							
							String[] Combine=new String[result.coldef1.length+result.coldef2.length];
							for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
								if(f<result.coldef1.length)
									Combine[f]=ComL[f];
								else
									Combine[f]=ComR[f-result.coldef1.length];
							}
							
							result.resultTable1.put(i, Combine);
							i++;
						}
					}
				}
			}
			
			
			
			
		}else{//------------------not join case
			
			String Key=null;
			if(attr_typeL.equalsIgnoreCase("int"))
				Key=where.con[0].ROperand.main;
			else
				Key=where.con[0].ROperand.main.substring(1,where.con[0].ROperand.main.length()-1);
			
			
			//---------------
			if(where.con[0].logicOp.equalsIgnoreCase("=")||where.con[0].logicOp.equalsIgnoreCase("<>") ){//doing equaling
				HashMap<String, HashMap<String, String[]>> index_selectedL=TableL.hashList[index_attrL];
				HashMap<String, HashMap<String, String[]>> index_selectedR=TableR.hashList[0];
				
				if(where.con[0].logicOp.equalsIgnoreCase("=")){
			
					
					HashMap<String, String[]> re=index_selectedL.get(Key);
					int i=0;
					
					if(re!=null){
						for(String key_re:re.keySet()){
							String LStr[]=re.get(key_re);
							for(String keyL1:index_selectedR.keySet()){
								HashMap<String, String[]> temp=index_selectedR.get(keyL1);
								for(String keyL2:temp.keySet()){
									String RStr[]=temp.get(keyL2);
									String[] ComL;
									String[] ComR;
									if(TableL==table1){
										ComL=LStr;
										ComR=RStr;
		
									}else{
										ComL=RStr;
										ComR=LStr;
									}
									
									String[] Combine=new String[result.coldef1.length+result.coldef2.length];
									for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
										if(f<result.coldef1.length)
											Combine[f]=ComL[f];
										else
											Combine[f]=ComR[f-result.coldef1.length];
									}
									
									result.resultTable1.put(i, Combine);
									i++;
								}
							}
						}
					}else
						result.resultTable1=null;
				}else{
					// <>    case
					HashMap<String, HashMap<String, String[]>> re= index_selectedL;
				
					int i=0;
					for(String keyRe:re.keySet()){
						if(keyRe.equals(Key)==false){
							HashMap<String, String[]> reTemp=re.get(keyRe);
							for(String key2:reTemp.keySet()){
								String LStr[]=reTemp.get(key2);
								for(String keyL1:index_selectedR.keySet()){
									HashMap<String, String[]> temp=index_selectedR.get(keyL1);
									for(String keyL2:temp.keySet()){
										String RStr[]=temp.get(keyL2);
										String[] ComL;
										String[] ComR;
										if(TableL==table1){
											ComL=LStr;
											ComR=RStr;
			
										}else{
											ComL=RStr;
											ComR=LStr;
										}
										
										String[] Combine=new String[result.coldef1.length+result.coldef2.length];
										for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
											if(f<result.coldef1.length)
												Combine[f]=ComL[f];
											else
												Combine[f]=ComR[f-result.coldef1.length];
										}
										result.resultTable1.put(i, Combine);
										i++;
									}
								}
						
							}
						}
				
					
						
					}
						
					
					
				}
							
			}else{//not join case and > or < case
			
				if(attr_typeL.equalsIgnoreCase("int")){
					BPTree<Integer, BPTree<Integer, String[]>> index_selectedL=TableL.bptList[index_attrL];
					HashMap<String, HashMap<String, String[]>> index_selectedR=TableR.hashList[0];
					SortedMap<Integer,  BPTree<Integer, String[]>> subMap = null;
					if(where.con[0].logicOp.equalsIgnoreCase(">")){
						
						subMap=index_selectedL.tailMap(Integer.parseInt(where.con[0].ROperand.main));
					
					}else if(where.con[0].logicOp.equalsIgnoreCase("<")){//inclusive and exculsive problem
						subMap=index_selectedL.headMap(Integer.parseInt(where.con[0].ROperand.main) );
					}
					
					int i=0;
					for(Integer keyRe : subMap.keySet()) {
						BPTree<Integer, String[]> resultBpt=subMap.get(keyRe);
						for(Integer keybpt : resultBpt.keySet()) {
							String[] LStr=resultBpt.get(keybpt);
						 
						    if(where.con[0].logicOp.equalsIgnoreCase(">")){
						    	if(keyRe==Integer.parseInt(Key)){
						    		LStr=null;//dump it 	
						    	}
						    		
						    }else if(where.con[0].logicOp.equalsIgnoreCase("<")){
						    	  //do nth need ti
						    }
						    
						    if(LStr!=null){
						    	for(String keyL1:index_selectedR.keySet()){
									HashMap<String, String[]> temp=index_selectedR.get(keyL1);
									for(String keyL2:temp.keySet()){
										String RStr[]=temp.get(keyL2);
										String[] ComL;
										String[] ComR;
										if(TableL==table1){
											ComL=LStr;
											ComR=RStr;
			
										}else{
											ComL=RStr;
											ComR=LStr;
										}
										
										String[] Combine=new String[result.coldef1.length+result.coldef2.length];
										for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
											if(f<result.coldef1.length)
												Combine[f]=ComL[f];
											else
												Combine[f]=ComR[f-result.coldef1.length];
										}
										result.resultTable1.put(i, Combine);
										i++;
									}
								}
						    }
						    
				
						}
					}
					
				}
				
				
				
			}
		
			
			//-----------
			
			
			
		}
		
		
		
		
		
		
		return result;
	}
	
	public static Result select2T2C(selectItem[] selectList, fromItem[] fromList, whereItem where) throws Exception{

		//get the two tables
		Table_obj_index table1=SimpleGUI.d_hash.tables_hashed.get(fromList[0].main.toUpperCase() );
		Table_obj_index table2=SimpleGUI.d_hash.tables_hashed.get(fromList[1].main.toUpperCase() );
		Result result = null;
		String alias1;
		String alias2;
		
		if(where.logicOp.equalsIgnoreCase("OR")){
			

			///////////-fix
		
			
			if(fromList[0].sub==null){
				alias1=null;
			}else{
				alias1=fromList[0].sub;
			}
			if(fromList[1].sub==null){
				alias2=null;
			}else{
				alias2=fromList[1].sub;
			}
			result=new Result(selectList, 
					table1.rawTable.cdata,
					table2.rawTable.cdata,
					table1.tname,
					table2.tname,
					alias1,
					alias2);
			result.specialCountSum=true;
		
			conditionItem joinItem;
			conditionItem criteria;
			if(where.join[0]==true){
				 joinItem=where.con[0];
				 criteria=where.con[1];
			}else{
				 joinItem=where.con[1];
				 criteria=where.con[0];
			}
			
			
			
			//doing join setup
			Table_obj_index LOpTable;
			Table_obj_index ROpTable;
			String attr_processedL;
			String attr_processedR;
			
			attr_processedL=joinItem.LOperand.sub;
			attr_processedR=joinItem.ROperand.sub;
			if(joinItem.LOperand.main.equalsIgnoreCase(fromList[0].main) ||joinItem.LOperand.main.equalsIgnoreCase(fromList[0].sub)){
				LOpTable=table1;
				ROpTable=table2;
			}else{
				LOpTable=table2;
				ROpTable=table1;
			}
			
			int index_attrL=LOpTable.getIndexOfAtrri(attr_processedL);
			int index_attrR=ROpTable.getIndexOfAtrri(attr_processedR);
			
			HashMap<String, HashMap<String, String[]>> joinHash=LOpTable.hashList[index_attrL];
			
			//===================
			int tableDone;
			
			
			//doing criteria
			String attr_processed;
			if(criteria.LOperand.sub==null){
				attr_processed=criteria.LOperand.main;
			}else{
				attr_processed=criteria.LOperand.sub;
			}
		
			Table_obj_index criteriaTable;
			if(criteria.LOperand.sub==null){
				if(SimpleGUI.d.tables.get(fromList[0].main.toUpperCase()).argument_in_schema(attr_processed)){
					
					 criteriaTable=table1;
				}
	
				else
					criteriaTable=table2;
					
			}else{
				if(criteria.LOperand.main.equalsIgnoreCase(fromList[0].main)||criteria.LOperand.main.equalsIgnoreCase(fromList[0].sub)){
					criteriaTable=table1;
				}else
					criteriaTable=table2;
			}
			
			if(criteriaTable==table1){
				 tableDone=0;
			}else
				 tableDone=1;
			
			int index_attr=criteriaTable.getIndexOfAtrri(attr_processed);
			//System.out.println(attr_processed+" "+index_attr);
			String attr_type=criteriaTable.getTypeOfAtrri_index(index_attr);
			
			int count=0;
			int sum=0;
			int sumIndex;
		
			sumIndex=criteriaTable.getIndexOfAtrri(selectList[0].sub);
			//System.out.println(sumIndex);
			
			if(criteria.logicOp.equalsIgnoreCase("=")||criteria.logicOp.equalsIgnoreCase("<>")){//doing equaling
			
				
				
			
					
			}else{
					Table LTable=SimpleGUI.d.tables.get(LOpTable.tname.toUpperCase());
					
					BPTree<Integer, BPTree<Integer, String[]>> index_selected=criteriaTable.bptList[index_attr];
					SortedMap<Integer,  BPTree<Integer, String[]>> subMap = null;
					SortedMap<Integer,  BPTree<Integer, String[]>> Complement_Map = null;
					if(criteria.logicOp.equalsIgnoreCase(">")){
						
						subMap=index_selected.tailMap(Integer.parseInt(criteria.ROperand.main) );
						Complement_Map =index_selected.headMap(Integer.parseInt(criteria.ROperand.main) );
						//System.out.println(subMap.size()+" "+Complement_Map.size());
						//System.out.println(index_selected.size());
					}else if(criteria.logicOp.equalsIgnoreCase("<")){
						subMap=index_selected.headMap( Integer.parseInt(criteria.ROperand.main) );
						Complement_Map =index_selected.tailMap( Integer.parseInt(criteria.ROperand.main) );
					}else{
						//int case <> also treat as char case <>
					}
					
				
					if(subMap!=null){
						for(Integer key : subMap.keySet()) {
							BPTree<Integer, String[]> resultBpt=subMap.get(key);
							for(Integer keybpt : resultBpt.keySet()) {
								String[] tempStr=resultBpt.get(keybpt);
							
								 if(criteria.logicOp.equalsIgnoreCase(">")){
								    	if(key!=Integer.parseInt(criteria.ROperand.main)){
								    		
								    		if(selectList[0].main.equalsIgnoreCase("count")){
												count+=LTable.rdata.size();
											}else
											{
												sum=sum+(Integer.parseInt(tempStr[sumIndex])*LTable.rdata.size());
											}
								    		
								    		
								    	}
								    		
								    	else{
								    		HashMap<String, String[]> tempHash=joinHash.get(tempStr[index_attrR]);
								    		
							    			if(tempHash!=null){
							    				if(selectList[0].main.equalsIgnoreCase("count")){
							    					count+= tempHash.size();
												}else
												{
													sum=sum+(Integer.parseInt(tempStr[sumIndex])*tempHash.size());
												}
							    			}
								    		
								    	}
							     }else if(criteria.logicOp.equalsIgnoreCase("<")){
							    	 
							    		if(selectList[0].main.equalsIgnoreCase("count")){
							    			count+=LTable.rdata.size();
										}else
										{
											sum=sum+(Integer.parseInt(tempStr[sumIndex])*LTable.rdata.size());
										}
							   
							     }
					
							}
						}
					}
					
					//doing check == part
					if(Complement_Map!=null){
						for(Integer key : Complement_Map.keySet()) {
							BPTree<Integer, String[]> resultBpt=Complement_Map.get(key);
							for(Integer keybpt : resultBpt.keySet()) {
								String[] tempStr=resultBpt.get(keybpt);
					  
							//	System.out.println(index_attrR);
					    		HashMap<String, String[]> tempHash=joinHash.get(tempStr[index_attrR]);
					    	
				    			if(tempHash!=null){
				    				if(selectList[0].main.equalsIgnoreCase("count")){
				    					count+= tempHash.size();
									}else
									{
										sum=sum+(Integer.parseInt(tempStr[sumIndex])*tempHash.size());
									}
				    			
				    			}
   	
							}
						}		
					}
					
					
				
				
					
					
					
					String tempRe[]=new String[1];
					
					if(selectList[0].main.equalsIgnoreCase("count")){
						tempRe[0]=new Integer(count).toString();
					}else
					{
						tempRe[0]=new Integer(sum).toString();
					}
					
					result.resultTable1.put(0,tempRe);
				
					//System.out.println(result.resultTable1.size());
					//
				
					
					
					return result;
					
					
			}
				
		
				
		
			
			//------------
		
			
			
		}else{
			
			if(fromList[0].sub==null){
				alias1=null;
			}else{
				alias1=fromList[0].sub;
			}
			if(fromList[1].sub==null){
				alias2=null;
			}else{
				alias2=fromList[1].sub;
			}
			result=new Result(selectList, 
					table1.rawTable.cdata,
					table2.rawTable.cdata,
					table1.tname,
					table2.tname,
					alias1,
					alias2);
			if(where.join[0]==null)
			 System.out.println("null");
			if(where.join[0]==true||where.join[1]==true){
				
				conditionItem joinItem;
				conditionItem criteria;
				if(where.join[0]==true){
					 joinItem=where.con[0];
					 criteria=where.con[1];
				}else{
					 joinItem=where.con[1];
					 criteria=where.con[0];
				}
				
				//mode for selecting join first then criteria or criteria first
				//0:First criteria then join; 1:first join then criteria
		//mode 0---------------------------------------------------------------
				if(SimpleGUI.indexingMode==0){//do the selection first
				
					int tableDone;
			
				
					//doing criteria
					String attr_processed;
					if(criteria.LOperand.sub==null){
						attr_processed=criteria.LOperand.main;
					}else{
						attr_processed=criteria.LOperand.sub;
					}
				
					Table_obj_index criteriaTable;
					if(criteria.LOperand.sub==null){
						if(SimpleGUI.d.tables.get(fromList[0].main.toUpperCase()).argument_in_schema(attr_processed)){
							
							 criteriaTable=table1;
						}
			
						else
							criteriaTable=table2;
							
					}else{
						if(criteria.LOperand.main.equalsIgnoreCase(fromList[0].main)||criteria.LOperand.main.equalsIgnoreCase(fromList[0].sub)){
							criteriaTable=table1;
						}else
							criteriaTable=table2;
					}
					
					if(criteriaTable==table1){
						 tableDone=0;
					}else
						 tableDone=1;
					
					int index_attr=criteriaTable.getIndexOfAtrri(attr_processed);
					//System.out.println(attr_processed+" "+index_attr);
					String attr_type=criteriaTable.getTypeOfAtrri_index(index_attr);
					
					if(criteria.logicOp.equalsIgnoreCase("=")||criteria.logicOp.equalsIgnoreCase("<>")){//doing equaling
					
							HashMap<String, HashMap<String, String[]>> index_selected=criteriaTable.hashList[index_attr];
							String key;
							if(attr_type.equalsIgnoreCase("int")){
								key=criteria.ROperand.main;
							}else{
								key=criteria.ROperand.main.substring(1, criteria.ROperand.main.length()-1);
							}
							
							if(criteria.logicOp.equalsIgnoreCase("=")){
								HashMap<String, String[]> re=index_selected.get(key);
								int i=0;
								if(re!=null){
									for(String key_re:re.keySet()){
										String[] tempStr=re.get(key_re);
										result.resultTable1.put(i,tempStr );
										i++;	
										
									   /* System.out.println(key+" result: ");
								    	for(int k=0;k<tempStr.length;k++){
								    	   System.out.print(tempStr[k]+"  ");
								    	}
								    	System.out.println();*/
									
									}
								}else
									result.resultTable1=null;
							}else{
								
								HashMap<String, HashMap<String, String[]>> re= index_selected;
								
								
								int i=0;
								for(String keyRe:re.keySet()){
									if(keyRe.equals(key)==false){
										HashMap<String, String[]> reTemp=re.get(keyRe);
										for(String key2:reTemp.keySet()){
											
											result.resultTable1.put(i, reTemp.get(key2));
											i++;	
									
										}
									}
							
								
									
								}
								
							}
							
						
					
							
						}else{
						
							if(attr_type.equalsIgnoreCase("int")){
								BPTree<Integer, BPTree<Integer, String[]>> index_selected=criteriaTable.bptList[index_attr];
								SortedMap<Integer,  BPTree<Integer, String[]>> subMap = null;
								if(criteria.logicOp.equalsIgnoreCase(">")){
									
									subMap=index_selected.tailMap(Integer.parseInt(criteria.ROperand.main) );
								
								}else if(criteria.logicOp.equalsIgnoreCase("<")){
									subMap=index_selected.headMap( Integer.parseInt(criteria.ROperand.main) );
								}else{
									//int case <> also treat as char case <>
								}
								
								int i=0;
								if(subMap!=null){
									for(Integer key : subMap.keySet()) {
										BPTree<Integer, String[]> resultBpt=subMap.get(key);
										for(Integer keybpt : resultBpt.keySet()) {
											String[] tempStr=resultBpt.get(keybpt);
										
											 if(criteria.logicOp.equalsIgnoreCase(">")){
											    	if(key!=Integer.parseInt(criteria.ROperand.main))
											    		result.resultTable1.put(i,tempStr);
											    }else if(criteria.logicOp.equalsIgnoreCase("<")){
											    		result.resultTable1.put(i,tempStr);
											    }
								
											 i++;
											/* System.out.println(keybpt+" result: ");
										    	for(int k=0;k<tempStr.length;k++){
										    	   System.out.print(tempStr[k]+"  ");
										    	}
										    	System.out.println();*/
										}
									}
								}
							}
						}
					
						//now doing join
						
					
						Table_obj_index LOpTable;
						Table_obj_index ROpTable;
						String attr_processedL;
						String attr_processedR;
						
						if(joinItem.LOperand.main.equalsIgnoreCase(fromList[0].main) ||joinItem.LOperand.main.equalsIgnoreCase(fromList[0].sub)){
							if(tableDone==0){
								LOpTable=table1;
								ROpTable=table2;
								attr_processedL=joinItem.LOperand.sub;
								attr_processedR=joinItem.ROperand.sub;
							}else{
								LOpTable=table2;
								ROpTable=table1;
								attr_processedL=joinItem.ROperand.sub;
								attr_processedR=joinItem.LOperand.sub;
								
							}
							
						}else{
							if(tableDone==0){
								LOpTable=table1;
								ROpTable=table2;
								attr_processedL=joinItem.ROperand.sub;
								attr_processedR=joinItem.LOperand.sub;
							}else{
								LOpTable=table2;
								ROpTable=table1;
								attr_processedL=joinItem.LOperand.sub;
								attr_processedR=joinItem.ROperand.sub;
								
							}
						}
						
						//System.out.println(attr_processedL+"  "+attr_processedR);
						int index_attrL=LOpTable.getIndexOfAtrri(attr_processedL);
						int index_attrR=ROpTable.getIndexOfAtrri(attr_processedR);
						//System.out.println(LOpTable.tname+"  "+ROpTable.tname);
						HashMap<Integer, String[]> newResult=new HashMap<Integer, String[]> ();
						int z=0;
						HashMap<String, HashMap<String, String[]>> index_selected_join=ROpTable.hashList[index_attrR];
						if(result.resultTable1!=null){
							for(Integer key: result.resultTable1.keySet()){
								String[] temp=result.resultTable1.get(key);
								String matchStr=temp[index_attrL];
								
								HashMap<String, String[]> matchRe=index_selected_join.get(matchStr);
								if(matchRe!=null){
									
									for(String key2:matchRe.keySet()){
										String matchReStr[]=matchRe.get(key2);
										String[] ComL;
										String[] ComR;
										if(LOpTable==table1){
											ComL=temp;
											ComR=matchReStr;
			
										}else{
											ComR=temp;
											ComL=matchReStr;
										}
										
										String[] Combine=new String[result.coldef1.length+result.coldef2.length];
										for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
											if(f<result.coldef1.length)
												Combine[f]=ComL[f];
											else
												Combine[f]=ComR[f-result.coldef1.length];
										}
										
									/*	for(int k=0;k<Combine.length;k++){
											System.out.print(Combine[k]+" ");
										}
										System.out.println();*/
										
										newResult.put(z, Combine);
										z++;
									}
								}
							}
						}
					
					    result.resultTable1=newResult;
					   /* testing
					    for(Integer key: result.resultTable1.keySet()){
					    	String[] temp=result.resultTable1.get(key);
					    	for (int i=0;i<temp.length;i++){
					    		System.out.print(temp[i]+"  ");
					    	}
					    	System.out.println();
					    }
					    */
					
	 //mode 0-------end--------------------------------------------------------	
					    
	//mode 1------------------------------------------------
				}else{ //mode 1
					
					//doing join setup
					Table_obj_index LOpTable;
					Table_obj_index ROpTable;
					String attr_processedL;
					String attr_processedR;
					
					attr_processedL=joinItem.LOperand.sub;
					attr_processedR=joinItem.ROperand.sub;
					if(joinItem.LOperand.main.equalsIgnoreCase(fromList[0].main) ||joinItem.LOperand.main.equalsIgnoreCase(fromList[0].sub)){
						LOpTable=table1;
						ROpTable=table2;
					}else{
						LOpTable=table2;
						ROpTable=table1;
					}
					
					int index_attrL=LOpTable.getIndexOfAtrri(attr_processedL);
					int index_attrR=ROpTable.getIndexOfAtrri(attr_processedR);
					
					//doing criteria setup
					String attr_processed_criteria;
					if(criteria.LOperand.sub==null){
						attr_processed_criteria=criteria.LOperand.main;
					}else{
						attr_processed_criteria=criteria.LOperand.sub;
					}
				
					Table_obj_index criteriaTable;
					if(criteria.LOperand.sub==null){
						if(SimpleGUI.d.tables.get(fromList[0].main.toUpperCase()).argument_in_schema(attr_processed_criteria)){
							
							 criteriaTable=table1;
						}
			
						else
							criteriaTable=table2;
							
					}else{
						if(criteria.LOperand.main.equalsIgnoreCase(fromList[0].main)||criteria.LOperand.main.equalsIgnoreCase(fromList[0].sub)){
							criteriaTable=table1;
						}else
							criteriaTable=table2;
					}
						
					int index_attr_criteria=criteriaTable.getIndexOfAtrri(attr_processed_criteria);
					//System.out.println(attr_processed_criteria+" "+index_attr_criteria);
					String attr_type_criteria=criteriaTable.getTypeOfAtrri_index(index_attr_criteria);
					String criteriaOp=criteria.logicOp;
					
					HashMap<String, HashMap<String,String[]>> LHash=LOpTable.hashList[index_attrL];
					HashMap<String, HashMap<String,String[]>> RHash=ROpTable.hashList[index_attrR];
					
					//System.out.println(index_attrL);
					//processing
					//System.out.println(LOpTable.tname+"  "+ROpTable.tname);
					if(criteriaOp.equalsIgnoreCase("=")||criteriaOp.equalsIgnoreCase("<>")){
					//	System.out.println("in mode1 = <>");
						HashMap<String, HashMap<Integer,String[]>> resultHash=new HashMap<String, HashMap<Integer,String[]>>();
						
						for(String LKey:LHash.keySet()){
							
							 HashMap<String,String[]> temp=LHash.get(LKey);
							 for(String LKey2:temp.keySet()){
								 String tempStr[]=temp.get(LKey2);
								 
								 HashMap<String,String[]> finding=RHash.get(tempStr[index_attrL]);
								 if(finding!=null){//has find match, so doing processing
									 for(String RKey:finding.keySet()){
										String matchStr[]=finding.get(RKey);
										 //combing the result and hash the result based on criteria filed 
										String[] ComL;
										String[] ComR;
										if(LOpTable==table1){
											ComL=tempStr;
											ComR=matchStr;
			
										}else{
											ComR=tempStr;
											ComL=matchStr;
										}
										
										
										String[] Combine=new String[result.coldef1.length+result.coldef2.length];
										for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
											if(f<result.coldef1.length)
												Combine[f]=ComL[f];
											else
												Combine[f]=ComR[f-result.coldef1.length];
										}
										
										//put the combine result into new hash
										//index based on criteria key
										String criteriaHashKey = null;
										if(criteriaTable==LOpTable){
											
												criteriaHashKey=tempStr[index_attr_criteria];
											
										}else {
											
												criteriaHashKey=matchStr[index_attr_criteria];
											
										}
										
										//System.out.println(criteriaHashKey);
									/*	for(int i=0;i<Combine.length;i++){
											System.out.print(Combine[i]+" ");
										}
										System.out.println();*/
										//see if already has a key existed
										HashMap<Integer,String[]> testExist=resultHash.get(criteriaHashKey);
										if(testExist!=null){
											testExist.put(testExist.size(), Combine);
											resultHash.put(criteriaHashKey, testExist);
											
										}else{
											HashMap<Integer,String[]> newOne=new HashMap<Integer,String[]>();
											newOne.put(0,Combine);
											resultHash.put(criteriaHashKey, newOne);
										}

									 }
								 }
								 
							 }
							
						}
						
						//System.out.println(resultHash.toString());
						//do criteria now
						String criteriaKey=null;
						if(attr_type_criteria.equalsIgnoreCase("int"))
							criteriaKey=criteria.ROperand.main;
						else
							criteriaKey=criteria.ROperand.main.substring(1, criteria.ROperand.main.length()-1);
						HashMap<Integer, String[]> tempResult=new HashMap<Integer, String[]>();

						//System.out.println(criteriaKey);
						if(criteriaOp.equalsIgnoreCase("<>")){
							resultHash.remove(criteriaKey);
							int i=0;
							for(String key1:resultHash.keySet()){
								HashMap<Integer, String[]> l1=resultHash.get(key1);
								for(Integer key2:l1.keySet()){
									tempResult.put(i,l1.get(key2));
									i++;
								}
							}
							//System.out.println(tempResult);
							
						}if(criteriaOp.equalsIgnoreCase("=")){
							int i=0;
							HashMap<Integer, String[]> match=resultHash.get(criteriaKey);
							if(match!=null){
								for(Integer key2:match.keySet()){
									tempResult.put(i,match.get(key2));
									i++;
								}
							}
							
								
						}
						result.resultTable1=tempResult;
						
						
						
						
					}else{//doing < or > case, better using b+ tree;
						if(attr_type_criteria.equalsIgnoreCase("int")){
							BPTree<Integer, BPTree<Integer, String[]>> resultHash=new BPTree<Integer, BPTree<Integer, String[]>>();
							
							for(String LKey:LHash.keySet()){
								 HashMap<String,String[]> temp=LHash.get(LKey);
								 for(String LKey2:temp.keySet()){
									 String tempStr[]=temp.get(LKey2);
									 
									 HashMap<String,String[]> finding=RHash.get(tempStr[index_attrL]);
									 if(finding!=null){//has find match, so doing processing
										 for(String RKey:finding.keySet()){
											String matchStr[]=finding.get(RKey);
											 //combing the result and hash the result based on criteria filed 
											String[] ComL;
											String[] ComR;
											if(LOpTable==table1){
												ComL=tempStr;
												ComR=matchStr;
				
											}else{
												ComR=tempStr;
												ComL=matchStr;
											}
											
											
											String[] Combine=new String[result.coldef1.length+result.coldef2.length];
											for(int f=0;f<result.coldef1.length+result.coldef2.length;f++){
												if(f<result.coldef1.length)
													Combine[f]=ComL[f];
												else
													Combine[f]=ComR[f-result.coldef1.length];
											}
											
											//put the combine result into new hash
											//index based on criteria key
											String criteriaHashKey = null;
											if(criteriaTable==LOpTable){
												
													criteriaHashKey=tempStr[index_attr_criteria];
												
											}else {
												
													criteriaHashKey=matchStr[index_attr_criteria];
												
											}
											
											//see if already has a key existed
											 BPTree<Integer, String[]> testExist=resultHash.get(Integer.parseInt(criteriaHashKey));
											if(testExist!=null){
												testExist.put(testExist.size(), Combine);
												resultHash.put(Integer.parseInt(criteriaHashKey), testExist);
												
											}else{
												 BPTree<Integer, String[]> newOne=new  BPTree<Integer, String[]>();
												 newOne.put(0,Combine);
												 resultHash.put(Integer.parseInt(criteriaHashKey), newOne);
											}

										 }
									 }
									 
								 }
								
							}
							
							//System.out.println(resultHash.toString());
							//do criteria now
							String criteriaKey=null;
							if(attr_type_criteria.equalsIgnoreCase("int"))
								criteriaKey=criteria.ROperand.main;
							else
								criteriaKey=criteria.ROperand.main.substring(1, criteria.ROperand.main.length()-1);
							HashMap<Integer, String[]> tempResult=new HashMap<Integer, String[]>();

							SortedMap<Integer,  BPTree<Integer, String[]>> subMap = null;
							if(criteria.logicOp.equalsIgnoreCase(">")){
								
								subMap=resultHash.tailMap( Integer.parseInt(criteriaKey ));
							
							}else if(criteria.logicOp.equalsIgnoreCase("<")){
								subMap=resultHash.headMap( Integer.parseInt(criteriaKey ));
							}
							
							int i=0;
							if(subMap!=null){
								for(Integer key : subMap.keySet()) {
									BPTree<Integer, String[]> resultBpt=subMap.get(key);
									for(Integer keybpt : resultBpt.keySet()) {
										String[] tempStr=resultBpt.get(keybpt);
									
										 if(criteria.logicOp.equalsIgnoreCase(">")){
										    	if(key!=Integer.parseInt(criteriaKey))
										    		tempResult.put(i,tempStr);
										  }else if(criteria.logicOp.equalsIgnoreCase("<")){
										    		tempResult.put(i,tempStr);
										  }
							
										 i++;
								
									}
								}
							}
							
							result.resultTable1=tempResult;
							//-------------------------
						}else{
							//no else case: because we dont do the > < of char
						}
					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				}
				
				
				
			}
			
		}
			
			
			
			

		
		
		
		return result;
	}

}
