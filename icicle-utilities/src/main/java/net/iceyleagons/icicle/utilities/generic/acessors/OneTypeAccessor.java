package net.iceyleagons.icicle.utilities.generic.acessors;

import lombok.Getter;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

@Getter
@SuppressWarnings("unchecked")
public abstract class OneTypeAccessor<A> {

    private final Class<A> aTypeClass;

    public OneTypeAccessor() {
        this.aTypeClass = (Class<A>) GenericUtils.getGenericTypeClass(getClass(), 0);
    }
}
