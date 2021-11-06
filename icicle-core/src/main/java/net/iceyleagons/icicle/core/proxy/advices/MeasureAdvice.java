package net.iceyleagons.icicle.core.proxy.advices;

import net.bytebuddy.asm.Advice;

public class MeasureAdvice {

    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static long enter() {
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit(suppress = Throwable.class, onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long time, @Advice.This Object thisObject, @Advice.Origin String origin) {
        long now = System.currentTimeMillis();

        System.out.printf("[STOPWATCH] Class: %s Method: %s took %s ms.\n",
                thisObject.getClass().getName(),
                origin,
                now - time
        );
    }
}
