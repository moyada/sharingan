package cn.moyada.sharingan.monitor.api.mbean;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Item {

    void getBase(int[] ids);

    String getName();

    void listener();
}
