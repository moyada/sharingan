package cn.moyada.dubbo.faker.core.model.queue;

import cn.moyada.dubbo.faker.core.factory.GroupThreadFactory;
import cn.moyada.dubbo.faker.core.utils.ThreadUtil;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author xueyikang
 * @create 2018-04-06 11:11
 */
public class QueueTest {

    static int poolSize = 20;
    static int size = 50;
    static ExecutorService pool = new ThreadPoolExecutor(poolSize, poolSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new GroupThreadFactory("queue", "test"));

    public static void main(String[] args) throws InterruptedException {
        test2();
        Thread.sleep(1000);
        test1();
        Thread.sleep(1000);
        test2();
        Thread.sleep(1000);
        test3();
        Thread.sleep(1000);
        test4();
    }

    private static void test4() {
        Queue<String> queue = new ArrayBlockingQueue<>(size * poolSize);
        long start = System.nanoTime();
        for (int i = 0; i < poolSize; i++) {
            pool.execute(() -> {
                int id = ThreadUtil.getInnerGroupId();
                int j = 0;
                for (; j < size; j++) {
                    queue.offer("" + id + "test" + j);
                }
            });
        }
        System.out.println((System.nanoTime() - start) / 1_000_000);
    }

    private static void test3() {
        AbstractQueue<String> queue = new SequenceQueue<>(size * poolSize + 1);
        long start = System.nanoTime();
        for (int i = 0; i < poolSize; i++) {
            pool.execute(() -> {
                int id = ThreadUtil.getInnerGroupId();
                int j = 0;
                for (; j < size; j++) {
                    queue.offer("" + id + "test" + j);
                }
            });
        }
        System.out.println((System.nanoTime() - start) / 1_000_000);
    }

    private static void test2() {
        AbstractQueue<String> queue = new AtomicQueue<>(size * poolSize);
        long start = System.nanoTime();
        for (int i = 0; i < poolSize; i++) {
            pool.execute(() -> {
                int id = ThreadUtil.getInnerGroupId();
                int j = 0;
                for (; j < size; j++) {
                    queue.offer("" + id + "test" + j);
                }
            });
        }
        System.out.println((System.nanoTime() - start) / 1_000_000);
    }

    private static void test1() {
        UnlockQueue<String> queue = UnlockQueue.build(poolSize, size * (poolSize + 10));
        long start = System.nanoTime();
        for (int i = 0; i < poolSize; i++) {
            pool.execute(() -> {
                int id = ThreadUtil.getInnerGroupId();
                int j = 0;
                for (; j < size; j++) {
//                    try {
                        queue.offer(id, "" + id + "test" + j);
//                    }
//                    catch (Exception ee) {
//                        LockSupport.parkNanos(1_00_000L);
//                        try {
//                            queue.offer(id, "" + id + "test" + j);
//                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        finally {
//                            queue.isDone();
//                            break;
//                        }
//                    }
//                    int c= 0;
//                    for (int x = 0;x < 10000; x++) {
//                        c=c+x;
//                    }
                }
//                System.out.println("size: " + id + " - " + j);
//                queue.done(id);
            });
        }
        System.out.println((System.nanoTime() - start) / 1_000_000);

//        Thread.sleep(1_000);

//        String next;
//        int count = 0;
//        while (true) {
//            next = queue.poll();
//            if(null == next) {
//                if(queue.isDone())
//                    break;
//                else
//                    LockSupport.parkNanos(1_000_000);
//                    continue;
//            }
//            System.out.println(next);
//            count++;
//        }
//
//        pool.shutdown();
//        System.out.println(count);
    }
}
