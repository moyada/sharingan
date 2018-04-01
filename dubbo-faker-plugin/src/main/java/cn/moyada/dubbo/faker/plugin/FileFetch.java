package cn.moyada.dubbo.faker.plugin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author xueyikang
 * @create 2018-04-01 19:21
 */
public class FileFetch {

    private BufferedReader reader;

    public FileFetch(String fileName) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(fileName));
    }

    protected String fetch(String firstTag, String endTag) {
        String line;
        StringBuilder contain = null;
        int index;
        boolean exists = false;
        try {
            while (null != (line = reader.readLine())) {
                if (!exists && (-1 != (index = line.indexOf(firstTag)))) {
                    contain = new StringBuilder(line.substring(index + firstTag.length()));
                    exists = true;
                    continue;
                }
                if (exists) { if (-1 != (index = line.indexOf(endTag))) {
                        contain.append("\n");
                        contain.append(line.substring(0, index));
                        exists = false;
                        break;
                    }
                    contain.append("\n");
                    contain.append(line);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(exists || contain == null) {
            return null;
        }
        return contain.toString();
    }
}
