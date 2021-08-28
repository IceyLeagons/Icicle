package net.iceyleagons.icicle.utilities.datastores.triple;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleTriple<A, B, C> implements Triple<A, B, C> {

    private A a;
    private B b;
    private C c;

}
