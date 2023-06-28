package com.cym.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Test {
	public static void main(String[] args) {
		System.out.println("hello");

		try {
			Process p = Runtime.getRuntime().exec("ls");

			// 读取命令的输出信息
			InputStream is = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			// 打印输出信息
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println("输出信息：" + line);
			}

			System.out.println("命令执行完成");
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
