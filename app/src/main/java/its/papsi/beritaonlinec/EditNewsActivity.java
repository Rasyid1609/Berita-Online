package its.papsi.beritaonlinec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import its.papsi.beritaonlinec.helper.SessionManager;
import its.papsi.beritaonlinec.helper.UtilMessage;
import its.papsi.beritaonlinec.model.News;

import static its.papsi.beritaonlinec.helper.GlobalVariable.BASE_URL;

public class EditNewsActivity extends AppCompatActivity {
private EditText edtJudul, edtIsi, edtUrl;
private Button btnUpdate;
private News news;
private UtilMessage utilMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);

        edtIsi = findViewById(R.id.edt_isi);
        edtJudul = findViewById(R.id.edt_judul);
        edtUrl = findViewById(R.id.edt_url);
        btnUpdate = findViewById(R.id.btn_submit);

        news= getIntent().getParcelableExtra("data");
        utilMessage = new UtilMessage(this);

        setData(news);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void setData(News news) {
        edtJudul.setText(news.getTitle());
        edtIsi.setText(news.getContent());
        edtUrl.setText(news.getImageUrl()) ;
    }

    private void submitData() {
        final String judul = edtJudul.getText().toString();
        final String isi = edtIsi.getText().toString();
        final String url = edtUrl.getText().toString();



        if (judul.trim().isEmpty()){
            Toast.makeText(this, "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else if (isi.trim().isEmpty()){
            Toast.makeText(this, "Isi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else if (url.trim().isEmpty()){
            Toast.makeText(this, "Url tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "ubah_berita.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    utilMessage.dismissProgressBar();
                    try {
                        JSONObject jsonresponse = new JSONObject(response);

                        int status = jsonresponse.getInt("status");
                        String message = jsonresponse.getString("message");

                        Toast.makeText(EditNewsActivity.this, message , Toast.LENGTH_SHORT).show();
                        if (status == 0) {
                        finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(EditNewsActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            utilMessage.dismissProgressBar();
                            Toast.makeText(EditNewsActivity.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("judul", judul);
                    params.put("isi", isi);
                    params.put("gambar", url);
                    params.put("id_penulis", news.getId());
                    return super.getParams();
                }
            };

            utilMessage.showProgressBar("Submitting Data");
            Volley.newRequestQueue(EditNewsActivity.this).add(request);
        }
    }

}
