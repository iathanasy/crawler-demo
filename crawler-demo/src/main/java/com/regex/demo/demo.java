package com.regex.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则
* @Project: crawler-demo 
* @Class demo 
* @Description: TODO
* @author cd 14163548@qq.com
* @date 2018年1月9日 下午5:33:49 
* @version V1.0
 */
public class demo {

	/**
	 * 1.常用 元字符
	 * 代码	说明
		.	匹配除换行符以外的任意字符
		\w	匹配字母或数字或下划线或汉字
		\s	匹配任意的空白符
		\d	匹配数字
		\b	匹配单词的开始或结束
		^	匹配字符串的开始
		$	匹配字符串的结束
		
		2.常用限定符
		*	重复零次或更多次
		+	重复一次或更多次
		?	重复零次或一次
		{n}	重复n次
		{n,}	重复n次或更多次
		{n,m}	重复n到m次
		
		3.反义符
		\W	匹配任意不是字母，数字，下划线，汉字的字符
		\S	匹配任意不是空白符的字符
		\D	匹配任意非数字的字符
		\B	匹配不是单词开头或结束的位置
		[^x]	匹配除了x以外的任意字符
		[^aeiou]	匹配除了aeiou这几个字母以外的任意字符
		
		4.常用分组语法
		捕获	(exp)	匹配exp,并捕获文本到自动命名的组里
		(?<name>exp)	匹配exp,并捕获文本到名称为name的组里，也可以写成(?'name'exp)
		(?:exp)	匹配exp,不捕获匹配的文本，也不给此分组分配组号
		零宽断言	(?=exp)	匹配exp前面的位置
		(?<=exp)	匹配exp后面的位置
		(?!exp)	匹配后面跟的不是exp的位置
		(?<!exp)	匹配前面不是exp的位置
		注释	(?#comment)	这种类型的分组不对正则表达式的处理产生任何影响，用于提供注释让人阅读
		
		5.懒惰限定符
		*?	重复任意次，但尽可能少重复
		+?	重复1次或更多次，但尽可能少重复
		??	重复0次或1次，但尽可能少重复
		{n,m}?	重复n到m次，但尽可能少重复
		{n,}?	重复n次以上，但尽可能少重复
	 */
	
	
	
	/**
	 * 查找以java开头，任意结尾的字符串
	 */
	public static void demo(){
		Pattern p = Pattern.compile("^Java.*");
		Matcher matcher = p.matcher("Java 是一门编程语言");
		boolean b = matcher.matches();
		//当条件满足时返回true,否则返回false
		System.out.println(b);
		System.out.println(matcher.group());
	}
	
	/**
	 * 以多个条件分割字符串
	 */
	public static void demo1(){
		Pattern p = Pattern.compile("[, |]+");
		String [] strs = p.split("Java Hello World Java,Hello,,World|Sun");
		for (int i = 0; i < strs.length; i++) {
			System.out.println(strs[i]);
		}
	}
	
	/**
	 * 文字替换(首次出现字符)
	 */
	public static void demo2(){
		Pattern p = Pattern.compile("正则表达式");
		Matcher matcher = p.matcher("正则表达式 Hello 正则表达式,Hello,World|Sun");
		//替换第一个符合正则表达式的数据
		System.out.println(matcher.replaceFirst("Java"));
		//替换全部
		System.out.println(matcher.replaceAll("PHP"));
	}
	
	/**
	 * 文字替换(替换字符)
	 */
	public static void demo3(){
		Pattern p = Pattern.compile("正则表达式");
		Matcher matcher = p.matcher("正则表达式 Hello 正则表达式,Hello,World|Sun");
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sbr, "Java");
		}
		matcher.appendTail(sbr);
		System.out.println(sbr.toString());
	}
	
	/**
	 * 验证是否为邮箱地址
	 */
	public static void demo4(){
		Pattern p = Pattern.compile("[\\w\\.\\~]+@([\\w\\-]+\\.)+[\\w\\-]+",Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher("zz@yahoo.com.cn");
		System.out.println(matcher.matches());
	}
	
	/**
	 * 去除html标记
	 */
	public static void demo5(){
		Pattern p = Pattern.compile("<.+?>",Pattern.DOTALL);
		Matcher matcher = p.matcher("<a href=\"index.html\">首页</a>");
		String str = matcher.replaceAll("");
		System.out.println(str);
	}
	
	/**
	 * 查找html中对应条件字符串
	 */
	public static void demo6(){
		Pattern p = Pattern.compile("href=\".+?\"");
		Matcher matcher = p.matcher("<a href=\"index.html\">首页</a><a href=\"order.html\">订单</a>");
		while(matcher.find()){
			System.out.println(matcher.group());
		}
	}
	
	/**
	 * 截取http://地址
	 */
	public static void demo7(){
		Pattern p = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+");
		Matcher matcher = p.matcher("<asahttps://zzz.app.com/index>");
		StringBuffer sb = new StringBuffer();
		while(matcher.find()){
			sb.append(matcher.group());
			sb.append("\r\n");
		}
		System.out.println(sb.toString());
	}
	
	public static void demo8(){
		String str = "Java目前发展史是由 {0}年-{1}年。";
		String[][] obj = {new String[]{"\\{0\\}","1995"},new String[]{"\\{1\\}","2018"}}; 
		
		for (int i = 0; i < obj.length; i++) {
			String [] result = obj[i]; 
			Pattern p = Pattern.compile(result[0]);
			Matcher matcher = p.matcher(str);
			str = matcher.replaceAll(result[1]);
			System.out.println(result[0]);
			System.out.println(result[1]);
		}
		System.out.println(str);
	}
	
	public static void main(String[] args) {
		demo8();
	}
}
