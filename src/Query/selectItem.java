package Query;

public class selectItem{
	public String main=null;
	public String sub=null;
	public String type=null;
	
	String print(){
		String str="";
		String temp=new String (main);
		if (temp.endsWith("'")&&temp.startsWith("'")){
			temp=(String) temp.subSequence(1, temp.length()-1);
		}
		if (main!=null&&sub!=null){
			str+=temp+"."+sub;
		}
		else if(main!=null&&sub==null)
			str+=temp;
		return str;

	}
}