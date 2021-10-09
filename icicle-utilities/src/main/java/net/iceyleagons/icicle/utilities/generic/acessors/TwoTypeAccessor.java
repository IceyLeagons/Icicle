package net.iceyleagons.icicle.utilities.generic.acessors;

import lombok.Getter;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

@Getter
@SuppressWarnings("unchecked")
public abstract class TwoTypeAccessor<A, B> {

    private final Class<A> aTypeClass;
    private final Class<B> bTypeClass;

    public TwoTypeAccessor() {
        this.aTypeClass = (Class<A>) GenericUtils.getGenericTypeClass(getClass(), 0);
        this.bTypeClass = (Class<B>) GenericUtils.getGenericTypeClass(getClass(), 1);
    }
}
