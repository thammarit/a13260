package com.egco428.a13260;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thammarit on 9/11/2559.
 */
public class CommentsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_FIRST};

    public CommentsDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Comment createComment(String comment, String date, String first){
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_DATE, date);
        values.put(MySQLiteHelper.COLUMN_FIRST, first);

        long insertID = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,allColumns, MySQLiteHelper.COLUMN_ID+ " = "+insertID, null, null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment){
        long id = comment.getId();
        System.out.println("Comment deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID+" = "+id, null);

    }

    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns,null,null, null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }

    public Comment cursorToComment(Cursor cursor){
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        comment.setDate(cursor.getString(2));
        comment.setFirst(cursor.getString(3));
        return comment;
    }
}
