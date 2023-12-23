package com.cym.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;

import com.cym.sqlhelper.utils.SqlHelper;

import cn.hutool.core.io.FileUtil;

@Component
public class TestUtils {

	@Inject
	SqlHelper sqlHelper;

	@Init
	public void test() {

	}

	public static boolean isUbuntu() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("linux")) {
			List<String> lines = FileUtil.readLines(new File("/etc/os-release"), Charset.forName("utf-8"));
			for (String line : lines) {
				if (line.contains("ubuntu")) {
					return true;
				}
			}
		}
		return false;
	}

    public static void main(String[] args) {
        if (isUbuntu()) {
            System.out.println("This system is running Ubuntu.");
        } else {
            System.out.println("This system is not running Ubuntu.");
        }
    }}
