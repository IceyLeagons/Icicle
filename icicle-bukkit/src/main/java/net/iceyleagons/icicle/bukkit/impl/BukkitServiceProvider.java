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

package net.iceyleagons.icicle.bukkit.impl;

import net.iceyleagons.icicle.bukkit.BukkitApplication;
import net.iceyleagons.icicle.bukkit.annotations.GlobalServicePriority;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.beans.GlobalServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 25, 2022
 */
public class BukkitServiceProvider implements GlobalServiceProvider {

    private final ServicesManager servicesManager = Bukkit.getServicesManager();

    // Due to Bukkit's generic things I cannot call ServicesManager#register directly. Instead I call it via Reflection as generics are not present at runtime.
    private static void register(ServicesManager manager, Class<?> interfaceType, Object object, JavaPlugin registrar, ServicePriority priority) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ServicesManager.class.getMethod("register", Class.class, Object.class, Plugin.class, ServicePriority.class);
        method.invoke(manager, interfaceType, object, registrar, priority);
    }

    @Override
    public <T> Optional<T> getService(Class<T> type) {
        RegisteredServiceProvider<T> registered = servicesManager.getRegistration(type);
        if (registered == null) {
            return Optional.empty();
        }

        return Optional.of(registered.getProvider());
    }

    @Override
    public boolean isRegistered(Class<?> type) {
        return servicesManager.isProvidedFor(type);
    }

    @Override
    public void registerService(Class<?> interfaceType, Class<?> providerType, Object object, Application registrar) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!(registrar instanceof BukkitApplication)) {
            throw new IllegalStateException("Only BukkitApplication project can register to BukkitServiceProvider!");
        }

        System.out.println("Registering: " + interfaceType.getName());
        register(servicesManager, interfaceType, object, ((BukkitApplication) registrar).getJavaPlugin(), providerType.isAnnotationPresent(GlobalServicePriority.class) ? providerType.getAnnotation(GlobalServicePriority.class).value() : ServicePriority.Normal);
    }
}
