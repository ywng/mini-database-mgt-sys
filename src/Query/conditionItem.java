package Query;

public class conditionItem{
	public selectItem LOperand=new selectItem();
	public String logicOp=null;
	public selectItem ROperand=new selectItem();
	
	public String print(){
		String str="";
		str+=LOperand.print();
		str+=" "+logicOp+" ";
		str+=ROperand.print();
		return str;
	}
}