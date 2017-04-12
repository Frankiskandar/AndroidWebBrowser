package edu.temple.webbrowser;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_web, container, false);
        View v = inflater.inflate(R.layout.fragment_web, container, false);
        //Set up web view
        webView = (WebView) v.findViewById(R.id.fragment_web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        return v;
    }

    public void changeURL(String url){
        log.info("ChangeURL Called in fragment\n");
        //validate non-empty url
        if(url != null)
            if(!url.startsWith("http://")){ //if it doesn't have valid start create a valid url
                String temp = "https://" + url;
                url = temp;
            }
        webView.loadUrl(url);
    }

    public void goBackward(){
        if(webView != null) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                Toast.makeText(getContext(), "Cannot go backward.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No webview exists.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goForward(){
        if(webView != null) {
            if (webView.canGoForward()) {
                webView.goForward();
            } else {
                Toast.makeText(getContext(), "Cannot go forward.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No webview exists.", Toast.LENGTH_SHORT).show();
        }
    }
}
