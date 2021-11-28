package net.iceyleagons.icicle.core;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.annotations.Bean;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.annotations.execution.Async;
import net.iceyleagons.icicle.core.annotations.execution.Measure;
import net.iceyleagons.icicle.core.annotations.execution.extra.After;

import java.util.UUID;
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

    @Bean
    public Test2 test2() {
        return new Test2();
    }

    static class Test2 {
        private final UUID uuid = UUID.randomUUID();

        public String asd() {
            return "works: " + uuid;
        }
    }
}
