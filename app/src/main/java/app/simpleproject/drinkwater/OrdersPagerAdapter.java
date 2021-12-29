package app.simpleproject.drinkwater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.simpleproject.drinkwater.fragments.HomeFragment;
import app.simpleproject.drinkwater.fragments.NotificationsFragment;
import app.simpleproject.drinkwater.fragments.SettingsFragment;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new NotificationsFragment();
            case 1:
                return new HomeFragment();
            default:
                return new SettingsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
