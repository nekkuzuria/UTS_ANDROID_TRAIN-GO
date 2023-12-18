package com.example.uts_traingo

import AdminAddTicketFragment
import DashboardFragment
import HistoryOrderFragment
import ListTrainFragment
import ProfileFragment
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.uts_traingo.databinding.ActivityMainBinding
import com.example.uts_traingo.db.TrainDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    //    Fragment
    private val listTrainFragment = ListTrainFragment()
    private val profileFragment = ProfileFragment()
    private val historyOrderFragment = HistoryOrderFragment()
    private val addTicketFragment = AdminAddTicketFragment()
    private val dashboardAdmin = AdminDashboardFragment()
    private val dashboard = DashboardFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Create instance of the database
        val db = Room.databaseBuilder(
            applicationContext,
            TrainDatabase::class.java, "train-database"
        ).build()

        with(binding){
            val role = getRoleFromSharedPreferences()
            setupBottomNavigation(role)
        }

    }

    private fun setupBottomNavigation(roleUser: String) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.bottomNavigation

        when (roleUser) {
            "admin" -> {
                navView.getMenu().clear()
                navView.inflateMenu(R.menu.admin_botnav_menu)
                navController.setGraph(R.navigation.nav_graph_admin)
            }
            "user" -> {
                navView.getMenu().clear()
                navView.inflateMenu(R.menu.botnav_menu)
                navController.setGraph(R.navigation.nav_graph_user)
            }
        }
        navView.setupWithNavController(navController)
    }


    private fun getRoleFromSharedPreferences(): String {
        val sharedPref = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        return sharedPref.getString("userRole", "user") ?: "user"
    }

}
