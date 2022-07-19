package com.xupt.nowcoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author yzw
 * @Date 2022-07-19 14:36 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = NowCoderApplication.class)
public class BlockingQueueTest {
    public static ArrayBlockingQueue<Integer> abq = new ArrayBlockingQueue<Integer>(10);
    public static LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque();
    public static void main(String[] args){
        Thread mp = new MyThreadProducer(abq);
        Thread mc = new MyThreadConsumer(abq);
        Thread mc1 = new MyThreadConsumer(abq);
        Thread mc2 = new MyThreadConsumer(abq);
        mp.start();
        mc.start();
        mc1.start();
        mc2.start();
        //new Thread(new MyThreadProducer(abq)).start();
        //new Thread(new MyThreadConsumer(abq)).start();
    }
    //abstract不允许修饰字段 abstract int a;
    //abstract方法不允许有方法体
    //abstract int test();
    //单例双锁模式 说明contructor可以有返回值
    public class Single{
        private Single single = null;
        Single Single(){
            if(single==null){
                synchronized(single){
                  if(single==null){
                      single = new Single();
                  }
                }
            }
            return single;
        }
    }
}
class MyThreadProducer extends Thread{
    private  ArrayBlockingQueue arrayBlockingQueue;
    MyThreadProducer(ArrayBlockingQueue arrayBlockingQueue){
         this.arrayBlockingQueue = arrayBlockingQueue;
    }
    @Override
    public void run() {
        try {
            for(int i=0;i<20;i++){
                Thread.sleep(20);
                arrayBlockingQueue.put(i);
                System.out.println(Thread.currentThread().getName()+"此时正在生产"+arrayBlockingQueue.size());
                /*synchronized (System.out){
                    System.out.println(Thread.currentThread().getName()+"此时正在生产"+i);
                }*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class MyThreadConsumer extends Thread{
    private  ArrayBlockingQueue arrayBlockingQueue;
    MyThreadConsumer(ArrayBlockingQueue arrayBlockingQueue){
        this.arrayBlockingQueue = arrayBlockingQueue;
    }
    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(new Random().nextInt(1000));
                System.out.println(Thread.currentThread().getName()+"此时正在消费"+arrayBlockingQueue.size());
                int k = (int)arrayBlockingQueue.take();
                /*synchronized (System.out){
                    System.out.println(Thread.currentThread().getName()+"此时正在消费"+k);
                }*/
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
