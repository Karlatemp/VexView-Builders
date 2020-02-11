/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: package-info.java@author: karlatemp@vip.qq.com: 2020/1/28 上午1:18@version: 2.0
 */
/**
 * 这里存有VexView各种组件的快速构建器. 可以很快的构建各种组件
 * <p>
 * 来自此模块的编写者:<br/>
 * 报错带有<b>此包的类</b>的时候请先确定是否是<b>你自身错误</b>然后在反馈给我<br/>
 * <span display="color: white; background: white;">QQ: 3279826484</span>
 * <p>
 * 项目开源地址: <a href="https://github.com/Karlatemp/VexView-Builders">VexView-Builders</a><br/>
 * 当前模块版本： 1.0.5
 * <p>
 * 注意: Builder于VexView v2.6版本开始内置, 此包的类开始版本是 "此模块版本" 并非 VexView 版本
 *
 * @author Karlatemp
 * @version 1.0.5
 * @since 2.6
 */
@BuildersModuleVersion("1.0.5")
@BuilderCommit("GitHub: https://github.com/Karlatemp/VexView-Builders")
package lk.vexview.builders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// @version 1.0.3: Mark module version
// Commits
@Retention(RetentionPolicy.CLASS)
@BuilderCommit("Source Location: package-info.java")
@interface BuilderCommit {
    String value();
}

// @version 1.0.3: Mark module version
@Retention(RetentionPolicy.CLASS)
@BuilderCommit("Tell user method add version/Module current version | Source Location: package-info.java")
@interface BuildersModuleVersion {
    String value();
}