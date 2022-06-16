package net.iceyleagons.test.icicle.core.bean.resolvable.modifiers;

import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.core.modifiers.impl.DefaultValue;
import net.iceyleagons.icicle.core.proxy.interceptor.modifiers.ModifiersActive;

@Service
public class DefaultValueService {

    @ModifiersActive
    public String testModifiers(@DefaultValue("1") String input) {
        return input;
    }

}
