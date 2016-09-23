package deadline.expandabletextview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExpandableTextView expandableTv = (ExpandableTextView) findViewById(R.id.expandabletv);


        expandableTv.setCollapseDrawable(R.drawable.down);
        expandableTv.setExpandDrawable(R.drawable.up);
        expandableTv.setMaxLines(5);
        expandableTv.setDrawableGravity(Gravity.RIGHT);

        expandableTv.setText("朋友远行回来，带回上好沉香，知我素爱品茗点香，邀我至其小院，别名香沁斋一起闻香品茶。偏逢不赶巧，待我梳理凡事却得知她已前往江南小镇，在那烟雨朦胧的乌镇留下芳踪。临行前，她托人将这沉香捎至家中，赠我品赏。\n" +
                " \n" +
                "　　细数过往，金兰之交已有数十载。虽一日无再晨，但犹记年少的我们共剪西窗，秉烛夜谈，共话未来。她期冀在未来的日子里，游遍大川南北，追随前人风骨。她心之所念，梦之所向，如同禅定般执着。正如她赠予我这块沉香，虽其貌不扬，心却结实如铁，沉浸水中，一入到底，故别名沉水香。沉水香又因内部散发芬芳香味，被人津津乐道。她那沉静内敛的品质，将自身香气保留至极致，历久弥新，皆因带着一份素心，静立于世。\n" +
                " \n" +
                "　　俗世沉浮，物欲横流，稍有倦怠，便被诱惑迷离，失却初心。多少人，打着奋战的旗号，却在红尘中渐行渐远，倘若得上苍垂帘，偶得捷报，却在翻滚浪海中再现原形，一无所有；多少人，借着距离的空间，却在流光的岁月里成为平行线，命运无情的轮回，再次相遇，却只能在一抹残阳中相顾无言，顾影自怜；多少人，踏着娇宠的名义，却有恃无恐地刺痛那颗宽容的心，翻开疲乏的痛楚，朱砂烙印，却再也寻不回那绽放的梦，空悔半生。");

    }

}
