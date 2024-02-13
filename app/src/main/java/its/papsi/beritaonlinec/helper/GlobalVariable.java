package its.papsi.beritaonlinec.helper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import its.papsi.beritaonlinec.R;

public class GlobalVariable {
    public static final String BASE_URL = "http://10.0.2.2/beritaonlineb/";

    public static class FeedbackActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feedback);
        }
    }
}
