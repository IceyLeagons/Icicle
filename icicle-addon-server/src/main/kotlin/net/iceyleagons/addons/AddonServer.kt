package net.iceyleagons.addons

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.compression.CompressionStrategy
import io.javalin.http.util.NaiveRateLimit
import java.util.concurrent.TimeUnit

class AddonServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val addonStore = AddonStore()

            val app = Javalin.create {
                it.asyncRequestTimeout = 3_000L
                it.enableCorsForAllOrigins()
                it.enforceSsl = false
                it.defaultContentType = "application/json"
                it.compressionStrategy(CompressionStrategy.GZIP)
            }.start(8079)
            app.routes {
                path("addons") {
                    get { ctx ->
                        NaiveRateLimit.requestPerTimeUnit(ctx, 30, TimeUnit.MINUTES)
                        val page = (ctx.queryParam("page") ?: "0").toInt()
                        val perPage = (ctx.queryParam("perPage") ?: "10").toInt()

                        val addons = addonStore.getAddonsPage(perPage, page)
                        if (addons.isEmpty())
                            ctx.status(404)
                        else ctx.json(addons)
                    }
                    get("{name}") { ctx ->
                        NaiveRateLimit.requestPerTimeUnit(ctx, 30, TimeUnit.MINUTES)
                        val addon = addonStore.getAddon(ctx.pathParam("name"))
                        if (addon == null)
                            ctx.status(404)
                        else ctx.json(addon)
                    }
                    post { ctx ->
                        NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.MINUTES)
                        val addonData = ctx.bodyAsClass(AddonData::class.java)

                        val authToken = ctx.header("Authorization")

                        if (authToken == null) {
                            ctx.status(404)
                            return@post
                        }

                        val result = addonStore.tryUploadAddon(addonData, authToken.removePrefix("Token "))
                        if (result > 0) {
                            ctx.json(
                                ExecutionMessage(
                                    code = 200,
                                    error = 0,
                                    message = "Uploaded with the id of $result."
                                )
                            )
                            return@post
                        } else {
                            ctx.json(
                                when (result) {
                                    -401 -> ExecutionMessage(code = 401, error = 1, message = "Invalid token.")
                                    -405 -> ExecutionMessage(
                                        code = 405,
                                        error = 1,
                                        message = "Developer name is not allowed for this token."
                                    )
                                    else -> ExecutionMessage(code = 500, error = 1, message = "Upload failed.")
                                }
                            )
                        }
                    }
                    path("search") {
                        path("amount") {
                            get("tags/<tag>") { ctx ->
                                NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                                ctx.json(
                                    ExecutionMessage(
                                        code = 200,
                                        error = 0,
                                        message = "" + addonStore.getAddonAmountByTags(ctx.pathParam("tag"))
                                    )
                                )
                            }
                            get("developer/{dev}") { ctx ->
                                NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                                ctx.json(
                                    ExecutionMessage(
                                        code = 200,
                                        error = 0,
                                        message = "" + addonStore.getAddonAmountByDeveloper(ctx.pathParam("dev"))
                                    )
                                )
                            }
                            get("title/{title}") { ctx ->
                                NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                                ctx.json(
                                    ExecutionMessage(
                                        code = 200,
                                        error = 0,
                                        message = "" + addonStore.getAddonAmountByTitle(ctx.pathParam("title"))
                                    )
                                )
                            }
                            get("type/{type}") { ctx ->
                                NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                                ctx.json(
                                    ExecutionMessage(
                                        code = 200,
                                        error = 0,
                                        message = "" + addonStore.getAddonAmountByType(ctx.pathParam("type").toShort())
                                    )
                                )
                            }
                        }
                        get("tags/<tag>") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                            val page = (ctx.queryParam("page") ?: "0").toInt()
                            val perPage = (ctx.queryParam("perPage") ?: "10").toInt()
                            ctx.json(addonStore.searchAddonsPageByTags(ctx.pathParam("tag"), perPage, page))
                        }
                        get("developer/{dev}") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                            val page = (ctx.queryParam("page") ?: "0").toInt()
                            val perPage = (ctx.queryParam("perPage") ?: "10").toInt()
                            ctx.json(addonStore.searchAddonsPageByDeveloper(ctx.pathParam("dev"), perPage, page))
                        }
                        get("title/{title}") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                            val page = (ctx.queryParam("page") ?: "0").toInt()
                            val perPage = (ctx.queryParam("perPage") ?: "10").toInt()
                            ctx.json(addonStore.searchAddonsPageByTitle(ctx.pathParam("title"), perPage, page))
                        }
                        get("type/{type}") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)
                            val page = (ctx.queryParam("page") ?: "0").toInt()
                            val perPage = (ctx.queryParam("perPage") ?: "10").toInt()
                            ctx.json(addonStore.searchAddonsPageByType(ctx.pathParam("type").toShort(), perPage, page))
                        }
                    }
                    path("by") {
                        get("name/{name}") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 30, TimeUnit.MINUTES)
                            val addon = addonStore.getAddon(ctx.pathParam("name"))
                            if (addon == null)
                                ctx.status(404)
                            else ctx.json(addon)
                        }
                        get("id/{id}") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 50, TimeUnit.MINUTES)
                            val addon = addonStore.getAddonFromId(ctx.pathParam("id").toInt())
                            if (addon == null)
                                ctx.status(404)
                            else ctx.json(addon)
                        }
                        get("all") { ctx ->
                            NaiveRateLimit.requestPerTimeUnit(ctx, 30, TimeUnit.MINUTES)
                            val page = (ctx.queryParam("page") ?: "0").toInt()
                            val perPage = (ctx.queryParam("perPage") ?: "10").toInt()

                            val addons = addonStore.getAddonsPage(perPage, page)
                            if (addons.isEmpty())
                                ctx.status(404)
                            else ctx.json(addons)
                        }
                    }
                }
            }
        }
    }

    data class ExecutionMessage(val error: Int, val code: Int, val message: String = "No message provided.")

}