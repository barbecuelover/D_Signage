package com.ecs.sign.view.edit.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ecs.sign.view.edit.fragment.CanvasFragment;

import java.util.List;

/**
 * @author zw
 * @time 2019/12/24
 * @description
 */
public class CanvasFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<CanvasFragment> fragmentList;

    public CanvasFragmentStatePagerAdapter(@NonNull FragmentManager fm, List<CanvasFragment> fragmentList) {
        super(fm, fragmentList.size());
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    public void addCanvas(CanvasFragment canvasFragment){
        fragmentList.add(canvasFragment);
        notifyDataSetChanged();
    }

    public void deleteCanvas(int index){
        fragmentList.remove(index);
        notifyDataSetChanged();
    }

}
