package Test.peixun;

import java.util.Arrays;

public class Array {
//	数组(Array)是Java 语言中内置的一种基本数据存储结构，通俗的理解，就是一组数的集合，
//	目的是用来一次存储多个数据。数组是程序中实现很多算法的基础，可以在一定程度上简化代码的书写。
//	Java 语言中的数组可以分为：一维数组和多维数组.
//	1、数组中的元素类型必须相同。
//	2、数组的长度一旦指定即不能改变。
//	3、数组中的值通过数组名和下标组合起来进行访问。

     public static void main(String[] args){
    	 //数组的声明：数据类型数    组名称[]  
    	 //       数据类型[]   数组名称
    	 //boolean char byte short int long float dublie
    	 //初始化 静态初始化，动态初始化
    	 int ary1[]=new int[3];
    	 String arg2[]=new String[]{"1","2","3"};
    	 String[] arg3={"1","2","3"};
    	 System.out.println(arg3.toString());
    	System.out.println(Arrays.toString(arg3));
    	System.out.println("123".toCharArray()[0]);
    	 //java数组是自动初始化的
    	int[] m = {3,2,4,6};
    	m[1] = 4;
    	m[2] = m[3] + m[0];
    	System.out.println(Arrays.toString(m));
    	for(int i = 0;i < num.length;i++){
            System.out.print(num[i]+" ");
    }

    	 
    	 
     }
}
