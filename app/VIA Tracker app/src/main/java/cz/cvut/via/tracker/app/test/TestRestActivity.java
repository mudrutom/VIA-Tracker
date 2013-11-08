package cz.cvut.via.tracker.app.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.cvut.via.tracker.app.R;

public class TestRestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_rest);
    }

    @Override
    public void onStart() {
        super.onStart();

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView about = (TextView) findViewById(R.id.about);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView website = (TextView) findViewById(R.id.website);

        new AsyncTask<Object, Object, Page>() {
            @Override
            protected Page doInBackground(Object... objects) {
                final RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                final Page page = restTemplate.getForObject("http://graph.facebook.com/gopivotal", Page.class);
                return page;
            }

            @Override
            protected void onPostExecute(Page page) {
                name.setText(page.getName());
                about.setText(page.getAbout());
                phone.setText(page.getPhone());
                website.setText(page.getWebsite());
                super.onPostExecute(page);
            }
        }.execute();
    }

}
