package com.example.administrator.chirp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String pearl = "";
    int mPosition = GridView.INVALID_POSITION;
    boolean mSelecting = false;
    List<String> listPool;
    List<Map<String, Object>> listCate = new ArrayList<>();
    final List<String> listBingo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cate部分
        final String[] pool = {"covert", "politic", "ancestor", "tropical", "topical", "accrue", "excise", "merge", "merger", "battle",
                "battery", "peek", "peak", "refuge", "refugee", "barge", "bargain", "vocation", "vacation", "drone", "pater", "hatch", "hitch",
                "fervor", "fever", "brokerage", "coax", "allusion", "cuisine", "vinegar"};
        listPool = Arrays.asList(pool);
        final Integer[] colors={Color.parseColor("#e91e63"),Color.parseColor("#9c27b0"),Color.parseColor("#5677fc"),
                Color.parseColor("#673ab7"),Color.parseColor("#3f51b5"),Color.parseColor("#e51c23"),
                Color.parseColor("#03a9f7"),Color.parseColor("#00bcd4"),Color.parseColor("#259b24"),
                Color.parseColor("#8bc34a"),Color.parseColor("#cddc39"),Color.parseColor("#ff9800"),
                Color.parseColor("#ffeb3b"),Color.parseColor("#ff5722"),Color.parseColor("#ffc107")};
//        final String[] pool = {"ok", "the", "jolt"};
//        final String[] listletter = {"m", "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t",
//                "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t",
//                "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t",
//                "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t", "o", "k", "t", "h", "e", "j", "o", "l", "t"};

//        final ArrayAdapter itemAddAdapter=new ArrayAdapter(MainActivity.this,R.layout.itemletter,R.id.letter,listCate);
        init();
        //        Collections.shuffle(listCate);
        final SimpleAdapter itemAddAdapter = new SimpleAdapter(this, listCate, R.layout.itemletter,
                new String[]{"letter"}, new int[]{R.id.letter});

        final GridView cateView = findViewById(R.id.listall);
        cateView.setAdapter(itemAddAdapter);

        final int SELECTED_CELL_COLOR = Color.MAGENTA;
