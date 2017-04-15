package edu.temple.webbrowser;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    ArrayList<WebFragment> fragments_list = new ArrayList<>(); // to keep track of frag
    ArrayList<String> url_list = new ArrayList<>(); // to keep track of url
    Logger log = Logger.getAnonymousLogger();
    public EditText editText;
    String url;
    ViewPager viewPager; //for swiping left and right
    PagerAdapter pagerAdapter;
    WebFragment receiver;
    Button go_button;
    int currentIndex = 0;
    int size = 0;
    int fragCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign view pager in layout, for swiping left and right
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        // 3 off screen page seems reasonable
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

        editText = (EditText) findViewById(R.id.url_text);
        go_button = (Button) findViewById(R.id.go_button);

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this function will be called when "Go" button is clicked
                log.info("Go button clicked");
                //Save the state
                currentIndex = viewPager.getCurrentItem();
                receiver = fragments_list.get(currentIndex);
                url = editText.getText().toString();
                //if we are on fragment number 1, add url to the first array list
                if (url_list.size() == 0) {
                    url_list.add(url);
                }
                // overwrite existing url
                if (url_list.size() > viewPager.getCurrentItem()) {
                    url_list.set(viewPager.getCurrentItem(), url);
                    log.info("existing overwritten");
                }
                //Call fragment to load url
                receiver.openURL(url);
                log.info(url_list.toString());
            }
        });
        //to respond to implicit intent from other applications
        Uri data = getIntent().getData();
        if(data != null) {
            String url = data.toString();
            receiver.openURL(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.prev_button:
                //if prev arrow button is clicked
                log.info("Previous button clicked");
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                editText.setText(url_list.get(viewPager.getCurrentItem()));
                currentIndex = viewPager.getCurrentItem();
                receiver = fragments_list.get(currentIndex);
                receiver.openURL(url_list.get(viewPager.getCurrentItem())); //open url when we navigate back and forth
                return true;

            case R.id.next_button:
                //if next arrow button clicked
                log.info("Next button clicked");
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                editText.setText(url_list.get(viewPager.getCurrentItem()));
                currentIndex = viewPager.getCurrentItem();
                receiver = fragments_list.get(currentIndex);
                receiver.openURL(url_list.get(viewPager.getCurrentItem())); //open url when we navigate back and forth
                return true;

            case R.id.new_button:
                //if NEW/PLUS button is clicked
                log.info("New button clicked");
                //create new webfrag and add it to fragment arraylist
                WebFragment webFragment = new WebFragment();
                fragments_list.add(size, webFragment);
                fragCount++;
                //link to adapter
                pagerAdapter = viewPager.getAdapter();
                pagerAdapter.notifyDataSetChanged();
                receiver = webFragment;
                viewPager.setCurrentItem(size);
                editText.setText("");// set tthe text field to empty everytime we start a new web fragment
                // add empty string to url array list when new button is clicked to reserve a space
                url_list.add(viewPager.getCurrentItem(), "");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //customPagerAdapter class
    private class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {

                default:
                    WebFragment fragment = new WebFragment();
                    fragments_list.add(size, fragment);
                    size++;
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return fragCount;
        }
    }
}
