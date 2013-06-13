package minidbms;

import java.io.Serializable;
import java.util.HashMap;

public class Table implements Serializable
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String tname; //table name
    public int id=0;
    public ColumnDef[] cdata ;
    public HashMap<Integer,String[]> rdata = new HashMap<Integer,String[]> ();
    public HashMap<String,String[]> priKeyHash=new HashMap<String,String[]> ();
    
    public void set_rdata (String[] x) throws Exception
    {
      rdata.put(++id, x);
    }
    
     public String [] get_row (int x) throws Exception
    { 
        return rdata.get(x);
    }
    
    int get_id() throws Exception
    {
        return id;
    }
    
    public boolean argument_in_schema(String arg) throws Exception{
    	
    	for (int i=0;i<cdata.length;i++)
        {
    		if(cdata[i].get_cname().equalsIgnoreCase(arg))
                {
    			return true;
    		}
    	}
    	
	return false;
    }
    
    public String argument_type_in_schema(String arg) throws Exception
    {
    	
    	for (int i=0;i<cdata.length;i++)
        {
    		if(cdata[i].get_cname().equalsIgnoreCase(arg))
                {
    			return cdata[i].get_ctype_noSize();
    		}
    	}
    	
	return null;
    } 
}