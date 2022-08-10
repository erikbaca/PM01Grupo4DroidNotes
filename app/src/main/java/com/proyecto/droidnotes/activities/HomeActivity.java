package com.proyecto.droidnotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.adapters.ViewPagerAdapter;
import com.proyecto.droidnotes.fragments.ChatsFragments;
import com.proyecto.droidnotes.fragments.ContactsFragment;
import com.proyecto.droidnotes.fragments.PhotoFragment;
import com.proyecto.droidnotes.fragments.StatusFragment;
import com.proyecto.droidnotes.providers.AuthProvider;
import com.proyecto.droidnotes.providers.UsersProvider;

// IMPLEMENTAMOS UNA INTERFACE PARA TRABAJAR CON EL SEARCHBAR
// - IMPLEMENTAMOS LOS 3 METODOS QUE REQUIERE
public class HomeActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{

    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    MaterialSearchBar mSearchBar;
    TabLayout mTabLayout;
    ViewPager mViewPager;

    // AÑADIMOS LOS FRAGMENTOS QUE MOSTRAREMOS
    ChatsFragments mChatsFragment;
    ContactsFragment mContactsFragment;
    StatusFragment mStatusFragment;
    PhotoFragment mPhotoFragment;

    int mTabSelected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setStatusBarColor();


        mAuthProvider = new AuthProvider();
//        mAuthProvider.signOut();
        mUsersProvider = new UsersProvider();
        mSearchBar = findViewById(R.id.searchBar);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);


    // F R A G M E N T S ===========================================================================
        // DEFINIMOS EL NUMERO DE FRAGMENTS
        mViewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // INSTANCIAMOS NUESTROS FRAGMENTS
        mChatsFragment = new ChatsFragments();
        mContactsFragment = new ContactsFragment();
        mStatusFragment = new StatusFragment();
        mPhotoFragment = new PhotoFragment();

        adapter.addFragment(mPhotoFragment, "");
        adapter.addFragment(mChatsFragment, "CHATS");
        adapter.addFragment(mStatusFragment, "ESTADOS");
        adapter.addFragment(mContactsFragment, "CONTACTOS");

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTabSelected);

        setupTabIcon();

        // C I E R R E ===========================================================================

        // METODO PARA INCLUIR NUESTRO MENU / CIERRE DE SESION
        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // ESTABLECEMOS LOS EVENTOS DEL MENU
                if (item.getItemId() == R.id.itemSignOut) {
                    sigOut();
                }
                else if (item.getItemId() == R.id.itemProfile) {
                    goToProfile();
                }
                else if (item.getItemId() == R.id.itemAdd) {
                    goToAddMultiUsers();
                }
                return true;
            }
        });

        mAuthProvider = new AuthProvider();


        createToken();
    }



    private void goToAddMultiUsers() {

        Intent intent = new Intent(HomeActivity.this, AddMultiUserActivity.class);
        startActivity(intent);
    }


    // METODO PARA LA CREACION DEL TOKEN
    private void createToken() {
        mUsersProvider.createToken(mAuthProvider.getId());
    }

    // METODO HACIA EL PERFIL
    private void goToProfile()
    {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // METODO PARA LA CAMARA
    private void setupTabIcon()
    {
        // RECIBE EL INDICE DONDE QUEREMOS TRABAJAR
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.weight = 0.5f;
        linearLayout.setLayoutParams(layoutParams);
    }


    // UTILIZAMOS NUESTRO AUTHPROVIDER Y EJECUTAR EL METODO SIGNOUT
    private void sigOut()
    {
        mAuthProvider.signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    // METODOS QUE NECESITAMOS PARA TRABAJAR CON EL SEACHBAR ----------
    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    // CIERRE -----------------------------------------------------------------

    private void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack));
        }
    }


}