//package com.mygdx.game.Draw.Actor;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Touchable;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
//import com.badlogic.gdx.utils.Clipboard;
//import com.badlogic.gdx.utils.FloatArray;
//import com.mygdx.game.Draw.ClickListener.TextFieldListener;
//import com.mygdx.game.Res;
//import com.qs.utils.assets.Assets;
//
//public class SimpleTextFieldActor extends Actor {
//    private TextFieldStyle style;
//    private float curserRegionX;
//    private float curserRegionY;
//
//    protected Clipboard clipboard; //剪贴板，调用libgdx自带的剪贴版
//    protected int selectionStart, selectionEnd;
//    protected float fontOffset;
//    protected final FloatArray glyphPositions = new FloatArray();
//    protected final GlyphLayout layout = new GlyphLayout();
//    private StringBuilder displayText;
//    protected String text;
//
//    protected boolean isUse = true; //是否使用这个Text
//    protected boolean isShowSelection = false;
//
//    protected TextureRegion selectionRegion;
//
//    public SimpleTextFieldActor(TextFieldStyle textFieldStyle) {
//        setStyle(textFieldStyle);
//        init();
//    }
//
//    protected void initSelection() {
//        selectionRegion = new TextureRegion(Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png"));
//    }
//
//    protected void init() {
//        if (style == null) {
//            throw new RuntimeException("style is no default!");
//        } else if (style.curserRegion == null) {
//            throw new RuntimeException("curserRegion is no default!");
//        } else if (style.bitmapFont == null) {
//            throw new RuntimeException("bitmapFont is no default!");
//        }
//
//        clipboard = Gdx.app.getClipboard(); //剪贴板实例化
//        displayText = new StringBuilder();
//
//        //chushi
//        if (style.curserRegionWidth == 0) {
//            style.curserRegionWidth = style.curserRegion.getRegionWidth();
//        }
//
//        if (style.backGroundRegion != null) {
//            if (style.backGroundRegionWidth == 0) {
//                setWidth(style.backGroundRegion.getRegionWidth());
//            } else {
//                setWidth(style.backGroundRegionWidth);
//            }
//
//            if (style.backGroundRegionHeight == 0) {
//                setHeight(style.backGroundRegion.getRegionHeight());
//            } else {
//                setHeight(style.backGroundRegionHeight);
//            }
//        }
//
//        addListener(new TextFieldListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                if (super.touchDown(event, x, y, pointer, button)) {
//                    return true;
//                }
//                return false;
//            }
//
//
//            @Override
//            public void copy() {
//
//            }
//
//
//        });
//    }
//
//    /**
//     * Copies the contents of this TextField to the {@link Clipboard} implementation set on this TextField.
//     */
//    public void copy() {
//        if (hasSelection && !passwordMode) {
//            clipboard.setContents(text.substring(Math.min(cursor, selectionStart), Math.max(cursor, selectionStart)));
//        }
//    }
//
//    /**
//     * Copies the selected contents of this TextField to the {@link Clipboard} implementation set on this TextField, then removes
//     * it.
//     */
//    public void cut() {
//        cut(clipboard);
//    }
//
//    void cut(boolean fireChangeEvent) {
//        if (hasSelection && !passwordMode) {
//            copy();
//            cursor = delete(fireChangeEvent);
//            updateDisplayText();
//        }
//    }
//
//    void paste(String content, boolean fireChangeEvent) {
//        if (content == null) return;
//        StringBuilder buffer = new StringBuilder();
//        int textLength = text.length();
//        if (hasSelection) textLength -= Math.abs(cursor - selectionStart);
//        BitmapFont.BitmapFontData data = style.font.getData();
//        for (int i = 0, n = content.length(); i < n; i++) {
//            if (!withinMaxLength(textLength + buffer.length())) break;
//            char c = content.charAt(i);
//            if (!(writeEnters && (c == ENTER_ANDROID || c == ENTER_DESKTOP))) {
//                if (c == '\r' || c == '\n') continue;
//                if (onlyFontChars && !data.hasGlyph(c)) continue;
//                if (filter != null && !filter.acceptChar(this, c)) continue;
//            }
//            buffer.append(c);
//        }
//        content = buffer.toString();
//
//        if (hasSelection) cursor = delete(fireChangeEvent);
//        if (fireChangeEvent)
//            changeText(text, insert(cursor, content, text));
//        else
//            text = insert(cursor, content, text);
//        updateDisplayText();
//        cursor += content.length();
//    }
//
//    String insert(int insertIndex, CharSequence text, String to) {
//        if (to.length() == 0) return text.toString();
//        return to.substring(0, position) + text + to.substring(position, to.length());
//    }
//
//
//    void updateDisplayText() {
//        BitmapFont font = style.bitmapFont;
//        BitmapFont.BitmapFontData data = font.getData();
//        String text = this.text;
//        int textLength = text.length();
//
//        StringBuilder buffer = new StringBuilder();
//        for (int i = 0; i < textLength; i++) {
//            char c = text.charAt(i);
//            //判断是否存在这个字符
//            buffer.append(data.hasGlyph(c) ? c : ' ');
//        }
//
//        String newDisplayText = buffer.toString();
//        displayText = buffer;
//
//        layout.setText(font, newDisplayText.replace('\r', ' ').replace('\n', ' '));
//        glyphPositions.clear();
//        float x = 0;
//        if (layout.runs.size > 0) {
//            GlyphLayout.GlyphRun run = layout.runs.first();
//            FloatArray xAdvances = run.xAdvances;
//            fontOffset = xAdvances.first();
//            for (int i = 1, n = xAdvances.size; i < n; i++) {
//                glyphPositions.add(x);
//                x += xAdvances.get(i);
//            }
//        } else
//            fontOffset = 0;
//        glyphPositions.add(x);
//    }
//
//    //根据偏移宽度获得准确第几个char之间
//    protected int letterUnderCursor(float offsetX) {
//        offsetX -= fontOffset;
//        int n = glyphPositions.size;
//        for (int i = 0; i < n; ++i) {
//            if (glyphPositions.get(i) > offsetX) {
//                if (glyphPositions.get(i) - offsetX <= offsetX - glyphPositions.get(i - 1)) {
//                    return i;
//                }
//                return i - 1;
//            }
//        }
//        return n - 1;
//    }
//
//    protected RepeatAction getWaitRegionAction() {
//        return Actions.forever(
//                Actions.sequence(
//                        Actions.delay(0.1f),
//                        Actions.alpha(0, style.twinkleTime),
//                        Actions.alpha(1, style.twinkleTime)
//                )
//        );
//    }
//
//    @Override
//    public void setTouchable(Touchable touchable) {
//        super.setTouchable(touchable);
//
//        if (touchable == Touchable.disabled) {
//            hideCurser();
//        } else {
//            showCurser();
//        }
//    }
//
//    //展示光标: 光标默认
//    public void showCurser() {
//        addAction(getWaitRegionAction());
//    }
//
//    //隐藏光标
//    public void hideCurser() {
//        clearActions();
//    }
//
//    public void showSelection() {
//        isShowSelection = true;
//
//    }
//
//    public void hideSelection() {
//        isShowSelection = false;
//    }
//
//    public String getClipBoard() {
//        return clipboard.getContents();
//    }
//
//    public void setStyle(TextFieldStyle textFieldStyle) {
//        style = textFieldStyle;
//    }
//
//    public TextFieldStyle getStyle() {
//        return style;
//    }
//
//    public void setIsUse(boolean isUse) {
//        if (!isUse) {
//            hideCurser();
//        } else if (!isUse()) {
//            showCurser();
//        }
//        this.isUse = isUse;
//    }
//
//    public boolean isUse() {
//        return isUse;
//    }
//
//    @Override
//    protected void sizeChanged() {
//        if (style.curserRegionHeight == 0) {
//            style.curserRegionWidth = style.curserRegion.getRegionHeight();
//        }
//        style.curserRegionWidth = Math.min(style.curserRegionHeight, getHeight());
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        if (style.backGroundRegion != null) {
//            batch.draw(style.backGroundRegion, getX(), getY(), getWidth(), getHeight());
//        }
//
//        if (isUse) {
//            batch.draw(style.curserRegion, curserRegionX, curserRegionY, style.curserRegionWidth, style.curserRegionHeight);
//        }
//    }
//
//
//    public class TextFieldStyle {
//        public TextureRegion backGroundRegion;
//        public float backGroundRegionWidth;
//        public float backGroundRegionHeight;
//
//        public float twinkleTime = 0.32f;
//        public int maxNum;
//
//        public float curserRegionWidth;
//        public float curserRegionHeight;
//        //Must
//        public TextureRegion curserRegion;
//        public BitmapFont bitmapFont;
//    }
//}
