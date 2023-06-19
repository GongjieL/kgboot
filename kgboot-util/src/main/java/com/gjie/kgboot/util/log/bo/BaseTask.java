package com.gjie.kgboot.util.log.bo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class BaseTask<K, V> implements Delayed {
    private Long start = System.currentTimeMillis();

    private String name;

    private Long delay;

    public BaseTask(String name, Long delay) {
        this.name = name;
        this.delay = delay;
    }


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(start + delay - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

}
