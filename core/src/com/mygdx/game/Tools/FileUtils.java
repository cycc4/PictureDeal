package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Date;

public class FileUtils {
    public static String file2String(FileHandle file) {
        BufferedReader br;
        StringBuilder strBlder = new StringBuilder("");
        try {
            br = new BufferedReader(new InputStreamReader(file.read()));
            String line = "";
            while (null != (line = br.readLine())) {
                strBlder.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBlder.toString();
    }

    public static String file2StringInternal(String fileName) {
//        return Gdx.files.internal(fileName).toString();
        return file2String(true ? Gdx.files.local(fileName) : Gdx.files.internal(fileName));
    }

    public static String readAbsoluteFile(String fileName) {
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void fileCopy(File f1, File f2) throws Exception {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        ByteBuffer b = null;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return;
            }
            if ((inC.size() - inC.position()) < length) {
                length = (int) (inC.size() - inC.position());
            } else
                length = 2097152;
            b = ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }

    public static String fileInputStream2String(InputStream is) {
        BufferedReader br;
        StringBuilder strBlder = new StringBuilder("");
        try {
            br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while (null != (line = br.readLine())) {
                strBlder.append(line + "\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strBlder.toString();
    }

    public static boolean saveString2File(File file, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write(content);
            bw.close();
//            System.out.println("save string2file: " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkFileExits(String filename) {
        try {
//			LogUtils.out("check file exist: " + filename);
            File f = new File(filename);
            return f.exists() && f.canRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //从FileChannel方式修改过来，fileChanel有个bug，不释放句柄，导致文件无法删除修改
    //https://www.cnblogs.com/icewee/articles/3703307.html
    public static String getMd5ByFile(File file) {
        try {
            String md5 = "";
            if (file.exists()) {
                MessageDigest messageDigest = MessageDigest.getInstance(ALGORIGTHM_MD5);
                InputStream in = new FileInputStream(file);
                byte[] cache = new byte[CACHE_SIZE];
                int nRead = 0;
                while ((nRead = in.read(cache)) != -1) {
                    messageDigest.update(cache, 0, nRead);
                }
                in.close();
                byte[] data = messageDigest.digest();
                md5 = byteArrayToHexString(data);
            }
            return md5.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * MD5摘要字节数组转换为16进制字符串
     * </p>
     *
     * @param data MD5摘要
     * @return
     */
    private static String byteArrayToHexString(byte[] data) {
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
        char[] arr = new char[16 * 2];
        int k = 0; // 表示转换结果中对应的字符位置
        // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
        for (int i = 0; i < 16; i++) {
            byte b = data[i]; // 取第 i 个字节
            // 取字节中高 4 位的数字转换, >>>为逻辑右移，将符号位一起右移
            arr[k++] = hexDigits[b >>> 4 & 0xf];
            // 取字节中低 4 位的数字转换
            arr[k++] = hexDigits[b & 0xf];
        }
        // 换后的结果转换为字符串
        return new String(arr);
    }

    private static final String ALGORIGTHM_MD5 = "MD5";
    private static final int CACHE_SIZE = 2048;
}
