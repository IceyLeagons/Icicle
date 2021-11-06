package net.iceyleagons.icicle.utilities.datastores.triple;

public interface Triple<A, B, C> {

    A getA();

    void setA(A a);

    B getB();

    void setB(B b);

    C getC();

    void setC(C c);

}
