package net.iceyleagons.icicle.core;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.DefaultBeanManager;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;

public class Icicle {

    public static void main(String[] args) {
        BeanManager beanManager = new DefaultBeanManager();

        try {
            beanManager.createAndRegisterBean(Test.class);

            beanManager.getBeanRegistry().getBean(Test.class).ifPresent(asd -> {
                System.out.println(asd.test());
            });
        } catch (BeanCreationException | CircularDependencyException e) {
            e.printStackTrace();
        }

    }

    @RequiredArgsConstructor
    static class Test {
        private final Test1 test;

        public String test() {
            return test.test();
        }
    }

    @RequiredArgsConstructor
    static class Test1 {
        private final Test2 test;

        public String test() {
            return test.test();
        }
    }

    static class Test2 {

        public String test() {
            return "from Test2";
        }
    }
}
