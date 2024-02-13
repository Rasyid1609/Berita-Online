package its.papsi.beritaonlinec.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import its.papsi.beritaonlinec.R;
import its.papsi.beritaonlinec.helper.SessionManager;
import its.papsi.beritaonlinec.helper.UtilMessage;

import static its.papsi.beritaonlinec.helper.GlobalVariable.BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewsFragment extends Fragment {

    private EditText edtJudul, edtIsi, edtUrl;
    private Button btnsubmit;
    private UtilMessage utilMessage;
    private SessionManager sessionManager;

    public AddNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtIsi = view.findViewById(R.id.edt_isi);
        edtJudul = view.findViewById(R.id.edt_judul);
        edtUrl = view.findViewById(R.id.edt_url);
        btnsubmit = view.findViewById(R.id.btn_submit);

        utilMessage = new UtilMessage(getActivity());
        sessionManager = new SessionManager(getContext());

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData() {
        final String judul = edtJudul.getText().toString();
        final String isi = edtIsi.getText().toString();
        final String url = edtUrl.getText().toString();


        if (judul.trim().isEmpty()){
            Toast.makeText(getContext(), "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else if (isi.trim().isEmpty()){
            Toast.makeText(getContext(), "Isi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else if (url.trim().isEmpty()){
            Toast.makeText(getContext(), "Url tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "berita_baru.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("beritaonlineb", response);
                    utilMessage.dismissProgressBar();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        int status = jsonResponse.getInt("status");
                        String message = jsonResponse.getString("message");

                        if (status == 0){
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            //Clear input
                            resetInput();
                        }else {
                            Toast.makeText(getContext(), "Tambah berita gagal " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            utilMessage.dismissProgressBar();
                            Toast.makeText(getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("judul", judul);
                    params.put("isi", isi);
                    params.put("gambar", url);
                    params.put("id_penulis", sessionManager.getUserId());
                    return super.getParams();
                }
            };

            utilMessage.showProgressBar("Submitting Data");
            Volley.newRequestQueue(getContext()).add(request);
        }
    }

    private void resetInput() {
    edtJudul.setText("");
    edtIsi.setText("");
    edtUrl.setText("");
    }
}
