package net.iceyleagons.icicle.core;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.annotations.execution.Async;
import net.iceyleagons.icicle.core.annotations.execution.Measure;
import net.iceyleagons.icicle.core.annotations.execution.extra.After;
import net.iceyleagons.icicle.core.annotations.execution.extra.Periodically;

import java.util.concurrent.TimeUnit;

@Service
public class Test {

    @Async
    @After(delay = 1, unit = TimeUnit.SECONDS)
    @Measure
    @SneakyThrows
    public void test() {
        System.out.println("Called");
    }
}
