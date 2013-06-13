package Query;
import GUI.GUIhelper;
import GUI.SimpleGUI;

import minidbms.*;
import parser.*;
import GUI.*;
import Indexing.HandleSelectByIndexing;
import Indexing.Result;
import Indexing.Table_obj_index;

import java.text.DecimalFormat;
import java.util.HashMap;





public class QueryValidation {
	
	public static selectItem[] selectList;
	public static fromItem[] fromList;
	public static whereItem whereList=null;
	
	public static String find_alias_tableName(String alias){
	    for (int i=0;i<fromList.length;i++){
	    	if(fromList[i].sub!=null&&fromList[i].sub.equalsIgnoreCase(alias)){
	    		return fromList[i].main;
	    	}
	    }
		return null;
	}
	
	public static String find_table_argumentIn(String argName){
		
		return null;
	}
	
	public static boolean isInteger(String string) {
	    try {
	        Integer.valueOf(string);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
        
        
	public static  Return selectValidation(SimpleNode node, Database d,int QueryNo) throws Exception{	
		
		whereList=null;
		
		GUI.SimpleGUI.result.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {
	                    {null, null, null, null},
	                    {null, null, null, null},
	                    {null, null, null, null},
	                    {null, null, null, null}
	                },
	                new String [] {
	                    "Title 1", "Title 2", "Title 3", "Title 4"
	                }
	            ));//clear previous old record in the table
		
		//traverse to get the data of the tree for validation
		SimpleNode node_clone=node;
		
		
		//select list
		SimpleNode selectListPt_=(SimpleNode)node_clone.jjtGetChild(0);
		if(selectListPt_.jjtGetNumChildren()==0&&selectListPt_.jjtGetValue().toString().equalsIgnoreCase("*")){
			
			selectList=new selectItem[1];
			selectList[0]=new selectItem();
			selectList[0].main=new String("*");
			
		}else if(selectListPt_.jjtGetNumChildren()==1&&selectListPt_.jjtGetChild(0).toString().equalsIgnoreCase("count")){
			selectList=new selectItem[1];
			selectList[0]=new selectItem();
			selectList[0].main="count";
			if(selectListPt_.jjtGetChild(0).jjtGetNumChildren()!=0){
				//return new Return("We haven't supported count(alias)!");
			    SimpleNode alisSumCount=(SimpleNode)selectListPt_.jjtGetChild(0).jjtGetChild(0);
			  // System.out.println(alisSumCount.toString());
			    selectList[0].sub=((SimpleNode)alisSumCount.jjtGetChild(0)).jjtGetValue().toString()+"."+((SimpleNode)alisSumCount.jjtGetChild(1)).jjtGetValue().toString();
			}else
			   selectList[0].sub=((SimpleNode)selectListPt_.jjtGetChild(0)).jjtGetValue().toString();
		}else if (selectListPt_.jjtGetNumChildren()==1&&selectListPt_.jjtGetChild(0).toString().equalsIgnoreCase("sum")){
			selectList=new selectItem[1];
			selectList[0]=new selectItem();
			selectList[0].main="sum";
			if(selectListPt_.jjtGetChild(0).jjtGetNumChildren()!=0){
				 //return new Return("We haven't supported sum(alias)!");
				   SimpleNode alisSumCount=(SimpleNode)selectListPt_.jjtGetChild(0).jjtGetChild(0);
				   selectList[0].sub=((SimpleNode)alisSumCount.jjtGetChild(0)).jjtGetValue().toString()+"."+((SimpleNode)alisSumCount.jjtGetChild(1)).jjtGetValue().toString();
			}else
				selectList[0].sub=((SimpleNode)selectListPt_.jjtGetChild(0)).jjtGetValue().toString();
		}else{
			selectList=new selectItem[selectListPt_.jjtGetChild(0).jjtGetNumChildren()];
			SimpleNode iterativeNode;
			for (int i=0;i<selectListPt_.jjtGetChild(0).jjtGetNumChildren();i++){
				iterativeNode=(SimpleNode)selectListPt_.jjtGetChild(0).jjtGetChild(i);
				selectList[i]=new selectItem();
				//System.out.println(iterativeNode.toString());
				if(iterativeNode.jjtGetNumChildren()==0){
					selectList[i].main=iterativeNode.jjtGetValue().toString();
				}else if(iterativeNode.jjtGetChild(0).toString().equalsIgnoreCase("Alis_AST")){
					selectList[i].main=((SimpleNode)iterativeNode.jjtGetChild(0).jjtGetChild(0)).jjtGetValue().toString();
					selectList[i].sub="*";
				}else{
					selectList[i].main=((SimpleNode)iterativeNode.jjtGetChild(0).jjtGetChild(0)).jjtGetValue().toString();
					selectList[i].sub=((SimpleNode)iterativeNode.jjtGetChild(0).jjtGetChild(1)).jjtGetValue().toString();
				}
				
			}
		}
/*for debug	
		for (int i=0;i<selectList.length;i++){
			selectList[i]. print(); 
		}*/
		
		//from list
		SimpleNode fromListPt_=(SimpleNode)node_clone.jjtGetChild(1);
		fromList=new fromItem[fromListPt_.jjtGetNumChildren()];
		for(int i=0;i<fromListPt_.jjtGetNumChildren();i++){
			fromList[i]=new fromItem();
			String tableName=((SimpleNode)fromListPt_.jjtGetChild(i)).jjtGetValue().toString();
			String[] splitName=tableName.split(" ");
			if (splitName.length==2){
				fromList[i].main=splitName[0];
				fromList[i].sub=splitName[1];
			}else
				fromList[i].main=splitName[0];
		}
	/*testing
		for (int i=0;i<fromList.length;i++){
			fromList[i]. print(); 
		}*/
		
		//where list
		if(node_clone.jjtGetNumChildren()==3){
			SimpleNode whereListPt_=(SimpleNode)node_clone.jjtGetChild(2);
			whereList=new whereItem();
			int i=0;//number of conditions
			
			if(whereListPt_.jjtGetValue()!=null){
				whereList.logicOp=whereListPt_.jjtGetValue().toString();
				i=2;
			}else
				i=1;
			
			for (int j=0;j<i;j++){
				SimpleNode condition =(SimpleNode)whereListPt_.jjtGetChild(j);
				whereList.con[j]=new conditionItem();
				whereList.con[j].logicOp=condition.jjtGetValue().toString();
				
				if(((SimpleNode)condition.jjtGetChild(0)).jjtGetNumChildren()==0){
					whereList.con[j].LOperand.main=((SimpleNode)condition.jjtGetChild(0)).jjtGetValue().toString();
				}else{
					//whereList.join[j]=true;
					whereList.con[j].LOperand.main=((SimpleNode)condition.jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).jjtGetValue().toString();
					whereList.con[j].LOperand.sub=((SimpleNode)condition.jjtGetChild(0).jjtGetChild(0).jjtGetChild(1)).jjtGetValue().toString();
				}
				
				if(((SimpleNode)condition.jjtGetChild(1)).jjtGetNumChildren()==0){
					whereList.con[j].ROperand.main=((SimpleNode)condition.jjtGetChild(1)).jjtGetValue().toString();
				}else{
					whereList.join[j]=true;
					whereList.con[j].ROperand.main=((SimpleNode)condition.jjtGetChild(1).jjtGetChild(0).jjtGetChild(0)).jjtGetValue().toString();
					whereList.con[j].ROperand.sub=((SimpleNode)condition.jjtGetChild(1).jjtGetChild(0).jjtGetChild(1)).jjtGetValue().toString();
				}	
			}
			
		}
		
	/*	//for testing 
		if(whereList!=null)
		  whereList.print();
		*/
		
		
		//check validity
		
		//first check the form list validity
		for (int i=0;i<fromList.length;i++){
	 		String tableName=new String(fromList[i].main).toUpperCase();
	 		Table table_to_be_inserted= (Table)d.tables.get(tableName);//retrieve the table with name test
			if(table_to_be_inserted==null)
				return new Return ("No such table ["+tableName+"] to be selected from!");
			for (int j=i+1;j<fromList.length;j++){
				if((fromList[i].sub!=null&&fromList[j].sub!=null)&&fromList[i].sub.equalsIgnoreCase(fromList[j].sub)){
					return new Return ("Duplicated alias name "+fromList[j].sub+" !");
				}
				
			}
		}
		
		//then check the validity of the select list arguments
		for (int i=0;i<selectList.length;i++){
			if(selectList[i].sub==null){
				if (selectList[i].main.equals("*")==false){
					boolean valid=false;
					int inNumTable=0;
					for(int j=0;j<fromList.length;j++){
						String tableName=new String(fromList[j].main).toUpperCase();
				 		Table from_table= (Table)d.tables.get(tableName);
				 		//System.out.println(from_table.argument_in_schema(selectList[i].main));
				 		if(from_table.argument_in_schema(selectList[i].main)==true)
				 		{
				 			valid=true;
				 			inNumTable++;
				 		}
					}
					if(valid ==false)
						return new Return ("The argument ["+selectList[i].main+"] is not in the table selected!");
					if(inNumTable>1)
						return new Return ("The argument ["+selectList[i].main+"] is in more than one tables, alias must be used!");
				}
			}else if (selectList[i].main.equalsIgnoreCase("count")||selectList[i].main.equalsIgnoreCase("sum")){
				if (selectList[i].sub.equals("*")==false){
					boolean valid=false;
					int inNumTable=0;
					if(selectList[i].sub.contains(".")){
						//System.out.println(selectList[i].sub);
						String temp[]=new String[2];
						temp=selectList[i].sub.split("\\.");
						System.out.println(temp[0]);
						for(int j=0;j<fromList.length;j++){
							if(fromList[j].main.equalsIgnoreCase(temp[0])||(fromList[j].sub!=null&&fromList[j].sub.equalsIgnoreCase(temp[0]))){
								String tableName;
							
								tableName=fromList[j].main.toUpperCase();
								System.out.println(tableName);
								Table from_table= (Table)d.tables.get(tableName);
								if(from_table.argument_in_schema(temp[1])==true)
						 		{
						 			valid=true;
						 			if(valid==true&&selectList[i].main.equalsIgnoreCase("sum")){
						 				if(from_table.argument_type_in_schema(temp[1]).equalsIgnoreCase("int")==false){
						 					return new Return ("We only sum int type argument");
						 				}
						 			}
						 			inNumTable++;
						 		}
							}
						}
						if(valid ==false)
							return new Return ("The argument ("+selectList[i].sub+") for "+selectList[i].main+" is not in the table selected!");
						if(inNumTable>1)
							return new Return ("The argument ("+selectList[i].sub+") for "+selectList[i].main+" is in more than one tables, alias must be used!");
					
						
					}else{
						for(int j=0;j<fromList.length;j++){
							
							
							
							String tableName=new String(fromList[j].main).toUpperCase();
					 		Table from_table= (Table)d.tables.get(tableName);
					 		//System.out.println(from_table.argument_in_schema(selectList[i].main));
					 		if(from_table.argument_in_schema(selectList[i].sub)==true)
					 		{
					 			valid=true;
					 			if(valid==true&&selectList[i].main.equalsIgnoreCase("sum")){
					 				if(from_table.argument_type_in_schema(selectList[i].sub).equalsIgnoreCase("int")==false){
					 					return new Return ("We only sum int type argument");
					 				}
					 			}
					 			inNumTable++;
					 		}
						}
					}
				
					if(valid ==false)
						return new Return ("The argument ("+selectList[i].sub+") for "+selectList[i].main+" is not in the table selected!");
					if(inNumTable>1)
						return new Return ("The argument ("+selectList[i].sub+") for "+selectList[i].main+" is in more than one tables, alias must be used!");
				}
			}else
			{
				boolean valid=false;
				if (selectList[i].sub.equals("*")==false){
					for(int j=0;j<fromList.length;j++){
						if(fromList[j].main.equalsIgnoreCase(selectList[i].main)||(fromList[j].sub!=null && fromList[j].sub.equalsIgnoreCase(selectList[i].main))){
							String tableName=new String(fromList[j].main).toUpperCase();
					 		Table from_table= (Table)d.tables.get(tableName);
					 		if(selectList[i].sub.equalsIgnoreCase("*")==false&&from_table.argument_in_schema(selectList[i].sub)==false)
					 			return new Return ("The argument ["+selectList[i].main+"."+selectList[i].sub+"] is not in the table ["+fromList[j].main+"]!");
					 		else
					 		{
					 			valid =true;
					 			break;
					 		}
						}		
					}
				}else
				{
					//System.out.println("in book.*");
					for(int j=0;j<fromList.length;j++)
					{
						if(fromList[j].main.equalsIgnoreCase(selectList[i].main)||(fromList[j].sub!=null &&fromList[j].sub.equalsIgnoreCase(selectList[i].main))){
						    valid =true;
							break;
						}
					}
				}
				if (valid ==false)
					return new Return ("The argument ["+selectList[i].main+"."+selectList[i].sub+"] in the select list is not valid!");	
			}
		}
		
		//check validity of conditions
		if (whereList!=null){
			
			int numOfCon=1;
			if(whereList.logicOp!=null){
				numOfCon=2;
			}
			for (int i =0;i<numOfCon;i++){
				//check left operand
				if(whereList.con[i].LOperand.sub==null&&whereList.con[i].LOperand.main.equalsIgnoreCase("count")==false&&whereList.con[i].LOperand.main.equalsIgnoreCase("sum")==false){		
						boolean valid=false;
						int inNumTable=0;
						for(int j=0;j<fromList.length;j++){
							String tableName=new String(fromList[j].main).toUpperCase();
					 		Table from_table= (Table)d.tables.get(tableName);
					 		//System.out.println(from_table.argument_in_schema(selectList[i].main));
					 		if(from_table.argument_in_schema(whereList.con[i].LOperand.main)==true)
					 		{
					 			
					 			whereList.con[i].LOperand.type=from_table.argument_type_in_schema(whereList.con[i].LOperand.main);
					 			valid=true;
					 			inNumTable++;
					 			
					 		}
						}
						if(valid ==false)
							return new Return ("The operand ["+whereList.con[i].LOperand.main+"] in the where clause is not in the table selected!");
						if(inNumTable>1)
							return new Return ("The operand ["+whereList.con[i].LOperand.main+"] is in more than one tables, alias must be used!");
					
				}else
				{
					boolean valid=false;
					for(int j=0;j<fromList.length;j++){
						if(fromList[j].main.equalsIgnoreCase(whereList.con[i].LOperand.main)||(fromList[j].sub!=null && fromList[j].sub.equalsIgnoreCase(whereList.con[i].LOperand.main))){
							String tableName=new String(fromList[j].main).toUpperCase();
					 		Table from_table= (Table)d.tables.get(tableName);
					 		if(from_table.argument_in_schema(whereList.con[i].LOperand.sub)==false)
					 			return new Return ("The operand ["+whereList.con[i].LOperand.main+"."+whereList.con[i].LOperand.sub+"] in the where clause is not in the table ["+fromList[j].main+"]!");
					 		else
					 		{
					 			whereList.con[i].LOperand.type=from_table.argument_type_in_schema(whereList.con[i].LOperand.sub);
					 		//	System.out.println(	whereList.con[i].LOperand.type);
					 			valid =true;
					 			break;
					 		}
						}		
					}
					if (valid ==false)
						return new Return ("The operand ["+whereList.con[i].LOperand.main+"."+whereList.con[i].LOperand.sub+"] in the where clause is not valid!");
				}
				
				//check Right operand
				if(whereList.con[i].ROperand.sub==null){	
					
						//it is the value argument not alias
						String value=whereList.con[i].ROperand.main;
						if(value.endsWith("'")&&value.endsWith("'")){
							if(whereList.con[i].LOperand.type.equalsIgnoreCase("varchar")==false){
								return new Return ("The operand ["+whereList.con[i].ROperand.main+"] type is mismatched with LOperand! Expected: "+whereList.con[i].LOperand.type);
							}
						}else if ( isInteger(value))
						{
							if(whereList.con[i].LOperand.type.equalsIgnoreCase("int")==false){
								return new Return ("The operand ["+whereList.con[i].ROperand.main+"] type is mismatched with LOperand! Expected: "+whereList.con[i].LOperand.type);
							}
						}else
						{
							whereList.join[i]=true;
							boolean valid=false;
							int inNumTable=0;
							for(int j=0;j<fromList.length;j++){
								String tableName=new String(fromList[j].main).toUpperCase();
						 		Table from_table= (Table)d.tables.get(tableName);
						 		//System.out.println(from_table.argument_in_schema(selectList[i].main));
						 		if(from_table.argument_in_schema(whereList.con[i].ROperand.main)==true)
						 		{
						 			
						 			if(whereList.con[i].LOperand.type.equalsIgnoreCase(from_table.argument_type_in_schema(whereList.con[i].ROperand.main))==false){
						 				return new Return ("The operand ["+whereList.con[i].ROperand.main+"] type is mismatched with LOperand! Expected: "+whereList.con[i].LOperand.type);
						 			}
						 			valid=true;
						 			inNumTable++;
						 			
						 		}
							}
							if(valid ==false)
								return new Return ("The operand ["+whereList.con[i].ROperand.main+"] in the where clause is not in the table selected!");
							if(inNumTable>1)
								return new Return ("The operand ["+whereList.con[i].ROperand.main+"] is in more than one tables, alias must be used!");
						}
								
				}else
				{
					boolean valid=false;
					if(whereList.con[i].logicOp.equalsIgnoreCase("=")==false)
						return new Return ("The use of alias in the where cluase in the right operand can only have '=' as the logical operator(we havent support the rest '<', '>')!");
					for(int j=0;j<fromList.length;j++){
						if(fromList[j].main.equalsIgnoreCase(whereList.con[i].ROperand.main)||(fromList[j].sub!=null && fromList[j].sub.equalsIgnoreCase(whereList.con[i].ROperand.main))){
							String tableName=new String(fromList[j].main).toUpperCase();
					 		Table from_table= (Table)d.tables.get(tableName);
					 		//System.out.println(from_table.tname);
					 		if(from_table.argument_in_schema(whereList.con[i].ROperand.sub)==false)
					 			return new Return ("The operand ["+whereList.con[i].ROperand.main+"."+whereList.con[i].ROperand.sub+"] in the where clause is not in the table ["+fromList[j].main+"]!");
					 		else
					 		{
					 			//System.out.println(whereList.con[i].LOperand.type);
					 			//System.out.println(from_table.argument_type_in_schema(whereList.con[i].ROperand.sub));
					 			if(whereList.con[i].LOperand.type.equalsIgnoreCase(from_table.argument_type_in_schema(whereList.con[i].ROperand.sub))==false){
					 				return new Return ("The operand ["+whereList.con[i].ROperand.main+"."+whereList.con[i].ROperand.sub+"] type is mismatched with LOperand! Expected: "+whereList.con[i].LOperand.type);
					 			}
					 			valid =true;
					 			break;
					 		}
						}		
					}
					if (valid ==false)
						return new Return ("The operand ["+whereList.con[i].ROperand.main+"."+whereList.con[i].ROperand.sub+"] in the where clause is not valid!");
				}
				
				
			}
		}

	//---------------------------finish check validity-------------------------
		
		
		
		Query SQLVar=new Query();
		for (int i=0;i<selectList.length;i++){
			if(i==selectList.length-1)
			   SQLVar.select+=selectList[i].print();
			else
				SQLVar.select+=selectList[i].print()+",";
		}
		
		for (int i=0;i<fromList.length;i++){
			if(i==fromList.length-1)
			   SQLVar.from+=fromList[i].print();
			else
				SQLVar.from+=fromList[i].print()+",";
		}
		
		if(whereList!=null)
		  SQLVar.where+=whereList.print();

		//System.out.println(SQLVar.select);
		//System.out.println(SQLVar.from);
		//System.out.println(SQLVar.where);
		
		
		//--------------------------------call------------
		QueryNo=QueryNo+1;
		Return selectResult = new Return();
		Result selectResult_indexing=null;
		if(whereList==null&&selectList[0].main.equalsIgnoreCase("count")==false&&selectList[0].main.equalsIgnoreCase("sum")==false){
			selectResult = d.select(SQLVar);
			
	        //Result tab  for that QueryNo    
	        javax.swing.JScrollPane jScrollPane9=new javax.swing.JScrollPane();
	        javax.swing.JTable Query_specific=new javax.swing.JTable();
	        GUI.SimpleGUI.jTabbedPane_result.addTab("Query "+QueryNo, jScrollPane9);
	        Query_specific.setModel(new javax.swing.table.DefaultTableModel(
					GUIhelper.displaySelectResultData(selectResult),
				    GUIhelper.displaySelectResultAttr(selectResult)
		    ));
	        jScrollPane9.setViewportView(Query_specific);   
		}else{
			if(whereList==null){
				if(selectList[0].main.equalsIgnoreCase("count") ){
					selectResult_indexing=countFunNoWhere(SimpleGUI.d,selectList,fromList);
				}else if(selectList[0].main.equalsIgnoreCase("sum")){
					selectResult_indexing=sumFunNoWhere(SimpleGUI.d,selectList,fromList);
				}
			}else{			
				selectResult_indexing = HandleSelectByIndexing.select(selectList, fromList, whereList);
			}
		
			
			
			  //Result tab  for that QueryNo    
	        javax.swing.JScrollPane jScrollPane9=new javax.swing.JScrollPane();
	        javax.swing.JTable Query_specific=new javax.swing.JTable();
	        GUI.SimpleGUI.jTabbedPane_result.addTab("Query "+QueryNo, jScrollPane9);
	        Query_specific.setModel(new javax.swing.table.DefaultTableModel(
					GUIhelper.displaySelectResultData_indexing(selectResult_indexing ),
				    GUIhelper.displaySelectResultAttr_indexing(selectResult_indexing )
		    ));
	        jScrollPane9.setViewportView(Query_specific);   
			
			
		}
        
        
 

        return new Return("Success: Select Query executed sucessfully!");
		
	}
	
	
	public static Result countFunNoWhere( Database d,selectItem[] selectList,fromItem[] fromList){
		Result result=new Result(selectList,null,null,null,null,null,null);
		if(fromList.length==1){//one table case
			Table_obj_index   table_hashed=SimpleGUI.d_hash.tables_hashed.get(fromList[0].main.toUpperCase() );
			HashMap<String,String[]>   rawMapPri=SimpleGUI.d.tables.get(fromList[0].main.toUpperCase()).priKeyHash;
			if(selectList[0].sub.equalsIgnoreCase("*")){
				String[] temp=new String[1];
				Integer totalCount=new Integer(rawMapPri.size());
				temp[0]=new String(totalCount.toString());
				System.out.println(temp[0]);
				result.resultTable1.put(1, temp);
			}else{
				int index=table_hashed.getIndexOfAtrri(selectList[0].sub);
				HashMap<String, HashMap<String,String[]>> hash=table_hashed.hashList[index];
				HashMap<String,String[]> nullRecord=hash.get("null");
				int nullSize;
				if(nullRecord==null){
					nullSize=0;
				}else{
					nullSize=nullRecord.size();
				}
				String[] temp=new String[1];
				Integer totalCount=new Integer(rawMapPri.size()-nullSize);
				temp[0]=new String(totalCount.toString());
				System.out.println(temp[0]);
				result.resultTable1.put(1, temp);
			}
			
		}else{//two table cases
			
			//nonsense
			
			
			
		}
		
		return result;
	}
	
	public static Result sumFunNoWhere( Database d,selectItem[] selectList,fromItem[] fromList){
		Result result=new Result(selectList,null,null,null,null,null,null);
		if(fromList.length==1){//one table case
			Table_obj_index   table_hashed=SimpleGUI.d_hash.tables_hashed.get(fromList[0].main.toUpperCase() );
			int index=table_hashed.getIndexOfAtrri(selectList[0].sub);
			Table raw=SimpleGUI.d.tables.get(fromList[0].main.toUpperCase());
			int sum=0;
			for(String key:raw.priKeyHash.keySet()){
				String[] temp=raw.priKeyHash.get(key);
				if(temp[index]!=null){
					sum+=Integer.parseInt(temp[index]);
				}
			}
			String[] temp=new String[1];
			Integer totalSum=new Integer(sum);
			temp[0]=new String(totalSum.toString());
			result.resultTable1.put(1, temp);
			
		}else{
			//two table case, nonsense
		}
		return result;
	}
	
	
	
	
	
	

}
