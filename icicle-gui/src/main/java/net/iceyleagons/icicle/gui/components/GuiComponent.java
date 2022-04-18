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

package net.iceyleagons.icicle.gui.components;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.gui.GUI;
import net.iceyleagons.icicle.gui.Pane;
import net.iceyleagons.icicle.gui.StateAware;
import net.iceyleagons.icicle.gui.utils.Pos2i;

import static net.iceyleagons.icicle.utilities.ErrorUtils.ILLEGAL;


/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
@Getter
@EqualsAndHashCode
public abstract class GuiComponent implements StateAware, Drawable {

    protected int x;
    protected int y;

    protected int width;
    protected int height;

    private boolean visible;
    private boolean dirty = true;

    @Setter
    private Pane parent = null;

    public GuiComponent(int x, int y) {
        this(x, y, 1, 1);
    }

    public GuiComponent(int x, int y, int width, int height) {
        if (width == 0 || height == 0)
            ILLEGAL("Width and Height must be at least 1!");

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.visible = true;
    }

    public void setX(int x) {
        this.x = x;
        setDirty(true);
    }

    public void setY(int y) {
        this.y = y;
        setDirty(true);
    }

    public void setWidth(int width) {
        this.width = width;
        setDirty(true);
    }

    public void setHeight(int height) {
        this.height = height;
        setDirty(true);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        setDirty(true);
    }

    public Pos2i getPosition() {
        return new Pos2i(x, y);
    }

    public boolean isPartOf(int rowSize, int slot, int xOff, int yOff, int mW, int mH) {
        int width = Math.min(this.width, mW);
        int height = Math.min(this.height, mH);

        int dX = getX() - 1 + xOff;
        int dY = getY() - 1 + yOff;

        int dS = slot - dX - rowSize * dY;

        int x = dS % rowSize;
        int y = dS / rowSize;

        return x >= 0 && x < width && y >= 0 && y < height;
    }

    protected void requestGuiUpdate() {
        if (this.getParent() == null) return;

        GUI gui = this.getParent().getGui();
        if (gui != null) {
            gui.update();
        }
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        if (this.parent != null) this.parent.setDirty(true);
        this.dirty = dirty;
    }
}
