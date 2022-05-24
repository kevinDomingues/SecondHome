package ipvc.estg.secondhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainPage : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val backButton = findViewById<ImageView>(R.id.updateButton)

        backButton.setOnClickListener {
            val intent = Intent(this, Update_user::class.java)
            startActivity(intent)
        }
// Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle.syncState()

        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener {
            when (it.itemId){
                  R.id.menu_favorities -> {
                            Toast.makeText(this, "Favorities", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.menu_insertAds -> {
                            Toast.makeText(this, "InsertAds", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.menu_yourAds -> {
                            Toast.makeText(this, "YourAds", Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> {
                            false
                        }
                    }
            }
        }
    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }


//    override fun onOptionsItemSelected(item: MenuItem) {
//        if(toggle.onOptionsItemSelected(item))
//        return super.onOptionsItemSelected(item)
//    }

}