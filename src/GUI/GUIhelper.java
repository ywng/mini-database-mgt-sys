package GUI;
import java.io.InputStream;
import java.util.HashMap;

import Indexing.Result;

import parser.ParseException;
import parser.SQL_parser;
import parser.SimpleNode;

import minidbms.*;

public class GUIhelper {
	public static Object [][] constrc_schema_table(Database d, String tableName) throws Exception{
		tableName=tableName.toUpperCase();
		Table selectedTable=d.getTable(tableName);
		if(selectedTable==null){
			//System.out.println("in null");
			return new Object[4][5];
			
		}
		ColumnDef[] attri_arr=selectedTable.cdata;
		Object[][] tableObj=new Object[attri_arr.length][5];
		for(int i=0;i<attri_arr.length;i++){
			//System.out.println(attri_arr[i].get_cname());
			tableObj[i][0]=attri_arr[i].get_cname();
			tableObj[i][1]=attri_arr[i].get_ctype();
			tableObj[i][2]=attri_arr[i].get_cpk().toString();
			tableObj[i][3]=attri_arr[i].get_cfk().toString();
			tableObj[i][4]=attri_arr[i].get_cnull().toString();
		}
		
		return tableObj;
		
	}
	
	public static String[] get_table_name_arr(Database d) throws Exception{
		int num_table=d.tables.keySet().size();
		String[] name_arr=new String [num_table];
		int i=0;
		for(String tname:d.tables.keySet()){
			name_arr[i]=tname;
			i++;
		}
		return name_arr;
		
	}
	
	public static Return eval_GUI(Database d, InputStream in) throws Exception{
		SQL_parser parser =  new SQL_parser(in);
        SimpleNode rootNode=parser.SqlScript();
       // MiniDBMS.eval_print(rootNode,"");
        return MiniDBMS.eval(rootNode,d,0);
	}
	
	public static String[] get_table_arr_name_list(Database d,String tableName) throws Exception{
		Table table_to_be_inserted= (Table)d.tables.get(tableName);//retrieve the table with name test
		String[] attr_name_list=null;
		if(table_to_be_inserted!=null){
			attr_name_list=new String [table_to_be_inserted.cdata.length];
 			for (int i=0;i<table_to_be_inserted.cdata.length;i++){
 				attr_name_list[i]=new String(table_to_be_inserted.cdata[i].get_cname());
 			}
		}
		return attr_name_list;
	}
	
	public static Object [][] constrc_data_table(Database d, String tableName){
		tableName=tableName.toUpperCase();
		Table selectedTable=d.getTable(tableName);
		if(selectedTable==null){
			//System.out.println("in null");
			return new Object[4][5];
			
		}
		HashMap<Integer,String[]> rdata=selectedTable.rdata;
		//System.out.println(rdata.size());
		Object[][] tableObj=new Object[rdata.size()][selectedTable.cdata.length];
		int i=0;
		for(Integer key:rdata.keySet()){
	        for (int j=0;j<selectedTable.cdata.length;j++){
	        	tableObj[i][j]=rdata.get(key)[j];
	        }
			i++;
		}
		
		return tableObj;
		
	}
        
