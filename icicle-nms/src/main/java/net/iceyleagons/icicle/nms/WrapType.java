package net.iceyleagons.icicle.nms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.nms.utils.AdvancedClass;
import net.iceyleagons.icicle.nms.utils.ClassHelper;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
@RequiredArgsConstructor
public enum WrapType {

    CRAFT_BUKKIT(ClassHelper::getBukkitClass), NMS(ClassHelper::getNMSClass);

    private final AdvancedClassProvider clazzProvider;

    interface AdvancedClassProvider {
        AdvancedClass<?> get(String name);
    }
}
