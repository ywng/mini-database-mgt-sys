package minidbms;

import GUI.*;
import Query.QueryValidation;

import java.io.FileReader;
import java.io.InputStreamReader;

import javax.swing.UIManager;

import parser.*;
import javax.swing.UnsupportedLookAndFeelException;

public class MiniDBMS 
{
    public static SimpleGUI sGui;
    public static void main(String[] args) throws Exception
    {    	
    	//GUI
    	 try {
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         //} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) { // ignore exceptions and continue
         } catch (Exception e) { // ignore exceptions and continue
         }
         SimpleGUI sGui = new SimpleGUI();
         sGui.setVisible(true);
         
    	/* InputStreamReader inputStream = null;
         
         try {
           if (args.length == 1) { 
             System.out.println("\nReading from file: " + args[0]);
             inputStream =  new FileReader(args[0]);		  
           }
           else {
             inputStream=new InputStreamReader(System.in);
             System.out.println("\nReading from user: ");
           }

           
         // Create parser and parse program
  		 SQL_parser parser =  new SQL_parser(inputStream);
  		 SimpleNode rootNode=parser.SqlScript();
  		 eval_print(rootNode,"");
  		Database d = new Database();
  	   //eval the SQL
        eval(rootNode,d);
  		 
  		 //SimpleNode ff=(SimpleNode)rootNode.jjtGetChild(0).jjtGetChild(0);
  		// System.out.println(ff.jjtGetValue());
           //rootNode.dump(" ");
           System.out.println("\nProgram parsed successfully.\n");

         }catch (java.io.FileNotFoundException e) {
           System.out.println("Main : " + e.getMessage());
           System.exit(0);
         }
         
         
      */
        
       // d.create("test", create_ColumnDef_arr(d));
       // d.insert("test", set_values());
        
     
         System.exit(0);
    }
    
    

     
     
 	public static void eval_print(SimpleNode rootNode,String inden){
 		if(rootNode!=null){
 			System.out.println(inden+rootNode.toString() + "<"+rootNode.jjtGetValue()+">");
 			String newInden=inden+"  ";
 			for (int i=0;i<rootNode.jjtGetNumChildren();i++){
 				eval_print((SimpleNode)rootNode.jjtGetChild(i),newInden);
 			}
 		}
 		
 	}
 	
 	public static Return eval(SimpleNode rootNode, Database d, int QueryNo) throws Exception{
 		if(rootNode!=null){
 			if(rootNode.toString().equals("SqlOperator")){
 				rootNode=(SimpleNode) rootNode.jjtGetChild(0);
 				String command=rootNode.toString();
 				
 				if(command.equalsIgnoreCase("Create")) 
 					return CreateTableEval(rootNode,d);
 				if(command.equalsIgnoreCase("Insert")) 
 					return InsertEval(rootNode,d);		
 				if(command.equalsIgnoreCase("Query")) 
 					return QueryValidation.selectValidation(rootNode,d,QueryNo);	
 				
 		    }else{
	 				Return returnMessage=new Return();
	 				//System.out.println(rootNode.jjtGetNumChildren());
	 				for(int i=0;i<rootNode.jjtGetNumChildren();i++){
	 				  returnMessage=eval((SimpleNode)rootNode.jjtGetChild(i),d,i);
	 				  if(returnMessage.message.substring(0, 7).equals("Success")==false){
	 					 return returnMessage;
	 				  }
	 				}
	 				return returnMessage;
 		    }
 			
 		}
		return new Return();
 		
 	}
 	
