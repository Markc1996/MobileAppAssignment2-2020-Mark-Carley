package ie.wit.activities

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import checkLocationPermissions
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import ie.wit.R
import ie.wit.fragments.AboutUsFragment
import ie.wit.fragments.DonateFragment
import ie.wit.fragments.FavouritesFragment
import ie.wit.fragments.ReportFragment
import ie.wit.main.DonationApp
import isPermissionGranted
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import setCurrentLocation

class Home : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var ft: FragmentTransaction
    lateinit var app: DonationApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)
        app = application as DonationApp
        app.locationClient = LocationServices.getFusedLocationProviderClient(this)
        app.currentLocation = Location("Default").apply {
            latitude = 52.245696
            longitude = -7.139102


        }
        if(checkLocationPermissions(this)) {
            // todo get the current location
            setCurrentLocation(app)
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.getHeaderView(0).nav_header_email.text = app.auth.currentUser?.email

        ft = supportFragmentManager.beginTransaction()

        val fragment = DonateFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_donate ->
                navigateTo(DonateFragment.newInstance())
            R.id.nav_report ->
                navigateTo(ReportFragment.newInstance())
            R.id.nav_aboutus ->
                navigateTo(AboutUsFragment.newInstance())
            R.id.nav_sign_out ->
                signOut()
            R.id.nav_favourites ->
                navigateTo(FavouritesFragment.newInstance())

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_donate -> toast("You Selected Donate")
            R.id.action_report -> toast("You Selected Report")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
         else
            super.onBackPressed()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            // todo get the current location
            setCurrentLocation(app)
        } else {
            // permissions denied, so use the default location
            app.currentLocation = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Log.v("Donation", "Home LAT: ${app.currentLocation.latitude} LNG: ${app.currentLocation.longitude}")
    }


    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun signOut()
    {
        app.auth.signOut()
        startActivity<Login>()
        finish()
    }
}