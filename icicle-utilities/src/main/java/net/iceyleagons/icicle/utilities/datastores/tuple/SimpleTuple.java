package net.iceyleagons.icicle.utilities.datastores.tuple;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleTuple<A, B> implements Tuple<A, B> {

    private A a;
    private B b;

}