 	public static Return CreateTableEval(SimpleNode node, Database d) throws Exception{
 		String table_name=(String) node.jjtGetValue();
 		node=(SimpleNode)node.jjtGetChild(0);
 		int Num_attribute=node.jjtGetNumChildren();
 		//System.out.println(Num_attribute);
 		ColumnDef[] attri_List=new ColumnDef[Num_attribute];
 		for(int i=0;i<attri_List.length;i++){
 			attri_List[i]=new ColumnDef();
 		}
 		for(int i=0;i<Num_attribute;i++){
 			SimpleNode attri_value_node=(SimpleNode)node.jjtGetChild(i);
 			attri_List[i].set_cname(attri_value_node.jjtGetValue().toString().toUpperCase());
 			//System.out.println((String)attri_value_node.jjtGetValue());
 			SimpleNode attri_type_node=(SimpleNode)attri_value_node.jjtGetChild(0);
 			String attri_type_and_primary=(String)attri_type_node.jjtGetValue();
 			String value_arr[];
 			value_arr=attri_type_and_primary.split(" ");
 			if(attri_type_node.toString().equals("Attri_int")){
 				
 				attri_List[i].set_ctype("INT");
 				if(value_arr.length>1&&value_arr[1].equalsIgnoreCase("primary")){
 					attri_List[i].set_cpk(true);
 					attri_List[i].set_cnull(false);
 				}
 			}else{
 				if(value_arr.length>2&&value_arr[2].equalsIgnoreCase("primary")){
 					attri_List[i].set_cpk(true);
 					attri_List[i].set_cnull(false);
 				}
 				attri_List[i].set_ctype(value_arr[0].toUpperCase()+" "+value_arr[1]);
 			}
 		}
 		
 		//System.out.println(attri_List[0].get_cname()+"  "+attri_List[1].get_cname());
 		return d.create(table_name.toUpperCase(),attri_List);
 	}
 	
 	public static Return InsertEval(SimpleNode node, Database d) throws Exception{
 		//System.out.println("in insert SQL");
 		
 		String tableName=node.jjtGetValue().toString().toUpperCase();
 		String attr_list[];
 		String insert_value_list[];
 		SimpleNode valueListNode;
 		
 		Table table_to_be_inserted= (Table)d.tables.get(tableName);//retrieve the table with name test
		if(table_to_be_inserted==null)
			return new Return ("No such table to be inserted!");
			
 		if(node.jjtGetNumChildren()==1){//no attribute list given case
 			valueListNode=(SimpleNode)node.jjtGetChild(0);
 			
 			if(table_to_be_inserted.cdata.length!=valueListNode.jjtGetNumChildren())
 				return new Return ("Number of arugments doesnt matched!");
 			
 			attr_list=new String [table_to_be_inserted.cdata.length];
 			for (int i=0;i<table_to_be_inserted.cdata.length;i++){
 				attr_list[i]=new String(table_to_be_inserted.cdata[i].get_cname());
 			}
 			
 		}else{//with the assigned order of attribute inserted
 			valueListNode=(SimpleNode)node.jjtGetChild(1);
 			SimpleNode attrListNode=(SimpleNode)node.jjtGetChild(0);
 			
 			if(attrListNode.jjtGetNumChildren()!=valueListNode.jjtGetNumChildren())
 				return new Return ("Number of arugments doesnt matched!");
 			
 			attr_list=new String [attrListNode.jjtGetNumChildren()];
 			for (int i=0;i<attrListNode.jjtGetNumChildren();i++){
 				String str=(String) ((SimpleNode)attrListNode.jjtGetChild(i)).jjtGetValue();
 				attr_list[i]=new String(str.toUpperCase());
 			}
 		}
 		
 		insert_value_list=new String[valueListNode.jjtGetNumChildren()];
		for(int i=0;i<valueListNode.jjtGetNumChildren();i++){
			SimpleNode valueNode=(SimpleNode)valueListNode.jjtGetChild(i);
			if(valueNode.jjtGetValue()==null){//string case
				valueNode=(SimpleNode)valueNode.jjtGetChild(0);
				String str=(String)valueNode.jjtGetValue();
				//System.out.println(str);
				insert_value_list[i]=new String(str);
			}else{//int case
				insert_value_list[i]=new String((String)valueNode.jjtGetValue());
			}
		}
	//	System.out.println(tableName);
		//for(int i=0;i<attr_list.length;i++){
	//		System.out.println(attr_list[i]+"  "+insert_value_list[i]);
	//	}
		return d.insert(tableName,attr_list, insert_value_list);
 		
 	}
 	
 	

}


