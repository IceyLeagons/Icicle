package net.iceyleagons.icicle.utilities.generic.acessors;

import lombok.Getter;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

@Getter
@SuppressWarnings("unchecked")
public abstract class ThreeTypeAccessor<A, B, C> {

    private final Class<A> aTypeClass;
    private final Class<B> bTypeClass;
    private final Class<C> cTypeClass;

    public ThreeTypeAccessor() {
        this.aTypeClass = (Class<A>) GenericUtils.getGenericTypeClass(getClass(), 0);
        this.bTypeClass = (Class<B>) GenericUtils.getGenericTypeClass(getClass(), 1);
        this.cTypeClass = (Class<C>) GenericUtils.getGenericTypeClass(getClass(), 2);
    }
}
