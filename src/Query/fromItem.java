package Query;

public class fromItem{
	public String main=null;
	public String sub=null;
	
	String print(){
		String str="";
		if (main!=null&&sub!=null)
			str+=main+" as "+sub;
		else if(main!=null&&sub==null)
			str+=main;
		return str;
	}
}
