import net.iceyleagons.gradle.IcicleDependencyHelper

fun icicle() =
    IcicleDependencyHelper.icicleCore()

fun lombok() =
    IcicleDependencyHelper.lombok()

fun spigotApi() =
    IcicleDependencyHelper.spigotApi()

fun spigotApi(version: String) =
    IcicleDependencyHelper.spigotApi(version)

fun paperApi() =
    IcicleDependencyHelper.paperApi()

fun paperApi(version: String) =
    IcicleDependencyHelper.paperApi(version)

fun nms() =
    IcicleDependencyHelper.nms()

fun nms(version: String) =
    IcicleDependencyHelper.nms(version)