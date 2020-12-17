package com.example.timetable.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.timetable.R;

import java.util.jar.Attributes;

public class EditTextWithDelete extends AppCompatEditText implements View.OnFocusChangeListener{
    private Drawable drawable;
    private Context mContext;
    private AppCompatEditText tmpEditText;
    private boolean focused=false;

    public EditTextWithDelete(Context context){
        super(context);
        init();
    }
    public EditTextWithDelete(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }
    public EditTextWithDelete(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs){
        mContext=context;
        TypedArray attributes=context.obtainStyledAttributes(attrs,R.styleable.EditTextWithDelete);
        drawable=attributes.getDrawable(R.styleable.EditTextWithDelete_delSrc);
        attributes.recycle();
        init();
    }
    private void init(){
        if(drawable==null){
            drawable= ContextCompat.getDrawable(mContext,R.drawable.delete_et);
            drawable.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
        }
        setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                drawable.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                drawable.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.focused=hasFocus;
        if(focused&&length()>0){
            setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.focused=focused;
        if(focused&&length()>0){
            setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }else {
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
    }

    private void setDrawable(){
        if(length()<=0||!focused){
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }else{
            drawable.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
            setCompoundDrawables(null,null,drawable,null);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(drawable!=null&&event.getAction()==MotionEvent.ACTION_UP){
            int x=(int)event.getX();
            //判断触摸点是否在水平范围内
            boolean isInnerWidth=(x>(getWidth()-getTotalPaddingRight()))&&(x<(getWidth()-getPaddingRight()));
            //获取删除图标的便捷，返回一个Rect对象
            Rect rect=drawable.getBounds();
            //获取删除图标的高度
            int height=rect.height();
            int y=(int)event.getY();
            //计算图标底部到控件底部的距离
            int distance=(getHeight()-height)/2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到(distance+图标自身的高度)之内，则视为点中删除图标
            boolean isInnerHeight=(y>distance)&&(y<(distance+height));
            if(isInnerWidth&&isInnerHeight){
                setText("");
                if(tmpEditText!=null)
                    tmpEditText.setText("");
                Log.i("EditTextWithDelete","1");
            }
        }
        return super.onTouchEvent(event);
    }
    public void setTmpEditText(AppCompatEditText tmpEditText){
        this.tmpEditText=tmpEditText;
    }
}
