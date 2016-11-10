package com.github.professor_x_core.util;

import com.github.professor_x_core.interfaces.Source;
import com.github.professor_x_core.service.TaskPoolService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xin.cao@100credit.com
 */
public class FileSource implements Source {

    private String filename;
    private String div;
    private int[] indexs;
    private int size = Integer.MAX_VALUE;
    private static final TaskPoolService taskPool = TaskPoolService.getInstance();

    public FileSource(String filename, String div, int[] indexs) {
        this.filename = filename;
        this.div = div;
        this.indexs = indexs;
    }

    public FileSource(String filename, String div, int[] indexs, int size) {
        this.filename = filename;
        this.div = div;
        this.indexs = indexs;
        this.size = size;
    }

    @Override
    public int read() {
        InputStream is = FileSource.class.getResourceAsStream(String.format("/%s", filename));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        for (int index : indexs) {
            if (index < 0) {
                Logger.info(String.format("indexs 中存在小于0的索引 %d", index));
                return -1;
            }
        }
        String line;
        int rows = 0;
        try {
            while ((line = br.readLine()) != null) {
                if (rows >= size) {
                    return rows;
                }
                String[] fields = line.split(div);
                if (fields == null) {
                    continue;
                }
                List<String> params = new ArrayList<String>();
                int len = fields.length;
                if (indexs == null) {
                    for (int i = 0; i < len; i++) {
                        if (fields[i] == null) {
                            params.add("");
                        } else {
                            params.add(fields[i]);
                        }
                    }
                } else {
                    for (int index : indexs) {
                        if (index >= len) {
                            continue;
                        }
                        if (fields[index] == null) {
                            params.add("");
                        } else {
                            params.add(fields[index]);
                        }
                    }
                }
                taskPool.add(params);
                rows++;
            }
        } catch (IOException ex) {
            Logger.info(ex.getMessage());
            return -1;
        } catch (InterruptedException ex) {
            Logger.info(ex.getMessage());
            return -1;
        } finally {
            try {
                br.close();
                is.close();
            } catch (IOException ex) {
                Logger.info(ex.getMessage());
            }
        }
        return rows;
    }
}
