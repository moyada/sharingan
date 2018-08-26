package cn.moyada.faker.common.utils;

import java.util.UUID;

/**
 * @author xueyikang
 * @create 2017-12-30 06:35
 */
public class UUIDUtil {

    /**
     * 生成唯一编号
     * @return
     */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
}
