package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration

import com.google.android.material.navigation.NavigationView


class MainPage : AppCompatActivity() {
  
  private lateinit var appBarConfiguration: AppBarConfiguration
  lateinit var toggle: ActionBarDrawerToggle
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var navView: NavigationView

  lateinit var sharedPreferences : SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main_page)

    val favorites = Favorites()
    val insertAds = InsertAds()
    val yourAds = YourAds()
    val search = Search()
    val help = Help()

    val logout = findViewById<Button>(R.id.logout)

    logout.setOnClickListener {
      val intent = Intent(this@MainPage, Login::class.java)
      startActivity(intent)

      sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
      val editor: SharedPreferences.Editor = sharedPreferences.edit()
      editor.clear()
      editor.apply()
      finish()
    }

    // Call findViewById on the DrawerLayout
    drawerLayout = findViewById(R.id.drawerLayout)
    toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
    drawerLayout.addDrawerListener(toggle)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    toggle.syncState()

    // Call findViewById on the NavigationView
    navView = findViewById(R.id.navView)

    supportFragmentManager.beginTransaction().apply {
      replace(R.id.flFragment, search)
      commit()
    }

    //add user profile button

        /*evaluations
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, evaluation)
            commit()
        }*/


    // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
    navView.setNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.menu_favorities -> {
          supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, favorites)
            commit()
          }
          true
        }
        R.id.menu_insertAds -> {
          supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, insertAds)
            commit()
          }
          true
        }
        R.id.menu_Ads -> {
          supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, yourAds)
            commit()
          }
          true
        }
        R.id.menu_search -> {
          supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, search)
            commit()
          }
          true
        }
        R.id.menu_help -> {
          supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, help)
            commit()
          }
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

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.userMenu -> {
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
