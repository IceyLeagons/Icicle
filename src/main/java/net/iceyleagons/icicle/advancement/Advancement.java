/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.advancement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TOTHTOMI
 */
public class Advancement {

    @Getter
    private final NamespacedKey id;
    private final String parent;
    private final String icon;
    private final Backgrounds background;
    private final TextComponent title;
    private final TextComponent description;
    private final Frames frame;
    private final boolean announceToChat;
    private final boolean showToast;
    private final boolean hidden;
    @Getter
    private final List<Advancement> children = new ArrayList<>();

    public Advancement(NamespacedKey id, String parent, String icon, Backgrounds background, AdvancementMetadata advancementMetadata) {
        this.id = id;
        this.parent = parent;
        this.icon = icon;
        this.background = background;
        this.title = new TextComponent(ChatColor.translateAlternateColorCodes('&', advancementMetadata.getTitle()));
        this.description = new TextComponent(ChatColor.translateAlternateColorCodes('&', advancementMetadata.getDescription()));
        this.frame = advancementMetadata.getFrame();
        this.announceToChat = advancementMetadata.isAnnounceToChat();
        this.showToast = advancementMetadata.isShowToast();
        this.hidden = advancementMetadata.isHidden();
    }

    private static JSONObject jsonFromTextComponent(TextComponent textComponent) {
        ///Gson gson = new Gson();
        //
        if (textComponent == null) throw new IllegalArgumentException("TextComponent cannot be null!");
        return new JSONObject(ComponentSerializer.toString(textComponent));
    }

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONObject display = new JSONObject();

        JSONObject icon = new JSONObject();
        icon.put("item", this.icon);
        display.put("icon", icon);
        display.put("title", jsonFromTextComponent(title));
        display.put("description", jsonFromTextComponent(description));
        display.put("background", background.getPath());
        display.put("frame", frame.getId());
        display.put("announce_to_chat", announceToChat);
        display.put("show_toast", showToast);
        display.put("hidden", hidden);

        JSONObject criteria = new JSONObject(); //just an empty json object
        JSONObject imp = new JSONObject();
        imp.put("trigger", "minecraft:impossible");
        criteria.put("imp", imp);

        jsonObject.put("criteria", criteria);
        jsonObject.put("display", display);
        if (parent != null)
            jsonObject.put("parent", parent);

        return jsonObject;
    }

    public org.bukkit.advancement.Advancement getBukkitAdvancement() {
        return Bukkit.getAdvancement(id);
    }

    public Advancement addChild(String name, Material icon, AdvancementMetadata metadata) {
        Advancement advancement = new Advancement(new NamespacedKey(id.getNamespace(), name), id.toString(), icon.getKey().toString(), background,
                metadata);
        children.add(advancement);
        return advancement;
    }

    public void grant(Player player) {
        org.bukkit.advancement.Advancement a = getBukkitAdvancement();
        player.getAdvancementProgress(a).awardCriteria("imp");
        //Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "advancement grant " + player.getName() + id.toString());
    }

    public void revoke(Player player) {
        org.bukkit.advancement.Advancement a = getBukkitAdvancement();
        player.getAdvancementProgress(a).revokeCriteria("imp");
        //Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "advancement revoke " + player.getName() + id.toString());
    }

    public void register(boolean update) {
        if (Bukkit.getAdvancement(id) == null)
            Bukkit.getUnsafe().loadAdvancement(id, getJSON().toString());
        else if (update)
            Bukkit.getUnsafe().removeAdvancement(id);
    }


    @RequiredArgsConstructor
    @Getter
    public enum Frames {
        TASK("task"),
        GOAL("goal"),
        CHALLENGE("challenge");

        private final String id;
    }

    @RequiredArgsConstructor
    public static class Backgrounds {

        public static final Backgrounds ADVENTURE = new Backgrounds("adventure.png");
        public static final Backgrounds END = new Backgrounds("end.png");
        public static final Backgrounds HUSBANDRY = new Backgrounds("husbandry.png");
        public static final Backgrounds NETHER = new Backgrounds("nether.png");
        public static final Backgrounds STONE = new Backgrounds("stone.png");

        private final String fileName;

        public static Backgrounds pathOf(String customFilename) {
            return new Backgrounds(customFilename);
        }

        public String getPath() {
            return "minecraft:textures/gui/advancements/backgrounds/" + fileName;
        }

    }
}
