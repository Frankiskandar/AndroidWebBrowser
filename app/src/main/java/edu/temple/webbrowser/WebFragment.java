package edu.temple.webbrowser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {

    WebView webView;
    Logger log = Logger.getAnonymousLogger();
    String url;

    public WebFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web, container, false);
        webView = (WebView) v.findViewById(R.id.fragment_web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //enable javascript
        webView.setWebViewClient(new WebViewClient());

        return v;
    }

    public void openURL(String url){
        log.info("openURL Called in fragment\n");
        //add https if url does not start with it
        if(url != null)
            if(!url.startsWith("http://")){
                String temp = "https://" + url;
                url = temp;
            }
        webView.loadUrl(url);
    }

}
