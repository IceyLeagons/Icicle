/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.bukkit.impl;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BukkitExecutionHandler implements ExecutionHandler {

    private final JavaPlugin javaPlugin;
    private final BukkitScheduler scheduler = Bukkit.getScheduler();

    @Override
    public <T> CompletableFuture<T> runSync(Callable<T> callable) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        scheduler.runTask(this.javaPlugin, () -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runSyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        scheduler.runTaskLater(this.javaPlugin, () -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, timeUnit.toSeconds(delay) * 20);

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runSyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        scheduler.runTaskTimer(this.javaPlugin, () -> {
            try {
                T obj = callable.call();

                if (!completableFuture.isDone())
                    completableFuture.complete(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delayUnit.toSeconds(delay) * 20L, periodUnit.toSeconds(period) * 20);

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runAsync(Callable<T> callable) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        scheduler.runTaskAsynchronously(this.javaPlugin, () -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runAsyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        scheduler.runTaskLaterAsynchronously(this.javaPlugin, () -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, timeUnit.toSeconds(delay) * 20);

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runAsyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        scheduler.runTaskTimerAsynchronously(this.javaPlugin, () -> {
            try {
                T obj = callable.call();

                if (!completableFuture.isDone())
                    completableFuture.complete(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delayUnit.toSeconds(delay) * 20L, periodUnit.toSeconds(period) * 20);

        return completableFuture;
    }
}
