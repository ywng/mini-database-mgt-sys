package minidbms;

import java.io.Serializable;

public class ColumnDef implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//the ltter c indicates the word column
    public  String tname;
    public  String cname;
    public  String ctype;
    public  Boolean cpk = false;
    public  Boolean cfk = false;
    public  Boolean cnull = true;
    
    /////////////////////////////////////////////////////////////////
    public String get_tname () throws Exception
    {
        return tname;
    }
    
    public String get_cname () throws Exception
    {
        return cname;
    }
    
    public String get_ctype () throws Exception
    {
        return ctype;
    }
    
    public String get_ctype_noSize() throws Exception
    {
    	String[] temp = new String (ctype).split(" ");
        return temp[0];
    }
    
    public Boolean get_cpk () throws Exception
    {
        return cpk;
    }
    
    public Boolean get_cfk () throws Exception
    {
        return cfk;
    }
    
    public Boolean get_cnull () throws Exception
    {
        return cnull;
    }
    ////////////////////////////////////////////////////////////////////
    public void set_tname (String x) throws Exception
    {
        tname = x;
    }
    
    public void set_cname (String x) throws Exception
    {
        cname = x;
    }
    
    public void set_ctype (String x) throws Exception
    {
        ctype = x;
    }
    
    public void set_cpk (Boolean x) throws Exception
    {
        cpk = x;
    }
    
    public void set_cfk (Boolean x) throws Exception
    {
        cfk = x;
    }
    
    public void set_cnull (Boolean x) throws Exception
    {
        cnull = x;
    }
    /////////////////////////////////////////////////////////////////////
    
}
