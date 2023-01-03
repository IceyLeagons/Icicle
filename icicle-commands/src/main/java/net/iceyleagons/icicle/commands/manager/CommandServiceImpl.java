/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands.manager;

import net.iceyleagons.icicle.commands.RegisteredCommand;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.params.InvocationParameterBuilder;
import net.iceyleagons.icicle.commands.params.InvocationParameterBuilderImpl;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverRegistry;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.core.beans.BeanRegistry;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@Service
public class CommandServiceImpl implements CommandService {

    private final CommandRegistry commandRegistry;
    private final ParameterResolverRegistry parameterResolverRegistry;

    private final InvocationParameterBuilder invocationParameterBuilder;

    @Autowired
    public CommandServiceImpl(BeanRegistry beanRegistry) {
        this.commandRegistry = new CommandRegistry();
        this.parameterResolverRegistry = new ParameterResolverRegistry();
        this.invocationParameterBuilder = new InvocationParameterBuilderImpl(beanRegistry, parameterResolverRegistry);
    }

    @Override
    public Object execute(String name, Object sender, Object[] commandInputs, Map<Class<?>, Object> externalParams) throws Exception {
        RegisteredCommand cmd = commandRegistry.get(name);
        if (cmd != null) {
            Object[] parameters = getParameterBuilder().buildParameters(cmd.getMethod(), sender, commandInputs, externalParams);
            Object response = cmd.execute(parameters);
            return cmd.getMethod().getReturnType().equals(Void.class) ? null : response;
        }

        return null;
    }

    @Override
    public void registerCommandContainer(Class<?> container, Object bean) {
        for (Method declaredMethod : container.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Command.class)) {
                this.commandRegistry.registerCommand(RegisteredCommand.from(declaredMethod, bean));
            }
        }
    }

    @Override
    public CommandRegistry getCommandRegistry() {
        return this.commandRegistry;
    }

    @Override
    public InvocationParameterBuilder getParameterBuilder() {
        return this.invocationParameterBuilder;
    }

    @Override
    public ParameterResolverRegistry getParameterResolverRegistry() {
        return this.parameterResolverRegistry;
    }
}
