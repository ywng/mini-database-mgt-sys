package Query;
import java.util.HashMap;
import minidbms.*;

public class Select 
{
    public String [] parts;
    public String [] tname;
    public String [] talias;
    public String [] cname;
    public String [] tcname;//table and column name together
    public String type;
    
    public void assign (Query q, From f,HashMap<String,Table> t)
    {
        parts = q.select.split(",");//split select statement by a ,
        String new_select="";
        
        
        
        for(int x = 0; x < parts.length; ++x)
        {
            if(parts[x].contains(".*") && !(parts[x].equalsIgnoreCase("sum.*")) && !(parts[x].equalsIgnoreCase("count.*")))
            {
                String [] decomp = parts[x].split("\\.");
                
                for(int y = 0; y < f.tname.length; ++y)
                {
                    if(decomp[0].equalsIgnoreCase(f.tname[y]) || decomp[0].equalsIgnoreCase(f.talias[y]))
                    {
                        for(int z = 0; z < t.get(f.tname[y].toUpperCase()).cdata.length; ++z)
                        {
                            new_select+=decomp[0]+"."+t.get(f.tname[y].toUpperCase()).cdata[z].cname+",";
                        }
                    }
                }
            } 
            else
            {
                new_select+=parts[x]+",";
            }
        }System.out.println(parts[0]);
        StringBuilder b = new StringBuilder(new_select);
        b.replace(new_select.lastIndexOf(","), new_select.lastIndexOf(",") + 1, "" );
        new_select = b.toString();
        
        parts = new_select.split(",");//split select statement by a ,
        tname=new String[parts.length];
        talias=new String[parts.length];
        cname=new String[parts.length];
        tcname=new String[parts.length];
        
        
        if(!parts[0].contains("count.") && !parts[0].contains("COUNT.") && !parts[0].contains("sum.") && !parts[0].contains("SUM.") && !parts[0].contains("*"))//acertain select type
        {
           
            type = "none";
           decomp_select(parts,f,t);
        }
        else
        {
            if(parts[0].contains("count.") || parts[0].contains("COUNT."))
            {
                type = "count";
                parts[0] = parts[0].replaceAll("count.", "");
                parts[0] = parts[0].replaceAll("COUNT.", "");
                //parts[0] = parts[0].replaceAll(")", "");
                decomp_select(parts,f,t);
                
            }
            else if (parts[0].contains("sum.") || parts[0].contains("SUM."))
            {
                type = "sum";
                parts[0] = parts[0].replaceAll("sum.", "");
                parts[0] = parts[0].replaceAll("SUM.", "");
                //parts[0] = parts[0].replaceAll(")", "");
                decomp_select(parts,f,t);
            }
            else if(parts[0].contains("*"))
            {
                type = "*";
                decomp_select(parts,f,t);
            }
        }
    }
    
    
    void decomp_select (String [] parts, From f, HashMap<String,Table> t)
    {
           for (int x=0; x < parts.length; ++x)
           {
               
               String [] column_parts = parts[x].split("\\.");

               if(column_parts.length > 1)
               {
                   cname[x] = column_parts[1];

                   for (int y=0; y < f.tname.length; ++y)
                   {    
                       if(column_parts[0].equalsIgnoreCase(f.tname[y]) || column_parts[0].equalsIgnoreCase(f.talias[y]))//if the first part equlas the table name or table alias name
                       {
                           tname[x] = f.tname[y];
                           talias[x] = f.talias[y];
                           tcname[x]  = tname[x]+"."+cname[x];
                       }
                   }
               }
               else
               {
                   cname[x] = column_parts[0];//no alias or table name is provided
                    
                   if(!type.equals("*"))
                   {
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
                   else
                   {
                       tname[0] = "";
                   }
               }
           }
    }
}
