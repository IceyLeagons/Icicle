/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.bukkit.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 20, 2022
 */
@RequiredArgsConstructor
public class Animation {

    private final List<Frame> frames = new ArrayList<>();
    private final JavaPlugin javaPlugin;

    private boolean running = false;
    private BukkitTask task;
    private Iterator<Frame> frameIterator;

    private int counter;
    private Frame currentFrame;

    public Animation start(boolean async) {
        if (this.running) return this;
        this.running = true;

        this.frameIterator = this.frames.iterator();
        this.task = async ? Bukkit.getScheduler().runTaskTimerAsynchronously(this.javaPlugin, this::onTick, 0L, 1L) :
                Bukkit.getScheduler().runTaskTimer(this.javaPlugin, this::onTick, 0L, 1L);

        return this;
    }

    public Animation stop() {
        this.task.cancel();
        this.currentFrame = null;
        this.counter = 0;

        return this;
    }

    public Animation addFrames(Frame... frame) {
        this.frames.addAll(Arrays.asList(frame));
        return this;
    }

    private void onTick() {
        if (!this.frameIterator.hasNext()) {
            stop();
            return;
        }

        if (this.currentFrame == null) {
            this.currentFrame = this.frameIterator.next();
        }

        if (this.currentFrame.getDuration() <= this.counter) {
            this.currentFrame = null;
            this.counter = 0;
            return;
        }

        if (this.counter == 0) {
            this.currentFrame.getRunnable().run();
            this.counter += 1;
            return;
        }

        this.counter += 1;
    }

    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class Frame {
        private final int duration;
        private final Runnable runnable;
    }
}