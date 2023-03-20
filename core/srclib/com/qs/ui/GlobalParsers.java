package com.qs.ui;

import net.mwplay.cocostudio.ui.BaseWidgetParser;
import net.mwplay.cocostudio.ui.parser.group.CCButton;
import net.mwplay.cocostudio.ui.parser.group.CCCheckBox;
import net.mwplay.cocostudio.ui.parser.group.CCLayer;
import net.mwplay.cocostudio.ui.parser.group.CCPageView;
import net.mwplay.cocostudio.ui.parser.group.CCPanel;
import net.mwplay.cocostudio.ui.parser.group.CCProjectNode;
import net.mwplay.cocostudio.ui.parser.group.CCScrollView;
import net.mwplay.cocostudio.ui.parser.group.CCSingleNode;
import net.mwplay.cocostudio.ui.parser.widget.CCImageView;
import net.mwplay.cocostudio.ui.parser.widget.CCLoadingBar;
import net.mwplay.cocostudio.ui.parser.widget.CCSlider;
import net.mwplay.cocostudio.ui.parser.widget.CCSprite;
import net.mwplay.cocostudio.ui.parser.widget.CCText;
import net.mwplay.cocostudio.ui.parser.widget.CCTextAtlas;
import net.mwplay.cocostudio.ui.parser.widget.CCTextBMFont;
import net.mwplay.cocostudio.ui.parser.widget.CCTextField;

import java.util.HashMap;
import java.util.Map;

public class GlobalParsers {
    public static GlobalParsers instance;

    public static GlobalParsers getInstance() {
        if (instance == null)
            instance = new GlobalParsers();
        return instance;
    }

    protected Map<Class, BaseWidgetParser> parsers;

    public GlobalParsers() {

        parsers = new HashMap<>();
        //GameMap 1
//		addParser(new CCParticle());//粒子 2
        //SimpleAudio 3
        addParser(new CCSingleNode());//节点 4
        addParser(new CCSprite());//精灵 5

        addParser(new CCButton());//按钮 1
        addParser(new CCCheckBox());//复选框 2
        addParser(new CCImageView());//图片 3

        addParser(new CCText());//文本 4
        addParser(new CCTextAtlas());//? 5
        addParser(new CCTextBMFont());//艺术字体 6

        addParser(new CCLoadingBar());//进度条 7
        addParser(new CCSlider());//滑动条 8
        addParser(new CCTextField());//输入框 9

        addParser(new CCPanel());//基础容器 1
        //CCListView 2
        addParser(new CCPageView());//翻页容器 3
        addParser(new CCScrollView());//滚动容器 4

//		addParser(new CCLabelAtlas());//?
//		addParser(new CCTImageView());//?

        addParser(new CCProjectNode());//项目节点

        addParser(new CCLayer());//图层
    }

    public static Map<Class, BaseWidgetParser> getParsers() {
        return getInstance().parsers;
    }

    public void addParser(BaseWidgetParser parser) {
        parsers.put(parser.getClassName(), parser);

    }

}
