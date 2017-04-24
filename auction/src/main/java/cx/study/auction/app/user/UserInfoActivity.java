package cx.study.auction.app.user;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.bean.User;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.UserRest;

public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    UserRest userRest;
    UserDao userDao;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setTitle("我的资料");
        userRest = new UserRest();
        userDao = new UserDao(this);
        user = userDao.getLocalUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private Task<User> loadUserInfo(){
        return Task.callInBackground(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return userRest.getUserInfo(user.getId());
            }
        }).continueWith(new Continuation<User, User>() {
            @Override
            public User then(Task<User> task) throws Exception {
                if (!task.isFaulted()){
                    user = task.getResult();
                    userDao.saveUser(user);
                    initView();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private void initView() {
        if (user.getGender() == 0){
            imageView.setImageResource(R.drawable.icon_portrait_nan);
            tvGender.setText("男");
        } else {
            imageView.setImageResource(R.drawable.icon_portrait_nv);
            tvGender.setText("女");
        }
        tvNickname.setText(user.getNickname());
    }
}
