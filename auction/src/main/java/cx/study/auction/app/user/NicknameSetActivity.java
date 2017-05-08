package cx.study.auction.app.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Callable;

import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.bean.User;
import cx.study.auction.model.rest.UserRest;

/**
 *
 * Created by chengxiao on 2017/5/8.
 */

public class NicknameSetActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.btn_save)
    Button btnSave;
    private int userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_set);
        ButterKnife.bind(this);
        setTitle("修改昵称");
        String nickname = getIntent().getStringExtra("nickname");
        userId = getIntent().getIntExtra("id",-1);
        etNickname.setText(nickname);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final UserRest userRest = new UserRest();
        final String nickname = etNickname.getText().toString();
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                userRest.updateNickname(userId,nickname);
                finish();
                return null;
            }
        });
    }
}
