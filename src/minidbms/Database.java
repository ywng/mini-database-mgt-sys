package minidbms;
import Query.*;

import java.io.Serializable;
import java.util.HashMap;

public class Database implements Serializable
{
      /**
	 * 
	 */
	private static final long serialVersionUID = -7189434153921522944L;
	public HashMap<String,Table> tables = new HashMap<String,Table>();
    
    
    public ColumnDef create_ColumnDef(String cname, String ctype, Boolean cpk, Boolean cfk, Boolean cnull ) throws Exception
    {
        ColumnDef b = new ColumnDef();
        b.set_cname(cname);
        b.set_ctype(ctype);
        b.set_cpk(cpk);
        b.set_cfk(cfk);
        b.set_cnull(cnull);
    
        return b;
    }

    static public Return create_Table(ColumnDef[] b) throws Exception
    {
        Return r = new Return();
        int count=0;
        for (int x=0;x<b.length;++x)
        {
            if(b[x]!=null && b[x].get_cpk())
            {
                ++count;
            }
        }
        
        if(count>1)
        {
            r.message = "Error: Duplicate primary keys have been defined.";
            return r;
        }
        else
        {
            r.message = "Success";
            r.table.cdata = b;
            return r;
        }
    }
    
    
    Return create (String t, ColumnDef[] b) throws Exception// t is table name and b is array of table  meta data   
    { 
        for (int x = 0; x < b.length; ++x)
        {
            b[x].tname = t;
        }

        Return r = new Return();
        t=t.toUpperCase();
        if(tables.containsKey(t))
        {
            r.message = "Error: Table already exists.";
            return r;
        }
        else
        {
            Table a = new Table();
            
            r = create_Table (b);
            
            if(r.message.contains("Error"))
            {
                return r;
            } 
            else
            {
                a = r.table;
                a.tname = t;
                tables.put(t, a);
                r.message = "Success: Table successfully added.";
                return r;
            }
        }
    }
    
    Return insert (String t, String[] c,String[] r) throws Exception
    {
        Table a;
        String priKeyStrVal=null;
        
        String[] r2 = new String[5];
        a = (Table)tables.get(t.toUpperCase());//retrieve the tabel with name test
        Return e = new Return();
        Boolean pk1 = false;//check if database has a primary key
        Boolean pk2 = false;//check if primary key is included in insert list
        
      
        for(int x=0;x<c.length;++x)
        {
        	
            for(int y=0;y<a.cdata.length;++y)
            {
                if(a.cdata[y].get_cpk())//check if database has a primary key
                {
                        pk1 = true;
                }

                if (c[x].equalsIgnoreCase(a.cdata[y].get_cname()))
                {
                    r2[y]=new String (r[x]);
                    if(a.cdata[y].get_ctype().toLowerCase().contains("int"))
                    {
                        //System.out.println("\n\n"+r[x]+"\n\n");
                        if(r[x].startsWith("\'") && r[x].endsWith("\'"))
                        {
                            e.message = "Error: Type mismatch in varible list for "+c[x]+" column.";
                            return e;  
                        }
                    }
                    else if(a.cdata[y].get_ctype().toLowerCase().contains("varchar"))
                    {
                    	//System.out.println("\n\n"+r[x]+"\n\n");
                        if(r[x].startsWith("\'") && r[x].endsWith("\'"))
                    	{
                    		r[x]=r[x].substring(1, r[x].length()-1);
                    		r2[y]=r2[y].substring(1, r2[y].length()-1);
                    		//System.out.println(r2[y]);
                    	}
                    	else
                    	{
                    		e.message = "Error: Type mismatch in varible list for "+c[x]+" column.";
                            return e;
                    	}
                    	
                    	String num = a.cdata[y].get_ctype().replaceAll("\\D+","");//get the varchar number
                       int num2 = Integer.parseInt(num);
                       
                       if(r[x].length()>num2)
                       {
                           e.message = "Error: "+r[x]+" is too large to be inserted into a "+ a.cdata[y].get_ctype()+"." ;
                           return e;  
                       }
                    }
    
                    if(a.cdata[y].get_cpk())//check if this variable is primary key
                    {
                        pk2 = true;
                        priKeyStrVal=r[x];
                        if(a.priKeyHash.containsKey(r[x])){
                        	e.message = "Error: Primary key value already exists. Primary key cannot be duplicated.";
                            return e; 
                        }
                       /* for(int f=1;f<=a.get_id();++f)//check if the primary key values are duplicated
                        {
                            String[] s = (String[])a.rdata.get(f);

                            if(s[y].equals((r[x])))
                            {
                                e.message = "Error: Primary key value already exists. Primary key cannot be duplicated.";
                                return e; 
                            }
                        }
                        */
                    }   
                }
            }
        }
 
        if(pk1)
        {
            if(pk2)
            {
            	  
            	//System.out.println(r2[0]+"  "+r2[1]);
                a.set_rdata(r2);//insert
                
                a.priKeyHash.put( priKeyStrVal, r2);

                
                tables.put(t, a);
                e.message = "Success: Record inserted sucessfully";
                return e;
            }
            else
            {
               e.message = "Error: Primary key cannot be null";
               return e; 
            }
        }
        else
        {
        	//System.out.println(r2[0]+"  "+r2[1]);
            a.set_rdata(r2);//insert
                    
            
            tables.put(t, a);
            e.message = "Success: Record inserted sucessfully";
            return e;
        }
    }
 
