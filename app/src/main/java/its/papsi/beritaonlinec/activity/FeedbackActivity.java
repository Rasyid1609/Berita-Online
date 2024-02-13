package its.papsi.beritaonlinec.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import its.papsi.beritaonlinec.R;
import its.papsi.beritaonlinec.helper.SessionManager;
import its.papsi.beritaonlinec.helper.UtilMessage;

import static its.papsi.beritaonlinec.helper.GlobalVariable.BASE_URL;

public class FeedbackActivity extends AppCompatActivity {

    public EditText edt_komentar;
    public RadioGroup rg_rating;
    public Button btn_submit;
    public SessionManager sessionManager;
    private UtilMessage utilMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

    edt_komentar = findViewById(R.id.edt_komentar);
    rg_rating = findViewById(R.id.rg_rating);
    btn_submit = findViewById(R.id.btn_submit);

    utilMessage = new UtilMessage(this);
    sessionManager = new SessionManager(this);

    btn_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            feedback();
        }
    });
    }

    private void feedback() {
    final String komentar = edt_komentar.getText().toString();
    int dipilih = rg_rating.getCheckedRadioButtonId();

    RadioButton selectedtext = (RadioButton) findViewById(dipilih);
    final String rating = selectedtext.getText().toString();

            utilMessage.showProgressBar("Menambah Komentar!");
            StringRequest request = new StringRequest(Request.Method.POST,
                    BASE_URL + "tambah_feedback.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //convert string to json object
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                int status = jsonResponse.getInt("status");
                                String message = jsonResponse.getString("message");

                                Toast.makeText(FeedbackActivity.this, message, Toast.LENGTH_SHORT).show();
                                if (status == 0) {
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(FeedbackActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id_penulis", sessionManager.getUserId());
                    params.put("rating", rating);
                    params.put("komentar", komentar);


                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
}
