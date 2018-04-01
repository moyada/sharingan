package cn.moyada.dubbo.faker.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author xueyikang
 * @create 2018-04-01 19:41
 */
public class FileReplace {

    protected String fileName;
    protected FileFetch file;

    public FileReplace(String fileName) throws FileNotFoundException {
        this.file = new FileFetch(fileName);
        this.fileName = fileName;
    }

    public void replace(String replacement, String contain) {
        String tag = "<!--@" + replacement + "-->";
        String replace = file.fetch(tag, tag);
        if(null == replace) {
            return;
        }
        String escape = findLastEscape(replace);
        replace = tag.concat(replace).concat(tag);
        contain = tag.concat(contain).concat(escape).concat(tag);

        Charset charset = StandardCharsets.UTF_8;
        Path path = Paths.get(fileName);
        String content;
        try {
            content = new String(Files.readAllBytes(path), charset);
            content = content.replace(replace, contain);
            Files.write(path, content.getBytes(charset), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findLastEscape(String str) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;

        byte current;
        byte[] escape = new byte[length];
        int index = length;
        for (int pos = length - 1; pos >= 0; pos--) {
            current = bytes[pos];
            switch (current) {
                case '\n':
                case '\t':
                case ' ':
                    index--;
                    escape[index] = current;
                    continue;
            }
            break;
        }
        if(escape[index] == '\n')
            index++;
        return new String(escape, index, length-index);
    }
}
