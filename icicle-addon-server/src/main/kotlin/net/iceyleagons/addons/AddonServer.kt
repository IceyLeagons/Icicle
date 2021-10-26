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
                it.enforceSsl = true
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
                    get("<name>") { ctx ->
                        NaiveRateLimit.requestPerTimeUnit(ctx, 30, TimeUnit.MINUTES)
                        val addon = addonStore.getAddon(ctx.pathParam("name"))
                        if (addon == null)
                            ctx.status(404)
                        else ctx.json(addon)
                    }
                    post { ctx ->
                        NaiveRateLimit.requestPerTimeUnit(ctx, 3, TimeUnit.MINUTES)
                        // TODO: Do actual database stuff? Ticket system? Who knows...
                    }
                    get("{id}") { ctx ->
                        NaiveRateLimit.requestPerTimeUnit(ctx, 50, TimeUnit.MINUTES)
                        val addon = addonStore.getAddonFromId(ctx.pathParam("id").toInt())
                        if (addon == null)
                            ctx.status(404)
                        else ctx.json(addon)
                    }
                    path("by") {
                        get("string/<name>") { ctx ->
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

}