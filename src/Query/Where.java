package Query;
//import minidbms.*;

import java.util.HashMap;
import minidbms.Table;


public class Where 
{
    public String [] tcname;//table and column name together
    public String [] tname;//table name
    public String [] talias;//alias
    public String [] cname;//column name
    public String [] operator;
    public String [] value;
    public String  and_or = "none";
    
    
    public void and_or (Query q)
    {
        if(q.where == null || q.where.equalsIgnoreCase(""))
        {
            and_or = "none";
            q.where = "";
        }
        else if(q.where.contains(" AND ") || q.where.contains(" and "))
        {
            and_or = "and";
        }
        else if (q.where.contains(" OR ") || q.where.contains(" OR "))
        {
            and_or = "or";
        }
        else
        {
            and_or = "none";
        }
        
    }
    
    public String operator (String q)
    {
        
        String operator = "";
        
        if(q.contains(" = "))
        {
            operator = "=";
        }
        else if (q.contains(" <> "))
        {
            operator = "<>";
        }
        else if (q.contains(" > "))
        {
            operator = ">";
        }
        else if (q.contains(" < "))
        {
            operator = "<";
        }
        
        
        return operator;
    }
    
    
    public void decomp_where (String [] q, From f,HashMap<String,Table> t) throws Exception
    {
        for(int x= 0;x<q.length;++x)
        {
            
            String temp_op = operator(q[x]);
            
            String [] criteria_parts = q[x].split(" "+temp_op+" ");//split statement by operator
            operator[x] = temp_op;
            
            String [] column_parts = criteria_parts[0].split("\\.");
            
            if (column_parts.length > 1)
            {    
                value[x] = criteria_parts[1];// assign value of where
                cname[x] = column_parts[1];
                
                
                for (int y=0;y<f.tname.length;++y)
                {    
                    if(column_parts[0].equalsIgnoreCase(f.tname[y]) || column_parts[0].equals(f.talias[y]))//if the first part equlas the table name or table alias name
                    {
                        tname[x] = f.tname[y];
                        talias[x] = f.talias[y];
                        tcname[x]  = tname[x]+"."+cname[x];
                    }
                }
            }
            else
            {
                if(criteria_parts.length > 1)
                {
                    value[x] = criteria_parts[1];// assign value of where
                }
                
                cname[x] = column_parts[0];
                
                
                for(int y = 0; y < f.tname.length; ++y)//assign table name to variable
                {
                    Table t2 = t.get(f.tname[y].toUpperCase());

                    for(int a = 0; a < t2.cdata.length; ++a)
                    {
                        if(t2.cdata[a].cname.equalsIgnoreCase(cname[x]))
                        {
                            tname[x] = f.tname[y];
                            tcname[x]  = tname[x]+"."+cname[x];
                        }
                    }
                }
                
                
                
            }
        }
    
    
    }

    public void assign (Query q, From f,HashMap<String,Table> t) throws Exception
    {
        
        if(and_or.equals("and"))
        {
            if(q.where.contains(" AND "))
            {
                String[] where_and = q.where.split(" AND ");
                tname=new String[where_and.length];
                cname=new String[where_and.length];
                tcname=new String[where_and.length];
                talias=new String[where_and.length];
                value=new String[where_and.length];
                operator=new String[where_and.length];
                decomp_where(where_and, f,t);
            }
            else
            {
                String[] where_and = q.where.split(" and ");
                tname=new String[where_and.length];
                cname=new String[where_and.length];
                tcname=new String[where_and.length];
                talias=new String[where_and.length];
                value=new String[where_and.length];
                operator=new String[where_and.length];
                decomp_where(where_and, f,t);
            }
            
        }
        else if (and_or.equals("or"))
        {
            if(q.where.contains(" OR "))
            {
                String[] where_or = q.where.split(" OR ");
                tname=new String[where_or.length];
                cname=new String[where_or.length];
                tcname=new String[where_or.length];
                talias=new String[where_or.length];
                value=new String[where_or.length];
                operator=new String[where_or.length];
                decomp_where(where_or, f,t);
            }
            else
            {
                String[] where_or = q.where.split(" or ");
                tname=new String[where_or.length];
                cname=new String[where_or.length];
                tcname=new String[where_or.length];
                talias=new String[where_or.length];
                value=new String[where_or.length];
                operator=new String[where_or.length];
                decomp_where(where_or, f,t);
            }
        }
        else if (and_or.equals("none"))
        {
            String [] where_none = {q.where};
            tname=new String[where_none.length];
            cname=new String[where_none.length];
            tcname=new String[where_none.length];
            talias=new String[where_none.length];
            value=new String[where_none.length];
            operator=new String[where_none.length];
            decomp_where(where_none, f,t);
        }
    }
}
