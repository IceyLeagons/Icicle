package net.iceyleagons.icicle.utilities.datastores.tuple;

public interface Tuple<A, B> {

    void setA(A a);
    void setB(B b);

    A getA();
    B getB();
}
