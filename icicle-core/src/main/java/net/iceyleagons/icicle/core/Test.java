package net.iceyleagons.icicle.core;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.annotations.execution.Measure;

@Service
public class Test {

    @Measure
    @SneakyThrows
    public void test() {
        System.out.println("=========");
        System.out.println("Entered");
        Thread.sleep(500);
        System.out.println("Exited");
    }
}
