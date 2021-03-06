package com.mallock.messiiitd.fragments.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mallock.messiiitd.DataSupplier;
import com.mallock.messiiitd.FullMenuActivity;
import com.mallock.messiiitd.NetworkUtils;
import com.mallock.messiiitd.R;
import com.mallock.messiiitd.models.Menu;
import com.mallock.messiiitd.retrofit.MenuService;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;


public class MenuFragment extends Fragment {

    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext())
                .inflate(R.layout.menulayout, container, false);
        mPager = (ViewPager) view.findViewById(R.id.menupager);
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FullMenuActivity.class));
            }
        });
        final TextView errorText = (TextView) view.findViewById(R.id.network_error);
        if(!NetworkUtils.isActive(getContext())){
            errorText.setVisibility(View.VISIBLE);
        }else{
            errorText.setVisibility(View.GONE);
        }
        MenuService service = DataSupplier.getRetrofit().create(MenuService.class);
        final Call<Menu> call = service.getMenu();
        call.enqueue(new retrofit.Callback<Menu>() {
            @Override
            public void onResponse(Response<Menu> response, Retrofit retrofit) {
                errorText.setVisibility(View.GONE);
                Menu menu;
                menu = response.body();

                mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(), menu);
                mPager.setAdapter(mPagerAdapter);
                mPager.setCurrentItem(menu.getIndex());
            }

            @Override
            public void onFailure(Throwable t) {
                errorText.setVisibility(View.VISIBLE);
                try {
                    Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        Menu menu;

        public ScreenSlidePagerAdapter(FragmentManager fm, Menu menu) {
            super(fm);
            this.menu = menu;
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<Menu.Item> req;
            String str;
            switch (position) {
                case 0:
                    req = menu.getBreakfast();
                    str = "BREAKFAST";
                    break;
                case 1:
                    req = menu.getLunch();
                    str = "LUNCH";
                    break;
                case 2:
                    req = menu.getSnacks();
                    str = "EVENING SNACKS";
                    break;
                case 3:
                    req = menu.getDinner();
                    str = "DINNER";
                    break;
                default:
                    req = null;
                    str = null;
                    break;
            }
            return ViewPagerFragment.newInstance(req, str);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}


