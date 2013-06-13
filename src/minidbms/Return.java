package minidbms;

import Query.*;
import java.util.HashMap;


public class Return 
{
    public String message="";
    public Table table = new Table();
    public ColumnDef coldef = new ColumnDef();
    
    public int id=0;//copied id value
    public int r_id=0;//result id values
    
    public ColumnDef[] cdata;
    public HashMap<Integer,String[]> rdata = new HashMap<Integer,String[]>();
    public String [] old_order;//original column order
    public String [] new_order;//new  column order depending on columns selected
    public HashMap<Integer,String[]> select_result = new HashMap<Integer,String[]>();
    
    public Return()
    {
   	 
    }
    public Return(String errMessage)
    {
    	 message= errMessage;
    }
    
    public void set_select_result (String[] x) throws Exception
    {
      select_result.put(++r_id, x);
    }
    
    int get_r_id() throws Exception
    {
        return r_id;
    }

    public void assign_order(Select s, Boolean yn, HashMap<String,Table> tables,From f) throws Exception //yn specifies whether to replace old_order or not
    {

        if(s.type.equalsIgnoreCase("none"))
        {
            new_order = new String[s.tcname.length];
            if(yn == true)
            {
                
                if(f.tname.length > 1)
                {
                    old_order = new String[tables.get(f.tname[0].toUpperCase()).cdata.length+tables.get(f.tname[1].toUpperCase()).cdata.length];
                }
                else
                {
                    old_order = new String[cdata.length];
                }
                
                for(int x = 0;x<cdata.length; ++x)//populate old_order array so that entire object array of ColumnDef doesnt have to be used
                {
                    old_order[x] = cdata[x].tname+"."+cdata[x].cname;
                }
            }

            for(int x = 0; x < s.tcname.length; ++x)//populate new_oder array to represent the order in which user wants data displayed
            {
                for(int y = 0; y < old_order.length; ++y )
                {
                    if(s.tcname[x].equalsIgnoreCase(old_order[y]))//y is where it is, x is where we want it to be
                    {
                        new_order[x] = old_order[y];
                    } 
                }
            }

            for(int x = 1; x <= r_id; ++x)//reorder result hashmap to reflect new_order array
            {
                String [] old_row = select_result.get(x);
                String [] new_row =  new String [new_order.length];

                for(int y = 0; y < new_order.length; ++y)
                {
                    for(int a = 0; a < old_order.length; ++a)
                    {
                        if(new_order[y].equalsIgnoreCase(old_order[a]))
                        {
                           new_row[y] = old_row[a]; 
                        }
                    }
                }

                select_result.put(x, new_row);//put new array in correct order into result hashmap
            }
            if(new_order.length == s.parts.length)
            {
                for(int c = 0; c < new_order.length; ++c)
                {
                    new_order[c] = s.parts[c];
                }
            }
            
            
        }
        else if (s.type.equalsIgnoreCase("*"))
        {		

            if(yn == true)
            {
                
                if(f.tname.length > 1)
                {
                    old_order = new String[tables.get(f.tname[0].toUpperCase()).cdata.length+tables.get(f.tname[1].toUpperCase()).cdata.length];
                }
                else
                {
                    old_order = new String[cdata.length];
                }
                
                for(int x = 0;x<cdata.length; ++x)//populate old_order array so that entire object array of ColumnDef doesnt have to be used
                {
                    old_order[x] = cdata[x].tname+"."+cdata[x].cname;
                }
            }
            

            if(s.tname[0].equals(""))
            {
                new_order = new String[old_order.length];
                new_order = old_order;
            }
            else
            {
                
                int new_order_length = 0;// find the length of the new order of a select * or b.*
                for(int y = 0; y < s.cname.length; ++y)
                {
                    new_order_length += tables.get(s.tname[y].toUpperCase()).cdata.length;
                }
                
                new_order = new String[new_order_length];
                int count = 0;
            
                for(int y = 0; y < s.tname.length; ++y)
                {
                    for( int z = 0; z < tables.get(s.tname[y].toUpperCase()).cdata.length; ++z)
                    {
                        new_order[count] = tables.get(s.tname[y].toUpperCase()).cdata[z].tname+"."+tables.get(s.tname[y].toUpperCase()).cdata[z].cname;
                        ++count;
                    }
                }
                
                
                
                for(int x = 1; x <=r_id; ++x)//reorder result hashmap to reflect new_order array
                {
                    String [] old_row = select_result.get(x);
                    String [] new_row =  new String [new_order.length];

                    for(int y = 0; y < new_order.length; ++y)
                    {
                        for(int a = 0; a < old_order.length; ++a)
                        {
                            if(new_order[y].equalsIgnoreCase(old_order[a]))
                            {
                               new_row[y] = old_row[a]; 
                            }
                        }
                    }

                    select_result.put(x, new_row);//put new array in correct order into result hashmap
                }   
            }  
        }
        else if (s.type.equalsIgnoreCase("sum"))
        {
            new_order = new String[1];
            new_order[0] = s.tcname[0];
            int sum = 0;
            
            if(yn == true)
            {
                
                if(f.tname.length > 1)
                {
                    old_order = new String[tables.get(f.tname[0].toUpperCase()).cdata.length+tables.get(f.tname[1].toUpperCase()).cdata.length];
                }
                else
                {
                    old_order = new String[cdata.length];
                }
                
                for(int x = 0; x < cdata.length; ++x)//populate old_order array so that entire object array of ColumnDef doesnt have to be used
                {
                    old_order[x] = cdata[x].tname+"."+cdata[x].cname;
                }
            }
            
            for(int x = 1; x <=r_id; ++x)//reorder result hashmap to reflect new_order array
            {
                 String [] old_row = select_result.get(x);
                
                 for(int y = 0; y < old_order.length; ++y)
                 {
                    if(old_order[y].equalsIgnoreCase(s.tcname[0]))
                    {
                        sum+=Integer.parseInt(old_row[y]);
                    }
                 } 
            }
            
            String [] new_row =  {Integer.toString(sum)};//remove
            select_result.clear();
            r_id=1;
            select_result.put(1, new_row);//put new array in correct order into result hashmap
            new_order[0] = "sum("+s.cname[0]+")";
        }
        else if (s.type.equalsIgnoreCase("count"))
        {
            new_order = new String[1];
            new_order[0] = "count("+s.cname[0]+")";
            String [] new_row =  {Integer.toString(r_id)};
            select_result.clear();
            r_id=1;
            select_result.put(1, new_row);//put new array in correct order into result hashmap
        }  
    }
    
}
