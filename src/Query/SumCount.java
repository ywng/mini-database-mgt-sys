package Query;

//given selected data array. 

public class SumCount {

//  public static void main(String[] arg) {
//		String[] data = new String[5];
//		data[0] = "0";
//		data[1] = "1";
//		data[2] = "2";
//		data[3] = "3";
//
//		System.out.println(Count(data));
//		System.out.println(Sum(data));
//	}

	public static int Count(String[] data) {
		int count = 0;

		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				count = count + 1;
			}
		}
		return count;
	}

	public static int Sum(String[] data) {
		int sum = 0;

		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				int tmp = Integer.parseInt(data[i]);
				sum = sum + tmp;
			}
		}

		return sum;
	}
}
