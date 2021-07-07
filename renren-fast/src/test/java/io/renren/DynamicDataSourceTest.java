/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren;

import com.google.common.util.concurrent.Runnables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * 多数据源测试
 *
 * @author Mark sunlightcs@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {


    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        executorService.execute(new ThreadForThread());

        FutureTask<Integer> integerFutureTask = new FutureTask<>(new Callanle01());
        executorService.execute(integerFutureTask);
        System.out.println(integerFutureTask.get());

        executorService.execute(new ThreadForRunable());

    }

    public static class ThreadForThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread快跑"+Thread.currentThread().getName());
        }
    }

    public static class ThreadForRunable implements Runnable {

        @Override
        public void run() {
            System.out.println("Runnable快跑"+Thread.currentThread().getName());
        }
    }

    public static class Callanle01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            return 100*100;
        }
    }

}