        public static Object[][] displaySelectResultData(Return result){
            Object[][] tableObj=new Object[result.select_result.size()][result.new_order.length];
            int i=0;
            //for(int key:result.select_result.keySet()){
            for(int key =1;key<=result.r_id;++key ){
                for (int j=0;j<result.new_order.length;j++){
                        tableObj[i][j]=result.select_result.get(key)[j];
                }
                 i++;
            }
            return tableObj;
            
        }
        
        
        public static Object[][] displaySelectResultData_indexing(Result result){
    		
    		if(result.resultTable1==null) 
    			return null;
    		Object[][] tableObj = null;
    		int resultAttrLen=displaySelectResultAttr_indexing(result).length;
    		int posIndex[]=new int[resultAttrLen];
    	
			int pos=0;
			if(result.selectList.length==1&&(result.selectList[0].main.equalsIgnoreCase("count")||result.selectList[0].main.equalsIgnoreCase("sum"))){
				tableObj=new Object[1][1];
				if(result.coldef1==null){
					
					tableObj[0][0]=result.resultTable1.get(1)[0];
				}else{//has where case sum count
					//locate the position of the index
					String countSumAttr=result.selectList[0].sub;
					int countSumIndex = 0;
					for(int p=0;p<result.coldef1.length;p++){
						if(result.coldef1[p].cname.equalsIgnoreCase(countSumAttr)){
							countSumIndex=p;
							break;
						}
					}
					if(result.coldef2!=null){
						for(int p=0;p<result.coldef2.length;p++){
							if(result.coldef2[p].cname.equalsIgnoreCase(countSumAttr)){
								countSumIndex=result.coldef1.length+p;
								break;
							}
						}
					}
				
					if(result.selectList[0].main.equalsIgnoreCase("count")){
						if(result.specialCountSum==true){
							tableObj[0][0]=result.resultTable1.get(0)[0];
						}else{

							int countResult;
							if(countSumAttr.equalsIgnoreCase("*")){
								countResult=result.resultTable1.size();
							}else{
								countResult=0;
								for(Integer resultKey:result.resultTable1.keySet()){
									String[] temp=result.resultTable1.get(resultKey);
									if(temp[countSumIndex]!=null){
										countResult++;
									}
								}
							}
							Integer temp=new Integer(countResult);
						
							tableObj[0][0]=new String(temp.toString());
						}
						
				
					}else{
						if(result.specialCountSum==true){
							tableObj[0][0]=result.resultTable1.get(0)[0];
						}else{

							int sumResult=0;
							
							for(Integer resultKey:result.resultTable1.keySet()){
								String[] temp=result.resultTable1.get(resultKey);
								if(temp[countSumIndex]!=null){
									sumResult+=Integer.parseInt(temp[countSumIndex]);
								}
							}
							if(sumResult==0){
								return null;
							}else{
								Integer temp=new Integer(sumResult);
								
								tableObj[0][0]=new String(temp.toString());
								
							}
						}
						
						
					}
					
				}
			
				
        	}else{
        		if(result.coldef2==null){
        			//no join case
        			//------------------------------------------------------------------
        			for(int i=0;i<result.selectList.length;i++){

        					if(result.selectList[i].sub==null){
                    			
                    			if(result.selectList[i].main.equalsIgnoreCase("*")){
                    				
                    				for(int j=0;j<result.coldef1.length;j++){
                    					posIndex[pos]=j;
                    					pos++;
                    				}
                    			
                    			}else{
                    				for(int j=0;j<result.coldef1.length;j++){
                    					if(result.coldef1[j].cname.equalsIgnoreCase(result.selectList[i].main)){
                    						posIndex[pos]=j;
                    						pos++;
                    					}
                    				}
                    				
                    			}
                    			
                    			
                    		}else{
                    			
                    			if(result.selectList[i].sub.equalsIgnoreCase("*")){
                    				
                    				for(int j=0;j<result.coldef1.length;j++){
                    					posIndex[pos]=j;
                    					pos++;
                    				}
                    			
                    			}else{
                    				for(int j=0;j<result.coldef1.length;j++){
                    					if(result.coldef1[j].cname.equalsIgnoreCase(result.selectList[i].sub)){
                    						posIndex[pos]=j;
                    						pos++;
                    					}
                    				}
                    				
                    			}
                    			
                    		}

        			
        			}
        			
        			tableObj=new Object[result.resultTable1.size()][resultAttrLen];
        			int i=0;
        			for(int key: result.resultTable1.keySet()){
        				String[] temp=result.resultTable1.get(key);
        				String[] re=new String[ resultAttrLen];
        				for(int k=0;k<posIndex.length;k++){
        					re[k]=temp[posIndex[k]];
        				}
        				tableObj[i]=(Object[])re;
        				i++;
            	  }
        		
        		
        			//------------------------------------------------------------------
        		}else{//join case
        			
        			
        			//------------------------------------------------------------------
        			for(int i=0;i<result.selectList.length;i++){

        					if(result.selectList[i].sub==null){
                    			
                    			if(result.selectList[i].main.equalsIgnoreCase("*")){
                    				
                    				for(int j=0;j<result.coldef1.length;j++){
                    					posIndex[pos]=j;
                    					pos++;
                    				}
                    				for(int j=0;j<result.coldef2.length;j++){
                    					posIndex[pos]=result.coldef1.length+j;
                    					pos++;
                    				}
                    			
                    			}else{
                    				for(int j=0;j<result.coldef1.length;j++){
                    					if(result.coldef1[j].cname.equalsIgnoreCase(result.selectList[i].main)){
                    						posIndex[pos]=j;            						
                    						pos++;
                    						continue;
                    					}
                    				}
                    				for(int j=0;j<result.coldef2.length;j++){
                    					if(result.coldef2[j].cname.equalsIgnoreCase(result.selectList[i].main)){
                    						posIndex[pos]=result.coldef1.length+j;               						
                    						pos++;
                    						continue;
                    					}
                    				}
                    				
                    			}
                    			
                    			
                    		}else{
                    			
                    			if(result.selectList[i].sub.equalsIgnoreCase("*")){
                    				if(result.selectList[i].main.equalsIgnoreCase(result.t1)||result.selectList[i].main.equalsIgnoreCase(result.Alias1)){
                    					for(int j=0;j<result.coldef1.length;j++){
                        					posIndex[pos]=j;
                        					pos++;
                        				}
                    				}else{
                    					for(int j=0;j<result.coldef2.length;j++){
                        					posIndex[pos]=result.coldef1.length+j;
                        					pos++;
                        				}
                    				}
                    			
                    			
                    			}else{
                    				if(result.selectList[i].main.equalsIgnoreCase(result.t1)||result.selectList[i].main.equalsIgnoreCase(result.Alias1)){
                    					for(int j=0;j<result.coldef1.length;j++){
                        					if(result.coldef1[j].cname.equalsIgnoreCase(result.selectList[i].sub)){
                        						posIndex[pos]=j;
                        						pos++;
                        					}
                        				}
                    				}else{
                    					for(int j=0;j<result.coldef2.length;j++){
                        					if(result.coldef2[j].cname.equalsIgnoreCase(result.selectList[i].sub)){
                        						posIndex[pos]=result.coldef1.length+j;
                        						pos++;
                        					}
                        				}
                    				}
                    				
                    			}
                    			
                    		}

        			
        			}
        			
        			tableObj=new Object[result.resultTable1.size()][resultAttrLen];
        			int i=0;
        			for(int key: result.resultTable1.keySet()){
        				String[] temp=result.resultTable1.get(key);
        				String[] re=new String[ resultAttrLen];
        				for(int k=0;k<posIndex.length;k++){
        					re[k]=temp[posIndex[k]];
        				}
        				tableObj[i]=(Object[])re;
        				i++;
            	  }
        		
        			//---------------------------end join case----
        			
        			
        		}
        
        	}
    		
      
			return tableObj;
        
        }
        
