package com.hemaapp.dtyw.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.hemaapp.dtyw.model.Select;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/27.
 */
public class SelectDBHelper extends DtywDbHelper {
    String tableName = SELECTUSER;
    String columns = "username,nickname,password,avatar";
    String updateColumns = "username=?,nickname=?,password=?,avatar=?";
    private static SelectDBHelper mClient;
    public SelectDBHelper(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public static SelectDBHelper get(Context context) {
        return mClient == null ? mClient = new SelectDBHelper(context)
                : mClient;
    }
    /**
     * @param //i//nfo
     * @return
     * @方法名称: insert
     * @功能描述: 插入一条数据
     * @返回值: boolean
     */
    public boolean insert(Select select) {
        String sql = "insert into " + tableName + " (" + columns
                + ") values (?,?,?,?)";
        Object[] bindArgs = new Object[]{
                select.getUsername(), select.getNickname(), select.getPassword(), select.getAvatar()
        };
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (Exception e) {
            // TODO: handle exception
            success = false;
        }
        db.close();
        return success;
    }

    /**
     * @param //info
     * @return
     * @方法名称: updata
     * @功能描述: 更新一条数据
     * @返回值: boolean
     */
    public boolean update(Select select) {
        int id = 1;
        String conditions = " where id=" + id;
        String sql = "update " + tableName + " set " + updateColumns
                + conditions;
        Object[] bindArgs = new Object[]{select.getUsername(), select.getNickname(), select.getPassword(), select.getAvatar()};
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (Exception e) {
            // TODO: handle exception
            success = false;
        }
        db.close();
        return success;
    }

    /**
     * 清空
     */
    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("delete from " + tableName);
        } catch (SQLiteException e) {
        } catch (SQLException e) {
        }
        db.close();
    }

    /**
     * 判断表是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        int num = cursor.getCount();
        cursor.close();
        db.close();
        return 0 == num;
    }

    /**
     * 删除
     */
    public boolean delete(Select select) {
        boolean success;
        String username = select.getUsername();
        String conditions = " where username='" + username + "'";
        String sql = "delete from " + tableName + conditions;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean isExist(Select select) {
        String username = select.getUsername();
        String sql = ("select * from " + tableName + " where username='" + username + "'");
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exist;
    }

    public boolean insertOrUpdate(Select select) {
        if (isExist(select)) {
            //delete(select);
            return update(select);
        } else {
            return insert(select);
        }
    }

    /**
     * 获取
     *
     * @return
     */
    public ArrayList<Select> select() {
        ArrayList<Select> selects = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + tableName, null);
            if (cursor != null && cursor.getCount() > 0) {
                selects = new ArrayList<Select>();
                //cursor.moveToFirst();
              while (cursor.moveToNext()){
                  Select count = new Select(cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("nickname")), cursor.getString(cursor.getColumnIndex("password")), cursor.getString(cursor.getColumnIndex("avatar")));
                  selects.add(count);
              }
            }
        } catch (Exception e) {
            Log.w("select", "select e=" + e);
        }
        if (cursor != null)
            cursor.close();
        db.close();
        return selects;
    }
}