//        final Drawable drawable = cateView.getBackground();

        cateView.setOnTouchListener(new View.OnTouchListener()

        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        GridView grid = (GridView) view;
                        int position = grid.pointToPosition(x, y);
                        if (position != GridView.INVALID_POSITION) {
                            view.getParent().requestDisallowInterceptTouchEvent(true); //Prevent parent from stealing the event
                            View cellView = grid.getChildAt(position);
                            Button button = cellView.findViewById((R.id.letter));
//                            View cellView = (View)grid.getItemAtPosition(position);
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    mSelecting = true;
                                    mPosition = position;
                                    cellView.setBackgroundColor(SELECTED_CELL_COLOR);
                                    pearl = pearl + button.getText();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (mPosition != position) {
                                        mPosition = position;
                                        cellView.setBackgroundColor(SELECTED_CELL_COLOR);
                                        pearl = pearl + button.getText();
                                    } else {
                                        //Repeated cell, noop
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    //判断
                                    Uri uri1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone rt1 = RingtoneManager.getRingtone(getApplicationContext(), uri1);
//                                    Uri uri2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                                    Ringtone rt2 = RingtoneManager.getRingtone(getApplicationContext(), uri2);
                                    if (listPool.contains(pearl)) {
                                        rt1.play();
                                        Random random = new Random();
                                        int color=colors[random.nextInt(colors.length)];
                                        for (int i = 0; i < listCate.size(); i++) {
                                            Drawable drawable=grid.getChildAt(i).getBackground();

                                            if (drawable instanceof ColorDrawable) {
                                                ColorDrawable colorDrawable = (ColorDrawable) drawable;
                                                if (colorDrawable.getColor() == SELECTED_CELL_COLOR)
                                                    grid.getChildAt(i).setBackgroundColor(color);
                                            }
                                        }
                                        listBingo.remove(pearl);
                                        Toast.makeText(MainActivity.this, pearl + listBingo.size(), Toast.LENGTH_SHORT).show();
                                        if (listBingo.size() == 0) {
                                            init();
                                            itemAddAdapter.notifyDataSetChanged();
                                            for (int i = 0; i < listCate.size(); i++) {
                                                grid.getChildAt(i).setBackgroundColor(Color.WHITE);

                                            }
                                        }
                                    }
//                                    else
//                                        rt2.play();
                                    for (int i = 0; i < listCate.size(); i++) {
                                        Drawable drawable=grid.getChildAt(i).getBackground();
                                        if (drawable instanceof ColorDrawable) {
                                            ColorDrawable colorDrawable = (ColorDrawable) drawable;
                                            if (colorDrawable.getColor() == SELECTED_CELL_COLOR)
                                                grid.getChildAt(i).setBackgroundColor(Color.WHITE);
                                        }
                                    }

                                    //重置
                                    mSelecting = false;
                                    mPosition = GridView.INVALID_POSITION;
                                    pearl = "";
//                                    grid.setBackgroundDrawable(drawable);
//                                    for(int i=0;i<grid.getChildCount();i++)
//                                    grid.getChildAt(i).setBackgroundColor(Color.WHITE);
                                    //Here you could call a listener, show a dialog or similar
//                                    Toast.makeText(MainActivity.this, pearl, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            ;
                        } else {
                            if (mSelecting) {
                                mSelecting = false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mSelecting = false;
                        break;
                }
                return true;

//                Toast.makeText(MainActivity.this, "touch", Toast.LENGTH_SHORT).show();
//                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
//                    pearl="";
//                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
////                    pearl="";
//                Toast.makeText(MainActivity.this, pearl, Toast.LENGTH_SHORT).show();
//                return false;
            }
        });
    }

    public void init() {
        Collections.shuffle(listPool);
listCate.clear();
        //自动钻取法
        int lay = 80;
        Random random = new Random();
        for (int i = 0; i < lay; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("letter", "0");
            listCate.add(map);
        }

        for (int i = 0; i < 15; i++) {
            String s = listPool.get(i);
            char[] c = s.toCharArray();
            int tried = 0;
            int fail = 0;
            do {
                fail = 0;
                int start = random.nextInt(lay);
                int pos = start;
                int direction = 1;
                int step = 0;
                List<Integer> listTrail = new ArrayList<Integer>();
                if (listCate.get(pos).get("letter") == "0" || listCate.get(pos).get("letter").equals(c[0])) {
                    if (listCate.get(pos).get("letter") == "0") listTrail.add(pos);
                    listCate.get(pos).put("letter", c[0]);
                    for (int i1 = 1; i1 < c.length; i1++) {
                        //为美观，同一个方向优先，冗余一步
                        pos = start + direction;
                        if ((step < 5) && (pos < lay) && (pos >= 0) && (pos % 8 != 0) && (pos % 8 != 7) && ((listCate.get(pos).get("letter") == "0" ||
                                listCate.get(pos).get("letter").equals(c[i1])))) {
                            if (listCate.get(pos).get("letter") == "0") listTrail.add(pos);
                            listCate.get(pos).put("letter", c[i1]);
                            start = pos;
                            step++;
                        } else {
                            pos = start + 8;
                            if ((step < 5) &&pos < lay && ((listCate.get(pos).get("letter") == "0" || listCate.get(pos).get("letter").equals(c[i1])))) {
                                if (listCate.get(pos).get("letter") == "0") listTrail.add(pos);
                                listCate.get(pos).put("letter", c[i1]);
                                start = pos;
                                if (direction == 8)
                                    step++;
                                else {
                                    direction = 8;
                                    step = 0;
                                }
                            } else {
                                pos = start - 8;
                                if ((step < 5) &&pos >= 0 && ((listCate.get(pos).get("letter") == "0" || listCate.get(pos).get("letter").equals(c[i1])))) {
                                    if (listCate.get(pos).get("letter") == "0")
                                        listTrail.add(pos);
                                    listCate.get(pos).put("letter", c[i1]);
                                    start = pos;
                                    if (direction == -8)
                                        step++;
                                    else {
                                        direction = -8;
                                        step = 0;
                                    }
                                } else {
                                    pos = start + 1;
                                    if ((step < 5) &&(pos < lay) && (pos % 8 != 0) && ((listCate.get(pos).get("letter") == "0" || listCate.get(pos).get("letter").equals(c[i1])))) {
                                        if (listCate.get(pos).get("letter") == "0")
                                            listTrail.add(pos);
                                        listCate.get(pos).put("letter", c[i1]);
                                        start = pos;
                                        if (direction == 1)
                                            step++;
                                        else {
                                            direction = 1;
                                            step = 0;
                                        }
                                    } else {
                                        pos = start - 1;
                                        if ((step < 5) &&(pos >= 0) && (pos % 8 != 7) && ((listCate.get(pos).get("letter") == "0" || listCate.get(pos).get("letter").equals(c[i1])))) {
                                            if (listCate.get(pos).get("letter") == "0")
                                                listTrail.add(pos);
                                            listCate.get(pos).put("letter", c[i1]);
                                            start = pos;
                                            if (direction == -1)
                                                step++;
                                            else {
                                                direction = -1;
                                                step = 0;
                                            }
                                        } else {
                                            fail++;
                                            tried++;
                                            //尝试失败,复位
//                                            Toast.makeText(MainActivity.this, "all" + listTrail.size(), Toast.LENGTH_SHORT).show();
                                            for (int i2 = 0; i2 < listTrail.size(); i2++) {
                                                listCate.get(listTrail.get(i2)).put("letter", "0");
//                                                listCate.get(listTrail.get(i2)).put("letter", Integer.toString(listTrail.size()));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    fail++;
                    tried++;
                }
            } while (fail > 0 && tried < 10);
            if (tried >= 10)
                break;
            else listBingo.add(s);
        }
        String sAll = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] charArray = sAll.toCharArray();
        for (int i = 0; i < listCate.size(); i++) {
            if (listCate.get(i).get("letter") == "0") {
                listCate.get(i).put("letter", charArray[random.nextInt(charArray.length)]);//随机字母填充无解部分,random直接没有
            }
        }
        //自动钻取法
//平铺法
//        for (int i = 0; i < 20; i++) {
//            String s = listPool.get(i);
//            char[] c = s.toCharArray();
//
//            for (int j = 0; j < c.length; j++) {
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("letter", c[j]);
////            map.put("description", descriptions[i]);
////            map.put("icon", icons[i]);
//                listCate.add(map);
//            }
//        }
// 平铺法
    }
// cateView.setOnScrollListener(new AbsListView.OnScrollListener() {
//     @Override
//     public void onScrollStateChanged(AbsListView absListView, int i) {
//
//     }
//
//     @Override
//     public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//
//     }
// });
//        cateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "in", Toast.LENGTH_SHORT).show();
//                Button button = view.findViewById(R.id.letter);
//                pearl = pearl + button.getText();
//                if(button.isSelected()==false) button.setSelected(true);
////                button.setBackgroundColor(Color.CYAN);
////                Toast.makeText(MainActivity.this, pearl, Toast.LENGTH_SHORT).show();
//            }
//        });
//        cateView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(i<adapterView.getChildCount()-1) {
//                    TextView textView = view.findViewById(R.id.itemid);
//                    cateid = Integer.parseInt(textView.getText().toString());
//                    db.execSQL("delete  from cateid where _id="+cateid);
//                    listCate.remove(adapterView.getItemAtPosition(i));
//                    itemAddAdapter.notifyDataSetChanged();
//                }
//                return true;
//            }
//        });
//    }
//    @Override
//    public void onBackPressed() {
//// 这里处理逻辑代码，大家注意：该方法仅适用于2.0或更新版的sdk
//        Intent intent=new Intent(NewItemActivity.this,NewOneActivity.class);
//        startActivity(intent);
//        finish();
//        return;
}

