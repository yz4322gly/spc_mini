package com.ruoyi.common.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guolinyuan
 */
public class PrintUtil
{
    /**
     * 创建一个空白图片
     *
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createBlankImg(int width, int height)
    {
        //创建一个图片缓冲区
        BufferedImage blank = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        //填充背景色
        Graphics graphics = blank.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, width, height);

        return blank;
    }

    /**
     * 创建一个code128的条形码
     *
     * @param width
     * @param height
     * @param content
     * @return
     */
    public static BufferedImage createCode128(int width, int height, String content)
    {
        Map<EncodeHintType, Object> hints = new HashMap<>(1);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        // 生成矩阵
        BitMatrix bitMatrix = null;
        try
        {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height, hints);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
        // 输出图像
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 创建为二维码
     *
     * @param width
     * @param height
     * @param content
     * @return
     */
    public static BufferedImage createQrCode(int width, int height, String content)
    {
        Map<EncodeHintType, Object> hints = new HashMap<>(1);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, "0");
        // 生成矩阵
        BitMatrix bitMatrix = null;
        try
        {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
        // 输出图像
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 画一个上面是条形码,下面是描述文字的图片
     * *
     *
     * @param height        图片宽度
     * @param width         图片高度
     * @param fontSize      文字大小
     * @param border        整体边框
     * @param textTopMargin 文字和条形码间距
     * @param content       条形码内容
     * @param descContent   描述文字内容
     * @return
     */
    public static BufferedImage drawTextCode(int height, int width, int fontSize, int border, int textTopMargin, String content, String descContent)
    {
        return drawTextCode(height, width, fontSize, border, textTopMargin, content, descContent, 1);
    }


    /**
     * 绘制固定宽度大小的图片, 宽度和二维码宽度一致,高度随着文字数量增加,
     * 字号,边距均为固定值,
     *
     * @param width       图片宽度
     * @param content     二维码内容
     * @param descContent 下面描述文字
     * @return
     */
    public static BufferedImage drawFixCodeTextCode(int width, String content, String descContent)
    {
        final int fontSize = 25;
        final int border = 15;
        final int textHeight = 30;
        final int textWidth = 30;
        final int textSpacing = 5;

        final int titleFontSize = 30;
        final int titleTextHeight = 10;//35;

        //final String title = "高邮人口系统";


        //每个字宽30,计算整个行数
        int lines = (descContent.length()*30) / width;
        //余数
        int lx = (descContent.length()*30) % width;
        //有余数,行数+1
        if (lx  > 0)
        {
            lines ++;
        }

        //画布大小
        BufferedImage blank = createBlankImg(width + border * 2, width + titleTextHeight + lines * (textWidth + textSpacing) + border);
        Graphics2D graphics = blank.createGraphics();
        graphics.setColor(Color.BLACK);

        //修改画笔字体对齐
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //定义标题字体样式
        Font titleFont = new Font("宋体", Font.BOLD, titleFontSize);
        FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);

        //定义描述文字字体样式
        Font descFont = new Font("宋体", Font.BOLD, fontSize);
        FontMetrics descMetrics = graphics.getFontMetrics(descFont);

        //画标题
//        graphics.setFont(titleFont);
//        graphics.drawString(title, border + ((width - titleMetrics.stringWidth(title)) / 2), titleTextHeight);

        //画图片
        BufferedImage barCode = createQrCode(width, width , content);
        graphics.drawImage(barCode, border, border + titleTextHeight, null);

        //画描述文字
        //每一行的字数是多少
        graphics.setFont(descFont);
        char[] chars = descContent.toCharArray();
        int everyCharNum = chars.length/lines;
        int everyCharLx = chars.length%lines;
        for (int i = 0 ; i <lines ; i++)
        {
            int count = i+everyCharNum;
            if (i == lines - 1)
            {
                count = everyCharNum  + everyCharLx;
            }
            String thisLineString = new String(chars,i * everyCharNum,count);

            // 字符分宽
            graphics.drawString(thisLineString,
                    border + ((width - descMetrics.stringWidth(thisLineString)) / 2),
                    titleTextHeight + width  + (i + 1) * (textHeight + textSpacing));
        }
        return blank;
    }

    /**
     * 画一个上面是码,下面是描述文字的图片
     *
     * @param height        图片宽度
     * @param width         图片高度
     * @param fontSize      文字大小
     * @param border        整体边框
     * @param textTopMargin 文字和条形码间距
     * @param content       码内容
     * @param descContent   描述文字内容
     * @param codeType      码类型 2.qrcode 1.barcode
     * @return
     */
    public static BufferedImage drawTextCode(int height, int width, int fontSize, int border, int textTopMargin, String content, String descContent, int codeType)
    {
        BufferedImage blank = createBlankImg(width, height);
        BufferedImage barCode;
        //高度等于画布高度减去留边2个border,再减去下方文字高度,再减去下方文字高度上边距5px
        if (codeType == 2)
        {
            barCode = createQrCode(width - border * 2, height - border * 2 - fontSize - textTopMargin, content);
        }
        else
        {
            barCode = createCode128(width - border * 2, height - border * 2 - fontSize - textTopMargin, content);
        }
        Graphics2D graphics = blank.createGraphics();

        //画图片
        graphics.drawImage(barCode, border, border, null);

        //画文字
        graphics.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, fontSize);
        graphics.setFont(font);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics metrics = graphics.getFontMetrics(font);


        graphics.drawString(descContent,
                (width - metrics.stringWidth(descContent)) / 2,
                border + barCode.getHeight() + textTopMargin + metrics.getHeight());

        return blank;
    }


    public static void main(String[] args) throws IOException
    {

        for (int i = 1 ; i < 35 ; i++)
        {
            String name ;
            if (i < 10)
            {
                 name = "YCB000" + i;
            }
            else
            {
                 name = "YCB00" + i;
            }
            BufferedImage blank = createBlankImg(400, 200);
            BufferedImage barCode = createQrCode(140, 140, name);
            Graphics2D graphics = blank.createGraphics();

            //画图片
            graphics.drawImage(barCode, 240, 30, null);

            //画文字
            graphics.setColor(Color.BLACK);
            Font font = new Font("宋体" , Font.PLAIN, 20);
            graphics.setFont(font);
            //抗锯齿,看打印情况需不需要这两行
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


            graphics.drawString("设备种类:计算机" , 20, 60);
            graphics.drawString("资产名称:台式机电脑" , 20, 100);
            graphics.drawString("资产编码:" + name , 20, 140);

            ImageIO.write(blank, "png" , new File("C:\\Users\\linyuan.guo\\Desktop\\code\\"+name+".png"));
        }
    }
}
