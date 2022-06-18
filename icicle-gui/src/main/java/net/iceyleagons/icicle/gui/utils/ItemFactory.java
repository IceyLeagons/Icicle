/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.gui.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.iceyleagons.icicle.utilities.ErrorUtils.ILLEGAL;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 20, 2022
 */
public class ItemFactory {

    private final ItemStack item;

    private ItemMeta itemMeta;
    private List<String> lores = new ObjectArrayList<>();

    public ItemFactory(Material material) {
        this(material, 1);
    }

    public ItemFactory(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemFactory(ItemStack itemStack) {
        this.item = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        if (this.itemMeta == null)
            ILLEGAL("Could not get ItemMeta for ItemStack.");
    }

    public ItemFactory setMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemFactory setType(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemFactory customModelId(int id) {
        this.itemMeta.setCustomModelData(id);
        return this;
    }

    public ItemFactory setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemFactory addEnchantment(Enchantment enchantment, int level) {
        this.item.addEnchantment(enchantment, level);
        return this;
    }

    public ItemFactory addEnchantments(Map.Entry<Enchantment, Integer>... enchantments) {
        return addEnchantments(Arrays.stream(enchantments).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public ItemFactory addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.item.addEnchantments(enchantments);
        return this;
    }

    public ItemFactory removeEnchantment(Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemFactory setDisplayName(String displayName) {
        this.itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    public ItemFactory addItemFlags(ItemFlag... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemFactory removeItemFlags(ItemFlag... flags) {
        this.itemMeta.removeItemFlags(flags);
        return this;
    }

    public ItemFactory hideEverything() {
        return addItemFlags(ItemFlag.values());
    }

    public ItemFactory hideAttributes() {
        this.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemFactory showAttributes() {
        this.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemFactory addLore(String... lines) {
        this.lores.addAll(Arrays.asList(lines));
        return this;
    }

    public ItemFactory setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemFactory setLore(List<String> lore) {
        this.lores = lore;
        return this;
    }

    public ItemFactory removeLoreLine(int index) {
        this.lores.remove(index);
        return this;
    }

    public ItemFactory resetLore() {
        this.lores.clear();
        return this;
    }

    public ItemFactory setUnbreakable(boolean value) {
        this.itemMeta.setUnbreakable(value);
        return this;
    }

    public ItemFactory setDamage(int damage) {
        if (!(this.itemMeta instanceof Damageable))
            ILLEGAL("Item is not instance of Damageable!");

        ((Damageable) this.itemMeta).setDamage(damage);
        return this;
    }

    public ItemFactory addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        this.itemMeta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public ItemFactory removeAttributeModifier(Attribute attribute) {
        this.itemMeta.removeAttributeModifier(attribute);
        return this;
    }

    public ItemFactory removeAttributeModifier(EquipmentSlot equipmentSlot) {
        this.itemMeta.removeAttributeModifier(equipmentSlot);
        return this;
    }

    public ItemFactory removeAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        this.itemMeta.removeAttributeModifier(attribute, modifier);
        return this;
    }

    public ItemFactory setGlowing(boolean glowing) {
        Enchantment ench = this.item.getType() != Material.BOW ? Enchantment.ARROW_INFINITE : Enchantment.LUCK;
        if (glowing) {
            this.removeEnchantment(ench);
            this.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            return this;
        }

        this.addEnchantment(ench, 10);
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        this.itemMeta.setLore(new ArrayList<>(this.lores));
        this.item.setItemMeta(itemMeta);
        return this.item;
    }
}
