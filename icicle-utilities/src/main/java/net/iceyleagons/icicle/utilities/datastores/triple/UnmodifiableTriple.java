package net.iceyleagons.icicle.utilities.datastores.triple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class UnmodifiableTriple<A, B, C> implements Triple<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    @Override
    public void setA(A a) {
        throw new IllegalStateException("Modification of an UnmodifiableTuple is not allowed!");
    }

    @Override
    public void setB(B b) {
        throw new IllegalStateException("Modification of an UnmodifiableTuple is not allowed!");
    }

    @Override
    public void setC(C c) {
        throw new IllegalStateException("Modification of an UnmodifiableTuple is not allowed!");
    }
}
