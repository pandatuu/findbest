package imui.jiguang.cn.imuisample.fragment.common;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.findbest.vip.R;


public class ShadowFragment extends Fragment {

    private  ShadowScreen shadow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shadow, container, false);
        shadow= (ShadowScreen) getActivity();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shadow.shadowOnclick();
            }
        });
        return view;
    }


    public interface  ShadowScreen{
        public void shadowOnclick();
    }

}




