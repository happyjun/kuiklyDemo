package com.mqunar.mykuiklyapplication

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.*
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.module.RouterModule
import com.tencent.kuikly.core.timer.setTimeout
import com.tencent.kuikly.core.views.*
import com.tencent.kuikly.core.reactive.handler.*
import com.mqunar.mykuiklyapplication.base.BasePager
import com.mqunar.mykuiklyapplication.module.StorageModule
import com.tencent.kuikly.core.datetime.DateTime

@Page("router", supportInLocal = true)
internal class RouterPage : BasePager() {

    override fun created() {
        super.created()
        val storage = acquireModule<StorageModule>(StorageModule.MODULE_NAME)
        repeat(STORAGE_LOOP_COUNT) { index ->
            storage.putString(
                sandbox = STORAGE_SANDBOX,
                key = "router_storage_test_$index",
                value = "value_$index",
                owner = STORAGE_OWNER,
            )
        }
        repeat(STORAGE_LOOP_COUNT) { index ->
            storage.getString(
                sandbox = STORAGE_SANDBOX,
                key = "router_storage_test_$index",
                owner = STORAGE_OWNER,
            )
        }
        setTimeout(timeout = STORAGE_TIMEOUT_DELAY_MS) {
            repeat(STORAGE_TIMEOUT_PUT_COUNT) { index ->
                sleepMs(STORAGE_PUT_SLEEP_MS)
                storage.putString(
                    sandbox = STORAGE_SANDBOX,
                    key = "router_storage_timeout_$index",
                    value = "timeout_value_$index",
                    owner = STORAGE_OWNER,
                )
            }
        }

        repeat(150) { index ->
            setTimeout(timeout = 10) {
                sleepMs(3)
                storage.putString(
                    sandbox = STORAGE_SANDBOX,
                    key = "router_storage_timeout_$index",
                    value = "timeout_value_$index",
                    owner = STORAGE_OWNER,
                )
            }
        }

        repeat(150) { index ->
            setTimeout(timeout = 10) {
                sleepMs(4)
                storage.putString(
                    sandbox = STORAGE_SANDBOX,
                    key = "router_storage_timeout_$index",
                    value = "timeout_value_$index",
                    owner = STORAGE_OWNER,
                )
            }
        }
    }

    private fun sleepMs(ms: Long) {
        val deadline = DateTime.currentTimestamp() + ms
        while (DateTime.currentTimestamp() < deadline) {
        }
    }

