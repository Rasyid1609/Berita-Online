package its.papsi.beritaonlinec.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import its.papsi.beritaonlinec.EditNewsActivity;
import its.papsi.beritaonlinec.R;
import its.papsi.beritaonlinec.helper.UtilMessage;
import its.papsi.beritaonlinec.model.News;

import static its.papsi.beritaonlinec.helper.GlobalVariable.BASE_URL;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvTittle, tvAuthor, tvContent;
    private Boolean isMine;
    private News news;
    private UtilMessage utilMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ivImage = findViewById(R.id.iv_image);
        tvAuthor = findViewById(R.id.tv_author);
        tvContent = findViewById(R.id.tv_content);
        tvTittle = findViewById(R.id.tv_title);

        news = (News) getIntent().getExtras().get("data");
        isMine = getIntent().getBooleanExtra("is_mine", false);
        setData(news);

    }

    private void setData(News news) {
        if (news !=null){
            if (getSupportActionBar() !=null){
                getSupportActionBar().setTitle(getTitle());
            }

            Glide.with(this).load(news.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(ivImage);

            tvTittle.setText(news.getTitle());
            tvAuthor.setText(news.getAuthor());
            tvContent.setText(news.getContent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMine) {
            getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intentEdit = new Intent(this, EditNewsActivity.class);
                intentEdit.putExtra("data", news);
                startActivity(intentEdit);

            break;

            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Anda yakin menghapus \"" + news.getTitle() + "\"?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest request = new StringRequest(Request.Method.POST,
                                BASE_URL + "hapus_berita.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);

                                        int status = jsonResponse.getInt("status");
                                        String message = jsonResponse.getString("message");
                                        Toast.makeText(NewsDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                                        if (status == 0){
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(NewsDetailActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    utilMessage.dismissProgressBar();
                                        Toast.makeText(NewsDetailActivity.this, "Error:", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("id", news.getId());

                                return params;
                            }

                        };


                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
