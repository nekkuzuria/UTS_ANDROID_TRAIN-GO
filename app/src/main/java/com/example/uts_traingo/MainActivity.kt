import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uts_traingo.AdminDashboardFragment
import com.example.uts_traingo.databinding.ActivityMainBinding
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

    }


}
