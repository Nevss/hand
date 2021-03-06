package com.darly.activities.ui.qinjia.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.text.TextUtils;

import com.darly.activities.common.LogFileHelper;

public class FileUtil {
	public static String toFile(byte[] bfile, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			// int len = bfile.length;
			file = new File(fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
			return fileName;
		} catch (Exception e) {
			LogFileHelper.getInstance().e("FileUtil", e.getMessage());
		} finally {
			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException e) {
					LogFileHelper.getInstance().e("FileUtil", e.getMessage());
				}
			}
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (IOException e) {
					LogFileHelper.getInstance().e("FileUtil", e.getMessage());
				}
			}
		}
		return null;
	}

	public static byte[] getBytes(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (Exception e) {
			LogFileHelper.getInstance().e("FileUtil", e.getMessage());
		}
		return buffer;
	}

}
