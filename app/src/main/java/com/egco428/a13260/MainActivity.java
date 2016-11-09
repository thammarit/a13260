package com.egco428.a13260;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CommentsDataSource datasource;
    ArrayAdapter<Comment> FortuneArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview1);

        datasource = new CommentsDataSource(this);
        datasource.open();
        final List<Comment> values = datasource.getAllComments();
        FortuneArrayAdapter = new FortuneArrayAdapter(this, 0, values);
        listView.setAdapter(FortuneArrayAdapter);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton mAdd = (ImageButton) myToolbar.findViewById(R.id.addButton);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, FortuneActivity.class);
                startActivityForResult(addIntent, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                if (FortuneArrayAdapter.getCount()>0) {
                    final Comment comment = values.get(i);
                    datasource.deleteComment(comment);
                    view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {

                            FortuneArrayAdapter.remove(comment);
                            view.setAlpha(1);
                        }
                    });
                }
            }
        });
        FortuneArrayAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Comment comment = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == FortuneActivity.RESULT_OK){
                String inputData = data.getStringExtra(FortuneActivity.value);
                String inputData1 = data.getStringExtra(FortuneActivity.value1);
                String inputData2 = data.getStringExtra(FortuneActivity.value2);
                datasource.open();
                comment = datasource.createComment(inputData,inputData1,inputData2);//add to database
                FortuneArrayAdapter.add(comment);
            }
        }
    }

    class FortuneArrayAdapter extends ArrayAdapter<Comment>{
        Context context;
        List<Comment> objects;
        public FortuneArrayAdapter(Context context, int resource, List<Comment> objects){
            super(context, resource, objects);
            this.context = context;
            this.objects = objects;
        }
        @Override public View getView(int position, View convertView, ViewGroup parent){
            Comment comment = objects.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cookies_layout, null);

            TextView ffCookiesTxt = (TextView)view.findViewById(R.id.resultFinalText);
            ffCookiesTxt.setText(comment.getComment());
            if((comment.getComment().equals("Work Harder")) || (comment.getComment().equals("Don't Panic")) ){
                ffCookiesTxt.setTextColor(Color.parseColor("#FF4500"));
            }
            else {
                ffCookiesTxt.setTextColor(Color.parseColor("#0000FF"));
            }

            TextView ffTimeTxt = (TextView)view.findViewById(R.id.resultTimeStamp);
            ffTimeTxt.setText("Date: " + comment.getDate());

            ImageView image = (ImageView)view.findViewById(R.id.cookieResultImg);
            int res = context.getResources().getIdentifier("image"+comment.getFirst(), "drawable", context.getPackageName());
            image.setImageResource(res);
            return view;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        ListView listView = (ListView) findViewById(R.id.listview1);

        datasource.open();
        List<Comment> values = datasource.getAllComments();
        listView.setAdapter(FortuneArrayAdapter);
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