    public Table getTable(String table_name)
    {
    	Table a;
        a = (Table)tables.get(table_name);//retrieve the tabel with name test
        return a;
    }
    
   public Return select (Query q) throws Exception
   {
       Return r = new Return();
       
       From f = new From();
       f.assign(q);//populate From object
       
       Select s = new Select();
       s.assign(q,f,tables);//popular select object
      
       Where w = new Where();
       w.and_or(q);
       w.assign(q,f,tables);//populate where object
       
       
       if(f.tname.length > 1)//join
       {
           
           if(!w.cname[0].equals(""))//join.N-criteria
           {
               Boolean [] b = new Boolean [w.cname.length];
               Return [] r2 = new Return[2];
               
               for (int x = 0; x < f.tname.length; ++x)//copy reference of table contents 
               {
                   r2[x] = new Return();
                   r2[x].rdata = tables.get(f.tname[x].toUpperCase()).rdata;
                   r2[x].cdata = tables.get(f.tname[x].toUpperCase()).cdata;//cdata is not changed so a clone is not needed only a reference
                   r2[x].id = tables.get(f.tname[x].toUpperCase()).get_id();
                   r2[x].r_id = tables.get(f.tname[x].toUpperCase()).get_id();
                   r2[x].select_result = tables.get(f.tname[x].toUpperCase()).rdata;
                   //r2[x].old_order = new String[r2[x].cdata.length];
                   r2[x].old_order = new String[r2[x].cdata.length];
                   
                   for(int y = 0;y<r2[x].cdata.length; ++y)
                   {
                    r2[x].old_order[y] = r2[x].cdata[y].tname+"."+r2[x].cdata[y].cname;
                   }
                   
               }
               
               /*  Merge column lists of both tables------------------------*/
               String[] order1 = r2[0].old_order.clone();
               String[] order2 = r2[1].old_order.clone();

               String[] order_final = new String[order1.length + order2.length];
               System.arraycopy(order1, 0, order_final, 0, order1.length);
               System.arraycopy(order2, 0, order_final, order1.length, order2.length);
               
               r.old_order = order_final;
               
               /*----------------------------------------------------*/
               
               for (int x = 1; x <= r2[0].r_id; ++x)
               {
                   String [] row1 = r2[0].rdata.get(x);
                   
                   for (int y = 1; y <= r2[1].r_id; ++y)
                   {
                       String [] row2 = r2[1].rdata.get(y);
                       String[] row_final = new String[order_final.length];
                       System.arraycopy(row1, 0, row_final, 0, order1.length);
                       System.arraycopy(row2, 0, row_final, order1.length, order2.length);

                       for(int a = 0; a < w.tcname.length; ++a)
                       {
                           if(a == 0)
                           {
                               for(int c = 0; c < r.old_order.length; ++c)
                               {
                                   if(w.tcname[a].equalsIgnoreCase(r.old_order[c]))
                                   {
                                       
                                       for(int d = 0; d < r.old_order.length; ++d)
                                       {
                                           
                                           String[] join = w.value[a].split("\\.");
                                           
                                            if(join.length > 1)
                                            {
                                                String cname1 = join[1];

                                                for (int p=0; p < f.tname.length; ++p)
                                                {    
                                                    if(join[0].equalsIgnoreCase(f.tname[p]) || join[0].equalsIgnoreCase(f.talias[p]))
                                                    {
                                                        w.value[a] = f.tname[p]+"."+join[1];
                                                        
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                for(int u = 0; u < f.tname.length; ++u)//assign table name to variable
                                                {
                                                    Table t2 = tables.get(f.tname[y].toUpperCase());

                                                    for(int k = 0; k < t2.cdata.length; ++k)
                                                    {
                                                        if(t2.cdata[k].cname.equalsIgnoreCase(join[0]))
                                                        {
                                                            w.value[a] = f.tname[u]+"."+join[0];
                                                        }
                                                    }
                                                }
                                            }
                                           
                                           if(w.value[a].equalsIgnoreCase(r.old_order[d]))
                                           {
                                               b[a] = row_final[c].equals(row_final[d]);
                                           }
                                       }
                                   }
                               }
                           }
                           else
                           {
                                for(int e = 0; e < r.old_order.length;++e)
                                {
                                    if(w.tcname[a].equalsIgnoreCase(r.old_order[e]))
                                    {
                                        b[a] = test_criteria(row_final[e],w.value[a],w.operator[a]); 
                                    }
                                }
                           }
                       }
                       
                       if(w.and_or.equals("none"))
                       {
                           if (b[0])
                           {
                                r.set_select_result(row_final);
                           }
                       }
                       else if (w.and_or.equals("and"))
                       {
                           if (b[0] && b[1])
                           {
                                r.set_select_result(row_final);
                           }
                       }
                       else if (w.and_or.equals("or"))
                       {
                           if (b[0] || b[1])
                           {
                                r.set_select_result(row_final);
                           }
                       }
                   }
               }
               r.assign_order(s,false,tables,f);
               return r;
           }
           else//join.no-criteria
           {
               Return [] r2 = new Return[2];
               
               for (int x = 0; x < f.tname.length; ++x)//copy reference of table contents 
               {
                   r2[x] = new Return();
                   r2[x].rdata = new HashMap<Integer,String[]>(tables.get(f.tname[x].toUpperCase()).rdata);
                   r2[x].cdata = tables.get(f.tname[x].toUpperCase()).cdata;//cdata is not changed so a clone is not needed only a reference
                   r2[x].id = tables.get(f.tname[x].toUpperCase()).get_id();
                   r2[x].r_id = tables.get(f.tname[x].toUpperCase()).get_id();
                   r2[x].select_result = new HashMap<Integer,String[]>(tables.get(f.tname[x].toUpperCase()).rdata);
                   r2[x].old_order = new String[r2[x].cdata.length];
                   
                   for(int y = 0;y<r2[x].cdata.length; ++y)
                   {
                    r2[x].old_order[y] = r2[x].cdata[y].tname+"."+r2[x].cdata[y].cname;
                   }
                   
               }

               /*  Merge column lists of both tables*/
               String[] order1 = r2[0].old_order;
               String[] order2 = r2[1].old_order;

               String[] order_final = new String[order1.length + order2.length];
               System.arraycopy(order1, 0, order_final, 0, order1.length);
               System.arraycopy(order2, 0, order_final, order1.length, order2.length);
               
               r.old_order = order_final;
               /*----------------------------------------------------*/

               for (int x = 1; x <= r2[0].r_id; ++x)
               {
                   String [] row1 = r2[0].select_result.get(x).clone();
                   
                   for (int y = 1; y <= r2[1].r_id; ++y)
                   {
                       String [] row2 = r2[1].rdata.get(y).clone();
                       String[] row_final = new String[10];
                       System.arraycopy(row1, 0, row_final, 0, order1.length);
                       System.arraycopy(row2, 0, row_final, order1.length, order2.length);
                       
                       r.set_select_result(row_final);
                   }
               }

               r.assign_order(s,false,tables,f);
               return r; 
           }
       }
       else//no-join.
       {   
           if(!w.operator[0].equals(""))//no-join.N-criteria
           {
               r.rdata = tables.get(f.tname[0].toUpperCase()).rdata;
               r.cdata = tables.get(f.tname[0].toUpperCase()).cdata;//cdata is not changed to a clone is not needed only a reference
               r.id = tables.get(f.tname[0].toUpperCase()).get_id();
               
               String [] order = new String[r.cdata.length] ;

               for(int y = 0; y < r.cdata.length; ++y)
               {
                   order[y] = r.cdata[y].cname;
               }

               for(int x = 1; x <= r.id; ++x)//traverse hashmap that contains rows 1 by 1
               {
                   Boolean [] b = new Boolean [w.cname.length];
                   String [] row = tables.get(f.tname[0].toUpperCase()).rdata.get(x); 
                  
                   for(int y = 0; y < w.cname.length; ++y)
                   {
                       for(int a = 0; a < order.length;++a)
                       {
                           if(w.cname[y].equalsIgnoreCase(order[a]))
                           {//System.out.println(row.length);
                               b[y] = test_criteria(row[a],w.value[y],w.operator[y]);
                           }
                       }
                   }

                   if(w.and_or.equals("none"))
                   {
                       if (b[0])
                       {
                            r.set_select_result(row);
                       }
                   }
                   else if (w.and_or.equals("and"))
                   {
                       if (b[0] && b[1])
                       {
                            r.set_select_result(row);
                       }
                   }
                   else if (w.and_or.equals("or"))
                   {
                       if (b[0] || b[1])
                       {
                            r.set_select_result(row);
                       }
                   }
               }
               
               r.assign_order(s,true,tables,f);
               return r;
           }
           else//no-join.no-criteria
           {
               //r.rdata = tables.get(f.tname[0].toUpperCase()).rdata;
               r.select_result = new HashMap<Integer,String[]>(tables.get(f.tname[0].toUpperCase()).rdata);
               r.cdata = tables.get(f.tname[0].toUpperCase()).cdata.clone();//cdata is not changed to a clone is not needed only a reference
               r.r_id = tables.get(f.tname[0].toUpperCase()).get_id();

               r.assign_order(s,true,tables,f);
               return r;
           }
       }  
   }
   
   Boolean test_criteria(String row_value,String criteria_value, String operator) throws Exception
   {
       Boolean e = false;
       
       if(operator.equals("="))
       {
           e = (criteria_value.equals(row_value));
       }
       else if (operator.equals("<>"))
       {
           e = (!criteria_value.equals(row_value));
       }
       else if (operator.equals(">"))
       {
           int row_value2 = Integer.parseInt(row_value);//convert value to interger
           int criteria_value2 = Integer.parseInt(criteria_value);//convert value to interger
           
           e = (row_value2 > criteria_value2);  
       }
       else if (operator.equals("<"))
       {
           int row_value2 = Integer.parseInt(row_value);//convert value to interger
           int criteria_value2 = Integer.parseInt(criteria_value);//convert value to interger
           
           e = (row_value2 < criteria_value2); 
       }
       return e;
   }  
}