package net.iceyleagons.icicle.utilities.datastores.tuple;

public interface Tuple<A, B> {

    A getA();

    void setA(A a);

    B getB();

    void setB(B b);
}
