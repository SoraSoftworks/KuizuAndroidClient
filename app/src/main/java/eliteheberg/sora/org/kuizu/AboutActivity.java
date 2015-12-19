package eliteheberg.sora.org.kuizu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    protected TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        getSupportActionBar().hide();
        tvAbout = (TextView)findViewById(R.id.about_tv_about);
        tvAbout.setText(Html.fromHtml("<center><H1>Kuizu</H1></center>" +
                        "This project is developed by<br>Chouri Soulaymen: <a href=\"mailto:sora.chouri@gmail.com\">sora.chouri@gmail.com</a><br>" +
                        "Ksibi Ahmed Ghazi: <a href=\"mailto:ahmed.g.k@hotmail.fr\">ahmed.g.k@hotmail.fr</a><br><br>" +
                        "  - Icons: Windows 8 metro style from <a href=\"https://icons8.com\">https://icons8.com/</a>" +
                        "  - Font PixelFlag by <a href=\"http://nalgames.com/\">Andrew McCluskey (NAL)</a>" +
                        "  - Font Laura by <a href=\"http://www.cyreal.org/\">Cyreal</a>"+
                        "  - Font laffriotnf by <a href=\"http://www.nicksfonts.com/\">Nick Curtis</a>"));
                tvAbout.startAnimation((Animation) AnimationUtils.loadAnimation(this, R.anim.scroll_animation));
    }
}
