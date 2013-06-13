package Query;

public class From 
{
    public String [] tname;
    public String [] talias;
    
    public void assign (Query q)
    {
    	String [] comma;
        
    	if( q.from.contains(","))
        {
    		comma = q.from.split(",");
    	}
    	else
        {
    		comma = new String[1];
    		comma[0]= q.from;
    	}
        
    	tname=new String[comma.length];
        talias=new String[comma.length];
        
        for(int x = 0; x < comma.length; x++)
        {
            String [] as;

            if( comma[x].contains(" AS "))
            {
                as = comma[x].split(" AS ");
                tname[x] = as[0];
                talias[x] = as[1];
            }
            else
            {
                tname[x] = comma[x];
            }
            
            if( comma[x].contains(" as "))
            {
                as = comma[x].split(" as ");
                tname[x] = as[0];
                talias[x] = as[1];
            }
            else
            {
                tname[x] = comma[x];
            }
        }
    }  
}
