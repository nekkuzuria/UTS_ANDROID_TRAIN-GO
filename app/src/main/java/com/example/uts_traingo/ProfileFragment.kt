import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uts_traingo.LoginRegisterActivity
import com.example.uts_traingo.R
import com.example.uts_traingo.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonLogout.setOnClickListener(){
                performLogout()
            }
        }
    }


    private fun performLogout() {
        saveLoginStatus(false, "")
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireContext(), LoginRegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun saveLoginStatus(isLoggedIn: Boolean, userRole: String) {
        val editor = requireActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE).edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("userRole", userRole)
        editor.apply()
    }
}
