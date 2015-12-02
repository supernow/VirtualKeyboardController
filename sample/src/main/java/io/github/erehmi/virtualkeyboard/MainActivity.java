package io.github.erehmi.virtualkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.erehmi.library.util.IMMUtils;
import io.github.erehmi.library.util.SoftKeyboardCompat;
import io.github.erehmi.library.vkb.LinearLayoutManager;
import io.github.erehmi.library.vkb.VirtualKeyboardController;
import io.github.erehmi.library.vkb.VirtualKeyboardController.LayoutManager;

public class MainActivity extends AppCompatActivity
        implements View.OnTouchListener, View.OnClickListener {
    private ListView mListView;
    private EditText mEditText;
    private ImageView mEmojiImage;

    private View mEmojiLayout;

    private VirtualKeyboardController mVirtualKeyboardController;
    private LayoutManager mLayoutManager;
    private SoftKeyboardCompat mSoftKeyboardCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOnTouchListener(this);
        initListAdapter();

        mEditText = (EditText) findViewById(android.R.id.input);
        mEditText.setOnClickListener(this);

        mEmojiImage = (ImageView) findViewById(R.id.emoji);
        mEmojiImage.setOnClickListener(this);

        mEmojiLayout = findViewById(R.id.emoji_layout);

        mVirtualKeyboardController = new VirtualKeyboardController(this);
        mSoftKeyboardCompat = new SoftKeyboardCompat(this);
        mLayoutManager = new LinearLayoutManager(this, mSoftKeyboardCompat);
        mLayoutManager.setContentView(mListView);
        mLayoutManager.setSoftInputFocusView(mEditText);
        mLayoutManager.setVirtualKeyboardView(mEmojiLayout);
        mVirtualKeyboardController.setLayoutManager(mLayoutManager);
    }

    private void initListAdapter() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("text1", "item " + i);
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_1,
                new String[]{"text1"}, new int[]{android.R.id.text1});

        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!hideVirtualKeyboardIfNeeded(false)) {
            IMMUtils.hideSoftInput(this, mEditText);
        }
        return false;
    }

    private boolean hideVirtualKeyboardIfNeeded(boolean shouldShowSoftInput) {
        if (mVirtualKeyboardController.isVirtualKeyboardShown()) {
            mVirtualKeyboardController.hideVirtualKeyboard(shouldShowSoftInput);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case android.R.id.input:
            hideVirtualKeyboardIfNeeded(true);
            break;

        case R.id.emoji:
            mVirtualKeyboardController.toggleVirtualKeyboard();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!hideVirtualKeyboardIfNeeded(false)) {
            super.onBackPressed();
        }
    }
}
