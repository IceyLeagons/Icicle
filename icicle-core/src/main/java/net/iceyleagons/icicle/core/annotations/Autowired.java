package net.iceyleagons.icicle.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark constructors as being auto wired, this is not a functional annotation, it's only used in documentation.
 * <p>
 * Since Icicle does not support field auto-wiring, and because the constructor auto
 * wiring is automatic (meaning no annotation needed) this annotation is only used for documentation ->
 * more understandable code (especially) for those, who are not familiar with the framework.
 *
 * @version 1.0.0
 * @since Oct. 31, 2021
 * @author TOTHTOMI
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.SOURCE)
public @interface Autowired {
}
