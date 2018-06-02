package com.exam.util;

import java.io.*;
import java.util.List;

/**
 * Created by deng on 2017/12/9.
 */
public class CSVUtil {
    /**
     * 导出
     *
     * @param completeFilePath     完整文件路径，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(String completeFilePath, List<String> dataList) {
        boolean isSuccessful;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(completeFilePath));
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bw.append(data).append("\r");
                }
            }
            bw.flush();
            isSuccessful = true;
        } catch (Exception e) {
            isSuccessful = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSuccessful;
    }
}
