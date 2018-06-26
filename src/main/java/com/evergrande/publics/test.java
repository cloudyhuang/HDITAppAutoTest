package com.evergrande.publics;

import java.lang.reflect.Method;

import com.evergrande.Interface.GetOrderPushStatus;

public class test {
	public static GetOrderPushStatus orderpushstatus = new GetOrderPushStatus();
	public static void main(String[] args) throws Exception {
		Class c=Class.forName("com.evergrande.publics.test");
		Class[] parameterTypes=getMethodParameterTypes(c,"recursionBinarySearch");
		for(Class p:parameterTypes){
			System.out.println(p.getSimpleName());
		}
	}
	public static Class[] getMethodParameterTypes(Class c,String methodName){
    	Class parameterTypes[] = null;
    	Method m[]=c.getDeclaredMethods();
    	for(Method method:m){
    		if(method.getName().equals(methodName)){
    			parameterTypes=method.getParameterTypes();
    		}
    	}
    	return parameterTypes;
    }
	/**
	 * 使用递归的二分查找 title:recursionBinarySearch
	 * 
	 * @param arr
	 *            有序数组
	 * @param key
	 *            待查找关键字
	 * @return 找到的位置
	 */
	public static int recursionBinarySearch(int[] arr, int key, int low, int high) {

		if (key < arr[low] || key > arr[high] || low > high) {
			return -1;
		}

		int middle = (low + high) / 2; // 初始中间位置
		if (arr[middle] > key) {
			// 比关键字大则关键字在左区域
			return recursionBinarySearch(arr, key, low, middle - 1);
		} else if (arr[middle] < key) {
			// 比关键字小则关键字在右区域
			return recursionBinarySearch(arr, key, middle + 1, high);
		} else {
			return middle;
		}

	}
	public static int recursionBinarySearch(int key, int low, int high) {
		int a=1;return a;
	}
}