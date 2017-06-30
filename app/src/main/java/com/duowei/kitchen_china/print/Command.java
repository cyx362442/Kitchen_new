package com.duowei.kitchen_china.print;

/**
 * 打印命令
 *
 * Created by Administrator on 2017-05-31.
 */

public final class Command {

    /** 初始化 */
    public static byte[] INIT = new byte[] {0x1b, '@'};

    /** 左对齐 */
    public static byte[] ALIGN_LEFT = new byte[] {0x1b, 0x61, 0x0};

    /** 居中对齐 */
    public static byte[] ALIGN_CENTER = new byte[] {0x1b, 0x61, 0x1};

    /* 右对齐 */
    public static byte[] ALIGN_RIGHT = new byte[] {0x1b, 0x61, 0x2};

    /** 加粗 */
    public static byte[] BOLD = new byte[] {0x1b, 0x45, 0x1};

    /** 取消加粗 */
    public static byte[] BOLD_NO = new byte[] {0x1b, 0x45, 0x0};

    /** 倍数 00-88 */
    public static byte[] WEIGHT = new byte[] {0x1d, 0x21, 0x00};

    /** 进纸 */
    public static byte[] ENTER = new byte[] {0x1b, 0x4a, 0x10};

    /** 切刀 */
    public static byte[] KNIFE = new byte[] {0x1d, 'V', 'B', 0x08 };

    /** 行间距 */
    public static byte[] LINE_HEIGHT = new byte[] {0x1b, 51, 0x00 };
}
