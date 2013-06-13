package Indexing;

import java.util.HashMap;

import minidbms.ColumnDef;



import Query.selectItem;

public class Result {
	
	    public HashMap<Integer,String[]> resultTable1 = new HashMap<Integer,String[]>();
	  
	    public ColumnDef[] coldef1;
	    public ColumnDef[] coldef2;
	    public String t1;
	    public String t2;
	    public String Alias1;
	    public String Alias2;
	    public selectItem[] selectList;
	    public boolean specialCountSum=false;
	    
	    public Result(selectItem[]_selectList,ColumnDef[] _coldef1,ColumnDef[] _coldef2,String _t1,String _t2,String _Alias1,String _Alias2){
	    	this.selectList=_selectList;
	    	this.coldef1=_coldef1;
	    	this.coldef2=_coldef2;
	    	this.t1=_t1;
	    	this.t2=_t2;
	    	this.Alias1=_Alias1;
	    	this.Alias2=_Alias2;
	    }
	    
	    

}