    override fun body(): ViewBuilder {
        val pageWidth = pageData.pageViewWidth.toFloat().coerceAtLeast(1f)
        val pageHeight = pageData.pageViewHeight.toFloat().coerceAtLeast(1f)
        val bottomInset = pageData.safeAreaInsets.bottom.coerceAtLeast(0f)
        val topInset = kotlin.math.max(
            pageData.statusBarHeight,
            pageData.safeAreaInsets.top.coerceAtLeast(0f),
        )
        val shellW = RouterHomeLayout.shellBodyContentWidth(pageWidth)
        val side = RouterHomeLayout.SIDE_MARGIN
        val bannerH = 220f
        val logTag = "RouterPageHomeMockBody"
        val U = RouterHomeMockUi
        return {
            attr {
                positionRelative()
                width(pageWidth)
                height(pageHeight)
                flexDirectionColumn()
                backgroundColor(Color(RouterHomePalette.PAGE_BG))
            }

            Scroller {
                attr {
                    width(pageWidth)
                    flex(1f)
                    bouncesEnable(true)
                    showScrollerIndicator(true)
                }
                View {
                    attr {
                        width(pageWidth)
                        flexDirectionColumn()
                    }

                    // 顶部 Banner（与 AdBanner 兜底图一致）
                    View {
                        attr {
                            width(pageWidth)
                            height(bannerH)
                            positionRelative()
                            overflow(false)
                        }
                        Image {
                            attr {
                                width(pageWidth)
                                height(bannerH)
                                src(U.ASSET_BANNER_BG)
                                resizeCover()
                            }
                        }
                        View {
                            attr {
                                absolutePosition(left = 0f, top = 0f, right = 0f, bottom = 0f)
                                flexDirectionColumn()
                                paddingLeft(side)
                                paddingRight(side)
                                paddingTop(topInset + 6f)
                            }
                            View {
                                attr {
                                    flexDirectionRow()
                                    alignItemsCenter()
                                }
                                View {
                                    attr {
                                        width(36f)
                                        height(36f)
                                        borderRadius(18f)
                                        backgroundColor(Color(0x7F000000.toInt()))
                                        allCenter()
                                    }
                                    event {
                                        click { KLog.d(logTag, "mock back") }
                                    }
                                    Text {
                                        attr {
                                            text("‹")
                                            fontSize(22f)
                                            color(Color.WHITE)
                                            marginLeft(-2f)
                                        }
                                    }
                                }
                            }
                            View {
                                attr {
                                    marginTop(12f)
                                    flexDirectionColumn()
                                }
                                Text {
                                    attr {
                                        text("百元机票搜索器")
                                        fontSize(22f)
                                        lineHeight(30f)
                                        color(Color(U.COLOR_HOME_TEXT_PRIMARY))
                                        fontWeight600()
                                    }
                                }
                                Text {
                                    attr {
                                        marginTop(4f)
                                        text("轻松帮你找低价")
                                        fontSize(13f)
                                        color(Color(U.COLOR_HOME_TEXT_SECONDARY))
                                    }
                                }
                            }
                        }
                    }

                    // 主卡片
                    View {
                        attr {
                            width(pageWidth)
                            marginTop(-18f)
                            borderRadius(
                                BorderRectRadius(
                                    U.SHELL_RADIUS,
                                    U.SHELL_RADIUS,
                                    0f,
                                    0f,
                                ),
                            )
                            backgroundColor(Color.WHITE)
                            flexDirectionColumn()
                            paddingLeft(U.SHELL_PADDING_HORIZONTAL)
                            paddingRight(U.SHELL_PADDING_HORIZONTAL)
                            paddingTop(U.SHELL_PADDING_TOP)
                            paddingBottom(20f)
                        }

                        // 机票 / 低价机票
                        View {
                            attr {
                                width(shellW)
                                flexDirectionRow()
                                alignItemsFlexEnd()
                                justifyContentFlexStart()
                                marginBottom(U.WAY_TAB_MARGIN_BOTTOM)
                            }
                            val mainTabs = listOf("机票", "低价机票")
                            mainTabs.forEachIndexed { index, label ->
                                View {
                                    attr {
                                        marginRight(if (index == 0) 28f else 0f)
                                        flexDirectionColumn()
                                        alignItemsCenter()
                                    }
                                    Text {
                                        attr {
                                            text(label)
                                            fontSize(
                                                if (index == 0) U.MAIN_TAB_FONT_SELECTED
                                                else U.MAIN_TAB_FONT_UNSELECTED,
                                            )
                                            lineHeight(
                                                if (index == 0) U.MAIN_TAB_LINE_HEIGHT_SELECTED
                                                else U.MAIN_TAB_LINE_HEIGHT_UNSELECTED,
                                            )
                                            color(
                                                Color(
                                                    if (index == 0) U.COLOR_HOME_TEXT_PRIMARY
                                                    else U.COLOR_HOME_TEXT_SECONDARY,
                                                ),
                                            )
                                            if (index == 0) fontWeight600() else fontWeight400()
                                        }
                                    }
                                    if (index == 0) {
                                        View {
                                            attr {
                                                marginTop(U.MAIN_TAB_LABEL_TO_UNDERLINE_GAP)
                                                width(U.MAIN_TAB_UNDERLINE_WIDTH)
                                                height(U.MAIN_TAB_UNDERLINE_HEIGHT)
                                                borderRadius(U.MAIN_TAB_UNDERLINE_HEIGHT / 2f)
                                                backgroundColor(Color(U.COLOR_HOME_ACCENT))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                height(U.HOME_WAY_TAB_HEIGHT)
                                borderRadius(U.HOME_WAY_TAB_OUTER_RADIUS)
                                backgroundColor(Color(U.COLOR_HOME_WAY_TAB_BG))
                                flexDirectionRow()
                                alignItemsCenter()
                                paddingLeft(U.HOME_WAY_TAB_INSET)
                                paddingRight(U.HOME_WAY_TAB_INSET)
                                paddingTop(U.HOME_WAY_TAB_INSET)
                                paddingBottom(U.HOME_WAY_TAB_INSET)
                                marginBottom(16f)
                            }
                            val ways = listOf("单程", "往返", "多程")
                            ways.forEachIndexed { index, label ->
                                View {
                                    attr {
                                        flex(1f)
                                        height(U.HOME_WAY_TAB_INNER_HEIGHT)
                                        borderRadius(U.HOME_WAY_TAB_INNER_RADIUS)
                                        if (index == 0) {
                                            backgroundColor(Color.WHITE)
                                        }
                                        allCenter()
                                    }
                                    Text {
                                        attr {
                                            text(label)
                                            fontSize(U.WAY_TAB_FONT_SIZE)
                                            color(
                                                Color(
                                                    if (index == 0) U.COLOR_HOME_TEXT_PRIMARY
                                                    else U.COLOR_HOME_TEXT_SECONDARY,
                                                ),
                                            )
                                            if (index == 0) fontWeight500() else fontWeight400()
                                        }
                                    }
                                }
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                flexDirectionRow()
                                alignItemsCenter()
                                marginBottom(12f)
                            }
                            Text {
                                attr {
                                    flex(1f)
                                    text("阿斯塔纳")
                                    fontSize(U.CITY_ROUTE_NAME_SIZE)
                                    color(Color(U.COLOR_CITY_DEFAULT))
                                    fontWeight600()
                                }
                            }
                            View {
                                attr {
                                    width(44f)
                                    height(44f)
                                    allCenter()
                                }
                                event {
                                    click { KLog.d(logTag, "mock swap city") }
                                }
                                Image {
                                    attr {
                                        width(32f)
                                        height(32f)
                                        src(U.ASSET_SWAP)
                                        resizeContain()
                                    }
                                }
                            }
                            Text {
                                attr {
                                    flex(1f)
                                    text("釜山")
                                    fontSize(U.CITY_ROUTE_NAME_SIZE)
                                    color(Color(U.COLOR_CITY_DEFAULT))
                                    fontWeight600()
                                    textAlignRight()
                                }
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                height(1f)
                                backgroundColor(Color(U.COLOR_HOME_DIVIDER))
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                flexDirectionRow()
                                alignItemsCenter()
                                justifyContentSpaceBetween()
                                marginTop(14f)
                                marginBottom(14f)
                            }
                            View {
                                attr {
                                    flexDirectionRow()
                                    alignItemsCenter()
                                }
                                Text {
                                    attr {
                                        text("5月15日")
                                        fontSize(17f)
                                        color(Color(U.COLOR_DATE_TEXT))
                                        fontWeight600()
                                    }
                                }
                                Text {
                                    attr {
                                        marginLeft(8f)
                                        text("明天")
                                        fontSize(13f)
                                        color(Color(U.COLOR_HOME_TEXT_SECONDARY))
                                    }
                                }
                            }
                            Text {
                                attr {
                                    text("返程日期")
                                    fontSize(14f)
                                    color(Color(U.COLOR_HOME_TEXT_SECONDARY))
                                }
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                height(1f)
                                backgroundColor(Color(U.COLOR_HOME_DIVIDER))
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                flexDirectionRow()
                                alignItemsCenter()
                                marginTop(14f)
                                marginBottom(14f)
                            }
                            event {
                                click { KLog.d(logTag, "mock passenger") }
                            }
                            Text {
                                attr {
                                    flex(1f)
                                    text("成人 1  儿童 0  婴儿 0")
                                    fontSize(14f)
                                    color(Color(RouterHomePalette.ATOM_FLIGHT_TEXT_222222))
                                    fontWeight500()
                                }
                            }
                            Text {
                                attr {
                                    text("选择乘机人获得精准低价 >")
                                    fontSize(12f)
                                    color(Color(U.COLOR_HOME_ACCENT))
                                }
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                height(1f)
                                backgroundColor(Color(U.COLOR_HOME_DIVIDER))
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                flexDirectionRow()
                                alignItemsCenter()
                                marginTop(14f)
                                marginBottom(12f)
                            }
                            View {
                                attr {
                                    flex(1f)
                                    height(32f)
                                    borderRadius(8f)
                                    backgroundColor(Color(U.COLOR_CABIN_TRACK))
                                    flexDirectionRow()
                                    padding(2f)
                                    alignItemsCenter()
                                }
                                View {
                                    attr {
                                        flex(1f)
                                        height(28f)
                                        borderRadius(6f)
                                        backgroundColor(Color(0xFFF5F5F5.toInt()))
                                        allCenter()
                                    }
                                    Text {
                                        attr {
                                            text("舱位不限")
                                            fontSize(12f)
                                            color(Color(U.COLOR_HOME_TEXT_PRIMARY))
                                            fontWeight500()
                                        }
                                    }
                                }
                                View {
                                    attr {
                                        flex(1f)
                                        height(28f)
                                        allCenter()
                                    }
                                    Text {
                                        attr {
                                            text("公务/头等舱")
                                            fontSize(12f)
                                            color(Color(U.COLOR_CABIN_SEGMENT_MUTED))
                                        }
                                    }
                                }
                            }
                        }

                        Text {
                            attr {
                                width(shellW)
                                marginBottom(10f)
                                text("超优惠 您有1张¥25抵扣券，速速下单 >")
                                fontSize(11f)
                                color(Color(U.COLOR_HOME_ACCENT))
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                height(U.SEARCH_BTN_HEIGHT)
                                borderRadius(U.SEARCH_BTN_RADIUS)
                                alignSelfCenter()
                                overflow(true)
                                backgroundLinearGradient(
                                    Direction.TO_RIGHT,
                                    ColorStop(Color(U.COLOR_SEARCH_BTN_START), 0f),
                                    ColorStop(Color(U.COLOR_SEARCH_BTN_END), 1f),
                                )
                            }
                            event {
                                click { KLog.d(logTag, "mock search") }
                            }
                            View {
                                attr {
                                    absolutePosition(left = 0f, top = 0f, right = 0f, bottom = 0f)
                                    allCenter()
                                }
                                Text {
                                    attr {
                                        text("搜索")
                                        fontSize(U.SEARCH_BTN_FONT_SIZE)
                                        color(Color(U.COLOR_SEARCH_BTN_TEXT))
                                        fontWeightBold()
                                    }
                                }
                            }
                        }

                        Text {
                            attr {
                                width(shellW)
                                marginTop(10f)
                                textAlignCenter()
                                text("低价购·安心飞优惠说明 (i)")
                                fontSize(11f)
                                color(Color(U.COLOR_HOME_TEXT_SECONDARY))
                            }
                        }

                        // 运营条占位（非 RN，避免 mock 阶段 RN 白屏干扰）
                        View {
                            attr {
                                width(shellW)
                                marginTop(16f)
                                height(72f)
                                borderRadius(8f)
                                backgroundLinearGradient(
                                    Direction.TO_RIGHT,
                                    ColorStop(Color(0xFFFFD54F.toInt()), 0f),
                                    ColorStop(Color(0xFFFF8A65.toInt()), 1f),
                                )
                                allCenter()
                            }
                            Text {
                                attr {
                                    text("518周年庆 抽518元最高机票神券啦  GO")
                                    fontSize(14f)
                                    color(Color(U.COLOR_HOME_TEXT_PRIMARY))
                                    fontWeight600()
                                }
                            }
                        }

                        View {
                            attr {
                                width(shellW)
                                marginTop(10f)
                                flexDirectionRow()
                                justifyContentSpaceBetween()
                            }
                            View {
                                attr {
                                    flex(1f)
                                    height(88f)
                                    marginRight(6f)
                                    borderRadius(8f)
                                    backgroundColor(Color(0xFFE3F2FD.toInt()))
                                    paddingLeft(10f)
                                    paddingTop(10f)
                                }
                                Text {
                                    attr {
                                        text("家庭特权")
                                        fontSize(13f)
                                        color(Color(U.COLOR_HOME_TEXT_PRIMARY))
                                        fontWeight600()
                                    }
                                }
                            }
                            View {
                                attr {
                                    flex(1f)
                                    height(88f)
                                    marginLeft(6f)
                                    borderRadius(8f)
                                    backgroundColor(Color(0xFFFFEBEE.toInt()))
                                    paddingLeft(10f)
                                    paddingTop(10f)
                                }
                                Text {
                                    attr {
                                        text("招行特惠")
                                        fontSize(13f)
                                        color(Color(U.COLOR_HOME_TEXT_PRIMARY))
                                        fontWeight600()
                                    }
                                }
                                Text {
                                    attr {
                                        marginTop(6f)
                                        text("领100元神券  领取 >")
                                        fontSize(11f)
                                        color(Color(0xFFE53935.toInt()))
                                    }
                                }
                            }
                        }

                        View {
                            attr {
                                width(pageWidth)
                                height(24f)
                            }
                        }
                    }
                }
            }

            // 底部 Tab（与 BottomBarVm 默认资源一致）
            View {
                attr {
                    width(pageWidth)
                    flexDirectionColumn()
                    backgroundColor(Color.WHITE)
                }
                View {
                    attr {
                        width(pageWidth)
                        height(1f)
                        backgroundColor(Color(U.COLOR_HOME_DIVIDER))
                    }
                }
                View {
                    attr {
                        width(pageWidth)
                        flexDirectionRow()
                        alignItemsFlexEnd()
                        justifyContentSpaceAround()
                        paddingTop(6f)
                        paddingBottom(bottomInset + 6f)
                    }
                    val tabs = listOf(
                        Triple("assets://common/flight_bottom_tab_home_icon.png", "机票首页", true),
                        Triple("assets://common/flight_bottom_tab_calendar_icon.png", "航班动态", false),
                        Triple("assets://common/flight_bottom_tab_activity_icon.png", "权益中心", false),
                        Triple("assets://common/flight_bottom_tab_coupon_icon.png", "领券中心", false),
                        Triple("assets://common/flight_bottom_tab_order_icon.png", "我的订单", false),
                    )
                    tabs.forEach { (icon, label, selected) ->
                        View {
                            attr {
                                flex(1f)
                                flexDirectionColumn()
                                alignItemsCenter()
                            }
                            event {
                                click { KLog.d(logTag, "mock tab $label") }
                            }
                            Image {
                                attr {
                                    width(24f)
                                    height(24f)
                                    src(icon)
                                    resizeContain()
                                }
                            }
                            Text {
                                attr {
                                    marginTop(2f)
                                    text(label)
                                    fontSize(10f)
                                    color(
                                        Color(
                                            if (selected) U.COLOR_HOME_ACCENT
                                            else U.COLOR_HOME_TEXT_SECONDARY,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val STORAGE_SANDBOX = "router_storage_test"
        const val STORAGE_OWNER = "mykuikly"
        const val STORAGE_LOOP_COUNT = 50
        const val STORAGE_TIMEOUT_PUT_COUNT = 10
        const val STORAGE_TIMEOUT_DELAY_MS = 0
        const val STORAGE_PUT_SLEEP_MS = 2L
    }
}

/** 与 [HomePage] mock body 对齐的布局常量 */
private object RouterHomeLayout {
    const val SIDE_MARGIN = 12f
    private const val SHELL_BODY_HORIZONTAL_INSET = SIDE_MARGIN + 20f

    fun shellBodyContentWidth(pageWidth: Float): Float =
        (pageWidth - 2f * SHELL_BODY_HORIZONTAL_INSET).coerceAtLeast(1f)
}

private object RouterHomePalette {
    const val PAGE_BG = 0xFFF2F4F7.toInt()
    const val ATOM_FLIGHT_TEXT_222222 = 0xFF222222.toInt()
}

/** 与 [HomePage] 中 HomePageMockUi 对齐的 mock UI 常量 */
private object RouterHomeMockUi {
    const val ASSET_BANNER_BG = "assets://common/flight_home_default_bg.png"
    const val ASSET_SWAP = "assets://common/flight_home_city_swip_flight_icon.webp"

    const val MAIN_TAB_FONT_SELECTED = 18f
    const val MAIN_TAB_FONT_UNSELECTED = 15f
    const val MAIN_TAB_LINE_HEIGHT_SELECTED = 25f
    const val MAIN_TAB_LINE_HEIGHT_UNSELECTED = 21f
    const val MAIN_TAB_LABEL_TO_UNDERLINE_GAP = 4f
    const val MAIN_TAB_UNDERLINE_WIDTH = 28f
    const val MAIN_TAB_UNDERLINE_HEIGHT = 3f

    const val HOME_WAY_TAB_HEIGHT = 40f
    const val HOME_WAY_TAB_OUTER_RADIUS = 8f
    const val HOME_WAY_TAB_INNER_HEIGHT = 32f
    const val HOME_WAY_TAB_INNER_RADIUS = 6f
    const val HOME_WAY_TAB_INSET = 4f
    const val WAY_TAB_FONT_SIZE = 14f

    const val CITY_ROUTE_NAME_SIZE = 22f
    const val SEARCH_BTN_HEIGHT = 48f
    const val SEARCH_BTN_RADIUS = 24f
    const val SEARCH_BTN_FONT_SIZE = 18f

    const val COLOR_HOME_TEXT_PRIMARY = 0xFF0F0F0F.toInt()
    const val COLOR_HOME_TEXT_SECONDARY = 0xFF888888.toInt()
    const val COLOR_HOME_ACCENT = 0xFF00CAD9.toInt()
    const val COLOR_HOME_WAY_TAB_BG = 0xFFF0F2F5.toInt()
    const val COLOR_HOME_DIVIDER = 0xFFE8E8E8.toInt()
    const val COLOR_CITY_DEFAULT = 0xFF0F0F0F.toInt()
    const val COLOR_DATE_TEXT = 0xFF0F0F0F.toInt()
    const val COLOR_CABIN_TRACK = 0xFFF0F2F5.toInt()
    const val COLOR_CABIN_SEGMENT_MUTED = 0xFF666666.toInt()
    const val COLOR_SEARCH_BTN_START = 0xFFFF8C42.toInt()
    const val COLOR_SEARCH_BTN_END = 0xFFFF4B5C.toInt()
    const val COLOR_SEARCH_BTN_TEXT = 0xFFFFFFFF.toInt()

    const val SHELL_RADIUS = 16f
    const val SHELL_PADDING_HORIZONTAL = 20f
    const val SHELL_PADDING_TOP = 16f
    const val WAY_TAB_MARGIN_BOTTOM = 12f
}

internal class RouterNavigationBar : ComposeView<RouterNavigationBarAttr, ComposeEvent>() {
    override fun createEvent(): ComposeEvent {
        return ComposeEvent()
    }

    override fun createAttr(): RouterNavigationBarAttr {
        return RouterNavigationBarAttr()
    }

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            View {
                attr {
                    paddingTop(ctx.pagerData.statusBarHeight)
                    backgroundColor(Color.WHITE)
                }
                // nav bar
                View {
                    attr {
                        height(44f)
                        allCenter()
                    }

                    Text {
                        attr {
                            text(ctx.attr.title)
                            fontSize(17f)
                            fontWeightSemisolid()
                            backgroundLinearGradient(
                                Direction.TO_BOTTOM,
                                ColorStop(Color(0xFF23D3FD), 0f),
                                ColorStop(Color(0xFFAD37FE), 1f)
                            )

                        }
                    }

                }

                vif({ !ctx.attr.backDisable }) {
                    Image {
                        attr {
                            absolutePosition(
                                top = 12f + getPager().pageData.statusBarHeight,
                                left = 12f,
                                bottom = 12f,
                                right = 12f
                            )
                            size(10f, 17f)
                            src("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAASBAMAAAB/WzlGAAAAElBMVEUAAAAAAAAAAAAAAAAAAAAAAADgKxmiAAAABXRSTlMAIN/PELVZAGcAAAAkSURBVAjXYwABQTDJqCQAooSCHUAcVROCHBiFECTMhVoEtRYA6UMHzQlOjQIAAAAASUVORK5CYII=")
                        }
                        event {
                            click {
                                getPager().acquireModule<RouterModule>(RouterModule.MODULE_NAME)
                                    .closePage()
                            }
                        }
                    }
                }

            }
        }
    }
}

internal class RouterNavigationBarAttr : ComposeAttr() {
    var title: String by observable("")
    var backDisable = false
}

internal fun ViewContainer<*, *>.RouterNavBar(init: RouterNavigationBar.() -> Unit) {
    addChild(RouterNavigationBar(), init)
}
