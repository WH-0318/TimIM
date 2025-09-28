package com.tencent.qcloud.tuikit.timcommon.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;

public class RoundedCenteredSizeSpan extends ReplacementSpan {
    private final float proportion;
    private final int backgroundColor;
    private final float radius;
    private final float paddingH;
    private final float paddingV;

    public RoundedCenteredSizeSpan(float proportion, int backgroundColor,
                                   float radius, float paddingH, float paddingV) {
        this.proportion = proportion;
        this.backgroundColor = backgroundColor;
        this.radius = radius;
        this.paddingH = paddingH;
        this.paddingV = paddingV;
    }

    private static class MeasureResult {
        float width;
        float maxAscent;
        float maxDescent;
    }

    private MeasureResult measure(CharSequence text, int start, int end, Paint basePaint) {
        MeasureResult r = new MeasureResult();
        r.width = 0;
        r.maxAscent = 0;
        r.maxDescent = 0;

        TextPaint tp = new TextPaint(basePaint);
        if (!(text instanceof Spanned)) {
            tp.setTextSize(tp.getTextSize() * proportion);
            Paint.FontMetricsInt fm = tp.getFontMetricsInt();
            float ascent = -fm.ascent;
            float descent = fm.descent;
            r.width = tp.measureText(text, start, end);
            r.maxAscent = ascent;
            r.maxDescent = descent;
            return r;
        }

        Spanned sp = (Spanned) text;
        int cur = start;
        while (cur < end) {
            int next1 = sp.nextSpanTransition(cur, end, MetricAffectingSpan.class);
            int next2 = sp.nextSpanTransition(cur, end, CharacterStyle.class);
            int next = Math.min(next1, next2);

            if (next <= cur) next = end;

            TextPaint tmp = new TextPaint(basePaint);

            MetricAffectingSpan[] mas = sp.getSpans(cur, next, MetricAffectingSpan.class);
            for (MetricAffectingSpan m : mas) m.updateMeasureState(tmp);

            CharacterStyle[] cs = sp.getSpans(cur, next, CharacterStyle.class);
            for (CharacterStyle c : cs) c.updateDrawState(tmp);

            tmp.setTextSize(tmp.getTextSize() * proportion);

            Paint.FontMetricsInt fm = tmp.getFontMetricsInt();
            float ascent = -fm.ascent;
            float descent = fm.descent;
            float w = tmp.measureText(text, cur, next);

            r.width += w;
            if (ascent > r.maxAscent) r.maxAscent = ascent;
            if (descent > r.maxDescent) r.maxDescent = descent;

            cur = next;
        }

        return r;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        MeasureResult mr = measure(text, start, end, paint);
        int totalWidth = (int) Math.ceil(mr.width + paddingH * 2);

        if (fm != null) {
            int ascent = (int) Math.ceil(-mr.maxAscent);
            int descent = (int) Math.ceil(mr.maxDescent);
            fm.ascent = ascent;
            fm.descent = descent;
            fm.top = fm.ascent;
            fm.bottom = fm.descent;
        }
        return totalWidth;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint originalPaint) {
        MeasureResult mr = measure(text, start, end, originalPaint);
        float totalWidth = mr.width + paddingH * 2;
        float textHeight = mr.maxAscent + mr.maxDescent;

        float availableHeight = bottom - top;
        float topOffset = top + (availableHeight - textHeight) / 2f;
        float baseline = topOffset + mr.maxAscent;

        if (backgroundColor != 0) {
            Paint bgPaint = new Paint(originalPaint);
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(backgroundColor);
            RectF rect = new RectF(x, topOffset - paddingV, x + totalWidth, topOffset + textHeight + paddingV);
            canvas.drawRoundRect(rect, radius, radius, bgPaint);
        }

        float curX = x + paddingH;
        if (!(text instanceof Spanned)) {
            TextPaint tp = new TextPaint(originalPaint);
            tp.setTextSize(tp.getTextSize() * proportion);
            canvas.drawText(text, start, end, curX, baseline, tp);
            return;
        }

        Spanned sp = (Spanned) text;
        int cur = start;
        while (cur < end) {
            int next1 = sp.nextSpanTransition(cur, end, MetricAffectingSpan.class);
            int next2 = sp.nextSpanTransition(cur, end, CharacterStyle.class);
            int next = Math.min(next1, next2);
            if (next <= cur) next = end;

            TextPaint tp = new TextPaint(originalPaint);

            MetricAffectingSpan[] mas = sp.getSpans(cur, next, MetricAffectingSpan.class);
            for (MetricAffectingSpan m : mas) m.updateMeasureState(tp);

            CharacterStyle[] cs = sp.getSpans(cur, next, CharacterStyle.class);
            for (CharacterStyle c : cs) c.updateDrawState(tp);

            tp.setTextSize(tp.getTextSize() * proportion);

            canvas.drawText(text, cur, next, curX, baseline, tp);

            float w = tp.measureText(text, cur, next);
            curX += w;
            cur = next;
        }
    }
}

