package Query;
public class whereItem{
	public conditionItem[] con=new conditionItem[2];
	public String logicOp=null;
	public Boolean[] join=new Boolean[2]; 

	public whereItem(){
		join[0]=false;
		join[1]=false;
	}
	
	public String print(){
		String str="";
		str+=con[0].print();
		if (logicOp!=null){
			str+=" "+logicOp+" ";
			str+=con[1].print();
			
		}
		return str;
	}
}

