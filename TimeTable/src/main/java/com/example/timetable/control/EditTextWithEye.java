package com.example.timetable.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.timetable.R;

public class EditTextWithEye extends AppCompatEditText implements View.OnFocusChangeListener{
    private Drawable drawable;
    private Drawable drawable_show;
    private Drawable drawable_hide;
    private Context mContext;
    private AppCompatEditText tmpEditText;

    public EditTextWithEye(Context context){
        super(context);
        init();
    }
    public EditTextWithEye(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context, attrs);
    }
    public EditTextWithEye(Context context,AttributeSet attrs,int defStyle){
        super(context, attrs,defStyle);
        init(context, attrs);
    }
    private void init(){
        if(drawable_hide==null){
            drawable_hide=ContextCompat.getDrawable(mContext, R.drawable.hide_et);
        }
        if(drawable_show==null){
            drawable_show=ContextCompat.getDrawable(mContext, R.drawable.show_et);
        }
        setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable_InputType();
            }
        });
    }
    private void init(Context context, AttributeSet attrs){
        mContext=context;
        TypedArray attributes=context.obtainStyledAttributes(attrs,R.styleable.EditTextWithEye);
        drawable_show=attributes.getDrawable(R.styleable.EditTextWithEye_eyeSrc_show);
        drawable_hide=attributes.getDrawable(R.styleable.EditTextWithEye_eyeSrc_hide);
        attributes.recycle();
        init();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        setDrawable_InputType();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        //setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        setDrawable_InputType();
    }
    private void setDrawable_InputType(){
        if(getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD){
            drawable_show.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
            //drawable=drawable_show;
            setCompoundDrawables(null,null,drawable_show,null);
        }else if(getInputType()==InputType.TYPE_CLASS_TEXT) {
            drawable_hide.setBounds(0, 0, Utils.Dp2Px(mContext, 20), Utils.Dp2Px(mContext, 20));
            //drawable=drawable_hide;
            setCompoundDrawables(null, null, drawable_hide, null);
        }/*else{
            drawable_show.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
            //drawable=drawable_show;
            setCompoundDrawables(null,null,drawable_show,null);
        }*/
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(drawable_hide!=null&&drawable_show!=null&&event.getAction()==MotionEvent.ACTION_UP){
            int x=(int)event.getX();
            boolean isInnerWidth=(x>(getWidth()-getTotalPaddingRight()))&&(x<(getWidth()-getPaddingRight()));
            Rect rect=null;
            if(getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD){
                rect=drawable_show.getBounds();
            }else{
                rect=drawable_hide.getBounds();
            }
            int height=rect.height();
            int y=(int)event.getY();
            int distance=(getHeight()-height);
            boolean isInnerHeight=(y>distance)&&(y<(distance+height));
            int n=getInputType();
            if(isInnerWidth&&isInnerHeight){
                if(getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD){
                    drawable_show.setBounds(0,0,Utils.Dp2Px(mContext,20),Utils.Dp2Px(mContext,20));
                    //drawable=drawable_show;
                    setCompoundDrawables(null,null,drawable_show,null);
                    setInputType(InputType.TYPE_CLASS_TEXT);
                }else if(getInputType()==InputType.TYPE_CLASS_TEXT) {
                    drawable_hide.setBounds(0, 0, Utils.Dp2Px(mContext, 20), Utils.Dp2Px(mContext, 20));
                    //drawable=drawable_hide;
                    setCompoundDrawables(null, null, drawable_hide, null);
                    setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                //setCompoundDrawables(null,null,drawable,null);
                Log.i("EditTextWithEye","1");
            }
            //Log.i("EditTextWithEye","2");
        }
        return super.onTouchEvent(event);
    }
    public void setTmpEditText(AppCompatEditText tmpEditText){
        this.tmpEditText=tmpEditText;
    }
}
