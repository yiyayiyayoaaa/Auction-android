package cx.study.auction.model.rest.http;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import cx.study.auction.util.IOUtils;


/**
 * User: LiangLong
 * Date: 2016-10-11
 * Time: 22:22
 * Note: com.microcardio.encrypt
 */

public class Digest {

	/**
	 * @param data 数据
	 * @return 结果
	 */
	public static long crc32(@Nullable byte[] data) {
		if (data == null) {
			return 0;
		}
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		return crc32.getValue();
	}

	/**
	 * 计算文件的crc32值
	 *
	 * @param file 待计算的文件
	 * @return 结果
	 */
	public static long fileCRC32(@Nullable File file) {
		if (file == null || !file.exists()) {
			return 0;
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			CRC32 crc32 = new CRC32();
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				crc32.update(buffer, 0, read);
			}
			return crc32.getValue();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.closeSilently(in);
		}
		return 0;
	}

	/**
	 * md5算法
	 *
	 * @param data 数据
	 * @return
	 */
	@Nullable
	public static String md5Hex(@Nullable byte[] data) {
		if (data == null) {
			return null;
		}
		try {
			MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			md5.update(data);
			byte[] messageDigest = md5.digest();
			return IOUtils.bytes2HexStr(messageDigest);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * md5算法
	 *
	 * @param file 数据
	 * @return
	 */
	@Nullable
	public static String fileMD5Hex(@Nullable File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				md5.update(buffer, 0, read);
			}
			byte[] messageDigest = md5.digest();
			return IOUtils.bytes2HexStr(messageDigest);
		}
		catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.closeSilently(in);
		}
		return null;
	}

}
