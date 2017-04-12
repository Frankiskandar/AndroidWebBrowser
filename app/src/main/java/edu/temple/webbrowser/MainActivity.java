package edu.temple.webbrowser;

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

    ArrayList<WebFragment> fragments = new ArrayList<>();
    ArrayList<String> url_list = new ArrayList<>();
    int currentIndex = 0;
    int sizeIndex = 0;
    int fragNum = 1;
    Logger log = Logger.getAnonymousLogger();
    public EditText textField;
    String input;
    WebFragment receiver;
    ViewPager pager;
    PagerAdapter pa;
    Button go;
    boolean addressBarLoaded = true;
    int i= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up view pager
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(3); // the number of "off screen" pages to keep loaded each side of the current page
        pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

        textField = (EditText) findViewById(R.id.url_text);

        //Implement soft keyboard listener for when the user presses enter key rather than use the go icon
//        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                log.info("onEditorAction called");
//                if(actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
//                    log.info("Enter key pressed");
//                    go();
//                }
//                return false;
//            }
//        });

        go = (Button) findViewById(R.id.go_button);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go();
            }
        });




        //Disable title
/*        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);*/

        //Service other applications that call this app
/*        Uri data = getIntent().getData();
        if(data != null) {
            String url = data.toString();
            //textField.setText(url);
            receiver.changeURL(url);
        }*/
    }




    private class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                default:
                    WebFragment frag = new WebFragment();
                    fragments.add(sizeIndex, frag);
                    sizeIndex++;
                    return frag;
            }
        }

        @Override
        public int getCount() {
            return fragNum;
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
                //User chose the "previous" action
                log.info("Prev Pressed.");
                pager.setCurrentItem(pager.getCurrentItem()-1);
                textField.setText(url_list.get(pager.getCurrentItem()));


/*                i--;
                if (url_list.get(i)==null){
                    textField.setText("");
                } else
                    textField.setText(url_list.get(i-1));*/


                return true;

            case R.id.next_button:
                //User chose the "next" action
                log.info("Next Pressed.");
                pager.setCurrentItem(pager.getCurrentItem()+1);
                textField.setText(url_list.get(pager.getCurrentItem()));

                //textField.setText(url_list.get(pager.getCurrentItem()+1));
/*                i++;
                if (url_list.get(i)==null){
                    textField.setText("");
                } else
                    textField.setText(url_list.get(i+1));
                    */
                return true;

            case R.id.new_button:
                //User chose the "new" action to get a new window
                log.info("New Pressed.");
                //Create new fragment and add web frag to array
                WebFragment fragment = new WebFragment();
                fragments.add(sizeIndex, fragment);
                fragNum++;
                //link to adapter and notify of the change
                pa = pager.getAdapter();
                pa.notifyDataSetChanged();
                receiver = fragment;
                pager.setCurrentItem(sizeIndex);
                textField.setText("");
                //Once the new button is pressed, call onPrepareOptionsMenu() to change to go icon
                //invalidateOptionsMenu();
                addressBarLoaded = true;
                //i++;
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void go(){
        //User pressed the "go" button to go to the page
        log.info("Go Pressed.");
        //Once the go button is pressed, call onPrepareOptionsMenu() to change to refresh icon
        invalidateOptionsMenu();
        //Save current state
        currentIndex = pager.getCurrentItem();
        receiver = fragments.get(currentIndex);
        input = textField.getText().toString();
        if (url_list.size() == 0) {
            url_list.add(input);
        } else if (url_list.size() > pager.getCurrentItem()) {
            url_list.set(pager.getCurrentItem(), input);
            System.out.println("existing overwritten");
        } else {
            url_list.add(pager.getCurrentItem(), input);
            System.out.println("new added");
        }
        //Call fragment to load url
        receiver.changeURL(input);
        addressBarLoaded = false;
        //i++;
        System.out.println(url_list.toString());
    }
}
