package net.iceyleagons.icicle.serialization.converters;

import net.iceyleagons.icicle.utilities.generic.acessors.TwoTypeAccessor;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public abstract class AbstractConverter<A, B> extends TwoTypeAccessor<A, B> implements ValueConverter<A, B> {

    @Override
    public Class<A> getAClass() {
        return super.getATypeClass();
    }

    @Override
    public Class<B> getBClass() {
        return super.getBTypeClass();
    }
}
