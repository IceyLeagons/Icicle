package net.iceyleagons.icicle.utilities.datastores.triple;

public interface Triple<A, B, C> {

    void setA(A a);
    void setB(B b);
    void setC(C c);

    A getA();
    B getB();
    C getC();

}