        public static String[] displaySelectResultAttr(Return result){
          //  System.out.println(result.new_order);
            return result.new_order;            

        }
        
        public static String[] displaySelectResultAttr_indexing(Result result){
        	String [] attri_result = null;
    		attri_result=new String[result.selectList.length];
    	//	System.out.println(","+result.selectList[0].main);
    		if(result.selectList.length==1&&(result.selectList[0].main.equalsIgnoreCase("count")||result.selectList[0].main.equalsIgnoreCase("sum"))){
        		attri_result[0]=new String(result.selectList[0].main+"("+result.selectList[0].sub+")");	
        	}else{
        		//----------------start non count case
        		int index=0;
            	for (int i=0;i<result.selectList.length;i++){
            		if(result.selectList[i].sub==null){
            			
            			if(result.selectList[i].main.equalsIgnoreCase("*")){
            			    if(result.coldef2!=null){
            			    	String[] temp=new String[attri_result.length-1+result.coldef1.length+result.coldef2.length];
    	        				for(int j=0;j<index;j++){
    	        					temp[j]=attri_result[j];
    	        				}
    	        				for(int k=0;k<result.coldef1.length;k++){
    	        					temp[index]=result.t1+"."+result.coldef1[k].cname;
    	        					index++;
    	        				}
    	        				for(int f=0;f<result.coldef2.length;f++){
    	        					temp[index]=result.t2+"."+result.coldef2[f].cname;
    	        					index++;
    	        				}
    	        				attri_result=temp;
            			    }else{
    	        				String[] temp=new String[attri_result.length-1+result.coldef1.length];
    	        				for(int j=0;j<index;j++){
    	        					temp[j]=attri_result[j];
    	        				}
    	        				for(int k=0;k<result.coldef1.length;k++){
    	        					temp[index]=result.coldef1[k].cname;
    	        					index++;
    	        				}
    	        				attri_result=temp;
            			    }
            				
            			}else{
            				attri_result[index]=result.selectList[i].main;
            				index++;
            			}
            			
            			
            		}else{
            			if(result.selectList[i].sub.equalsIgnoreCase("*")){
            				ColumnDef[] attriListAdd;
            				String[] temp;
            				String addTableName;
            				if(result.selectList[i].main.equalsIgnoreCase(result.t1)||result.selectList[i].main.equalsIgnoreCase(result.Alias1)){
            					attriListAdd=result.coldef1;
            					if(result.selectList[i].main.equalsIgnoreCase(result.Alias1)){
            						addTableName=result.Alias1;
            					}else
            						addTableName=result.t1;
            				}
            				else{
            					attriListAdd=result.coldef2;
            					if(result.selectList[i].main.equalsIgnoreCase(result.Alias2)){
            						addTableName=result.Alias2;
            					}else
            						addTableName=result.t2;
            				}
            				
            				temp=new String[ attri_result.length-1+attriListAdd.length];
            				for(int j=0;j<index;j++){
            					temp[j]=attri_result[j];
            				}
            				for(int k=0;k<attriListAdd.length;k++){
            					temp[index]=addTableName+"."+attriListAdd[k].cname;
            					index++;
            				}
            				
            				attri_result=temp;
            				
            				
            			}else{
            				attri_result[index]=result.selectList[i].main+"."+result.selectList[i].sub;
            				index++;
            			}
            		}
            			
            	}
                         
                	
            	//end non count case------------
        	}
        	
        	
        	
        	return attri_result;
        
          }
        
	
	
	
}
