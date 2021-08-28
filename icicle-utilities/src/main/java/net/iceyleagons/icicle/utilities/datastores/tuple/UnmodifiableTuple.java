package net.iceyleagons.icicle.utilities.datastores.tuple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class UnmodifiableTuple<A, B> implements Tuple<A, B> {

    private final A a;
    private final B b;

    @Override
    public void setA(A a) {
        throw new IllegalStateException("Modification of an UnmodifiableTuple is not allowed!");
    }

    @Override
    public void setB(B b) {
        throw new IllegalStateException("Modification of an UnmodifiableTuple is not allowed!");
    }
}
