package net.iceyleagons.icicle.extensions

import net.iceyleagons.icicle.utilities.ListUtils
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple

fun <T> List<T>.merge(vararg lists: List<T>): MutableList<T> =
    ListUtils.mergeLists(this, *lists)

fun <A, B> Pair<A, B>.asTuple(): UnmodifiableTuple<A, B> = UnmodifiableTuple(first, second)