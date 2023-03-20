/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.mwplay.cocostudio.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.qs.ui.util.NUtils;
import net.mwplay.cocostudio.ui.model.ObjectData;

public abstract class BaseWidgetParser<T extends ObjectData> {

    BaseCocoStudioUIEditor editor;

    /**
     * 由于libgdx的zindex并不表示渲染层级,所以这里采用这种方式来获取子控件的当前层级
     */

    /**
     * get widget type name
     */
    public abstract Class getClassName();

    /**
     * convert cocostudio widget to libgdx actor
     */
    public abstract Actor parse(BaseCocoStudioUIEditor editor, T widget);

    /**
     * common attribute parser
     * according cocstudio ui setting properties of the configuration file
     *
     * @param editor
     * @param widget
     * @param parent
     * @param actor
     * @return
     */
    public Actor commonParse(BaseCocoStudioUIEditor editor, T widget, Group parent, Actor actor) {
        this.editor = editor;
        actor.setName(widget.Name);
        actor.setSize(widget.Size.X, widget.Size.Y);
        // set origin
        if (widget.AnchorPoint != null) {
            actor.setOrigin(widget.AnchorPoint.ScaleX * actor.getWidth(), widget.AnchorPoint.ScaleY * actor.getHeight());
        }

        //判空，因为新版本的单独节点没有Postion属性
        if (widget.Position != null) {
            actor.setPosition(widget.Position.X - actor.getOriginX(), widget.Position.Y - actor.getOriginY());
        }

        // CocoStudio的编辑器ScaleX,ScaleY 会有负数情况
        //判空，因为新版本的单独节点没有Scale属性
        if (widget.Scale != null) {
            if (actor instanceof Label)
                ((Label) actor).setFontScale(widget.Scale.ScaleX, widget.Scale.ScaleY);
            else
                actor.setScale(widget.Scale.ScaleX, widget.Scale.ScaleY);
        }

        if (widget.Rotation != 0) {// CocoStudio 是顺时针方向旋转,转换下.
            actor.setRotation(360 - widget.Rotation % 360);
        }
        //添加倾斜角
        if (widget.RotationSkewX != 0 && MathUtils.isEqual(widget.RotationSkewX, widget.RotationSkewY, 0.1f)) {
            actor.setRotation(360 - widget.RotationSkewX % 360);
        }

        // 设置可见
        actor.setVisible(widget.VisibleForFrame);

        Color color = NUtils.getColor(widget.CColor, widget.Alpha);

        actor.setColor(color);

        actor.setTouchable(deduceTouchable(actor, widget));

        if (widget.Children == null || widget.Children.size() == 0) {
            return actor;
        }

        return null;
    }

    private Touchable deduceTouchable(Actor actor, T widget) {
        if (widget.TouchEnable) {
            return Touchable.enabled;
        } else if (Touchable.childrenOnly.equals(actor.getTouchable())) {
            return Touchable.childrenOnly;
        } else {
            return Touchable.disabled;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

}
